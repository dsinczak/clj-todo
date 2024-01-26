(ns org.dsinczak.todo.persistence
  (:require [org.dsinczak.todo.db.connection :as db])
  (:import (java.time LocalDateTime ZoneOffset)
           (java.util UUID)))

(defn ^:private ->uuid [id]
  (cond
    (string? id) (UUID/fromString id)
    (instance? UUID id) id
    :else (throw (ex-info "Invalid UUID" {:id id}))))

(defn create [todo]
  (db/exec-one-sql {:insert-into :todo
                    :values      [todo]}
                   {:return-keys true}))

(defn update-by-id [id updates]
  (let [updates* (assoc updates :updated (.atOffset (LocalDateTime/now) ZoneOffset/UTC))]
    (db/exec-one-sql {:update :todo
                      :set    updates*
                      :where  [:= :id (->uuid id)]})))

(defn find-all [limit offset]
  (db/exec-sql {:select   [:*]
                :from     :todo
                :order-by [[:created :asc]]
                :limit    limit
                :offset   offset}))

(defn count []
  (:count
    (db/exec-one-sql {:select :%count.*
                    :from :todo})))

(defn find-by-id [id]
  (db/exec-one-sql {:select :*
                    :from   :todo
                    :where  [:= :id (->uuid id)]}))

(defn delete-all []
  (db/exec-one-sql {:delete-from :todo}))

(defn delete-by-id [id]
  (db/exec-one-sql {:delete-from :todo
                    :where       [:= :id (->uuid id)]}))

(comment
  (db/with-transaction
    (create {:title   "Buy whiskey"
             :content "No reason. Just buy it."}))

  (db/with-transaction
    (find-all 10 0))

  (db/with-transaction
    (find-by-id "5b7c3ed3-36f1-4fc3-a7b9-47255122739a"))

  (db/with-transaction
    (update-by-id "XXX" {:content "Because you really need it."}))

  (db/with-transaction
    (delete-all))

  (dotimes [n 100]
    (db/with-transaction
      (create {:title   (str "Buy " n " beers")
               :content (str "You have " (+ n 1) " reasons to buy it")}))
    (Thread/sleep 10))

  (db/with-transaction
    (find-all 10 0))
  (db/with-transaction
    (find-all 10 10))
  (db/with-transaction
    (find-all 10 20))

  )