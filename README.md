# Chat API

A Spring Boot backend for managing chat sessions and messages with full CRUD operations, health checks, and Swagger documentation.

---

## Table of Contents

- [Features](#features)  
- [API Endpoints](#api-endpoints)  
  - [Sessions](#sessions)  
  - [Messages](#messages)  
  - [Health](#health)  
- [Swagger Documentation](#swagger-documentation)  
- [Getting Started](#getting-started)  
- [Running with Docker](#running-with-docker)

---

## Features

- Create, read, update, delete chat sessions and messages
- Soft and hard deletes for sessions
- Pagination support for listing sessions and messages
- Health checks: full, readiness, and liveness
- Swagger/OpenAPI documentation

---

## API Endpoints

### Sessions

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/sessions` | Create a new chat session |
| GET | `/api/sessions?user_id={userId}&page=1&limit=50&include_deleted=false` | List chat sessions for a user |
| GET | `/api/sessions/{sessionId}?user_id={userId}` | Get a session by ID |
| PATCH | `/api/sessions/{sessionId}` | Update session (title / favorite flag) |
| DELETE | `/api/sessions/{sessionId}?user_id={userId}&hard_delete=false` | Soft or hard delete a session |

#### Example: Create a session
```bash
POST /api/sessions
Content-Type: application/json

{
  "userId": "12345",
  "title": "New Chat Session"
}
