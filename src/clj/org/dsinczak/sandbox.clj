(+ 1 2 3)

(def damian "Damian")

(defn hello [name] (str "Hello " name "!"))
(hello "Dupa")
(hello damian)
(def hello-damian (partial hello "Damian"))
(hello-damian)

(let [name "Local Damian"]
  (hello name))

(defn hello-local-damian []
  (let [name "Local Damian"]
    (hello name)))
(hello-local-damian)

(defn fizz-buzz [n]
  (cond (.contains (str n) "3") "lucky"
        (zero? (mod n 15)) "fizzbuzz"
        (zero? (mod n 5)) "buzz"
        (zero? (mod n 3)) "fizz"
        :else n))
(map fizz-buzz (range 1 21))

(def person {:first-name "Kelly"
             :last-name "Keen"
             :age 32
             :occupation "Programmer"})
(:first-name person)
(get person :occupation)
(person :occupation)

(def lodz-clojure-community
  [{:first-name "Damian"
    :last-name "Sinczak"
    :age 28}
   {:first-name "Michal"
    :last-name "Mela"
    :age 21}
   {:first-name "Pawel"
    :last-name "Wlodarski"
    :age 29}])

(map (fn [person] (:first-name person)) lodz-clojure-community)
(map #(:first-name %) lodz-clojure-community)
(map :first-name lodz-clojure-community)

(filter (fn [person] (>= (:age person) 28)) lodz-clojure-community)
(filter #(>= (:age %) 28) lodz-clojure-community)


