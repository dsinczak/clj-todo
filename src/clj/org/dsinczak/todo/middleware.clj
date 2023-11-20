(ns org.dsinczak.todo.middleware
  (:require [taoensso.timbre :as timbre]))

(defn token-auth [handler]
  (fn [request]
    (timbre/info "Dummy auth middleware")
    (if (= "ptaki-leca-kluczem" (get-in request [:headers "x-todo-auth-token"]))
      (handler request)
      {:status 401
       :body {:error "You shall not pass"}})))

(def token-auth-middleware
  {:name ::token-based-authentication
   :description "Middleware that authenticates caller using API token... kind of..."
   :wrap token-auth})