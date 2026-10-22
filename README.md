# 🏛️ Atlas Bank — Spring Boot MVC with SOLID & Clean Code

[![Java](https://img.shields.io/badge/Java-25-orange.svg?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x%20%2F%204.x-brightgreen.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![Architecture](https://img.shields.io/badge/Architecture-MVC%20%2F%20Package--by--Feature-blue.svg)]()
[![Code Quality](https://img.shields.io/badge/Design-SOLID%20%26%20Clean%20Code-purple.svg)]()
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)]()

Repository: [springboot-mvc-solid](https://github.com/sergiopj/springboot-mvc-solid)

> **A banking backend demonstration demonstrating how to refactor a monolithic Spring Boot CRUD into clean, maintainable, and decoupled code by applying SOLID principles and GoF design patterns.**

---

## 📌 Project Overview

**Atlas Bank** simulates a banking engine handling bank accounts, money transfers with dynamic fee calculations, and transaction histories. 

Rather than settling for an unmaintainable "GOD Service" anti-pattern typical in conventional Spring Boot MVC projects, this codebase demonstrates how to apply **SOLID design principles**, **Clean Code**, and **Package-by-Feature modularization** while retaining the simplicity of the Spring Boot MVC stack.

---

## 🎯 Architecture & Design Highlights

### 1. Elimination of the GOD Service (Single Responsibility Principle - SRP)
Initially, a single `AccountService` managed everything: account CRUD, validations, money transfers, fee calculations, and audit queries. The service was split into cohesive, single-responsibility components:
* **`AccountService`**: Manages account lifecycle operations (creation, lookups).
* **`TransferService`**: Orquestrates transactional money transfers between accounts.
* **`TransactionQueryService`**: Dedicated read-only service for audit trails and account transaction histories.

### 2. Strategy Pattern with Spring IoC (Open/Closed Principle - OCP)
Fee calculation does not rely on hardcoded `if/else` ladders:
* Defined a polymorphic contract: `FeeCalculator` with `supports(accountType)` and `calculate(amount)`.
* Specific implementations: `SavingsFeeCalculator`, `CheckingFeeCalculator`, and a fallback `DefaultFeeCalculator`.
* **Spring IoC Magic**: `TransferService` injects `List<FeeCalculator>`. When a new account type is introduced, a new `@Component` class is added without altering a single line in `TransferService`.

### 3. Interface Segregation & Dependency Inversion (ISP & DIP)
* Services implement dedicated interfaces (`IAccountService`, `ITransferService`, `ITransactionQueryService`), shielding controllers from concrete implementation details.
* Dependencies are strictly injected via **Constructor Injection** (`@RequiredArgsConstructor` with `final` fields), ensuring immutability, thread-safety, and testability.

### 4. Package-by-Feature Organization
Organized by business domain rather than purely technical layers:
```text
src/main/java/com/atlas/bank/atlas_bank/
├── account/
│   ├── controller/      # AccountController (REST endpoints)
│   ├── model/           # Account JPA Entity
│   ├── repository/      # AccountRepository (Spring Data JPA)
│   └── service/         # IAccountService & AccountService
├── transaction/
│   ├── controller/      # TransactionController (Transfers & query endpoints)
│   ├── model/           # Transaction JPA Entity
│   ├── repository/      # TransactionRepository (Spring Data JPA)
│   ├── service/         # ITransferService, TransferService, TransactionQueryService
│   └── fee/             # FeeCalculator Strategy implementations
└── AtlasBankApplication.java
```

---

## 🛠️ Technology Stack

| Technology | Purpose |
| :--- | :--- |
| **Java 25 LTS** | Modern Java with records, pattern matching, and functional streams |
| **Spring Boot 3.x / 4.x** | Core MVC web framework & IoC container |
| **Spring Data JPA & Hibernate** | Object-Relational Mapping (ORM) and persistence |
| **H2 Database** | Fast, lightweight in-memory database for local development |
| **Lombok** | Boilerplate reduction (`@Data`, `@RequiredArgsConstructor`) |
| **Maven Wrapper** | Portable, reproducible build automation |

---

## ⚡ API Endpoints

### 🏦 Accounts (`/api/v1/accounts`)
* `POST /api/v1/accounts` — Create a new account
* `GET /api/v1/accounts` — Retrieve all accounts
* `GET /api/v1/accounts/{id}` — Find account by ID

### 💸 Transfers & Transactions (`/api/v1/accounts` & `/api/v1/transactions`)
* `POST /api/v1/accounts/transfer?fromId=1&toId=2&amount=500` — Execute a transactional money transfer with fee calculation
* `GET /api/v1/accounts/{id}/transactions` — Retrieve transaction history for a specific account

---

## 🚀 Getting Started

### Prerequisites
* **Java 25** or later installed.
* Terminal (Linux / macOS / WSL / Windows).

### Clone & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/sergiopj/springboot-mvc-solid.git
   cd springboot-mvc-solid
   ```

2. Compile the project:
   ```bash
   ./mvnw clean compile
   ```

3. Launch the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. The server runs at `http://localhost:8080`.

---

## 🗄️ H2 Database Console

The in-memory database console is enabled for easy inspection:
* **URL:** `http://localhost:8080/h2-console`
* **Driver Class:** `org.h2.Driver`
* **JDBC URL:** `jdbc:h2:mem:atlasbank`
* **User Name:** `sa`
* **Password:** *(leave blank)*

> [!NOTE]
> Since H2 runs in-memory with `ddl-auto: create-drop`, tables are automatically initialized on startup and cleared when the application stops.

---

## 🧪 Running Tests

Execute the unit and integration tests:

```bash
./mvnw test
```

---

## 📄 License
This project is licensed under the [MIT License](LICENSE).
