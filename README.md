Product Management REST API
Overview

This project is a RESTful API built using Java 17 and Spring Boot.
It provides full CRUD operations for managing Products and their Items.
The API is secured using JWT authentication with refresh token support and follows RESTful design best practices including API versioning.

Base URL:

http://localhost:8080/api/v1

Tech Stack

Java 17

Spring Boot

Spring Data JPA (Hibernate)

MySQL

Spring Security (JWT + Refresh Token)

JUnit 5 & Mockito

Swagger / OpenAPI

Docker & Docker Compose

H2 (for testing)

Architecture

The project follows a layered architecture:

Controller → Service → Repository → Database

Package Structure

controller – REST endpoints

service – Business logic layer

repository – Data access layer

entity – JPA entities

dto – Request and Response objects

security – JWT authentication and authorization logic

exception – Global exception handling

config – Configuration classes

This structure ensures separation of concerns, maintainability, and scalability.

Security

JWT Access Token authentication

Refresh Token mechanism

Role-based authorization

Secure endpoints protected using Spring Security

CORS configuration enabled

Authentication Flow

User logs in using /api/v1/auth/login

Server returns Access Token and Refresh Token

Access Token is sent in request header:
Authorization: Bearer <access_token>

When Access Token expires, use /api/v1/auth/refresh to generate a new token

All product endpoints require authentication except login and refresh.

API Endpoints
Product APIs

GET /api/v1/products
GET /api/v1/products/{id}
POST /api/v1/products
PUT /api/v1/products/{id}
DELETE /api/v1/products/{id}
GET /api/v1/products/{id}/items

Pagination Example

GET /api/v1/products?page=0&size=10

Swagger Documentation

Swagger UI is available at:

http://localhost:8080/swagger-ui.html

Database Schema
Product Table

CREATE TABLE product (
id INT PRIMARY KEY AUTO_INCREMENT,
product_name VARCHAR(255) NOT NULL,
created_by VARCHAR(100) NOT NULL,
created_on TIMESTAMP NOT NULL,
modified_by VARCHAR(100),
modified_on TIMESTAMP
);

Item Table

CREATE TABLE item (
id INT PRIMARY KEY AUTO_INCREMENT,
product_id INT NOT NULL,
quantity INT NOT NULL,
FOREIGN KEY (product_id) REFERENCES product(id)
);

Running with Docker

Build and start containers:

docker-compose up --build

Application runs at:

http://localhost:8080

Swagger:

http://localhost:8080/swagger-ui.html

Stop containers:

docker-compose down

Running Without Docker

Clone repository:

git clone https://github.com/Rohan2422/product-api-assignment.git

cd product-api-assignment

Build project:

mvn clean install

Run application:

mvn spring-boot:run

Testing

Run tests:

mvn test

Unit tests implemented using JUnit 5 and Mockito

Integration tests use H2 in-memory database

Features

Full CRUD operations

RESTful API design with versioning

Standardized error handling

Input validation using Jakarta Validation

JWT authentication with refresh token

Role-based access control

Pagination support

Dockerized deployment

Unit and integration testing

Author

Rohan Patil