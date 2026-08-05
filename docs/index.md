---
title: Spring Boot Process API Basics
description: Small Java 21 / Spring Boot REST API portfolio project
---

# Spring Boot Process API Basics

**Small Java 21 / Spring Boot REST API project exposing validated and paginated process-check data through a layered backend structure, automated tests, H2 persistence and a prepared local PostgreSQL service.**

[View repository](https://github.com/DataTideHH/spring-boot-process-api-basics) · [Read the full README](https://github.com/DataTideHH/spring-boot-process-api-basics/blob/main/README.md) · [View CI](https://github.com/DataTideHH/spring-boot-process-api-basics/actions/workflows/ci.yml) · [DataTideHH portfolio](https://datatidehh.de/)

---

## Project purpose

This project is a deliberately compact backend learning project.

It demonstrates how process-related records can be represented, validated, persisted and exposed through a small REST API using Spring Boot.

The goal is not to present a production service or an enterprise backend system. The goal is to document a clean first step from Java basics toward a small layered REST API with explicit HTTP behavior, bounded list queries, persistence and automated verification.

---

## Portfolio context

The project supports a Data/BI and process-analysis learning path by connecting backend API fundamentals with structured process data.

For Data/BI and process analysis work, APIs are an important interface between operational systems and downstream data workflows. This project is useful as a supporting IT foundation because it shows how process-related records move through a simple backend structure.

It follows the [IPv4 Subnet Calculator Multilang](https://datatidehh.github.io/ipv4-subnet-calculator-multilang/) in the Java learning progression: the subnet project demonstrates a compact, tested command-line implementation and shared cross-language contract, while this repository adds framework structure, HTTP endpoints, validation, persistence and CI.

---

## What the project demonstrates

- Java 21 and Spring Boot 4.1
- controller, service and repository separation
- Spring Data JPA repository usage
- request and response records
- Jakarta Validation aligned with entity constraints
- H2 in-memory persistence for the running application and tests
- a local PostgreSQL 18 service provisioned with Docker Compose
- CRUD endpoints for process-check data
- status filtering and pagination
- stable page metadata through Spring Data `PagedModel`
- standard `ProblemDetail` error responses
- explicit transaction boundaries and JPA dirty checking
- restrained parameterized logging for write operations
- integration tests with Spring Boot Test and MockMvc
- reproducible Maven Wrapper builds
- GitHub Actions verification on Java 21

---

## API contract

| Method | Endpoint | Result | Purpose |
|---|---|---:|---|
| `GET` | `/api/process-checks?page=0&size=20` | `200` | List one page of records |
| `GET` | `/api/process-checks?status=OK&page=0&size=20` | `200` | Filter and page by status |
| `GET` | `/api/process-checks/{id}` | `200` | Read one record |
| `POST` | `/api/process-checks` | `201` | Create a record and return `Location` |
| `PUT` | `/api/process-checks/{id}` | `200` | Update a record |
| `DELETE` | `/api/process-checks/{id}` | `204` | Delete a record |

The list endpoint defaults to 20 records, sorts by `lastCheckedAt` descending and caps requested page sizes at 100.

Invalid input returns `400 Bad Request`. Unknown record IDs return `404 Not Found` as `application/problem+json`.

---

## Example paged response

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

`processName` and `owner` are required and limited to 120 characters. The JPA entity mirrors these length and nullability rules.

---

## Local usage

### Run the application with H2

macOS or Linux:

```bash
./mvnw spring-boot:run
./mvnw clean verify
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
.\mvnw.cmd clean verify
```

The API is available at:

```text
http://localhost:8080/api/process-checks
```

The H2 database starts empty and is reset when the application stops.

### Start the local PostgreSQL service

The repository includes a root-level `compose.yaml`:

```bash
docker compose up -d
docker compose ps
```

Stop the service without deleting its named data volume:

```bash
docker compose down
```

The service exposes PostgreSQL 18 on port `5432` with database `processdb` and user `processapp`.

The Spring Boot application is not connected to PostgreSQL yet. H2 remains the active application and test database until dedicated Spring profiles and PostgreSQL configuration are added.

---

## Verification

The integration suite covers pagination, sorting, status filtering, lookup by ID, creation, blank and oversized input, updates, deletion, persistence effects and `404` Problem Detail responses.

The GitHub Actions workflow runs `clean verify` with Eclipse Temurin Java 21 for pull requests and pushes to `main`.

---

## Official learning references

The matching primary documentation is curated in [Open Learning Resources](https://github.com/DataTideHH/open-learning-resources):

- [Spring Boot Documentation](https://github.com/DataTideHH/open-learning-resources/tree/main/resources/java/spring-boot-documentation)
- [Spring Data JPA Documentation](https://github.com/DataTideHH/open-learning-resources/tree/main/resources/java/spring-data-jpa-documentation)
- [Apache Maven and Maven Wrapper Documentation](https://github.com/DataTideHH/open-learning-resources/tree/main/resources/java/apache-maven-and-wrapper-documentation)
- [GitHub Actions Documentation](https://github.com/DataTideHH/open-learning-resources/tree/main/resources/git/github-actions-documentation)

---

## Related DataTideHH project pages

- [IPv4 Subnet Calculator Multilang](https://datatidehh.github.io/ipv4-subnet-calculator-multilang/) — one IPv4/CIDR specification implemented and tested in Java, C++ and Python
- [Cisco Switching Lab](https://datatidehh.github.io/cisco-switching-lab/) — physical networking and CCNA-oriented switching context
- [Music Production Data Lab](https://datatidehh.github.io/music-production-data-lab/) — public-safe data modeling, SQL/Python workflow and Power BI reporting layer
- [Network Operations Data Lab](https://datatidehh.github.io/network-operations-data-lab/) — public-safe operational IT data, Python, SQL and data-quality workflow

---

## Data and limitations

The running application currently uses an H2 in-memory database. Data is reset when the application stops.

Synthetic fixtures are created inside the integration tests. The application does not load sample records during normal startup.

Docker Compose prepares a persistent local PostgreSQL service, but the application does not use it yet.

This is a learning project with a deliberately limited scope. It does not yet include Spring profiles for PostgreSQL, database migrations, authentication, cloud operation, monitoring infrastructure or enterprise-scale operation.
