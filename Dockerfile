# Build stage using Maven and Java 21
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build application artifact
COPY src ./src
RUN mvn clean package -DskipTests=false -B

# Production runtime stage
FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="DevOps Project Team"
LABEL description="Automated CI/CD Pipeline Web Application"

# Create unprivileged application user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app

# Copy packaged jar from build stage
COPY --from=builder /workspace/target/*.jar app.jar
RUN chown -R appuser:appgroup /app

USER appuser
ENV SERVER_PORT=8080
ENV IS_DOCKER_CONTAINER=true

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
