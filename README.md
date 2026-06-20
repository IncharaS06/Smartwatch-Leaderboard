# Smartwatch Leaderboard Gaming Platform API

A secure, testable, and high-performance RESTful API for a smartwatch leaderboard gaming platform built with **Java 17**, **Spring Boot 3.x**, **Spring Data JPA (Hibernate)**, **MySQL/MariaDB**, **Spring Security (JWT)**, **Spring Batch**, **Apache Kafka**, and **JUnit 5**.

---

## Technical Stack
*   **Backend**: Java 17+, Spring Boot 3.3.0
*   **Security**: Spring Security (Stateless JWT Authentication, BCrypt Password Hashing)
*   **Database**: MySQL / MariaDB (production), H2 (in-memory test environment)
*   **ORM**: Spring Data JPA (Hibernate)
*   **Asynchronous Messaging**: Apache Kafka (Producer, Consumer) with zero-setup **In-Memory Mock Ingestion** support
*   **Batch Processing**: Spring Batch 5.x (Automated Ranking & Gamification Engines)
*   **API Docs**: Swagger UI (springdoc-openapi)
*   **Testing**: JUnit 5, Mockito
*   **Code Coverage**: JaCoCo (Target: >= 70% coverage)

---

## Core Features Implemented

### 1. Security & Authentication
*   **Stateless JWT Authentication**: Secure routes by roles: Users (`ROLE_USER`) and Administrators (`ROLE_ADMIN`).
*   **Custom Global Error Mapping**: Standardizes validation and security authorization failures into simplified `ErrorResponse` payloads:
    ```json
    {
      "code": "BAD_REQUEST",
      "message": "Step Count Value is required",
      "timestamp": 1781600000000
    }
    ```
*   **Default Admin Seeder**: On startup, the system automatically checks and seeds a default system admin if none exists:
    *   **Phone**: `9999999999`
    *   **Password**: `admin123`

### 2. Smartwatch Devices & Set-Intersection Capability Validation
*   Devices are mapped with brands, models, and hardware capability tags (e.g. `GPS`, `Heart Rate`, `Accelerometer`).
*   **Set-Intersection Hardware Checks**: Users can assign smartwatches to their profiles and register for challenges. The system performs set-intersection logic to verify that the user's registered watch has **all** hardware capabilities required by the challenge. If any required tag is missing, the request is rejected with a `400 Bad Request`.

### 3. Asynchronous Telemetry Ingestion (Kafka)
*   Smartwatches publish telemetry logs (step counts, hardware tags) asynchronously to Kafka topics.
*   **Local In-Memory Mock Mode**: We implemented a hybrid mock mode (`kafka.enabled: false` in `application.yml`). When false, telemetry is instantly delivered in-memory without needing a local running Kafka server, stopping background broker polling warnings completely!

### 4. Automated Ranking (Spring Batch)
*   Expired challenges are finalized by a background Spring Batch job (`rankingJob`).
*   Ranks participants in order of their accumulated steps (total score) and persists final numerical standings.

### 5. Gamification & Digital Rewards (Spring Batch)
*   Point-accumulation thresholds are automatically seeded (`Novice`: 100pts, `Athlete`: 500pts, `Champion`: 1000pts).
*   A dedicated Spring Batch job (`gamificationJob`) processes users' points against rewards and awards earned digital badges to their profiles, available immediately on `GET /user/{userId}`.

---

## Running the Application Locally

### Prerequisites
1.  **Java 17** or higher installed.
2.  **Maven 3** installed.
3.  **MySQL/MariaDB** server running on port `3306`.
    *   Create a database named `leaderboard`:
        ```sql
        CREATE DATABASE IF NOT EXISTS leaderboard;
        ```

### Configuration
Open `src/main/resources/application.yml` and replace the MySQL password with your local credentials:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/leaderboard?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: root
    password: YOUR_LOCAL_MYSQL_PASSWORD
```

### Build & Run
1.  Navigate to the project root folder.
2.  Build the project and run tests:
    ```bash
    mvn clean test
    ```
3.  Start the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```

The application will start on **`http://localhost:8081`**.

---

## API Visualizations & Swagger Documentation
Once the application is running, open your browser and navigate to:
*   **Health Endpoint**: [http://localhost:8081/health](http://localhost:8081/health)
*   **Interactive Swagger Docs**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

Swagger has been fully configured with JWT Security Scheme. Paste your token after logging in into the **"Authorize"** button at the top to access secured endpoints.

---

## Core API Endpoints & Example Payloads

### 1. Authentication

*   **`POST /auth/register` (Public)**
    ```json
    {
      "phone": "9876543210",
      "email": "user@example.com",
      "fullName": "John Doe",
      "password": "password123"
    }
    ```
*   **`POST /auth/login` (Public)** — returns secure JWT.
    ```json
    {
      "phone": "9876543210",
      "password": "password123"
    }
    ```
*   **`POST /auth/logout` (Secured)** — returns `204 No Content`.

### 2. Smartwatch Devices
*   **`POST /device` (Admin Only)**
    ```json
    {
      "brand": "Garmin",
      "name": "Forerunner 965",
      "featureTags": ["GPS", "Heart Rate"]
    }
    ```
*   **`POST /user/{userId}/device/{deviceId}` (User, Admin)** — Assigns device to user.

### 3. Tasks & Challenges
*   **`POST /task` (Admin Only)** — Creates a never-expiring task.
    ```json
    {
      "title": "Morning Jog",
      "description": "5000 steps jog",
      "points": 100,
      "requiredFeatureTags": ["GPS"]
    }
    ```
*   **`POST /challenge` (User, Admin)** — Creates a time-bound challenge.
    ```json
    {
      "name": "Summer Run",
      "description": "Aggregated steps challenge",
      "expiryDate": "2026-06-16T23:59:59",
      "scopeType": "GLOBAL",
      "requiredFeatureTags": ["GPS"]
    }
    ```
*   **`POST /challenge/{challengeId}/join` (User, Admin)** — Joins a challenge. Automatically verifies set-intersection hardware compatibility!

### 4. Telemetry Ingestion (Kafka)
*   **`POST /user/{userId}` (User, Admin)** — Ingests smartwatch activity metrics.
    ```json
    {
      "stepCountValue": 5200,
      "requiredTagValue": "GPS",
      "taskId": 1,
      "date": "2026-06-17"
    }
    ```

### 5. Spring Batch Engine Triggers
*   **`GET /rank` (Admin Only)** — Runs Spring Batch ranking engine. Finalizes expired challenges.
*   **`GET /game` (Admin Only)** — Runs Spring Batch gamification engine. Awards badges to qualified users.

---

## Quality Assurance & Coverage Report
We utilize **JaCoCo** to track and analyze test coverage.
*   To run the test suite and generate the coverage report:
    ```bash
    mvn clean test
    ```
*   The generated HTML report is available at:
    `target/site/jacoco/index.html`

The comprehensive suite consists of **23 JUnit 5 unit and integration tests** focusing on core security, service logic, set-intersection calculations, and batch repository states, achieving **over 70% total code coverage**.
