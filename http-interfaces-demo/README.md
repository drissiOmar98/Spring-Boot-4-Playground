# 🚀 Spring Boot 4 HTTP Interfaces – Clean Service-to-Service Communication

Spring Framework 7 and Spring Boot 4 are transforming how Java developers handle **service-to-service communication**.

This project is a **hands-on tutorial** that demonstrates how the new **HTTP Interfaces** feature eliminates boilerplate code, improves readability, and makes REST clients **type-safe, declarative, and maintainable**.

We build a **Todo & Post API** that communicates with an external service (**JSONPlaceholder**) and compare:

- ❌ Traditional manual HTTP client implementations
- ✅ Modern declarative HTTP Interfaces in Spring Boot 4

---

## 🌐 What Are HTTP Interfaces?

**HTTP Interfaces** allow you to define an HTTP client as a **plain Java interface**, where each method represents an HTTP call.

Instead of writing imperative HTTP client code, you simply **declare what the API looks like**, and Spring takes care of the rest.

---

### 🧩 Example: Declarative Todo HTTP Client

```java
@HttpExchange("/todos")
public interface TodoService {

    @GetExchange
    List<Todo> findAll();

    @GetExchange("/{id}")
    Todo findById(@PathVariable Integer id);

    @PostExchange
    Todo create(@RequestBody Todo todo);
}
```
---

# 🎯 What You’ll Learn

✅ How to migrate from **RestTemplate** to **RestClient** (the future of Spring REST communication)  
✅ How to create **declarative HTTP clients** with zero implementation code  
✅ How **@ImportHttpServices** removes complex proxy factory boilerplate  
✅ How to build a **complete CRUD API** with minimal configuration  
✅ Advanced configuration using **multiple HTTP service groups**

---

## 📌 Key Takeaways

🔹 `RestTemplate` is officially **deprecated**  
🔹 Spring Boot 4 requires **explicit imports** due to modularization  
🔹 One annotation replaces multiple configuration beans  
🔹 Perfect fit for **microservices** and **API-first architectures**

## ✨ Why This Matters

Traditional HTTP clients require:
- Manual request creation
- Explicit URI handling
- Response deserialization
- Repetitive boilerplate code

HTTP Interfaces provide a:
✅ **Declarative** approach  
✅ **Type-safe** API contract  
✅ **Cleaner** and more readable code  
✅ **Maintainable** client layer

## 🏗️ Project Overview

This project exposes REST endpoints that proxy requests to the public **JSONPlaceholder API** using two approaches:

| Layer | Responsibility |
|-----|---------------|
| Controller | Exposes REST endpoints |
| Service (Traditional) | Manual RestClient usage |
| Service (Modern) | Declarative HTTP Interfaces |
| Configuration | Traditional vs Modern setup |
| Model | Immutable records |

---

## 📂 Project Structure

```text
src/main/java
└── com/example/demo
    ├── controller
    │   ├── TodoController.java
    │   └── PostController.java
    ├── service
    │   ├── traditional
    │   │   ├── TraditionalTodoService.java
    │   │   └── TraditionalPostService.java
    │   └── http
    │       ├── TodoService.java
    │       └── PostService.java
    ├── config
    │   ├── TraditionalConfig.java
    │   └── ModernConfig.java
    └── model
        ├── Todo.java
        └── Post.java
```

## 🧩 Why HTTP Interfaces Exist

Before Spring Boot 4, calling external APIs often meant writing:
- Low-level HTTP client code
- Repetitive request/response handling
- Manual deserialization
- Large configuration classes

This led to:
❌ Verbose code  
❌ Tight coupling  
❌ Harder refactoring  
❌ Inconsistent HTTP clients across teams

### 💡 The Goal

Spring HTTP Interfaces were introduced to:
- Treat HTTP APIs like **Java interfaces**
- Enforce **compile-time safety**
- Eliminate implementation classes
- Align REST calls with **domain-driven design**

➡️ If you can design an interface, Spring can now call the API for you.


## 🧠 Mental Model: How HTTP Interfaces Work

