# Simple TODO application 

The repository contains an application I wrote for a presentation in which I tried to encourage participants to use 
the clojure language.

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
  curl -v -X PUT http://localhost:3000/api/todo/03619212-4a3c-46ea-98b4-0bded55daa80 -H 'Content-Type: application/json' -d '{"content": "Nobody exists on purpose. Nobody belongs anywhere. Just watch TV"}'
  ```

* Dummy auth  
  ```bash
  curl -X GET http://localhost:3000/api/todo\?limit\=10\&offset\=0 -H 'x-todo-auth-token: ptaki-leca-kluczem' 
  ```