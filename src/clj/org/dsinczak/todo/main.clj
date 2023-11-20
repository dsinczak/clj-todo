(ns org.dsinczak.todo.main

  (:require [clojure.tools.logging :as log]
            [org.dsinczak.todo.config :as config]
            [ring.adapter.jetty :as jetty]
            [org.dsinczak.todo.routes :as routes]
            [org.dsinczak.todo.db.connection :as db.connection]
            [org.dsinczak.todo.db.migrations :as db.migrations]
            [taoensso.timbre :as timbre]
            ))

(defn -main

  [& args]

  (timbre/set-min-level! :info)
  (db.connection/init-connection-pool)
  (db.migrations/apply-migrations)
  ;; REST API Server
  (jetty/run-jetty #'routes/frontend-api (config/get-cfg :api-server))
  ;; UI Resources Server
  (jetty/run-jetty #'routes/frontend-ui (config/get-cfg :ui-server))
  (log/info "server running in port:" (config/get-cfg :server :port)))

(comment

  (-main)

  )