# 🌟 Spring Boot 4 OpenTelemetry: From Zero to Full Observability

Spring Boot 4 introduces a dedicated **OpenTelemetry starter** that simplifies application observability. With this starter, you can export **traces, metrics, and logs** without pulling in the full Actuator dependency. This project demonstrates a **production-ready observability setup** with OpenTelemetry, OTLP, and Grafana.

---

## 🚀 Project Overview

This project is a hands-on demo showing how to:

- Integrate Spring Boot 4 OpenTelemetry starter.
- Export **logs**, **metrics**, and **traces** to OTLP endpoints.
- Visualize metrics and traces in **Grafana** using **Loki**, **Tempo**, and **Mimir**.
- Correlate logs with traces for easy debugging.
- Add custom metrics using `@Observed` annotations.
- Simulate application operations with multiple endpoints to see full observability in action.

> 📝 *As the saying goes: "Hope is not a production strategy." With this starter, production-ready observability is easier than ever!*

---

## 🛠 Features

✅ **Automatic OpenTelemetry configuration** via `spring-boot-starter-opentelemetry`  
✅ **OTLP export** for metrics, traces, and logs  
✅ **Log ↔ Trace correlation** for powerful debugging workflows  
✅ **Custom metrics** via `@Observed` annotations  
✅ **100% sampling support** for development (configurable in `application.yml`)  
✅ **Docker Compose** setup for full observability stack (LGTM: Loki, Grafana, Tempo, Mimir)

---

## 📦 Project Structure

```text
src/
├── main/
│   ├── java/com/omar/opentelemetry/
│   │   ├── HomeController.java
│   │   │   └── Sample REST endpoints
│   │   ├── InstallOpenTelemetryAppender.java
│   │   │   └── Bean responsible for installing the OpenTelemetry log appender
│   │   └── OpenTelemetryDemoApplication.java
│   │       └── Spring Boot application entry point
│   └── resources/
│       ├── application.yml
│       │   └── OpenTelemetry & logging configuration
│       └── logback-spring.xml
│           └── OTLP log appender configuration
└── test/
    └── java/com/omar/opentelemetry/
        └── Unit & integration tests
```

## 🧰 Prerequisites

Before running the project, make sure you have the following installed:

- ☕ **Java 17+**
- 📦 **Maven**
- 🐳 **Docker** (required for running the Grafana **LGTM** stack)

---

## 🔍 What is the LGTM Stack?

The **LGTM stack** is Grafana Labs’ open-source observability stack.  
It provides a complete, production-ready observability platform:

- 🪵 **Loki** — Log aggregation and querying
- 📊 **Grafana** — Visualization, dashboards, and alerts
- 🧵 **Tempo** — Distributed tracing backend
- 📈 **Mimir** — Scalable, long-term storage for Prometheus metrics

Together, they cover the three pillars of observability:  
**logs, metrics, and traces** — all in one stack 🚀


## 📦 Dependencies

The key dependency in this project is the new **Spring Boot OpenTelemetry starter**:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-opentelemetry</artifactId>
</dependency>
```
This starter includes:
- OpenTelemetry API
- Micrometer tracing bridge to OpenTelemetry
- OTLP exporters for metrics and traces

## Configuration

```yaml
spring:
  application:
    name: ot

management:
  tracing:
    sampling:
      probability: 1.0  # 100% sampling for development
  otlp:
    metrics:
      export:
        url: http://localhost:4318/v1/metrics
  opentelemetry:
    tracing:
      export:
        otlp:
          endpoint: http://localhost:4318/v1/traces
    logging:
      export:
        otlp:
          endpoint: http://localhost:4318/v1/logs
```

### Configuration Notes

- **sampling.probability**: Set to `1.0` for development (all traces). Use lower values in production (default is `0.1`)
- **Port 4318**: HTTP OTLP endpoint (use 4317 for gRPC)
- The `spring-boot-docker-compose` module auto-configures these endpoints when using Docker Compose

### Understanding the OTLP Export Configuration

**`management.otlp.metrics.export.url`** — Tells Spring Boot where to send **metrics** (counts, gauges, histograms like request counts, response times, memory usage). The data goes to an OTLP-compatible collector.

**`management.opentelemetry.tracing.export.otlp.endpoint`** — Tells Spring Boot where to send **traces** (timing/flow data showing how requests move through your app, spans showing each operation and duration).

**Why two separate configs?** Spring Boot's observability evolved over time:
- Metrics use Micrometer's OTLP exporter (hence `management.otlp.metrics`)
- Traces use the OpenTelemetry tracing bridge (hence `management.opentelemetry.tracing`)

Both send data to the same collector (port 4318), but the configuration paths differ due to how the libraries are integrated.