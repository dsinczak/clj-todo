(ns org.dsinczak.todo.routes

  (:require
    [better-cond.core :as b]
    [malli.util :as mu]
    [muuntaja.core :as m]
    [org.dsinczak.todo.schema :as schema]
    [org.dsinczak.todo.service :as service]
    [org.dsinczak.todo.middleware :as middleware]
    [reitit.coercion.malli]
    [reitit.core :as r]
    [reitit.dev.pretty :as pretty]
    [reitit.ring :as ring]
    [reitit.ring :as ring]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.exception :as exception]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [reitit.ring.spec :as spec]
    [ring.util.response :as resp]
    [ring.middleware.content-type :as content-type]))

(def routes
  ["/api"
   ["/todo" {:get  {:summary    "Get all todos"
                    :parameters {:query schema/get-all-todos-request}
                    :responses  {200 {:body schema/get-all-todos-response}}
                    :handler    (fn [{{{:keys [limit offset]} :query} :parameters}]
                                  {:status 200
                                   :body   (service/find-all-todos limit offset)})}

             :post {:summary    "Create new todo"
                    :parameters {:body schema/create-todo-request}
                    :responses  {200 {:body schema/create-todo-response}
                                 400 {:body schema/error-response}}
                    :handler    (fn [{{:keys [body]} :parameters}]
                                  (b/cond
                                    :let [[status data] (service/create-todo body)]

                                    (= ::service/success status)
                                    {:status 200
                                     :body   data}

                                    (= ::service/failure status)
                                    {:status 400
                                     :body   {:error data}}))}}]

   ["/todo/:id" {:get    {:summary    "Get todo by id"
                          :parameters {:path schema/get-todo-request}
                          :responses  {200 {:body schema/get-todo-response}
                                       404 {:body schema/error-response}}
                          :handler    (fn [{{{:keys [id]} :path} :parameters}]
                                        (if-let [todo (service/find-todo id)]
                                          {:status 200
                                           :body   todo}
                                          {:status 404
                                           :body   {:error "Not found"}}))}

                 :put    {:summary    "Update todo by id"
                          :parameters {:path schema/get-todo-request
                                       :body schema/update-todo-request}
                          :responses  {200 {:body schema/get-todo-response}
                                       404 {:body schema/error-response}}
                          :handler    (fn [{:keys [path-params body-params]}]
                                        (if-let [todo (service/update-todo (:id path-params) body-params)]
                                          {:status 200
                                           :body   todo}
                                          {:status 404
                                           :body   {:error "Not found"}}))}

                 :delete {:summary    "Delete todo by id"
                          :parameters {:path schema/get-todo-request}
                          :responses  {404 {:body schema/error-response}}
                          :handler    (fn [{:keys [path-params]}]
                                        (if (service/delete-todo (:id path-params))
                                          {:status 200}
                                          {:status 404
                                           :body   {:error "Not found"}}))}}]])

(def router
  (ring/router routes
               {:validate  spec/validate
                :exception pretty/exception
                :data      {:coercion   (reitit.coercion.malli/create
                                          {:error-keys       #{:humanized}
                                           :compile          mu/closed-schema
                                           :strip-extra-keys true
                                           :default-values   true
                                           :options          nil})
                            :muuntaja   m/instance
                            :middleware [parameters/parameters-middleware
                                         muuntaja/format-middleware
                                         exception/exception-middleware
                                         coercion/coerce-exceptions-middleware
                                         coercion/coerce-request-middleware
                                         coercion/coerce-response-middleware
                                         ;middleware/token-auth-middleware
                                         ]}}))

(def frontend-api
  (ring/ring-handler
    router
    (ring/create-default-handler)))

(def frontend-ui
  (-> (fn [request]
        (or (resp/resource-response (:uri request) {:root "public"})
            (-> (resp/resource-response "index.html" {:root "public"})
                (resp/content-type "text/html"))))
      content-type/wrap-content-type))


(comment

  (clojure.pprint/pprint (dissoc request :reitit.core/match))

  (r/router-name router)

  (r/match-by-path router "/api/todo")

  (r/match-by-path router "/api/todo/10")

  )