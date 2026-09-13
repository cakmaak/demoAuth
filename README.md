# DemoAuth – Authorization System

DemoAuth, Spring Boot tabanlı bir yetkilendirme sistemi demosudur.

Projenin amacı; kimlik doğrulamayı Keycloak üzerinden gerçekleştirirken, uygulama içi yetkilendirme kararlarını rol, permission, grant, scope ve constraint yapıları üzerinden yönetmektir.

Sistem ER11 yetkilendirme modelindeki temel kavramları uygulamalı olarak göstermek amacıyla geliştirilmiştir.

## Architecture

```text
React Frontend
      |
      | Login
      v
   Keycloak
      |
      | JWT
      v
Spring Security
      |
      v
Controller
      |
   @Authorize
      |
      v
Authorization Aspect
      |
      v
AuthorizationService
      |
      +--> Principal
      +--> Roles
      +--> Caffeine Cache
      +--> Casbin
      +--> Permission Grants
      +--> Constraints
      +--> Scopes
      |
      v
   ALLOW / DENY
      |
      v
 PostgreSQL
```

## Technologies

### Backend
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- OAuth2 Resource Server
- Casbin
- Caffeine Cache
- PostgreSQL
- Springdoc OpenAPI / Swagger
- Spring Boot Actuator

### Frontend
- React
- Vite
- Axios
- Keycloak JS

### Infrastructure
- Docker
- Docker Compose
- Keycloak 26.3.2
- PostgreSQL 17
- Nginx

Performance tests were performed with k6. Prometheus and Grafana were used during development for observability and performance analysis.

---

## Authorization Model

Authentication and authorization are separated in the system.

**Keycloak** is responsible for authentication:

> Who is the user?

**DemoAuth** is responsible for authorization:

> Is this user allowed to perform this action on this resource?

After authentication, the JWT `sub` claim is mapped to an internal `Principal`.

```text
Keycloak User
     |
     | JWT sub
     v
Principal
     |
     v
Role Assignment
     |
     v
Role
     |
     v
Permission Grant
     |
     v
Permission
```

Authorization decisions can additionally depend on:

- Role inheritance
- Assignment scope
- Authorization constraints
- Grant priority
- ALLOW / DENY decisions

---

## Request Authorization Flow

Protected controller methods use the custom `@Authorize` annotation.

Example:

```java
@Authorize(resource = "gtfs.data", action = "VIEW")
```

The annotation is intercepted using Spring AOP.

Simplified request flow:

```text
HTTP Request + JWT
        |
        v
Spring Security
        |
        v
JWT Validation
        |
        v
Controller / @Authorize
        |
        v
AuthorizationAspect
        |
        v
AuthorizationService
        |
        v
Principal Resolution
        |
        v
Decision Cache
     /       \
   HIT       MISS
    |          |
    |       Roles
    |          |
    |       Permission
    |          |
    |        Casbin
    |          |
    |        Grants
    |          |
    |      Constraints
    |          |
    |        Scope
    |          |
    +----------+
        |
        v
   ALLOW / DENY
```

If the authorization decision is `ALLOW`, the requested controller method continues execution.

If the decision is `DENY`, the method is not executed and the request returns HTTP `403 Forbidden`.

---

## Casbin

Casbin is used as an in-memory policy enforcement layer.

The Casbin policy model focuses on:

```text
Role + Resource + Action
```

The database remains the source of truth.

At application startup, role permission information is synchronized from PostgreSQL into Casbin.

When authorization configuration changes, the corresponding Casbin policies are synchronized and the authorization decision cache is invalidated.

---

## Authorization Cache

Caffeine is used to cache final authorization decisions.

The cache key contains authorization context such as:

```text
tenant
principal
resource
action
scope type
organization
region
target principal
```

Both `ALLOW` and `DENY` decisions can therefore be reused for the same authorization context.

The cache has a TTL of 3 minutes.

### Performance Test

A k6 test was performed using:

```text
20 Virtual Users
1000 Requests
```

Results from the final test runs:

| Metric | Cache ON | Cache OFF |
|---|---:|---:|
| Successful Requests | 1000/1000 | 1000/1000 |
| Error Rate | 0% | 0% |
| Throughput | 323.73 req/s | 128.66 req/s |
| Average Response Time | 60.45 ms | 153.44 ms |
| Median Response Time | 56.24 ms | 137.80 ms |
| p95 Response Time | 112.23 ms | 287.41 ms |

Under this test scenario, enabling the authorization cache increased observed throughput by approximately **2.5x** and reduced average response time by approximately **61%**.

These values represent the observed results of this specific load-test scenario and should not be interpreted as the maximum capacity of the system.

---

# Running the Project

## Requirements

Only the following are required:

- Git
- Docker Desktop
- Docker Compose

Java, Maven, Node.js, PostgreSQL and Keycloak do **not** need to be installed locally when running the project with Docker.

## 1. Clone the Repository

```bash
git clone https://github.com/cakmaak/demoAuth.git
cd demoAuth
```

## 2. Start the Application

Make sure Docker Desktop is running.

Then execute:

```bash
docker compose up --build
```

Docker Compose will start:

```text
PostgreSQL
Keycloak
Spring Boot Backend
React / Nginx Frontend
```

The PostgreSQL database is initialized using the included database seed.

The `demo-realm` Keycloak realm is automatically imported when the Keycloak container starts.

## 3. Application URLs

| Service | URL |
|---|---|
| Frontend | http://localhost:5173 |
| Backend | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| Keycloak | http://localhost:8081 |
| PostgreSQL | localhost:5435 |

## 4. Stop the Application

```bash
docker compose down
```

PostgreSQL data is stored in a Docker volume and is preserved between normal container restarts.

To rebuild the application after source-code changes:

```bash
docker compose up --build
```

---

## Example Protected Endpoint

Example GTFS authorization request:

```text
GET /api/gtfs
```

The endpoint requires:

```text
resource = gtfs.data
action   = VIEW
```

The authorization system evaluates the authenticated principal's roles, grants, constraints and scope before allowing access.

Example successful response:

```text
GTFS verisine erişim başarılı.
Authorization sonucu: ALLOW
```

---

## API Documentation

Swagger UI is available after the application starts:

```text
http://localhost:8080/swagger-ui/index.html
```

It can be used to inspect the available authorization and administration APIs.

---

## Project Structure

```text
demoAuth/
│
├── demoauth/
│   ├── src/
│   ├── Dockerfile
│   ├── pom.xml
│   └── load-test.js
│
├── demoauth-ui/
│   ├── src/
│   ├── Dockerfile
│   └── package.json
│
├── keycloak-export/
│   └── demo-realm-realm.json
│
├── demoauth-seed.sql
├── docker-compose.yml
└── README.md
```

## Main Authorization Components

```text
Authorize
AuthorizationAspect
AuthorizationService
PrincipalService
PrincipalRoleService
RolePermissionGrantService
RoleAssignmentScopeService
AuthorizationConstraintService
CasbinService
CasbinPolicySyncService
CasbinPolicyBootstrapService
```

---

## Purpose

This project focuses on demonstrating a centralized and extensible authorization architecture where:

- Keycloak handles authentication.
- The application owns its authorization domain.
- Permissions are modeled using resource/action pairs.
- Roles can inherit permissions.
- Grants support ALLOW/DENY decisions and priority.
- Role assignments can be restricted by scope.
- Grants can contain additional authorization constraints.
- Casbin provides fast in-memory policy checks.
- Caffeine caches final authorization decisions.
- Authorization enforcement is applied declaratively using `@Authorize`.

The project is intended as an authorization architecture demonstration rather than a production-ready identity platform.
