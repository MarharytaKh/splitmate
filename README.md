# splitmate

App for splitting expenses with friends (backend for now, frontend coming).

## Run

```
cd backend
mvn spring-boot:run
```

API is on http://localhost:8080/api/groups

## Endpoints
- GET /api/groups
- POST /api/groups
- GET /api/groups/{id}
- POST /api/groups/{id}/members
- GET/POST /api/groups/{id}/expenses
- DELETE /api/groups/{id}/expenses/{expenseId}
- GET /api/groups/{id}/balances
- GET /api/groups/{id}/settle-up
