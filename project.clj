(defproject server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/core.async "0.4.490"]
                 [amazonica "0.3.114"]
                 [org.postgresql/postgresql "9.3-1102-jdbc41"]
                 [com.datomic/datomic-pro "0.9.5561" :exclusions [[com.fasterxml.jackson.core/jackson-core]
                                                                  [com.fasterxml.jackson.core/jackson-databind]
                                                                  [joda-time]]]
                 [hiccup "1.0.5"]
                 [bidi "2.1.1"]
                 [ring "1.6.1"]
                 [http-kit "2.2.0"]
                 [ring/ring-core "1.6.1"]
                 [ring-server "0.4.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [clj-time "0.13.0"]
                 [com.taoensso/nippy "2.13.0"]
                 [org.clojure/data.json "0.2.6"]
                 [buddy/buddy-hashers "1.2.0"]
                 [byte-streams "0.2.3"]]
  :profiles {:uberjar {:aot :all}}
  :main server.core
  :target-path "target/%s")
