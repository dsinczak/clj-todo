(ns org.dsinczak.todo.shared-validation
  (:require [clojure.string :as str]))

(def bad-words
  #{"dupa" "lorem" "ipsum"})

(defn check-for-bad-words [text]
  (let [lowercased-text (str/lower-case text)]
    (some #((fn [bad-word] (re-find (re-pattern (str "(?i)" bad-word)) lowercased-text)) %)
          bad-words)))