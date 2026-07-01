<div align="center">

# Microservice Email

Microservice responsible for sending and tracking emails, built with **Spring Boot** and **PostgreSQL**.

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

</div>

---

## About the project

This microservice exposes a REST API for sending emails via SMTP, persisting the history of each send (status, sender, recipient, subject, and content) to a PostgreSQL database.

It is part of a microservices architecture and can be consumed by other services in the application to centralize email notification dispatch.

---

## Tech stack

- **Java 17+**
- **Spring Boot**
- **Spring Web** — REST API layer
- **Spring Data JPA** — data persistence
- **Bean Validation** (`jakarta.validation`) — DTO validation
- **Lombok** — boilerplate reduction
- **PostgreSQL** — relational database
- **Spring Mail** — SMTP email sending

---

## Project structure

```
com.ms.email
├── controllers
│   └── EmailController.java      # REST endpoints
├── dtos
│   └── EmailDto.java             # Validated input object
├── enums
│   └── StatusEmail.java          # Send status (PENDING, ERROR, SENT)
├── models
│   └── EmailModel.java           # Persisted entity (TB_EMAIL)
├── repositories
│   └── EmailRepository.java      # Data access via JPA
└── services
    └── EmailService.java         # Email sending business logic
```

---

## Endpoints

### Send email

```http
POST /sending-email
```

**Request Body**

```json
{
  "ownerRef": "student-001",
  "emailFrom": "sender@example.com",
  "emailTo": "recipient@example.com",
  "subject": "Email subject",
  "text": "Email content"
}
```

**Validation rules**

| Field | Rule |
|---|---|
| `ownerRef` | required |
| `emailFrom` | required, valid email format |
| `emailTo` | required, valid email format |
| `subject` | required |
| `text` | required |

**Response** `200 OK`

```json
{
  "emailId": 1,
  "ownerRef": "student-001",
  "emailFrom": "sender@example.com",
  "emailTo": "recipient@example.com",
  "subject": "Email subject",
  "text": "Email content",
  "sendDateEmail": "2026-07-01T14:32:00",
  "statusEmail": "SENT",
  "error": null
}
```

---

## Configuration

This project should **not** contain credentials directly in `application.properties`. Set the environment variables below before running the application.

```properties
# Application name
spring.application.name=Microservice Email

# Server
server.port=8080

# PostgreSQL database
spring.datasource.url=jdbc:postgresql://localhost:5432/emaildb
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Email sending (SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Required environment variables

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_USERNAME` | PostgreSQL database user |
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL database password |
| `MAIL_USERNAME` | Email address used for sending (SMTP) |
| `MAIL_PASSWORD` | Gmail app password (**not** your regular account password) |

> **Never** commit real credentials to the repository. Use a `.env` file (ignored by `.gitignore`) or system/CI environment variables.

---

## Getting started

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL running locally (or via Docker)

### Steps

```bash
# Clone the repository
git clone https://github.com/your-username/microservice-email.git
cd microservice-email

# Set environment variables
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export MAIL_USERNAME=your_email@gmail.com
export MAIL_PASSWORD=your_app_password

# Start the database (example with Docker)
docker run --name emaildb -e POSTGRES_DB=emaildb -e POSTGRES_PASSWORD=your_password -p 5432:5432 -d postgres

# Run the application
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## Data model — `TB_EMAIL`

| Field | Type | Description |
|---|---|---|
| `emailId` | `long` | Unique identifier (auto-generated) |
| `ownerRef` | `String` | Reference to the sender/originator |
| `emailFrom` | `String` | Sender email |
| `emailTo` | `String` | Recipient email |
| `subject` | `String` | Subject |
| `text` | `TEXT` | Email content |
| `sendDateEmail` | `LocalDateTime` | Send date/time |
| `statusEmail` | `enum` | `PENDING` \| `ERROR` \| `SENT` |
| `error` | `String` | Error message, if any |

---

## Roadmap

- [ ] Automatic retry for emails with `ERROR` status
- [ ] Query emails by `ownerRef` and `statusEmail`
- [ ] Unit and integration tests
- [ ] API documentation with Swagger/OpenAPI
- [ ] Asynchronous sending via message queue (RabbitMQ/Kafka)

---

<div align="center">

Built by **Luís F. Lima**

</div>
