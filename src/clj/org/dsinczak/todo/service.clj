(ns org.dsinczak.todo.service
  (:require
    [org.dsinczak.todo.db.connection :refer [with-transaction]]
    [org.dsinczak.todo.persistence :as persistence]
    [taoensso.timbre :as timbre]))

(defn find-all-todos [limit offset]
  (timbre/info "Finding all todos")
  (with-transaction
    {:total (persistence/count)
     :todos (persistence/find-all limit offset)}))

(defn create-todo [todo]
  (timbre/info "Creating todo:" todo)
  (with-transaction
    (persistence/create todo)))

(defn find-todo [id]
  (timbre/info "Finding todo by id:" id)
  (with-transaction
    (persistence/find-by-id id)))

(defn update-todo [id todo]
  (timbre/info "Updating todo:" todo "by id:" id)
  (with-transaction
    (persistence/update-by-id id todo)))

(defn delete-todo [id]
  (timbre/info "Deleting todo by id:" id)
  (with-transaction
    (persistence/delete-by-id id)))

(defn set-todo-done [id]
  (timbre/info "Setting todo done by id:" id)
  (with-transaction
    (persistence/update-by-id id {:done true})))

(defn set-todo-undone [id]
  (timbre/info "Setting todo undone by id:" id)
  (with-transaction
    (persistence/update-by-id id {:done false})))

(defn set-priority [id priority]
  (timbre/info "Setting todo priority by id:" id "to:" priority)
  (with-transaction
    (persistence/update-by-id id {:priority priority})))

(comment

  (create-todo {:title "Buy beer" :content "Because your an alcoholic" :priority 4})

  (create-todo {:title "Go to gym" :content "Just look at yourself if you need a reason" :priority 5})

  (find-all-todos 10 0)

  (->> (find-all-todos 10 0)
       (mapv :title)
       (mapv #(str/upper-case %)))

  (find-todo "2b7b73a3-5dd1-4ba3-a058-980f2fe8b809")

  (update-todo "2b7b73a3-5dd1-4ba3-a058-980f2fe8b809" {:content "Because its healthy for you"})

  (set-todo-done "2b7b73a3-5dd1-4ba3-a058-980f2fe8b809")
  (set-todo-undone "2b7b73a3-5dd1-4ba3-a058-980f2fe8b809")
  (set-priority "2b7b73a3-5dd1-4ba3-a058-980f2fe8b809" 5)
  (delete-todo "2b7b73a3-5dd1-4ba3-a058-980f2fe8b809")

  )