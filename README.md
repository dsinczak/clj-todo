# Simple TODO application 

A very simple and unfortunately unfinished task management application (a.k.a. todos). Created for fun as part of a 
presentation on the Clojure language. I wanted to show with this simple example that people also live in the Clojure 
galaxy, they also program (both backend and frontend) and they have nice-to-use tooling. Additionally, I used this
opportunity to learn something new myself (such as reirit and migratus).

# Running

In order to run application:

* Backend:
  * In IntelliJ REPL - there is pre-configured run config [REPL.run.xml](.run/REPL.run.xml) you can use.
  * In CLI REPL - just type `lein repl` in project main directory and then 

  Run `(-main)` in REPL to start application

* Frontend - I used figwheel and its lein plugin so clojure script compilation (along with hot-swap) is as easy as: `lein figwheel`

# Sample calls

* Create
  ```bash
  curl -v -X POST http://localhost:3000/api/todo -H 'Content-Type: application/json' -d '{"title": "Go out of your basement", "content": "See some people, you need it"}'
  ```
* Get all 
  ```bash
  curl -v -X GET http://localhost:3000/api/todo?limit=10&offset=0
  ```
* Get by id
  ```bash
  curl -v -X GET http://localhost:3000/api/todo/03619212-4a3c-46ea-98b4-0bded55daa80
  ```
* Update
  ```bash
  curl -v -X PUT http://localhost:3000/api/todo/ca0726cb-18f9-446c-83d4-ae46e0f89bfa -H 'Content-Type: application/json' -d '{"content": "There is no easy way out."}'
  ```

* Dummy auth  
  ```bash
  curl -X GET http://localhost:3000/api/todo\?limit\=10\&offset\=0 -H 'x-todo-auth-token: ptaki-leca-kluczem' 
  ```