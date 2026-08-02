# SplitMate

App for splitting shared expenses with friends. Add people to a group, add who paid for what,
and the app shows who owes who + the shortest way to pay everything back.

## Stack

- Java 21 + Spring Boot (web, data jpa, validation), H2 database
- React + Vite

## Run

backend:
```
cd backend
mvn spring-boot:run
```

frontend (in another terminal):
```
cd frontend
npm install
npm run dev
```

then open http://localhost:5173

tests: `mvn test` in backend folder

## TODO
- postgres instead of h2
- docker
- edit expense
- split by custom amounts
