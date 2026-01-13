# 🚀 Dynamic Bean Registration Made Easy with BeanRegistrar (Spring Boot 4 + Spring Framework 7)
Spring Framework 7 and Spring Boot 4 introduce a **first-class, modern solution for programmatic bean registration**: the **`BeanRegistrar` interface**.

No more jumping through hoops with `BeanDefinitionRegistryPostProcessor`, complex `@Bean` methods, or unreadable conditional configurations.

This project is a **hands-on, production-ready example** demonstrating how to dynamically register beans in a clean, intuitive, and performant way using Spring’s newest container API.

---

## ✨ Why This Project Exists

Have you ever struggled with Spring configuration when you needed to:

- Register beans **dynamically** based on environment properties?
- Create **multiple beans in loops**?
- Apply **complex conditional logic** that `@Conditional` annotations can’t express cleanly?
- Optimize startup time by **loading only the beans you actually need**?

Until now, Spring offered solutions — but they were **low-level, verbose, and hard to maintain**.

**Spring Framework 7 changes that.**

---

## ✨ What This Project Demonstrates

This demo shows how to dynamically register a `MessageService` implementation at startup using the **new `BeanRegistrar` interface** introduced in **Spring Framework 7**.

Depending on the application configuration, the system automatically wires:

- 📧 **EmailMessageService**
- 📱 **SmsMessageService**

👉 **Without using**:
- `@Conditional`
- `BeanDefinitionRegistryPostProcessor`
- Reflection-heavy or error-prone boilerplate


## 🧠 What’s New in Spring Framework 7

Spring 7 introduces the **`BeanRegistrar` interface**, a high-level, type-safe, and intention-revealing API for programmatic bean registration.

### ✅ What Makes `BeanRegistrar` Different?

| Traditional Approach | BeanRegistrar (Spring 7) |
|---------------------|--------------------------|
| Low-level APIs | High-level, expressive API |
| Manual `BeanDefinition` handling | Direct class registration |
| Boilerplate-heavy | Minimal, readable code |
| Hard to reason about | Clean and maintainable |
| Error-prone | Type-safe |

---

## 📚 What You’ll Learn

✔ How to implement the **new `BeanRegistrar` interface**  
✔ Programmatic bean registration without post-processors  
✔ Conditional beans based on **environment properties**  
✔ Building a **real-world MessageService example** (Email & SMS)  
✔ Performance optimization through **selective bean loading**  
✔ When to use **BeanRegistrar vs `@Bean`** (and why both still matter)

---

## ⚡ Perfect For Developers Who Need To

- Dynamically register beans based on configuration
- Create multiple beans programmatically
- Handle complex runtime conditions cleanly
- Optimize application startup and memory usage
- Modernize legacy Spring configurations
- Adopt **Spring Boot 4 & Spring Framework 7 best practices**