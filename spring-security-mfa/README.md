# 🔐 Spring Boot 4 MFA Playground

Welcome to the **Spring Boot 4 + Spring Security 7 Multi-Factor Authentication (MFA) Playground**!  
This project demonstrates **native MFA support** in Spring Security 7 using **passwords + one-time tokens (OTT)**. Perfect for learning, experimenting, and reference.

---

## 🏗️ Project Overview

This playground showcases:

- Spring Boot 4 project setup
- Spring Security 7 configuration with **MFA**
- In-memory user management for testing
- Custom **5-digit OTP/OTT token service**
- Smart **factor-based authentication**
- Simple controllers for demonstration

**Goal:** Learn how to implement MFA from scratch in a professional, maintainable way.

---

## 🔑 Features

### 1. Multi-Factor Authentication (MFA)
- Enabled via `@EnableMultiFactorAuthentication`
- Supports **password + one-time token (OTT)**
- Uses **FactorGrantedAuthority** to track verified factors

### 2. Form Login + OTT
- Standard form login for password authentication
- One-time token login for second factor
- Smart redirects if factors are missing

### 3. Custom One-Time Token Service
- Generates **5-digit PINs**
- Token expiration configurable (default 3 minutes)
- In-memory storage for simplicity
- Easily replaceable with DB-backed storage for production

### 4. Role-Based Access Control
- `/` → public
- `/admin/**` → ADMIN role only
- `/ott/sent` → public (OTT confirmation page)

---

## 📦 Project Structure

```text
src/main/java
└── com/ariana/security/mfa
    ├── config
    │   └── SecurityConfig.java             # 🔐 Spring Security + MFA configuration
    ├── security
    │   └── ott
    │       ├── PinOneTimeTokenService.java # 🎟️ Custom 5-digit OTP service
    │       └── OttSuccessHandler.java      # ✅ Handles successful OTP generation
    ├── web
    │   └── HomeController.java             # 🌐 Demo endpoints: "/", "/admin", "/ott/sent"
    └── SpringSecurityMfaApplication.java  # 🚀 Main Spring Boot application entry point
```

## 🔄 Workflow / Sequence

Step-by-step user authentication:

1. User submits username/password
2. Spring Security verifies password
3. If MFA required:
    - Generate One-Time Token using `PinOneTimeTokenService`
    - Trigger `OttSuccessHandler` → redirect or email/SMS
4. User provides token to complete authentication
5. SecurityFilterChain grants access to protected endpoints

> 🎯 This shows **factor-based authentication flow** in a clear, linear way.