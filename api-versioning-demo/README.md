# 🚀 API Versioning Strategies with Spring Boot 4

A professional demonstration of **Spring Boot 4.0 native API versioning**, showcasing all supported strategies:
**Path Segment**, **Request Header**, **Query Parameter**, and **Media Type (Content Negotiation)** — with clean DTO evolution and backward compatibility.

---

## 🧭 Why API Versioning Matters

As APIs evolve, maintaining **backward compatibility** while introducing new features becomes critical.  
Spring Boot 4 introduces **first-class API versioning**, eliminating custom hacks and providing a **framework-level, standardized solution**.

This project demonstrates how to:
- Evolve APIs safely
- Support multiple versions simultaneously
- Keep contracts clean and predictable
- Scale API design across teams

---

## ✨ Key Features

✅ Native API versioning (Spring Boot 4 / Spring Framework 7)  
✅ Multiple versioning strategies demonstrated  
✅ DTO-based contract evolution  
✅ Custom `ApiVersionParser` for flexible client inputs  
✅ Clean separation between domain and API models  
✅ Zero breaking changes between versions


## 🛠️ Versioning Strategies

This project implements **four distinct versioning strategies**:

### 1️⃣ Path Segment Versioning (URI Versioning)

Embed the version directly in the URL path.

```
GET /api/v1/users
GET /api/v2/users
```

### 2️⃣ Request Header Versioning

Specify the version through a custom HTTP header.

```
GET /api/users
Headers: X-API-Version: 1.0
```

### 3️⃣ Query Parameter Versioning

Pass the version as a query parameter.

```
GET /api/users/list?version=1.0
GET /api/users/list?version=v2
```

### 4️⃣ Media Type Versioning (Content Negotiation)

Specify the version through the `Accept` header using media type parameters.

```
GET /api/users/media
Headers: Accept: application/json;version=1.0
```


## 🚦 Getting Started

### Prerequisites

- Java 21+
- Spring Boot 4.0+
- Maven or Gradle


## 📡 API Endpoints

### Path Segment Versioning

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users` | Get all users (v1 format) |
| GET | `/api/v2/users` | Get all users (v2 format) |

### Request Header Versioning

| Method | Endpoint | Header | Description |
|--------|----------|--------|-------------|
| GET | `/api/users` | `X-API-Version: 1.0` | Get users v1 |
| GET | `/api/users` | `X-API-Version: 2.0` | Get users v2 |

### Query Parameter Versioning

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/list?version=1.0` | Get users v1 |
| GET | `/api/users/list?version=v2` | Get users v2 |

### Media Type Versioning

| Method | Endpoint | Accept Header | Description |
|--------|----------|---------------|-------------|
| GET | `/api/users/media` | `application/json;version=1.0` | Get users v1 |
| GET | `/api/users/media` | `application/json;version=2.0` | Get users v2 |

## 📂 Project Structure

```
src/main/java/
├── model/
│   └── User.java                 # Core domain model
├── dto/
│   ├── UserDTOv1.java           # Version 1 DTO (name as single field)
│   └── UserDTOv2.java           # Version 2 DTO (firstName + lastName)
├── mapper/
│   └── UserMapper.java          # Bidirectional DTO mapper
├── repository/
│   └── UserRepository.java      # Data access layer
├── controller/
│   └── UserController.java      # REST endpoints
├── config/
│   ├── WebConfig.java           # API versioning configuration
│   └── ApiVersionParser.java   # Custom version parser
```

