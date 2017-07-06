(ns server.db-core
  (:require [datomic.api :as d]))


(def schema [{:db/id #db/id[:db.part/db]
              :db/ident :user/name
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "User name"}

             {:db/id #db/id[:db.part/db]
              :db/ident :user/pw
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "User pw"}

             {:db/id #db/id[:db.part/db]
              :db/ident :rundbrief/name
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "Name of the Rundbrief"}

             {:db/id #db/id[:db.part/db]
              :db/ident :rundbrief/file-name
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "File name of the Rundbrief"}

             {:db/id #db/id[:db.part/db]
              :db/ident :rundbrief/instant
              :db/valueType :db.type/instant
              :db/cardinality :db.cardinality/one
              :db/doc "Date of the rundbrief"}

             {:db/id #db/id[:db.part/db]
              :db/ident :news/section
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "The section the news article is in"}

             {:db/id #db/id[:db.part/db]
              :db/ident :news/title
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "Title of the news"}

             {:db/id #db/id[:db.part/db]
              :db/ident :news/body
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "Body of the news"}

             {:db/id #db/id[:db.part/db]
              :db/ident :event/instant
              :db/valueType :db.type/instant
              :db/cardinality :db.cardinality/one
              :db/doc "Instant of the event"}

             {:db/id #db/id[:db.part/db]
              :db/ident :event/title
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "Title of the news"}

             {:db/id #db/id[:db.part/db]
              :db/ident :event/location
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "Location of the event"}

             {:db/id #db/id[:db.part/db]
              :db/ident :event/description
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc "Description of the event"}])


(defn startup-in-memory-db [env]
  (let [uri (env "ELKKWEB_DATOMIC_URI")]
    (d/create-database uri)
    (let [conn (d/connect uri)]
      @(d/transact conn schema)
      conn)))



;; AWS URI: datomic:ddb://eu-central-1/eldoret-kids.de/ELKKDEP1
