(ns org.dsinczak.todo.db.migrations
  (:require
    [org.dsinczak.todo.db.connection :as db.connection]
    [migratus.core :as migratus]))

(defn apply-migrations []
  (migratus/migrate {:store                :database
                     :migration-dir        "migrations/"
                     :migration-table-name "migratus_state"
                     :db                   {:datasource (db.connection/connection-pool)}}))

(comment

  (apply-migrations)
  )