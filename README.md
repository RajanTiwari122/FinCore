# FinCore
<<<<<<< HEAD

A backend trading and fund management system built with Java and Spring Boot, covering equity/bond workflows, market data ingestion, NAV calculation, and role-based authentication.

---

## Overview

FinCore simulates core operations of an investment management platform — the kind of system used in real-world AIFs, mutual funds, and portfolio management setups. It was built to go beyond tutorial-level projects and reflect production-relevant patterns in fintech backend development.

---

## Features

### Equity & Bond Workflows
- Buy/sell transaction processing for equities and bonds
- Trade lifecycle management: order placement → execution → settlement
- P&L computation per instrument and per portfolio

### Market Data Ingestion
- Integration with NSE/BSE data feeds
- Scheduled ingestion of OHLC (Open, High, Low, Close) price data
- Historical price storage for NAV and valuation calculations

### NAV Calculation
- Net Asset Value computation for fund-level portfolios
- Considers unrealised gains, realised P&L, accrued income, and expenses
- Designed to support periodic (daily/weekly) valuation runs

### Role-Based Authentication
- JWT-based stateless authentication
- Roles: `ADMIN`, `FUND_MANAGER`, `INVESTOR`
- Endpoint-level access control using Spring Security

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 11 |
| Framework | Spring Boot, Spring MVC, Spring Security |
| ORM | Hibernate, JPA |
| Database | MySQL |
| Auth | JWT (jjwt) |
| Build | Maven |

---

## Project Structure

```
fincore/
├── src/
│   ├── main/
│   │   ├── java/com/fincore/
│   │   │   ├── auth/          # JWT filter, token util, security config
│   │   │   ├── trade/         # Trade entities, services, controllers
│   │   │   ├── instrument/    # Equity and bond models
│   │   │   ├── nav/           # NAV calculation engine
│   │   │   ├── ingestion/     # Market data ingestion (NSE/BSE)
│   │   │   └── user/          # User management, roles
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

---

## Getting Started

### Prerequisites
- Java 11+
- MySQL 8.x
- Maven 3.6+

### Setup

```bash
# Clone the repo
git clone https://github.com/your-username/fincore.git
cd fincore

# Create the database
mysql -u root -p -e "CREATE DATABASE fincore_db;"

# Update DB credentials in src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/fincore_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# Build and run
mvn clean install
mvn spring-boot:run
```

### Authentication

```bash
# Register a user
POST /api/auth/register
{
  "username": "rajan",
  "password": "securepass",
  "role": "FUND_MANAGER"
}

# Login and get JWT token
POST /api/auth/login
```

Use the returned token as `Authorization: Bearer <token>` for subsequent requests.

---

## Key Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Authenticate and get JWT |
| POST | `/api/trades` | Place a buy/sell order |
| GET | `/api/trades/{id}` | Get trade details |
| GET | `/api/portfolio` | View portfolio summary |
| GET | `/api/nav/calculate` | Trigger NAV calculation |
| GET | `/api/instruments/equity` | List equity instruments |
| GET | `/api/instruments/bond` | List bond instruments |

---

## Design Decisions

- **No Spring Data JPA repositories for complex queries** — uses `JdbcTemplate` / `NamedParameterJdbcTemplate` for performance-sensitive queries to retain full SQL control
- **Stateless auth** — no server-side session; every request is validated against the JWT signature and expiry
- **NAV engine decoupled** — NAV calculation is a standalone service that pulls trade, price, and expense data; easy to trigger on a schedule or on-demand
- **MySQL-compatible SQL** — all queries written for MySQL 5.7+ (no CTEs); range predicates used on date columns to allow index utilization

---

## Background

This project was built alongside 2+ years of professional experience on **Genesis**, an investment banking platform at Novel Patterns Pvt. Ltd., covering portfolio management, AIFs, mutual funds, and fund accounting. FinCore applies the same domain concepts in a clean, standalone codebase.

---

## Author

**Rajan**  
Java Backend Developer | Fintech Domain  
[LinkedIn](https://linkedin.com/in/your-profile) • [GitHub](https://github.com/your-username)
=======
Backend trading system built with Java &amp; Spring Boot — equity/bond workflows, NSE/BSE data ingestion, NAV calculation, and JWT-based role authentication.
>>>>>>> bb1aa7d4b334ade630971f1b5c78deb6391a2173