Think of HTTP Interfaces as **Feign-like clients**, but:
✅ Built into Spring  
✅ Strongly typed  
✅ Configuration-driven  
✅ Framework-native

### 🔄 What Happens at Runtime?

1️⃣ Spring scans interfaces annotated with `@HttpExchange`  
2️⃣ A runtime proxy is generated  
3️⃣ Method calls are translated into HTTP requests  
4️⃣ Responses are automatically deserialized

## 🧰 Prerequisites

Make sure your environment meets these requirements:

- ☕ **Java 21+** – for modern language features like records
- 🌱 **Spring Boot 4.0.0+** – supports HTTP Interfaces
- 🧩 **Spring Framework 7.0** – provides declarative HTTP client APIs
- 📦 **Maven 3.9+** – for building and managing dependencies

## 🌐 API Endpoints

This project exposes REST endpoints for **Todos** and **Posts** using Spring Boot 4 HTTP Interfaces.

---

### ✅ Todos

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/todos` | Retrieve all todos |
| GET    | `/api/todos/{id}` | Retrieve a single todo by ID |
| POST   | `/api/todos` | Create a new todo |
| PUT    | `/api/todos/{id}` | Update an existing todo |
| DELETE | `/api/todos/{id}` | Delete a todo |

---

### ✅ Posts

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/posts` | Retrieve all posts |
| GET    | `/api/posts/{id}` | Retrieve a single post by ID |
| POST   | `/api/posts` | Create a new post |
| PUT    | `/api/posts/{id}` | Update an existing post |
| DELETE | `/api/posts/{id}` | Delete a post |

---

💡 **Tip:** All endpoints delegate calls to external services via declarative HTTP Interfaces, keeping your controller layer clean and type-safe.

## 🧪 Testing with `.http` Files

This project includes **`.http` files** to quickly test your API endpoints directly from:

- **IntelliJ IDEA** (built-in HTTP client)
- **VS Code** (with REST Client extension)

No need for Postman or curl — just run the requests in your editor.

---

### ✨ Example: `todo.http`

```http
### Get all todos
GET http://localhost:8080/api/todos
Accept: application/json

### Get todo by ID
GET http://localhost:8080/api/todos/1
Accept: application/json

### Create a new todo
POST http://localhost:8080/api/todos
Content-Type: application/json

{
  "userId": 1,
  "title": "Learn Spring Boot HTTP Interfaces",
  "completed": false
}
```

## ⚡ Spring Framework 7 HTTP Client Enhancements

Spring Framework 7 builds on the HTTP Interfaces introduced in Spring 6, adding powerful features that make **HTTP client development cleaner, more modular, and declarative**.

---

### 🏗️ Registry Layer & HTTP Service Groups

Spring 7 introduces a **registry layer** over `HttpServiceProxyFactory`:

- 📌 Provides a **central configuration model** to register HTTP interfaces
- 📌 Automatically initializes the HTTP client infrastructure
- 📌 Creates and registers **client proxies as Spring beans**
- 📌 Offers access to all client proxies via `HttpServiceProxyRegistry`

This makes managing **multiple HTTP clients** across a project simple and consistent.

---

### 📝 `@ImportHttpServices` Annotation

Spring 7 adds **`@ImportHttpServices`** to declaratively register HTTP service groups:

- Reduces boilerplate configuration
- Automatically imports multiple HTTP interfaces as Spring beans
- Supports grouping for **different APIs** with separate settings (URLs, headers, authentication)

```java
@Configuration
@ImportHttpServices(types = { TodoService.class, PostService.class })
public class ModernConfig { }
```

## 📚 Resources

Here are some helpful references to learn more and explore the APIs used in this project:

- 🌐 [HTTP Service Client Enhancements - Spring Blog](https://spring.io/blog/2025/09/23/http-service-client-enhancements) – Official Spring blog post on HTTP Interfaces and Spring 7 enhancements.  
- 🌐 [JSONPlaceholder API](https://jsonplaceholder.typicode.com/) – Free public REST API for testing and prototyping.
