# Product Management REST API

## Overview
This project is a RESTful API built using Java 17 and Spring Boot.
It supports full CRUD operations for Products and Items with JWT-based authentication.

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL / PostgreSQL
- Spring Security (JWT + Refresh Token)
- JUnit 5 & Mockito
- Swagger / OpenAPI
- Docker & Docker Compose

## Architecture
The project follows layered architecture:

Controller → Service → Repository → Database

Main packages:
- controller
- service
- repository
- entity
- dto
- security
- exception
- config

## API Base URL
/api/v1/

## Running with Docker

### Build and Run
docker-compose up --build

Application runs at:
http://localhost:8080

Swagger:
http://localhost:8080/swagger-ui.html

## Running Without Docker

mvn clean install
mvn spring-boot:run

## Features
- CRUD operations
- JWT authentication
- Role-based authorization
- Input validation
- Global exception handling
- Pagination support
- Unit and integration testing

## Author
Rohan Patil