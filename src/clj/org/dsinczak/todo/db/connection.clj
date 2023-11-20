(ns org.dsinczak.todo.db.connection
  (:require [honey.sql :as sql]
            [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [next.jdbc.result-set :as rs]
            [org.dsinczak.todo.config :as config]
            [taoensso.timbre :as timbre])
  (:import (com.zaxxer.hikari HikariDataSource)
           (java.sql Timestamp)
           (java.time Instant OffsetDateTime ZoneId)))

(def ^{:private true :dynamic true} *current-conn* nil)

(defn Timestamp->OffsetDateTime [ts]
  (OffsetDateTime/ofInstant (Instant/ofEpochMilli (.getTime ts)) (ZoneId/of "UTC")))

(extend-protocol rs/ReadableColumn
  ;; Convert java.sql.Timestamp to OffsetDateTime
  Timestamp
  (read-column-by-label [^Timestamp v _]
    (Timestamp->OffsetDateTime v))
  (read-column-by-index [^Timestamp v _2 _3]
    (Timestamp->OffsetDateTime v)))

(defn ^:private connection-pool-uncached [db-spec]
  (timbre/info "Initializing connection pool for config: " db-spec)
  (connection/->pool HikariDataSource
                     db-spec))

(def connection-pool
  (memoize (fn [] (connection-pool-uncached (config/get-cfg :db)))))

(defn init-connection-pool []
  (.close (jdbc/get-connection (connection-pool))))

(def default-opts
  {:return-keys true :builder-fn rs/as-unqualified-lower-maps})

(defn exec-one-sql
  ([sc]
   (exec-one-sql sc nil))

  ([sc opts]
   (assert *current-conn*)
   (jdbc/execute-one! *current-conn* (sql/format sc) (merge default-opts opts))))

(defn exec-sql
  ([sc]
   (exec-sql sc nil))

  ([sc opts]
   (assert *current-conn*)
   (jdbc/execute! *current-conn* (sql/format sc) (merge default-opts opts))))

(defn has-current-conn? []
  (some? *current-conn*))

(defn exec-using-current-conn [f]
  (try
    (f)
    (catch Throwable t
      (timbre/error t "Exception occurred - rolling back transaction")
      (try
        (.rollback *current-conn*)
        (catch Exception e
          (timbre/error e "Exception when rolling back database transaction")))
      (throw t))))

(defn with-transaction* [f]
  (binding [next.jdbc.transaction/*nested-tx* :prohibit]
    (jdbc/with-transaction [conn (connection-pool)]
                           (binding [*current-conn* conn]
                             (exec-using-current-conn f)))))

(defmacro with-transaction [& body]
  `(with-transaction* (fn [] ~@body)))

(defmacro requires-transaction [& body]
  `(if (has-current-conn?)
     (exec-using-current-conn (fn [] ~@body))
     (with-transaction* (fn [] ~@body))))


(comment

  (with-open [^HikariDataSource ds
              (connection/->pool
                HikariDataSource
                {:dbtype          "postgres"
                 :username        "todo_dev_user"
                 :password        "secret"
                 :jdbcUrl         "jdbc:postgresql://localhost:6666/todo_dev_db"
                 :checkoutTimeout 5000
                 :description     "PostgreSQL/localhost"
                 :maxPoolSize     20})]
    ;; this code initializes the pool and performs a validation check:
    (.close (jdbc/get-connection ds)))


  ;; Debugging

  (let [todos [{:id       "ca0726cb-18f9-446c-83d4-ae46e0f89bfa",
                :title    "Go to gym",
                :content  "Just look at yourself if you need a reason",
                :done     false,
                :priority 5,
                :created  (Timestamp. (System/currentTimeMillis)),
                :updated  (Timestamp. (System/currentTimeMillis))}
               {:id       "3227a3e7-71e4-4d14-b437-a1359eb927ce",
                :title    "Go out of your basement",
                :content  "See some people, you need it",
                :done     false,
                :priority 0,
                :created  (Timestamp. (System/currentTimeMillis)),
                :updated  (Timestamp. (System/currentTimeMillis))}]]
    (postwalk (fn [f]
                (if (instance? Timestamp f)
                  ;; OffsetDateTime.ofInstant(Instant.ofEpochMilli(tstamp.getTime), ZoneId.of("UTC"))
                  (OffsetDateTime/ofInstant (Instant/ofEpochMilli (.getTime f)) (ZoneId/of "UTC"))
                  f)
                ) todos))
  )