(ns server.remote
  (:require [pneumatic-tubes.core :as pt :refer [receiver transmitter]]
            [pneumatic-tubes.httpkit :refer [websocket-handler]]))

(def tx (transmitter))

(def rx (atom nil))

(def handler (atom nil))

(defn dispatch [tube payload]
  (pt/dispatch tx tube payload))

(defn setup-rx [dispatch-map]
  (swap! rx #(receiver %2) dispatch-map)
  (swap! handler #(websocket-handler %2) @rx))
