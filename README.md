# ToDo API (Spring Boot)

Small REST service to manage Users and their Todos.

- **Stack**: Java 17, Spring Boot 3, Spring Data JPA, Swagger/OpenAPI.
- **Profiles**: dev (H2, local) and prod (PostgreSQL via Docker Compose).
- **Security (M6)**: HTTPS is enabled. HTTP :8080 redirects to HTTPS :8443.

---

## How to start

### Option A — Docker Compose (PostgreSQL + HTTPS)

```bash
docker compose build
docker compose up -d
```

### Health (HTTP redirects to HTTPS)

```bash
curl -i http://localhost:8080/api/v1/health
curl -k https://localhost:8443/api/v1/health
```

- **API**: [https://localhost:8443](https://localhost:8443)
- **Swagger UI**: [https://localhost:8443/swagger-ui/index.html](https://localhost:8443/swagger-ui/index.html)

> *(Self-signed cert for local use — in Postman, disable SSL verification or trust the cert.)*

---

### Option B — Local dev (H2, no Docker)

Requirements: Java 17 + Maven wrapper

```bash
./mvnw spring-boot:run
```

- **API**: [http://localhost:8080](http://localhost:8080)
- **Swagger**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## API examples (cURL)

### Users

#### Create user
```bash
curl -s -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"username":"flo","email":"flo@example.com"}'
```

#### List users
```bash
curl -s http://localhost:8080/api/v1/users
```

#### Get user by id
```bash
curl -s http://localhost:8080/api/v1/users/<USER_ID>
```

---

### Todos (per user)

```bash
USER_ID=<USER_ID>
```

#### Create todo
```bash
curl -s -X POST http://localhost:8080/api/v1/users/$USER_ID/todos -H "Content-Type: application/json" -d '{"title":"Buy milk","description":"2L","dueDate":"2025-10-01"}'
```

#### List todos (filters & paging supported)
```bash
curl -s "http://localhost:8080/api/v1/users/$USER_ID/todos"
curl -s "http://localhost:8080/api/v1/users/$USER_ID/todos?status=PENDING"
curl -s "http://localhost:8080/api/v1/users/$USER_ID/todos?dueBefore=2025-10-01"
curl -s "http://localhost:8080/api/v1/users/$USER_ID/todos?page=0&size=5&sort=dueDate,desc"
```

#### Update & Delete

```bash
TODO_ID=<TODO_ID>
```

##### Partial update (PATCH)
```bash
curl -s -X PATCH http://localhost:8080/api/v1/todos/$TODO_ID -H "Content-Type: application/json" -d '{"status":"DONE"}'
```

##### Delete
```bash
curl -i -X DELETE http://localhost:8080/api/v1/todos/$TODO_ID
```

---

### Response headers for paging

- `X-Total-Count`
- `X-Total-Pages`
- `X-Page`
- `X-Size`

---

## Errors & Validation

- Bean Validation with `@Valid` + global `@ControllerAdvice` ⇒ consistent JSON error format.
- Status codes used: **201 Created**, **200 OK**, **204 No Content**, **400 Bad Request**, **404 Not Found**, **409 Conflict**.

**Typical error JSON**:

```json
{
  "timestamp": "2025-09-21T12:34:56.789Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/v1/users",
  "message": "Validation failed"
}
```

---

## Short reflection

This project helped me solidify how to design clear REST endpoints and apply the right HTTP status codes (200/201/204/404/409). We had already learned basic endpoint design in the IT & Information Security course, but here I focused on using the correct codes consistently and returning a uniform error JSON with validation messages, which improves the developer experience. I also enforced HTTPS with an HTTP→HTTPS redirect—small but valuable for security even in a student project.

On the process side, working with GitHub Issues, Milestones, and Pull Requests kept the scope clear. From the Agile course on, I had used these tools, but this time I did it more carefully: feature branches, smaller and clearer commits, and one PR per milestone made reviews and fixes simpler and kept the repo organized.

Finally, writing short acceptance criteria and providing Postman/cURL proofs aligned expectations quickly, and documenting “how to run” (local + Docker/Compose + HTTPS) reduced setup friction for anyone testing the service.  