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
  ;; HTTP Server
  (jetty/run-jetty #'routes/ring-handler (config/get-cfg :http-server))
  (log/info "Server running in port:" (config/get-cfg :server :port)))







(comment

  (-main)

  ; monkey patching
  (alter-var-root (var org.dsinczak.todo.service/some-very-expensive-call)
                  (fn [original-fn] (fn [] (println "Mocked"))))

  ;; Fun with TAP

  (require '[portal.api :as p])
  (add-tap #'p/submit)
  (def p (p/open {:launcher :intellij}))
  (tap> :hello)

  )