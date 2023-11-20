(defproject clj-todo "0.1.0"
  :description "TODO List in Clojure for fun and learning"

  :min-lein-version "2.5.3"
  :source-paths ["src/clj" "src/cljc" "src/cljs"]
  :resource-paths ["resources"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :dependencies
  [;; Basic deps
   [org.clojure/clojure "1.11.1"]
   [aero "1.1.6"]

   ;; Logging
   [com.taoensso/timbre "6.3.1"]
   [com.fzakaria/slf4j-timbre "0.4.0"]

   ;; Web
   [ring/ring-jetty-adapter "1.7.1"]
   [metosin/reitit "0.7.0-alpha7"]
   [metosin/jsonista "0.2.6"]
   [metosin/malli "0.13.0"]

   ;; DB Access and migrations
   [com.github.seancorfield/next.jdbc "1.3.894"]
   [com.github.seancorfield/honeysql "2.5.1091"]
   [com.zaxxer/HikariCP "5.0.1"]
   [org.postgresql/postgresql "42.5.1"]
   [migratus "1.5.3"]

   ;; Frontend
   [org.clojure/clojurescript "1.11.121"]
   [com.bhauman/figwheel-main "0.2.18"]
   [cljsjs/react "17.0.2-0"]
   [cljsjs/react-dom "17.0.2-0"]
   [cljs-ajax "0.8.4"]
   [reagent "1.2.0"]
   [figwheel "0.5.20"]]

  :managed-dependencies
  [[org.eclipse.jetty/jetty-server "9.4.52.v20230823"]
   [org.eclipse.jetty/jetty-http "9.4.52.v20230823"]
   [org.eclipse.jetty/jetty-util "9.4.52.v20230823"]
   [org.eclipse.jetty/jetty-io "9.4.52.v20230823"]]

  :jvm-opts ["-Dtimbre.level=info"]

  :repl-options {:init-ns org.dsinczak.todo.main}

  :plugins [[lein-cljsbuild "1.1.8"]]

  :aliases {"figwheel" ["run" "-m" "figwheel.main" "--build" "dev" "--repl"]})

