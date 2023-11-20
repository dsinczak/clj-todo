(ns org.dsinczak.todo.schema
  (:require [malli.core :as m]
            [malli.error :as me]
            [malli.experimental.time :as met]
            [malli.registry :as mr]
            [malli.util :as mu]))

(mr/set-default-registry!
  (mr/composite-registry
    (m/default-schemas)
    (met/schemas)))

(def error-response
  [:map
   [:error [:string {:min 3, :max 1000}]]])

(def create-todo-request
  [:map
   [:title [:string {:min 3, :max 100}]]
   [:content [:string {:min 10, :max 1000}]]
   [:done {:optional true} :boolean]
   [:priority {:optional true} [:and :int [:>= 0] [:<= 5]]]])

(def todo-id
  [:id [:or :uuid [:string {:min 36, :max 36}]]])

(def create-todo-response
  (conj
    create-todo-request
    todo-id
    [:created :time/offset-date-time]))

(def update-todo-request
  (mu/optional-keys create-todo-request))

(def get-todo-request
  [:map todo-id])

(def get-todo-response
  (mu/merge
    create-todo-response
    [:map [:updated {:optional true} :time/offset-date-time]]))

(def get-all-todos-request
  [:map
   [:limit [:and :int [:>= 1] [:<= 100]]]
   [:offset [:and :int [:>= 0]]]])

(def get-all-todos-response
  [:map
   [:total :int]
   [:todos [:vector create-todo-response]]])

(comment
  (require '[malli.core :as m])

  (m/validate create-todo-request {:title   "Some Title"
                                   :content "Some description"})

  (m/validate create-todo-request {:title    "Some Title"
                                   :content  "Some description"
                                   :priority 10})

  (m/explain create-todo-request {:title    "Some Title"
                                  :content  "Some description"
                                  :priority 10})

  (require '[malli.error :as me])

  (-> create-todo-request
      (m/explain {:title    "Some Title"
                  :content  "Some description"
                  :priority 10})
      (me/humanize))

  ;; Just FYI
  (-> [:and [:map
             [:password string?]
             [:password2 string?]]
       [:fn {:error/message "passwords don't match"}
        (fn [{:keys [password password2]}]
          (= password password2))]]
      (m/explain {:password  "secret"
                  :password2 "faarao"})
      (me/humanize))

  (require '[malli.util :as mu])

  (-> [:map [:address [:map [:street string?]]]]
      (mu/closed-schema)
      (m/explain
        {:name "Lie-mi"
         :address {:streetz "Hämeenkatu 14"}})
      (me/with-spell-checking)
      (me/humanize))

  )