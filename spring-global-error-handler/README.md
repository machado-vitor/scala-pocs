# Spring Global Error Handler (Scala + Spring Boot)

This demo shows how to implement a centralized GlobalExceptionHandler in a Spring Boot app written in Scala 3.

What you get:
- Custom exceptions (`UserNotFoundException`, `UserAlreadyExistsException`)
- `@RestControllerAdvice` mapping exceptions to a consistent JSON `ErrorResponse`
- Simple in-memory `UserService` to exercise error flows
- `UserController` exposing basic endpoints
- Jackson Scala module configured for Scala case classes

## Build and test

Requirements: JDK 17+, sbt.

Run tests:

```bash
cd spring-global-error-handler
sbt test
```

Run the app:

```bash
sbt run
```

The app starts on port 8080 by default.

## API

- GET `/api/user/all` → `List[UserDto]`
- GET `/api/user/id/{id}` → `UserDto` (404 with structured error if not found)
- POST `/api/user` with body `{ "id": "1", "name": "Jane", "email": "jane@example.com" }` → inserted `UserDto` (409 if id exists)

## Try it

Insert a user:

```bash
curl -sS -X POST http://localhost:8080/api/user \
  -H 'Content-Type: application/json' \
  -d '{"id":"1","name":"Jane","email":"jane@example.com"}' | jq .
```

Fetch all:

```bash
curl -sS http://localhost:8080/api/user/all | jq .
```

Fetch by id (exists):

```bash
curl -sS http://localhost:8080/api/user/id/1 | jq .
```

Fetch by id (not found):

```bash
curl -sS -i http://localhost:8080/api/user/id/does-not-exist
```

Example error response (404):

```json
{
  "timestamp": "2025-10-20T12:34:56.789Z",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: does-not-exist",
  "path": "/api/user/id/does-not-exist"
}
```

