(ns server.core
  (:gen-class)
  (:use org.httpkit.server) (:use clojure.pprint)
  (:use bidi.bidi bidi.ring ring.server.standalone)
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream])
  (:require [server.db-core :as db-core]
            [server.db-api :as db-api]
            [server.setup :as setup]
            [server.remote :as remote]
            [datomic.api :as d]
            [clojure.core.async :as async]
            [server.rundbrief :as rundbrief]
            [hiccup.core :as hiccup]
            [server.pages :as pages]
            [ring.util.response :as res]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [clj-time.core :as t]
            [taoensso.nippy :as nippy]
            [clojure.data.json :as json]
            [buddy.hashers :as hashers]))


(def env
  {"ELKKWEB_RESOURCES_DIR" (or (System/getenv "ELKKWEB_RESOURCES_DIR")
                               "/Users/mago/Desktop/elkk_res/public")
   "ELKKWEB_INFOLETTER_DIR" (or (System/getenv "ELKKWEB_INFOLETTER_DIR")
                                "/Users/mago/Desktop/elkk_res/public/rundbriefe")
   "ELKKWEB_SERVER_PORT" (or (System/getenv "ELKKWEB_SERVER_PORT")
                             "8090")
   "ELKKWEB_DATOMIC_URI" (or (System/getenv "ELKKWEB_DATOMIC_URI")
                             "datomic:mem://meta-frontend")})

(def conn nil)

(defn wrap-page [meta-info page]
  [:html
    [:head
      [:meta {:charset "utf-8"}]
      meta-info
      [:link {:href "https://fonts.googleapis.com/css?family=Nunito:300,400,700|Open+Sans:300,400,700|Quicksand:300,400,700"
              :rel "stylesheet"}]
      [:link {:href "resources/css/bootstrap-grid.min.css"
              :rel "stylesheet"}]
      [:link {:href "resources/css/style.css"
              :rel "stylesheet"}]]
    [:body
      page]])

(defn build-page [page]
  [:div
    (pages/header (d/db conn))
    page
    (pages/footer (d/db conn))])

(defn pages-server [page-keyword]
    {:status 200
     :body
      (hiccup/html
                      (case page-keyword
                            :home            (wrap-page
                                                (list
                                                  [:meta {:name "description"
                                                           :content "Wir unterstützen Straßenkinder in Kenia auf ihrem Weg zurück in ein geordnetes Leben."}]
                                                  [:meta {:name "author"
                                                           :content "Eldoret Kids Kenia e.V."}]
                                                  [:title "Eldoret Kids Kenia e.V. - Start"])
                                                (build-page (pages/home (d/db conn))))
                            :org             (wrap-page
                                                (list
                                                 [:meta {:name "description"
                                                         :content "Am 11.09.2013 in Bempflingen gegründet, unterstützt der Eldoret Kids Kenia e.V. die Arbeit mit Straßenkindern in Eldoret/Kenia, vor allem die Arbeit im Rehabilitationszentrum \"Badilisha Maisha Centre\"."}]
                                                 [:meta {:name "author"
                                                         :content "Eldoret Kids Kenia e.V."}]
                                                 [:title "Eldoret Kids Kenia e.V. - Verein"])
                                                (build-page (pages/org (d/db conn))))
                            :project         (wrap-page
                                                [[:meta {:name "description"
                                                         :content "Das Badilisha Maisha Centre nimmt Kinder auf, die durch widrige Umstände auf der Straße leben müssen. In der Einrichtung lernen sie, ohne Drogenkonsum zu leben und wieder einem geregelten Tagesablauf nachzugehen."}]
                                                 [:meta {:name "author"
                                                         :content "Eldoret Kids Kenia e.V."}]
                                                 [:title "Eldoret Kids Kenia e.V. - Projekt"]]
                                                (build-page (pages/project (d/db conn))))
                            :support         (wrap-page
                                                [[:meta {:name "description"
                                                         :content "Ihre Spende kommt auf direktem Wege in voller Höhe dem Projekt zugute. Spenden können Sie durch eine Überweisung des gewünschten Betrags auf unser Spendenkonto."}]
                                                 [:meta {:name "author"
                                                         :content "Eldoret Kids Kenia e.V."}]
                                                 [:title "Eldoret Kids Kenia e.V. - Unterstützung"]]
                                                (build-page (pages/support (d/db conn))))
                            :rundbriefarchiv (wrap-page
                                                [[:meta {:name "author"
                                                         :content "Eldoret Kids Kenia e.V."}]
                                                 [:title "Eldoret Kids Kenia e.V. - Rundbriefarchiv"]]
                                                (build-page (pages/rundbriefarchiv (d/db conn))))
                            :impressum       (wrap-page
                                                [[:meta {:name "author"
                                                         :content "Eldoret Kids Kenia e.V."}]
                                                 [:title "Eldoret Kids Kenia e.V. - Impressum"]]
                                                (build-page (pages/impressum (d/db conn))))))})


(def route-handlers
  {:home (fn [_] (pages-server :home))
   :org  (fn [_] (pages-server :org))
   :project (fn [_] (pages-server :project))
   :support (fn [_] (pages-server :support))
   :rundbriefarchiv (fn [_] (pages-server :rundbriefarchiv))
   :chronik (fn [_] (pages-server :rundbriefarchiv))
   :impressum (fn [_] (pages-server :impressum))
   :resources (->Files {:dir (env "ELKKWEB_RESOURCES_DIR")})
   :rundbriefe (->Files {:dir (env "ELKKWEB_INFOLETTER_DIR")})})

(def app-routes ["/" {""                (:home route-handlers)
                      "verein"          (:org route-handlers)
                      "projekt"         (:project route-handlers)
                      "unterstuetzung"  (:support route-handlers)
                      "rundbriefarchiv" (:rundbriefarchiv route-handlers)
                      "impressum"       (:impressum route-handlers)
                      "resources/"      (:resources route-handlers)
                      "rundbriefe/"     (:rundbriefe route-handlers)
                      "post-rundbrief"   (fn [req]
                                            (let [file ((:multipart-params req) "file")
                                                  comment ((:multipart-params req) "comment")
                                                  auth (:auth comment)
                                                  facts (:facts comment)]
                                              (println "multiparts: ")
                                              (println (:multipart-params req))
                                              (when (hashers/check "dt720pa8" (:password auth))
                                                (println "password checked")
                                                (println "moving on")
                                                (println "auth: " auth " facts: " facts " file: " file)
                                                (println (clojure.java.io/copy (:tempfile file) (clojure.java.io/file (str (env "ELKKWEB_INFOLETTER_DIR") (:filename file)))))
                                                (db-api/transact conn facts)))
                                          {:status "200" :body req})
                      "post"             (fn [req]
                                          (let [req-body (:body req)
                                                decoded-body (read-string (slurp req-body))
                                                content (second decoded-body)]
                                            (println decoded-body)
                                            (println content)
                                            (when (hashers/check "dt720pa8" (:password content))
                                              (db-api/transact conn (dissoc content :password))))
                                          {:status "200" :body req})}])


(def app-handler (make-handler app-routes))

(def app
  (-> app-handler
      wrap-session
      wrap-flash
      wrap-multipart-params))

(defn -main
  [& args]
  (let [conn (db-core/startup-in-memory-db env)]
   (def conn conn))
  #_(db-api/transact conn (setup/init-data conn))
  (serve app {:port (read-string (env "ELKKWEB_SERVER_PORT"))
              :open-browser? false})
  (println "started server"))
