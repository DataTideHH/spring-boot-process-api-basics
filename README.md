# Spring Boot Process API Basics

[![CI](https://github.com/DataTideHH/spring-boot-process-api-basics/actions/workflows/ci.yml/badge.svg)](https://github.com/DataTideHH/spring-boot-process-api-basics/actions/workflows/ci.yml)
[![GitHub Pages](https://github.com/DataTideHH/spring-boot-process-api-basics/actions/workflows/pages/pages-build-deployment/badge.svg)](https://github.com/DataTideHH/spring-boot-process-api-basics/actions/workflows/pages/pages-build-deployment)

**Java 21 · Spring Boot 4.1 · REST API · Spring Data JPA · H2 · Validation · Maven · GitHub Actions**

Small Java 21 / Spring Boot learning project that exposes validated process-check data through a layered REST API.

Project page: https://datatidehh.github.io/spring-boot-process-api-basics/

This repository is part of my broader **DataTideHH** portfolio. It supports my learning path toward **Data/BI and process-oriented IT roles** by connecting Java/Spring backend basics with structured operational process data.

---

## Purpose

The project focuses on a deliberately small backend use case: storing and exposing **process-check records**.

It demonstrates:

- REST endpoints for a small process-related resource
- layered backend structure with controller, service and repository
- request validation and matching database constraints
- status-based filtering and pagination
- explicit HTTP success and error behavior
- basic persistence with Spring Data JPA
- restrained parameterized logging for write operations
- an H2 in-memory database for local development and tests
- automated API integration tests with MockMvc
- a reproducible Maven Wrapper workflow
- GitHub Actions CI on Java 21
- synthetic sample data for process-oriented API testing

The goal is not to present a large enterprise backend. The goal is to document a clean first step from Java basics toward a small, tested Spring Boot REST API that handles structured process data.

---

## Portfolio Context

For Data/BI and process analysis work, APIs are an important interface between operational systems and downstream data workflows.

This project is useful as a supporting IT foundation because it shows how process-related records can be represented, validated, persisted and exposed through a small HTTP API.

It complements my main Data/BI portfolio projects around SQL, Python, Power BI, data quality and process-oriented analysis.

---

## Tech Stack

| Layer | Tool / Concept | Purpose |
|---|---|---|
| Language | Java 21 | Main implementation language |
| Framework | Spring Boot 4.1 | REST API application framework |
| API layer | Spring Web MVC | HTTP endpoints and JSON responses |
| Pagination | Spring Data `Pageable` and `PagedModel` | Bounded list responses with stable page metadata |
| Persistence | Spring Data JPA | Repository abstraction and entity persistence |
| Database | H2 | In-memory local development and test database |
| Validation | Jakarta Validation | Validation for incoming request data |
| Error format | Spring `ProblemDetail` | Consistent `application/problem+json` responses |
| Logging | SLF4J | Structured create, update and delete messages |
| Tests | JUnit 5, Spring Boot Test, MockMvc | API integration and persistence verification |
| Build tool | Maven Wrapper | Reproducible builds on Windows, macOS and Linux |
| CI | GitHub Actions | Automated Java 21 Maven verification |

---

## Architecture

```text
HTTP client
    -> ProcessCheckController
        -> ProcessCheckService
            -> ProcessCheckRepository
                -> H2 in-memory database
```

Current package focus:

```text
src/main/java/de/datatidehh/processapi/processcheck/
```

The API uses request and response records instead of exposing the JPA entity directly.

---

## API Contract

| Method | Endpoint | Success | Purpose |
|---|---|---:|---|
| `GET` | `/api/process-checks?page=0&size=20` | `200 OK` | Return one page of process-check records |
| `GET` | `/api/process-checks?status=OK&page=0&size=20` | `200 OK` | Filter and page records by status |
| `GET` | `/api/process-checks/{id}` | `200 OK` | Return one process-check record by ID |
| `POST` | `/api/process-checks` | `201 Created` | Create a record and return its URI in `Location` |
| `PUT` | `/api/process-checks/{id}` | `200 OK` | Replace the editable values of an existing record |
| `DELETE` | `/api/process-checks/{id}` | `204 No Content` | Delete an existing record |

List endpoints accept the standard Spring Data parameters:

- `page`: zero-based page number, default `0`
- `size`: requested page size, default `20`, capped at `100`
- `sort`: field and direction, for example `sort=processName,asc`

The default list order is `lastCheckedAt,desc`.

Typical client errors:

| Situation | Result |
|---|---:|
| Invalid request body | `400 Bad Request` |
| Invalid status query value | `400 Bad Request` |
| Unknown record ID | `404 Not Found` |

---

## Example Paged Response

```json
{
  "content": [
    {
      "id": 1,
      "processName": "Daily sales import",
      "owner": "Data Operations",
      "status": "OK",
      "lastCheckedAt": "2026-07-10T00:25:00",
      "slaMinutes": 60
    }
  ],
  "page": {
    "size": 20,
    "totalElements": 1,
    "totalPages": 1,
    "number": 0
  }
}
```

Request validation requires:

- a non-blank `processName` with at most 120 characters
- a non-blank `owner` with at most 120 characters
- a valid status: `OK`, `WARNING` or `CRITICAL`
- a non-null ISO local date-time value
- `slaMinutes` of at least `1`

The entity mirrors the non-null and maximum-length constraints so the API and database schema enforce the same basic rules.

---

## Error Response Example

Requests for unknown IDs return a standard Spring `ProblemDetail` response with media type `application/problem+json`:

```json
{
  "type": "about:blank",
  "title": "Process check not found",
  "status": 404,
  "detail": "Process check not found: 999999",
  "instance": "/api/process-checks/999999"
}
```

---

## Run Locally

### macOS or Linux

From the repository root:

```bash
./mvnw spring-boot:run
```

### Windows PowerShell

```powershell
.\mvnw.cmd spring-boot:run
```

Then open:

```text
http://localhost:8080/api/process-checks
```

At first startup, the H2 database is empty, so the list endpoint returns an empty page:

```json
{
  "content": [],
  "page": {
    "size": 20,
    "totalElements": 0,
    "totalPages": 0,
    "number": 0
  }
}
```

---

## Build and Test

### macOS or Linux

```bash
./mvnw clean verify
```

### Windows PowerShell

```powershell
.\mvnw.cmd clean verify
```

The automated suite verifies:

- application context startup
- default and requested pagination
- unfiltered and status-filtered list requests
- empty filter results
- invalid status handling
- lookup by ID
- `404` Problem Detail responses
- successful creation with `201 Created` and `Location`
- blank, invalid and oversized request values
- update behavior and persisted values
- successful deletion with `204 No Content`
- update and delete behavior for unknown IDs

---

## Continuous Integration

The workflow under `.github/workflows/ci.yml` runs for pull requests and pushes to `main`.

It uses:

- an Ubuntu GitHub-hosted runner
- Eclipse Temurin Java 21
- Maven dependency caching
- the repository's Maven Wrapper
- `clean verify` as the build and test gate

The workflow has read-only repository permissions and cancels superseded runs for the same branch or pull request.

---

## Manual API Test Flow

The full CRUD flow can also be exercised manually with curl, an API client or an IDE HTTP client:

```text
POST   /api/process-checks
GET    /api/process-checks?page=0&size=20
GET    /api/process-checks?status=OK&page=0&size=20
GET    /api/process-checks/1
PUT    /api/process-checks/1
DELETE /api/process-checks/1
```

Example test data:

```text
processName: Daily sales import
owner: Data Operations
status: OK / WARNING / CRITICAL
lastCheckedAt: 2026-07-10T00:25:00
slaMinutes: 60
```

---

## Logging

Create, update and delete operations write one parameterized application log entry containing the record ID and, where useful, its status.

Read requests and complete request bodies are not logged. This keeps the example useful for troubleshooting without producing noisy logs or copying input data unnecessarily.

---

## H2 Database Note

This project uses an **H2 in-memory database** for local development and API testing.

That means:

- no external database server is required
- the database is recreated when the application starts
- inserted records are lost when the application stops
- this is suitable for a small learning project, not for production persistence

The H2 console is available while the application is running:

```text
http://localhost:8080/h2-console
```

Connection values:

```text
JDBC URL: jdbc:h2:mem:processdb
User: sa
Password: <empty>
```

---

## Learning References

The related official documentation is curated in [`open-learning-resources`](https://github.com/DataTideHH/open-learning-resources):

- [Spring Boot Documentation](https://github.com/DataTideHH/open-learning-resources/tree/main/resources/java/spring-boot-documentation)
- [Spring Data JPA Documentation](https://github.com/DataTideHH/open-learning-resources/tree/main/resources/java/spring-data-jpa-documentation)
- [Apache Maven and Maven Wrapper Documentation](https://github.com/DataTideHH/open-learning-resources/tree/main/resources/java/apache-maven-and-wrapper-documentation)
- [GitHub Actions Documentation](https://github.com/DataTideHH/open-learning-resources/tree/main/resources/git/github-actions-documentation)

This keeps the project implementation connected to official primary documentation without mirroring dynamic framework documentation locally.

---

## GitHub Pages Project Site

This repository includes a small project landing page under:

```text
docs/index.md
```

Published project page:

```text
https://datatidehh.github.io/spring-boot-process-api-basics/
```

GitHub Pages settings:

```text
Source: Deploy from a branch
Branch: main
Folder: /docs
```

---

## What This Demonstrates

This repository demonstrates a small but realistic backend foundation:

- Java 21 project workflow
- Spring Boot application structure
- REST endpoint and HTTP-status design
- JSON request and response handling
- paginated list queries and status filtering
- layered backend organization
- request validation aligned with persistence constraints
- standard Problem Detail error responses
- explicit transaction boundaries
- JPA dirty checking for managed updates
- persistence abstraction with Spring Data JPA
- restrained parameterized logging
- local development and testing with H2
- automated integration testing
- reproducible Maven builds
- GitHub Actions CI
- public portfolio documentation for a focused learning project

---

## Limitations

This is a learning project.

It does not include production database configuration, Docker deployment, authentication and authorization, a frontend UI, cloud deployment, metrics, tracing or enterprise-scale operational error handling.

These omissions are intentional. The current scope is limited to a clean, understandable and tested Spring Boot REST API baseline.

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
