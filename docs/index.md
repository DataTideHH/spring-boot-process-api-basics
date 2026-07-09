---
title: Spring Boot Process API Basics
description: Small Java 21 / Spring Boot REST API portfolio project
---

# Spring Boot Process API Basics

**Small Java 21 / Spring Boot REST API project exposing process-check data through a layered backend structure, validation and H2 persistence.**

[View repository](https://github.com/DataTideHH/spring-boot-process-api-basics) · [Read the full README](https://github.com/DataTideHH/spring-boot-process-api-basics/blob/main/README.md) · [DataTideHH portfolio](https://datatidehh.de/)

---

## Project purpose

This project is a deliberately compact backend learning project.

It demonstrates how process-related records can be represented, validated, persisted and exposed through a small REST API using Spring Boot.

The goal is not to present a production service or an enterprise backend system. The goal is to document a clean first step from Java basics toward a small enterprise-style REST API that supports structured operational data.

---

## Portfolio context

The project supports a Data/BI and process-analysis learning path by connecting backend API fundamentals with structured process data.

For Data/BI and process analysis work, APIs are an important interface between operational systems and downstream data workflows. This project is useful as a supporting IT foundation because it shows how process-related records can move through a simple backend structure.

---

## What the project demonstrates

- Java 21 project setup
- Spring Boot REST API basics
- controller, service and repository separation
- Spring Data JPA repository usage
- request validation
- H2 in-memory persistence
- CRUD endpoints for process-check data
- local Maven build and run workflow

---

## API scope

The API exposes a small process-check resource.

```text
GET    /api/process-checks
GET    /api/process-checks/{id}
POST   /api/process-checks
PUT    /api/process-checks/{id}
DELETE /api/process-checks/{id}
```

---

## Example process-check record

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

## Local usage

Run the application from the repository root:

```bash
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080/api/process-checks
```

Build and test locally:

```bash
./mvnw clean package
```

---

## Data and limitations

The project uses an H2 in-memory database. Data is reset when the application stops.

The sample data is synthetic and does not contain personal, customer or production data.

This is a learning project and does not include authentication, production database configuration, cloud deployment or enterprise-scale error handling.
