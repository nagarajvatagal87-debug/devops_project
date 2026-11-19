# Automated CI/CD Pipeline for Web Application Deployment

[![DevOps CI/CD Pipeline](https://github.com/nagarajvatagal87-debug/devops_project/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/nagarajvatagal87-debug/devops_project/actions)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue.svg)
![GitHub Actions](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-blueviolet.svg)

An automated software delivery pipeline demonstrating Continuous Integration and Continuous Deployment (CI/CD) for a modern full-stack web application using Git, GitHub Actions, Docker, and Spring Boot.

---

## 📋 Table of Contents
1. [Project Overview](#-project-overview)
2. [Architecture & Technology Stack](#-architecture--technology-stack)
3. [End-to-End Workflow Pipeline](#-end-to-end-workflow-pipeline)
4. [Project Structure](#-project-structure)
5. [Local Development & Execution](#-local-development--execution)
6. [Containerization with Docker](#-containerization-with-docker)
7. [Multi-Container Orchestration (Docker Compose)](#-multi-container-orchestration-docker-compose)
8. [Automated CI/CD Pipeline (GitHub Actions)](#-automated-cicd-pipeline-github-actions)
9. [Health & Verification Endpoints](#-health--verification-endpoints)
10. [Deliverables & Evidence](#-deliverables--evidence)

---

## 🎯 Project Overview

Manual software delivery involves repetitive steps, inconsistent environments, and late discovery of defects. This project resolves these issues by implementing a repeatable, automated delivery pipeline:
- **Continuous Integration (CI)**: Automatically compiles code, resolves dependencies, and executes unit & integration tests on every commit and pull request.
- **Continuous Delivery (CD)**: Automatically packages the application into a lightweight, multi-stage Docker container image and verifies availability using automated smoke tests.
- **Monitoring & Observability**: Provides health probes, Actuator metrics, and Prometheus integration.

---

## 🛠 Architecture & Technology Stack

| Stage / Component | Tool / Technology | Responsibility |
| :--- | :--- | :--- |
| **Source Control** | Git + GitHub | Version control, change history, and branching. |
| **CI/CD Orchestrator** | GitHub Actions | Automated workflow triggers, runner jobs, and pipeline execution. |
| **Backend Framework** | Spring Boot 3 (Java 21) | RESTful APIs, Spring Data JPA, Actuator health probes. |
| **Testing** | JUnit 5 & MockMvc | Automated unit and integration testing. |
| **Frontend** | Modern HTML5/CSS/JS Dashboard | Responsive UI showing live pipeline metrics, health tester, and tasks. |
| **Database** | H2 (Dev/Test) & MySQL 8 (Prod) | In-memory DB for rapid CI tests; persistent relational DB for Docker. |
| **Containerization** | Docker (Multi-Stage) | Secure packaging with Eclipse Temurin 21 Alpine runtime. |
| **Orchestration** | Docker Compose | Multi-container stack (App + MySQL + Prometheus). |
| **Monitoring** | Spring Actuator + Prometheus | Metrics scraping via `/actuator/prometheus`. |

---

## 🔄 End-to-End Workflow Pipeline

```
┌──────────────┐     ┌──────────────┐     ┌──────────────────────┐
│  01. DEVELOP │ ──> │ 02. PUSH GIT │ ──> │ 03. BUILD & TEST (CI) │
│  Spring Boot │     │    GitHub    │     │    Maven + JUnit     │
└──────────────┘     └──────────────┘     └──────────────────────┘
                                                     │
                                                     ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────────────┐
│  06. VERIFY  │ <── │  05. DEPLOY  │ <── │ 04. BUILD IMAGE (CD) │
│ Health Check │     │ Docker / Prod│     │  Docker Multi-Stage  │
└──────────────┘     └──────────────┘     └──────────────────────┘
```

1. **Develop**: Write clean application code, REST endpoints, and JUnit test suites.
2. **Push**: Commit and push changes to the GitHub repository.
3. **Build & Test (CI)**: GitHub Actions executes `mvn clean test` to validate code automatically.
4. **Build Image (CD)**: Builds the container image using a multi-stage Dockerfile.
5. **Deploy**: Deploys the containerized application to the target runtime.
6. **Verify**: Runs automated smoke tests against `/api/health/ping` and `/api/health`.

---

## 📁 Project Structure

```
devops_project/
├── .github/
│   └── workflows/
│       └── ci-cd.yml             # GitHub Actions CI/CD Pipeline Definition
├── docker/
│   ├── mysql/
│   │   └── init.sql              # Database initialization script
│   └── prometheus/
│       └── prometheus.yml        # Prometheus metrics scraper config
├── src/
│   ├── main/
│   │   ├── java/com/devops/cicddemo/
│   │   │   ├── CicdDemoApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── HealthController.java
│   │   │   │   ├── TaskController.java
│   │   │   │   └── SystemInfoController.java
│   │   │   ├── model/
│   │   │   │   ├── Task.java
│   │   │   │   └── HealthResponse.java
│   │   │   ├── repository/
│   │   │   │   └── TaskRepository.java
│   │   │   └── service/
│   │   │       └── TaskService.java
│   │   └── resources/
│   │       ├── application.yml         # Dev config (H2 DB & Actuator)
│   │       ├── application-prod.yml    # Prod config (MySQL)
│   │       └── static/                 # Interactive Dashboard UI
│   │           ├── index.html
│   │           ├── css/styles.css
│   │           └── js/app.js
│   └── test/
│       └── java/com/devops/cicddemo/
│           └── CicdDemoApplicationTests.java
├── Dockerfile                    # Multi-stage production container build
├── .dockerignore                 # Docker context optimization
├── docker-compose.yml            # Multi-service composition (App, DB, Prometheus)
├── pom.xml                       # Maven dependencies & build configuration
└── README.md                     # Project documentation
```

---

## 💻 Local Development & Execution

### Prerequisites
- Java 21 JDK
- Maven 3.9+
- Docker Desktop (for container deployment)

### Run Standalone Application
```bash
# Build and run with Maven
mvn clean spring-boot:run
```
- Open [http://localhost:8080](http://localhost:8080) to access the interactive web dashboard.
- Access the H2 database console at [http://localhost:8080/h2-console](http://localhost:8080/h2-console).

---

## 🐳 Containerization with Docker

The project uses a **multi-stage Dockerfile** for optimal build caching, security, and minimal image size:

```bash
# Build the Docker image
docker build -t devops-cicd-demo:latest .

# Run the container
docker run -d -p 8080:8080 --name cicd-app devops-cicd-demo:latest

# Check container status & health
docker ps
```

---

## 📦 Multi-Container Orchestration (Docker Compose)

To run the complete production stack (Web Application, MySQL 8, and Prometheus):

```bash
# Start all containers in background
docker compose up -d

# Verify all services are healthy
docker compose ps

# View live application logs
docker compose logs -f app

# Tear down the stack
docker compose down
```

### Exposed Endpoints:
- **Web Application Dashboard**: [http://localhost:8080](http://localhost:8080)
- **Prometheus Metrics Explorer**: [http://localhost:9090](http://localhost:9090)
- **MySQL Database**: `localhost:3306`

---

## ⚙️ Automated CI/CD Pipeline (GitHub Actions)

The pipeline defined in [`.github/workflows/ci-cd.yml`](.github/workflows/ci-cd.yml) runs automatically on every `push` and `pull_request` to `main`:

```yaml
Jobs executed in GitHub Actions:
├── 1. Build & Run Automated Tests  (Compile, JUnit 5, Package JAR, Upload Artifact)
├── 2. Docker Build & Smoke Test    (Docker Buildx, Container Launch, Health Probe Smoke Test)
└── 3. Continuous Deployment        (Verification & Production Ready)
```

---

## 🩺 Health & Verification Endpoints

| Endpoint | Method | Purpose |
| :--- | :--- | :--- |
| `/api/health` | `GET` | Comprehensive system health JSON (database, memory, uptime, container status). |
| `/api/health/ping` | `GET` | Fast liveness probe returning `PONG`. |
| `/api/system/info` | `GET` | JVM, OS, processor, and runtime metadata. |
| `/api/tasks` | `GET/POST` | RESTful task management API backed by JPA. |
| `/actuator/health` | `GET` | Spring Boot Actuator standard health check. |
| `/actuator/prometheus` | `GET` | Prometheus formatted application metrics. |

---

## 🏆 Deliverables & Evidence

1. **GitHub Actions Workflow**: 100% green checkmarks across Build & Test, Docker Packaging, and Smoke Verification stages.
2. **Docker Artifact**: Multi-stage, unprivileged Alpine container image ready for deployment.
3. **Automated Smoke Tests**: Validated with automated HTTP ping and health inspection.
4. **Full Documentation**: Complete architecture, setup guide, and presentation reference.
