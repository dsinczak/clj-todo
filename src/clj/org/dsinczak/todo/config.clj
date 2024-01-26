(ns org.dsinczak.todo.config
  (:require
    [aero.core :refer [read-config]]
    [taoensso.timbre :as timbre]))

(defn ^:private read-config-uncached []
  (let [config-resource (clojure.java.io/resource "config.edn")]
    (timbre/info "Reading config from:" config-resource)
    (read-config config-resource)))

(def app-config (memoize read-config-uncached))

(defn get-cfg
  [& path]
  (get-in (app-config) path))

(comment
  (app-config)

  (get-cfg :db :jdbcUrl))