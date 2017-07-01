(ns server.db-api
   (:require [datomic.api :as d]
             [server.remote :as remote]
             [clojure.spec :as s]
             [clojure.core.async :as async]
             [clj-time.coerce :as c]
             [clj-time.core :as t]))

(defn get-transaction-datoms [transaction]
  (d/q '[:find ?e ?aname ?v ?added
         :in $ [[?e ?a ?v _ ?added]]
         :where
         [?e ?a ?v _ ?added]
         [?a :db/ident ?aname]]
       (:db-after transaction)
       (:tx-data transaction)))

(def connected-clients (atom #{}))

(def client-queries (atom {}))

(defn transact [conn facts]
  (if (map? facts)
    (d/transact conn [facts])
    (d/transact conn facts)))

(defn apply-query [db query]
    (apply d/q (first query) db (rest query)))

(defn dispatch-query [tube query res]
  (remote/dispatch tube [query res]))

(defn reg-query [tube conn query]
  (if (not (@client-queries (:id tube)))
    (swap! client-queries assoc (:id tube) #{query})
    (swap! client-queries update (:id tube) conj query))
  (dispatch-query tube query (apply-query (d/db conn) query)))

(defn monitor-db-change [conn]
  (let [report-queue (d/tx-report-queue conn)]
    (while true
      (let [report (.take report-queue)]
        (println "new report in queue")
        (doseq [[tube cq] @client-queries]
          (dispatch-query tube cq (apply-query (:db-after report) cq)))))))

(defn get-current-rundbrief-name [db]
  (let [rundbrief-resources (d/q (quote [:find ?rundbrief-file-name ?rundbrief-instant
                                         :in $
                                         :where [?rundbrief :rundbrief/file-name ?rundbrief-file-name]
                                                [?rundbrief :rundbrief/instant ?rundbrief-instant]])
                                 db)
        merged-ress (map zipmap (repeat [:rundbrief/file-name :rundbrief/instant]) rundbrief-resources)
        sorted-ress (sort-by (comp - #(c/to-long (c/from-date %)) :rundbrief/instant) merged-ress)]
   (println "rundbrief name result: ")
   (println rundbrief-resources)
   (println "database: ")
   (println db)
   (:rundbrief/file-name (first (take 1 sorted-ress)))))

(defn get-rundbriefe [db]
  (let [rundbrief-resources (d/q (quote [:find ?rundbrief-file-name ?rundbrief-instant
                                         :in $
                                         :where [?rundbrief :rundbrief/file-name ?rundbrief-file-name]
                                                [?rundbrief :rundbrief/instant ?rundbrief-instant]])
                                 db)
        merged-ress (map zipmap (repeat [:rundbrief/file-name :rundbrief/instant]) rundbrief-resources)]
    merged-ress))

(defn get-grouped-rundbriefe [db]
  (let [rundbrief-resources (d/q (quote [:find ?rundbrief-name ?rundbrief-file-name ?rundbrief-instant
                                         :in $
                                         :where [?rundbrief :rundbrief/name ?rundbrief-name]
                                                [?rundbrief :rundbrief/file-name ?rundbrief-file-name]
                                                [?rundbrief :rundbrief/instant ?rundbrief-instant]])
                                 db)
        merged-ress (map zipmap (repeat [:rundbrief/name :rundbrief/file-name :rundbrief/instant]) rundbrief-resources)
        grouped-ress (group-by #(t/year (c/from-date (:rundbrief/instant %))) merged-ress)
        sorted-ress (sort-by (comp - first) grouped-ress)]
    sorted-ress))

(defn get-event [db]
  (let [event-resources (d/q (quote [:find ?event-title ?event-description ?event-location ?event-instant
                                     :in $
                                     :where [?event :event/title ?event-title]
                                            [?event :event/description ?event-description]
                                            [?event :event/location ?event-location]
                                            [?event :event/instant ?event-instant]])
                             db)
        _ (println "event-resources: " event-resources)
        merged-ress (map zipmap (repeat [:event/title :event/description :event/location :event/instant]) event-resources)
        _ (println "merged resources: " merged-ress)
        filtered-ress (filter #(t/after? (t/minus (t/today) (t/days 1)) (c/from-date (:event/instant %))) merged-ress)
        sorted-ress (sort-by (comp + #(c/to-long (c/from-date %)) :event/instant) merged-ress)]
     (println "sorted resources: " sorted-ress)
     (take 3 sorted-ress)))

(defn get-news
  ([db]
   (let [news-resources (d/q (quote [:find ?news-title ?news-body ?news-instant
                                     :in $
                                     :where [?news :news/section _ ?tx]
                                            [?tx   :db/txInstant ?news-instant]
                                            [?news :news/title   ?news-title]
                                            [?news :news/body    ?news-body]])
                            db)

         merged-ress (map zipmap (repeat [:news/title :news/body :db/txInstant]) news-resources)
         sorted-ress (sort-by (comp - #(c/to-long (c/from-date %)) :db/txInstant) merged-ress)]
    (take 10 sorted-ress)))
  ([db section]
   (let [news-resources (d/q (quote [:find ?news-title ?news-body ?news-instant
                                     :in $ ?section
                                     :where [?news :news/section ?section ?tx]
                                            [?tx   :db/txInstant ?news-instant]
                                            [?news :news/title   ?news-title]
                                            [?news :news/body    ?news-body]])
                            db
                            section)
         merged-ress (map zipmap (repeat [:news/title :news/body :db/txInstant]) news-resources)
         sorted-ress (sort-by (comp - #(c/to-long (c/from-date %)) :db/txInstant) merged-ress)]
    (take 10 sorted-ress))))
