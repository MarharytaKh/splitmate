# SplitMate

Small app for splitting shared expenses with friends (trips, flat, dinners).
You add people to a group, add who paid for what, and the app tells you who owes who
and how to pay everything back with as few transfers as possible.

I started it because after every trip with friends we had a mess of messages like
"I paid for pizza, you paid for taxi, so... who owes who?".

![screenshot](docs/screenshot.png)

## Features

- create groups and add people (everyone gets a little avatar)
- add expenses: who paid and who it was for
- balances: how much each person is up or down
- **settle up**: minimal list of transfers so everyone is even
- money is stored in grosze (`long`), not `double`, so no rounding problems
- when 10 zł is split between 3 people the leftover grosz goes to someone (3.34 + 3.33 + 3.33), nothing gets lost

## Tech stack

**Backend:** Java 21, Spring Boot 3, Spring Data JPA, Bean Validation, H2 (dev) / PostgreSQL, JUnit 5 + MockMvc
**Frontend:** React 19, Vite, plain CSS
**Other:** Docker Compose for postgres, GitHub Actions

## How settle up works

For every person I calculate `balance = what they paid - their share of everything`.
Then I put people who should get money in one max-heap (`PriorityQueue`) and people who owe in another.
Every step I take the biggest debtor and the biggest creditor, the debtor pays `min(debt, credit)`,
and whoever still has something left goes back to the heap.
Every step at least one person is done, so for `n` people it's at most `n - 1` transfers,
and it runs in `O(n log n)`.

It's a greedy algorithm so it's not always the perfect minimum (finding the true minimum is NP-hard),
but for a group of friends it works really well.

Code: `backend/src/main/java/com/splitmate/service/SettleUpService.java`

## How I used AI

I built this project with **Claude Code**. I used it for:

- **debugging**: I pasted errors (infinite JSON recursion, H2 reserved word problem)
  and asked *why* it happens, not only for a fix
- **the algorithm**: I asked about ways to turn balances into transfers and chose the greedy
  approach with two priority queues because it's simple and fast enough
- **tests**: asking for edge cases I didn't think about (like 10 zł split between 3 people)

What I learned: AI is fast, but you still need to understand the code. Every suggestion
has to be checked, because "it compiles" doesn't mean "it's correct".
## Running locally

Backend (uses H2 in memory by default, data is lost on restart):

```bash
cd backend
mvn spring-boot:run
```

With PostgreSQL:

```bash
docker compose up -d
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

Frontend:

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173

Tests:

```bash
cd backend
mvn test
```

## API

| Method | URL | What it does |
|--------|-----|--------------|
| GET | `/api/groups` | all groups |
| POST | `/api/groups` | create group `{ "name": "..." }` |
| GET | `/api/groups/{id}` | one group with members |
| POST | `/api/groups/{id}/members` | add person `{ "name": "...", "avatar": 1-10 }` |
| GET | `/api/groups/{id}/expenses` | list of expenses |
| POST | `/api/groups/{id}/expenses` | add expense |
| DELETE | `/api/groups/{id}/expenses/{expenseId}` | delete expense |
| GET | `/api/groups/{id}/balances` | balance of every person |
| GET | `/api/groups/{id}/settle-up` | who pays who |

## What I learned

- why you should never keep money in `double` (0.1 + 0.2 != 0.3)
- `group` is a reserved word in SQL, don't name your table like that
- JPA relations, cascade and orphanRemoval
- returning DTOs instead of entities (I had infinite JSON recursion before that)
- `@RestControllerAdvice` for nice error messages instead of 500
- CORS and the Vite proxy

## TODO

- [ ] login / accounts
- [ ] edit expense
- [ ] split by custom amounts or percent, not only equally
- [ ] different currencies
- [ ] delete group / remove person
