(ns server.rundbrief)

(defn save [path name data]
  (spit (str path name) data))

(defn load [path name]
  (slurp (str path name)))
