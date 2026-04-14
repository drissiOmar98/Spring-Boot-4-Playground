# 🚀 Spring Boot 4 Playground

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot 4"/>
  <img src="https://img.shields.io/badge/Java-21+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21+"/>
  <img src="https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="License"/>
  <img src="https://img.shields.io/badge/Status-Active-success?style=for-the-badge" alt="Status"/>
</p>

<p align="center">
  A comprehensive, hands-on <strong>Spring Boot 4</strong> playground that explores and demonstrates modern Java practices — from declarative HTTP clients and API versioning strategies to null-safety, observability, Spring AI, and AOT optimizations.
</p>

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Repository Structure](#-repository-structure)
- [Modules](#-modules)
  - [HTTP Interfaces Demo](#1-️-http-interfaces-demo)
  - [API Versioning](#2--api-versioning)
  - [Null Safety with JSpecify & NullAway](#3--null-safety---jspecify--nullaway)
  - [OpenTelemetry Observability](#4--opentelemetry-demo)
  - [REST Client Error Handling](#5--rest-client-error-handling)
  - [REST Test Client](#6--rest-test-client)
  - [REST vs MockMvc Testing](#7--rest-vs-mock-web-testing)
  - [Jackson 3 JSON Mapping](#8--jackson3-json-mapping)
  - [JMS Messaging](#9--messaging-jms)
  - [Dynamic Bean Registration](#10--dynamic-bean-registration)
  - [Spring AI 2 Demo](#11--spring-ai-2-demo)
  - [Spring Data AOT](#12--spring-data-aot)
  - [Spring Native Resilience](#13--spring-native-resilience)
  - [Spring Security MFA](#14--spring-security-mfa)
- [Technologies Used](#-technologies-used)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [Running Individual Modules](#-running-individual-modules)
- [Key Spring Boot 4 Features Showcased](#-key-spring-boot-4-features-showcased)
- [Author](#-author)

---

## 🌟 Overview

This repository serves as a **learning playground and reference implementation** for Spring Boot 4 capabilities. Each sub-module is a self-contained Spring Boot application that focuses on a specific feature, pattern, or best practice introduced or improved in the Spring Boot 4 / Spring Framework 7 ecosystem.

Whether you're a **beginner** exploring Spring Boot 4 for the first time, or an **experienced developer** looking for a reference implementation of cutting-edge features, this playground has something for you.

### ✨ What makes this special?

- 🔬 **19+ focused modules** — each module demonstrates one concept cleanly
- 🏗️ **Real-world patterns** — not toy examples, but production-ready approaches
- 📐 **Modern Java practices** — leverages Java 21+ features throughout
- 📦 **Self-contained** — each module runs independently
- 🧪 **Test-driven** — testing strategies are first-class citizens

---

## 📁 Repository Structure

```
Spring-Boot-4-Playground/
│
├── api-versioning-demo/               # URI path segment versioning
├── api-versioning-header/             # HTTP header-based versioning
├── api-versioning-media-type-param/   # Media type parameter versioning
├── api-versioning-path-segment/       # Path segment versioning strategy
├── api-versioning-query-param/        # Query parameter versioning
│
├── dynamic-bean-registration/         # Runtime bean registration
├── http-interfaces-demo/              # Declarative HTTP clients
├── jackson3-json-mapping/             # Jackson 3 new features
├── messaging-jms/                     # JMS messaging with Spring
├── null-safety-jspecify-nullaway/     # Compile-time null safety
├── opentelemetry-demo/                # Distributed tracing & metrics
├── rest-client-error-handling/        # Robust error handling patterns
├── rest-test-client/                  # RestTestClient usage
├── rest-vs-mock-web-testing/          # Testing strategy comparison
├── spring-ai-2-demo/                  # Spring AI 2.x integration
├── spring-data-aot/                   # Spring Data with AOT optimizations
├── spring-native-resilience/          # Native image + resilience patterns
└── spring-security-mfa/              # Multi-Factor Authentication
```

---

## 📦 Modules

### 1. 🌐 HTTP Interfaces Demo

**Path:** `http-interfaces-demo/`

Demonstrates Spring Boot 4's **declarative HTTP client** support using the `@HttpExchange` annotation family. This is the Spring-native alternative to Feign clients — no third-party dependencies required.

**Key concepts:**
- `@HttpExchange`, `@GetExchange`, `@PostExchange`, `@PutExchange`, `@DeleteExchange`
- Registering HTTP interface proxies with `HttpServiceProxyFactory`
- Integration with `RestClient` and `WebClient` backends
- Error handling in declarative clients

**Example:**
```java
@HttpExchange("/users")
public interface UserClient {

    @GetExchange("/{id}")
    UserDto getUser(@PathVariable Long id);

    @PostExchange
    UserDto createUser(@RequestBody UserDto user);
}
```

---

### 2. 🔢 API Versioning

**Paths:** `api-versioning-demo/`, `api-versioning-header/`, `api-versioning-media-type-param/`, `api-versioning-path-segment/`, `api-versioning-query-param/`

A comprehensive exploration of **5 different API versioning strategies** in Spring Boot 4. Each module implements the same API with a different versioning approach, allowing direct comparison.

#### Versioning Strategies Covered:

| Module | Strategy | Example |
|---|---|---|
| `api-versioning-path-segment` | URI Path | `/api/v1/users` vs `/api/v2/users` |
| `api-versioning-query-param` | Query Parameter | `/api/users?version=1` |
| `api-versioning-header` | Custom Header | `X-API-Version: 2` |
| `api-versioning-media-type-param` | Accept Header | `Accept: application/vnd.app.v2+json` |
| `api-versioning-demo` | Combined Demo | Showcases all strategies |

**When to use which strategy:**
- 🛣️ **Path versioning** — most visible, great for public APIs
- 🔍 **Query param** — easy to test in browser, less RESTful
- 📨 **Header versioning** — clean URLs, used by GitHub API
- 📄 **Media type** — most RESTful, preferred by purists

---

### 3. 🛡️ Null Safety — JSpecify & NullAway

**Path:** `null-safety-jspecify-nullaway/`

Demonstrates **compile-time null safety** using [JSpecify](https://jspecify.dev/) annotations and [NullAway](https://github.com/uber/NullAway) — a fast Checker Framework alternative that plugs into the Java compiler via Error Prone.

**Key concepts:**
- `@Nullable` and `@NonNull` annotations from JSpecify
- Configuring NullAway with the Error Prone compiler plugin
- Null-safe service and controller layers
- Eliminating `NullPointerException`s at compile time

**Maven configuration:**
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <configuration>
    <compilerArgs>
      <arg>-XDcompilePolicy=simple</arg>
      <arg>-Xplugin:ErrorProne -XepOpt:NullAway:AnnotatedPackages=com.example</arg>
    </compilerArgs>
  </configuration>
</plugin>
```

> 💡 Spring Boot 4 / Spring Framework 7 now ships with JSpecify null-safety annotations natively across the framework codebase.

---

### 4. 📊 OpenTelemetry Demo

**Path:** `opentelemetry-demo/`

Showcases **full observability** with the new `spring-boot-starter-opentelemetry` that ships in Spring Boot 4 — covering distributed tracing, metrics, and log correlation out of the box via OTLP.

**Key concepts:**
- Auto-configuration of the OpenTelemetry SDK
- Exporting traces and metrics over OTLP (gRPC/HTTP)
- Automatic instrumentation of Spring MVC, WebClient, JDBC
- Trace context propagation across service boundaries
- Integration with Jaeger / Zipkin / Grafana

**Configuration example:**
```yaml
management:
  otlp:
    tracing:
      endpoint: http://localhost:4318/v1/traces
  tracing:
    sampling:
      probability: 1.0
```

---

### 5. ⚠️ REST Client Error Handling

**Path:** `rest-client-error-handling/`

A deep-dive into **robust error handling strategies** with Spring Boot 4's `RestClient` — the modern, synchronous successor to `RestTemplate`.

**Key concepts:**
- Custom `ResponseErrorHandler` implementations
- `onStatus()` error mapping chains
- Translating HTTP errors into domain exceptions
- Retry strategies for transient failures
- Logging and auditing failed requests

**Example:**
```java
RestClient.builder()
    .baseUrl("https://api.example.com")
    .defaultStatusHandler(HttpStatusCode::is4xxClientError,
        (req, res) -> { throw new ClientException(res.getStatusCode()); })
    .defaultStatusHandler(HttpStatusCode::is5xxServerError,
        (req, res) -> { throw new ServerException("Upstream failure"); })
    .build();
```

---

### 6. 🧪 REST Test Client

**Path:** `rest-test-client/`

Demonstrates the **new `RestTestClient`** introduced in Spring Boot 4 — a test-focused HTTP client that can operate against both `MockMvc` (no server needed) and a real running server.

**Key concepts:**
- `@AutoConfigureMockMvc` + `RestTestClient` injection
- Fluent assertion API for response validation
- JSON path assertions
- Testing both controller slices and full integration

**Example:**
```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    RestTestClient restTestClient;

    @Test
    void shouldReturnUser() {
        restTestClient.get().uri("/api/users/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isEqualTo("Omar");
    }
}
```

---

### 7. ⚖️ REST vs Mock Web Testing

**Path:** `rest-vs-mock-web-testing/`

A **side-by-side comparison** of different testing strategies in Spring Boot 4:

| Approach | Speed | Scope | Use Case |
|---|---|---|---|
| `MockMvc` | ⚡ Fastest | Controller layer | Unit & slice tests |
| `RestTestClient` (MockMvc) | ⚡ Fast | Controller layer | Fluent API preference |
| `RestTestClient` (Real server) | 🐢 Slower | Full stack | Integration tests |
| `TestRestTemplate` | 🐢 Slower | Full stack | Legacy integration |

**Key concepts:**
- When to use `@WebMvcTest` vs `@SpringBootTest`
- Trade-offs between speed and fidelity
- Structuring test suites for maximum coverage

---

### 8. 🗂️ Jackson 3 JSON Mapping

**Path:** `jackson3-json-mapping/`

Explores the **Jackson 3.x** changes that come with Spring Boot 4, including new annotation behavior, improved Records support, and updated serialization defaults.

**Key concepts:**
- Jackson 3 migration from Jackson 2
- `@JsonView` and `@JsonProperty` changes
- Serialization of Java Records
- Custom serializers and deserializers
- Property naming strategies

---

### 9. 📨 Messaging — JMS

**Path:** `messaging-jms/`

Demonstrates **JMS (Java Message Service)** messaging patterns with Spring Boot 4, covering both point-to-point queues and publish-subscribe topics.

**Key concepts:**
- Configuring `JmsTemplate` for sending messages
- `@JmsListener` for consuming messages
- Message converters (JSON, Object)
- Transaction management with JMS
- Error handling and Dead Letter Queues (DLQ)
- Integration with ActiveMQ Artemis

**Example:**
```java
@Component
public class OrderProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendOrder(Order order) {
        jmsTemplate.convertAndSend("orders.queue", order);
    }
}

@Component
public class OrderConsumer {

    @JmsListener(destination = "orders.queue")
    public void processOrder(Order order) {
        // process the order
    }
}
```

---

### 10. 🧩 Dynamic Bean Registration

**Path:** `dynamic-bean-registration/`

Demonstrates **runtime bean registration** in Spring Boot 4 using the new `BeanDefinitionRegistrar` APIs and programmatic context customization.

**Key concepts:**
- `BeanDefinitionRegistryPostProcessor` for dynamic registration
- Condition-based bean creation at runtime
- Plugin-style architecture with dynamic discovery
- Using `DefaultListableBeanFactory` for programmatic registration

---

### 11. 🤖 Spring AI 2 Demo

**Path:** `spring-ai-2-demo/`

An introduction to **Spring AI 2.x** — the official Spring framework for integrating AI/ML capabilities into Spring applications, including LLM chat completions, embeddings, and prompt engineering.

**Key concepts:**
- `ChatClient` for LLM interactions
- Prompt templates and `PromptTemplate`
- Streaming responses with `Flux<String>`
- Embedding models for vector search
- Integration with OpenAI, Anthropic, Ollama, and other providers
- Retry and observability for AI calls

**Example:**
```java
@Service
public class AiService {

    private final ChatClient chatClient;

    public String ask(String question) {
        return chatClient.prompt()
            .user(question)
            .call()
            .content();
    }
}
```

> 🔑 Requires an AI provider API key (e.g., `OPENAI_API_KEY`).

---

### 12. ⚡ Spring Data AOT

**Path:** `spring-data-aot/`

Explores **Ahead-of-Time (AOT) optimizations** for Spring Data in Spring Boot 4, enabling dramatically faster startup times and lower memory footprint — particularly useful for serverless and containerized deployments.

**Key concepts:**
- AOT processing with `spring-data-aot` processor
- Repository proxy generation at build time
- GraalVM Native Image compatibility
- AOT hints for reflection-heavy components
- Build-time validation of repository queries

**Benefits:**
- ⚡ Faster application startup (no proxy creation at runtime)
- 💾 Lower memory usage
- 🔒 Build-time detection of invalid queries

---

### 13. 🛡️ Spring Native Resilience

**Path:** `spring-native-resilience/`

Combines **GraalVM Native Image compilation** with **resilience patterns** (circuit breakers, retries, rate limiters) to build ultra-fast, fault-tolerant microservices.

**Key concepts:**
- Resilience4j integration with Spring Boot 4
- `@CircuitBreaker`, `@Retry`, `@RateLimiter`, `@Bulkhead` annotations
- Native image compilation with Spring AOT
- Custom AOT hints for Resilience4j
- Health indicators for circuit breaker state

**Example:**
```java
@Service
public class ExternalService {

    @CircuitBreaker(name = "externalAPI", fallbackMethod = "fallback")
    @Retry(name = "externalAPI")
    public String callExternal() {
        return externalClient.getData();
    }

    public String fallback(Exception ex) {
        return "Fallback response";
    }
}
```

---

### 14. 🔐 Spring Security MFA

**Path:** `spring-security-mfa/`

Implements **Multi-Factor Authentication (MFA)** using Spring Security 7 with TOTP (Time-based One-Time Password) support — the same standard used by Google Authenticator and Authy.

**Key concepts:**
- TOTP-based MFA with `google-authenticator` / `JJWT`
- Custom `AuthenticationProvider` for MFA verification
- QR code generation for authenticator app setup
- MFA enrollment flow (register → scan → verify)
- Remember-device functionality
- Spring Security filter chain customization

**Authentication flow:**
```
1. User submits username + password
2. System validates credentials ✅
3. System prompts for TOTP code 📱
4. User enters 6-digit code from authenticator app
5. System validates TOTP ✅
6. Access granted 🔓
```

---

## 🛠️ Technologies Used

| Category | Technology | Version |
|---|---|---|
| ☕ **Language** | Java | 21+ |
| 🌱 **Framework** | Spring Boot | 4.x |
| 🌿 **Core** | Spring Framework | 7.x |
| 🔐 **Security** | Spring Security | 7.x |
| 🤖 **AI** | Spring AI | 2.x |
| 📊 **Observability** | OpenTelemetry | Latest |
| 🛡️ **Null Safety** | JSpecify + NullAway | Latest |
| 📦 **Build** | Apache Maven | 3.9+ |
| 🗂️ **JSON** | Jackson | 3.x |
| 📨 **Messaging** | Spring JMS / ActiveMQ Artemis | Latest |
| 🔄 **Resilience** | Resilience4j | Latest |
| 🧪 **Testing** | JUnit 5, MockMvc, RestTestClient | Latest |

---

## 📋 Prerequisites

Before running any module, ensure you have the following installed:

- ☕ **Java 21+** — [Download OpenJDK](https://openjdk.org/projects/jdk/21/)
- 📦 **Maven 3.9+** — [Download Maven](https://maven.apache.org/download.cgi)
- 🐳 **Docker** (optional, for external services like Jaeger, ActiveMQ) — [Download Docker](https://www.docker.com/)
- 🔑 **API Keys** (for `spring-ai-2-demo`) — e.g., OpenAI API Key

**Verify your setup:**
```bash
java -version   # Should show 21+
mvn -version    # Should show 3.9+
docker --version # Optional
```

---

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/drissiOmar98/Spring-Boot-4-Playground.git
cd Spring-Boot-4-Playground
```

### Build All Modules

```bash
mvn clean install -DskipTests
```

### Build a Specific Module

```bash
cd http-interfaces-demo
mvn clean install
```

---

## ▶️ Running Individual Modules

Each module is a standalone Spring Boot application. Navigate to the module directory and run:

```bash
# Navigate to any module
cd <module-name>

# Run with Maven
mvn spring-boot:run

# Or build and run the JAR
mvn clean package -DskipTests
java -jar target/*.jar
```

### Module-Specific Instructions

#### 🤖 Spring AI Demo
```bash
cd spring-ai-2-demo
export OPENAI_API_KEY=your_api_key_here
mvn spring-boot:run
```

#### 📊 OpenTelemetry Demo
```bash
# Start Jaeger for trace visualization
docker run -d --name jaeger \
  -p 16686:16686 \
  -p 4318:4318 \
  jaegertracing/all-in-one:latest

cd opentelemetry-demo
mvn spring-boot:run

# View traces at http://localhost:16686
```

#### 📨 JMS Messaging
```bash
# Start ActiveMQ Artemis
docker run -d --name artemis \
  -p 8161:8161 \
  -p 61616:61616 \
  apache/activemq-artemis:latest

cd messaging-jms
mvn spring-boot:run
```

#### 🔐 Spring Security MFA
```bash
cd spring-security-mfa
mvn spring-boot:run
# Access at http://localhost:8080
```

---

## 🌟 Key Spring Boot 4 Features Showcased

This playground highlights the major improvements and new features in **Spring Boot 4**:

### 🔧 New `spring-boot-starter-opentelemetry`
Auto-configures the OpenTelemetry SDK for traces and metrics over OTLP — demonstrated in `opentelemetry-demo`.

### 🌐 HTTP Service Client Auto-Configuration
Spring Boot 4 adds auto-configuration for `@HttpExchange` interface clients, making declarative HTTP clients even simpler — demonstrated in `http-interfaces-demo`.

### 🧪 `RestTestClient`
The new test-focused HTTP client that works with both MockMvc and real servers — demonstrated in `rest-test-client` and `rest-vs-mock-web-testing`.

### 🛡️ JSpecify Null Safety
Spring Framework 7 adopts JSpecify annotations across the codebase, enabling null-safe programming by default — demonstrated in `null-safety-jspecify-nullaway`.

### ⚡ Enhanced AOT Support
Improved build-time processing for faster startup and GraalVM compatibility — demonstrated in `spring-data-aot` and `spring-native-resilience`.

### 📦 Jackson 3 Migration
Spring Boot 4 migrates to Jackson 3, with updated serialization behavior and better Records support — demonstrated in `jackson3-json-mapping`.

---


## 👨‍💻 Author

<p align="center">
  <strong>Omar Drissi</strong><br/>
  <a href="https://github.com/drissiOmar98">🐙 GitHub</a> •
  <a href="https://www.linkedin.com/in/omar-drissi-4798171a4/">💼 LinkedIn</a>
</p>

---

<p align="center">
  ⭐ <strong>If you find this project useful, please give it a star!</strong> ⭐<br/>
  It helps others discover the project and motivates further development.
</p>

<p align="center">
  Made with ❤️ and ☕ using Spring Boot 4
</p>
