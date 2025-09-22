# Todo API (Spring Boot)

A small REST web service to manage **Users** and their **Todos** (create, list, partially update, delete).

## How to start
```bash
# Requirements: Java 17
./mvnw spring-boot:run
# App: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html

API EXAMPLES:

USERS
# Create user
curl -s -X POST http://localhost:8080/api/v1/users \
 -H "Content-Type: application/json" \
 -d '{"username":"flo","email":"flo@example.com"}'

# List users
curl -s http://localhost:8080/api/v1/users

# Get by id (replace <UUID>)
curl -s http://localhost:8080/api/v1/users/<UUID>

TODOS:
# Assume USER_ID is a valid UUID
USER_ID=<UUID>

# Create todo
curl -s -X POST http://localhost:8080/api/v1/users/$USER_ID/todos \
 -H "Content-Type: application/json" \
 -d '{"title":"Buy milk","description":"2L","dueDate":"2025-09-30"}'

# List (with optional filters)
curl -s "http://localhost:8080/api/v1/users/$USER_ID/todos"
curl -s "http://localhost:8080/api/v1/users/$USER_ID/todos?status=PENDING"
curl -s "http://localhost:8080/api/v1/users/$USER_ID/todos?dueBefore=2025-10-01"
curl -s "http://localhost:8080/api/v1/users/$USER_ID/todos?status=PENDING&dueBefore=2025-10-01"

# Partial update (PATCH)
TODO_ID=<TODO_UUID>
curl -s -X PATCH http://localhost:8080/api/v1/todos/$TODO_ID \
 -H "Content-Type: application/json" \
 -d '{"status":"DONE"}'

# Delete
curl -i -X DELETE http://localhost:8080/api/v1/todos/$TODO_ID

Errors & Validation

The API uses Bean Validation (@Valid) and a global @ControllerAdvice to return consistent JSON error responses with proper HTTP status codes.

Status codes used: 201 Created, 200 OK, 204 No Content, 400 Bad Request, 404 Not Found, 409 Conflict.

404 is returned when a resource doesn’t exist (e.g., unknown userId/todoId).

409 is returned on unique constraint violations (duplicate username/email).

Typical error JSON

{
  "timestamp": "2025-09-21T12:34:56.789Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/v1/users",
  "message": "Validation failed"
}

400 – Bad Request (validation/type)

Occurs when the request has missing/invalid fields or wrong types.

Example

curl -i -X POST "http://localhost:8080/api/v1/users" \
  -H "Content-Type: application/json" \
  -d '{ "username": "", "email": "not-an-email" }'

### Docs
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Filtering & paging on: `GET /api/v1/users/{userId}/todos`
  - Query params: `status`, `dueBefore`, `page`, `size`, `sort` (e.g., `dueDate,desc`)
  - Response headers: `X-Total-Count`, `X-Total-Pages`, `X-Page`, `X-Size`



In this project we learned to:

- Design clear REST endpoints using ResponseEntity and proper status codes (200/201/204/404).
- Use DTO records and validation; handle Optional to express 200/404 semantics.
- Persist with Spring Data JPA, using H2 for development.
- Plan and collaborate with GitHub Issues/Milestones/Projects, work on feature branches, open Pull Requests, and iterate from automated feedback (e.g., Copilot).