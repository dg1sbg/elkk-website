(ns server.setup
  (:require [datomic.api :as d]
            [clj-time.coerce :as c]
            [clj-time.core :as t]))

(defn init-data [conn]
  [
    {:db/id "mago-user"
     :user/name "mago"
     :user/pw "dt720pa8"}
    {:db/id (d/tempid :db.part/user)
     :news/section "elkk"
     :news/title "Birgit wieder wohlbehalten zurück"
     :news/body "Nach ihrem 8-wöchigen Aufenthalt in Deutschland ist Birgit Zimmermann wieder wohlbehalten in Eldoret/Outspan eingetroffen. Auch Simeon Kamau, stellvertretender BMC-Leiter, traf nach seinem Deutschland-Aufenthalt wieder wohlbehalten in Kenia ein. Nun stehen Besuche bei den Kinder in ihren Familien und die Vorbereitungen für die Aufnahme neuer Kinder an."}
    {:db/id (d/tempid :db.part/user)
     :news/section "elkk"
     :news/title "Birgit wiedrtgrtger wohlbehalten zurück"
     :news/body "Nach ihrem 8-wöchigen Aufenthalt in Deutschland ist Birgit Zimmermann wieder wohlbehalten in Eldoret/Outspan eingetroffen. Auch Simeon Kamau, stellvertretender BMC-Leiter, traf nach seinem Deutschland-Aufenthalt wieder wohlbehalten in Kenia ein. Nun stehen Besuche bei den Kinder in ihren Familien und die Vorbereitungen für die Aufnahme neuer Kinder an."}
    {:db/id "first-rundbrief"
     :rundbrief/file-name "rb-06-2017-v2r.pdf"
     :rundbrief/name "Rundbrief Juni 2017"
     :rundbrief/instant (java.util.Date. (c/to-long (t/date-time 2017 06 01)))}])
