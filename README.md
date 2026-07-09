# Spring Boot Process API Basics

**Java 21 · Spring Boot · REST API · Spring Data JPA · H2 · Validation**

This repository is a small Spring Boot learning project that demonstrates how process-related data can be exposed through a simple REST API using Java 21, Spring Web, Spring Data JPA, validation and an H2 database.

It is part of my broader DataTideHH portfolio and supports my learning path toward Data/BI and process-oriented IT roles by connecting backend API basics with structured operational data.

## Purpose

The project focuses on a deliberately small backend use case: storing and exposing process-check records.

It demonstrates REST endpoints, layered structure, validation, basic persistence, Java 21 / Spring Boot workflow and process-oriented sample data.

## Architecture

Controller -> Service -> Repository -> H2 Database

## API Endpoints

GET /api/process-checks

GET /api/process-checks/{id}

POST /api/process-checks

PUT /api/process-checks/{id}

DELETE /api/process-checks/{id}

## Run Locally

Run:

./mvnw spring-boot:run

Then open:

http://localhost:8080/api/process-checks

## Portfolio Context

This is not intended to be a large enterprise backend.

The goal is to show a clean first step from Java basics toward a small enterprise-style REST API that handles structured process data.
