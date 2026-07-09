# Spring Boot Process API Basics

**Java 21 · Spring Boot · REST API · Spring Data JPA · H2 · Validation · Maven**

Small Java 21 / Spring Boot learning project that exposes process-check data through a simple REST API.

Project page: https://datatidehh.github.io/spring-boot-process-api-basics/

This repository is part of my broader **DataTideHH** portfolio. It supports my learning path toward **Data/BI and process-oriented IT roles** by connecting Java/Spring backend basics with structured operational process data.

---

## Purpose

The project focuses on a deliberately small backend use case: storing and exposing **process-check records**.

It demonstrates:

- REST endpoints for a small process-related resource
- layered backend structure with controller, service and repository
- request validation
- basic persistence with Spring Data JPA
- an H2 in-memory database for local development
- a local Maven build and test workflow
- synthetic sample data for process-oriented API testing

The goal is not to present a large enterprise backend. The goal is to document a clean first step from Java basics toward a small Spring Boot REST API that handles structured process data.

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
| Framework | Spring Boot | REST API application framework |
| API layer | Spring Web | HTTP endpoints and JSON responses |
| Persistence | Spring Data JPA | Repository abstraction and entity persistence |
| Database | H2 | In-memory local development database |
| Validation | Jakarta Validation | Request validation for incoming data |
| Build tool | Maven Wrapper | Reproducible local build workflow |

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

---

## API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| `GET` | `/api/process-checks` | Return all process-check records |
| `GET` | `/api/process-checks/{id}` | Return one process-check record by ID |
| `POST` | `/api/process-checks` | Create a new process-check record |
| `PUT` | `/api/process-checks/{id}` | Update an existing process-check record |
| `DELETE` | `/api/process-checks/{id}` | Delete a process-check record |

---

## Example Process-Check Record

```json
{
  "id": 1,
  "processName": "Daily sales import",
  "owner": "Data Operations",
  "status": "OK",
  "lastCheckedAt": "2026-07-10T00:25:00",
  "slaMinutes": 60
}
```

---

## Run Locally

From the repository root:

```bash
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080/api/process-checks
```

At first startup, the API returns an empty JSON array because the H2 database is empty:

```json
[]
```

---

## Build and Test

Run:

```bash
./mvnw clean package
```

The project has been built and smoke-tested locally with Java 21 and the Maven Wrapper.

---

## Manual API Test Flow

The API was manually tested with curl for the full CRUD flow:

```text
POST   /api/process-checks       create a process-check record
GET    /api/process-checks       list all process-check records
GET    /api/process-checks/1     read one process-check record
PUT    /api/process-checks/1     update one process-check record
DELETE /api/process-checks/1     delete one process-check record
```

Example test data:

```text
processName: Daily sales import
owner: Data Operations
status: OK / WARNING
lastCheckedAt: 2026-07-10T00:25:00
slaMinutes: 60
```

After deletion, the list endpoint returns an empty JSON array again.

---

## H2 Database Note

This project uses an **H2 in-memory database** for local development and API testing.

That means:

- no external database server is required
- the database is recreated when the application starts
- inserted records are lost when the application stops
- this is suitable for a small learning project, not for production persistence

The H2 console is available locally while the application is running:

```text
http://localhost:8080/h2-console
```

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
- REST endpoint design
- JSON request and response handling
- CRUD operations
- layered backend organization
- request validation
- basic persistence abstraction with Spring Data JPA
- local development with H2
- Maven build and test workflow
- public portfolio documentation for a focused learning project

---

## Limitations

This is a learning project.

It does not include production database configuration, Docker deployment, a frontend UI, cloud deployment, monitoring infrastructure or enterprise-scale error handling.

These omissions are intentional. The current scope is limited to a clean, understandable Spring Boot REST API baseline.

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
