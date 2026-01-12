# ☕ Coffee Shop Demo – Spring Boot 4 AOT Repositories
Welcome to the **Coffee Shop API**, a fully functional demo application built with **Spring Boot 4** and **Spring Data AOT Repositories**. This project demonstrates how **ahead-of-time (AOT) compilation** optimizes your repository queries, improves performance, and ensures safer, debuggable database access.



## 🚀 Project Overview

This is a **Coffee Shop demo application** showcasing **Spring Boot 4 Ahead-of-Time (AOT) Repository capabilities**.  
Spring Data AOT repositories allow your repository interfaces to be **compiled into optimized implementations at build time**, giving you:

- ⚡ **Faster startup times** – ideal for microservices and serverless apps
- 🛠️ **Compile-time validation** – catch SQL typos and query errors before runtime
- 🔍 **Full transparency** – inspect generated SQL and repository code
- 🐞 **Better debugging** – set breakpoints in actual repository implementations

This project demonstrates both **Coffee and Order management** with relational data and advanced repository queries.

---


## 📚 Features

### Coffee Management
- ✅ CRUD operations for coffee products
- 🔎 Search by coffee name (case-insensitive)
- 💰 Filter by size and price
- 🏷️ Affordable coffees endpoint with default parameters

### Order Management
- ✅ CRUD operations for orders and order items
- 👤 Find orders by customer name
- ⏱️ Query recent orders by status and date
- ☕ Query orders by coffee product
- 📦 Retrieve all items for a specific order with coffee details

### AOT Repository Highlights
- 💡 **Derived queries** validated at build time
- 🧩 **Multi-property and temporal queries** pre-compiled
- 🔗 **Relationship queries with JOINs** fully validated
- ⚠️ **SQL errors caught at compile-time** instead of production


