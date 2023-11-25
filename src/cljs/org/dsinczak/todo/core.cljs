(ns org.dsinczak.todo.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            ["react" :as react]
            [org.dsinczak.todo.shared-validation :as shared-validation]
            [ajax.core :as ajax]))

#_{:todo-list
   {:total 10
    :todos [{:id       "2f7ca4f9-48d9-46de-8f70-1d61be36b8d5",
             :title    "Go out of your basement",
             :content  "See some people, you need it",
             :done     false,
             :priority 0,
             :created  "2023-11-18T19:58:05.058Z"}]}
   }

(defonce todos-atom
         (r/atom {}))

(defn swap-todo [updated-todo]
  (swap! todos-atom update-in [:todo-list :todos]
         (fn [todos]
           (mapv #(if (= (:id %) (:id updated-todo)) updated-todo %) todos))))

(defn toggle-done [todo]
   (ajax/PUT (str "/api/todo/" (:id todo))
            {:params          {:done (not (:done todo))}
             :format          :json
             :keywords?       true
             :response-format :json
             :handler         (fn [updated-todo]
                                (swap-todo updated-todo))}))

(defn todo-item [todo]
  ^{:key (:id todo)}
  [:li {:class [(when (:done todo) "completed ")]}
   [:div.view
    [:input.toggle
     {:type    "checkbox"
      :checked (:done todo)
      :on-change #(toggle-done todo)}]
    [:label {:class "title"}
     (:title todo)]
    [:label {:class "content"}
     (:content todo)]]])

(defn todo-app []
  [:div
   [:section#todoapp
    [:header#header
     [:h1 "TODO LIST"]
     [:ul#todo-list
      (for [todo (-> @todos-atom :todo-list :todos)]
        (todo-item todo))]]]
   [:footer#info
    [:p "Click on check mark to set it to done"]]])

(defn load-todos [limit offset]
  (ajax/GET "/api/todo"
            {:params          {:limit limit :offset offset}
             :format          :json
             :keywords?       true
             :response-format :json
             :handler         #(swap! todos-atom assoc :todo-list %)}))

(defn ^:export run []
  (load-todos 100 0)
  (rdom/render [todo-app] (js/document.getElementById "app")))



(comment
  (let [todos-atom
                     (atom
                       {:todo-list {:todos [{:id       "2e99c516-2e19-452d-802d-deb4f5c62412",
                                             :title    "Go out of your basement",
                                             :content  "See some people, you need it",
                                             :done     false,
                                             :priority 0,
                                             :created  "2023-11-21T16:26:34.451Z"}
                                            {:id "6759a38b-10f0-493d-b9c7-eb68e4a4389f",
                                             :title "Go out of your basement",
                                             :content "See some people, you need it",
                                             :done false,
                                             :priority 0,
                                             :created "2023-11-21T16:27:15.131Z"}]}})
        updated-todo {:id "6759a38b-10f0-493d-b9c7-eb68e4a4389f",
                      :title "Go out of your basement",
                      :content "See some people, you need it",
                      :done true,
                      :priority 0,
                      :created "2023-11-21T16:27:15.131Z"}]

    (swap! todos-atom update-in [:todo-list :todos]
           (fn [todos]
             (mapv #(if (= (:id %) (:id updated-todo)) updated-todo %) todos)))))