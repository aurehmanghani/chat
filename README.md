# Chat API

A Spring Boot backend for managing chat sessions and messages with health checks and Swagger documentation.

---

## Table of Contents

- [API Endpoints](#api-endpoints)  
  - [Sessions](#sessions)  
  - [Messages](#messages)  
  - [Health](#health)  
- [Docs](#docs)  
- [Getting Started](#getting-started)  
- [Running with Docker](#running-with-docker)  

---

## API Endpoints

### Sessions

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/sessions` | Create new chat session |
| GET | `/api/sessions?user_id={userId}&page=1&limit=50&include_deleted=false` | List sessions |
| GET | `/api/sessions/{sessionId}?user_id={userId}` | Get session by ID |
| PATCH | `/api/sessions/{sessionId}` | Update session (title / favorite flag) |
| DELETE | `/api/sessions/{sessionId}?user_id={userId}&hard_delete=false` | Soft or hard delete |

### Messages

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/messages` | Create message (user/assistant) |
| GET | `/api/messages/session/{sessionId}?user_id={userId}&page=1&limit=100` | List messages for a session |
| GET | `/api/messages/{messageId}?user_id={userId}` | Get message by ID |
| PATCH | `/api/messages/{messageId}` | Update message (content/context/metadata) |
| DELETE | `/api/messages/{messageId}?user_id={userId}` | Delete message |

### Health

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Full health check |
| GET | `/api/health/ready` | Readiness probe |
| GET | `/api/health/live` | Liveness probe |

---

## Docs

- **Swagger UI:** `/swagger-ui.html`

---

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/your-username/rag-chat-springboot.git
cd rag-chat-springboot


