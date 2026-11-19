package com.devops.cicddemo;

import com.devops.cicddemo.model.Task;
import com.devops.cicddemo.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class CicdDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CicdDemoApplication.class, args);
    }

    /**
     * Pre-populate database with default DevOps mini-project milestone tasks.
     */
    @Bean
    public CommandLineRunner initDatabase(TaskRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Task(
                        null,
                        "Phase 1: Application Architecture & Source Code",
                        "Develop Spring Boot web application with RESTful APIs, JPA, and Health endpoints.",
                        "COMPLETED",
                        "HIGH",
                        LocalDateTime.now().minusHours(4),
                        LocalDateTime.now().minusHours(3)
                ));
                repository.save(new Task(
                        null,
                        "Phase 2: CI Pipeline with Automated Testing",
                        "Configure GitHub Actions workflow for automated Maven compile, JUnit 5 unit and integration tests.",
                        "COMPLETED",
                        "HIGH",
                        LocalDateTime.now().minusHours(3),
                        LocalDateTime.now().minusHours(2)
                ));
                repository.save(new Task(
                        null,
                        "Phase 3: Docker Containerization",
                        "Create multi-stage Dockerfile and Docker Compose orchestration with MySQL and Prometheus.",
                        "COMPLETED",
                        "HIGH",
                        LocalDateTime.now().minusHours(2),
                        LocalDateTime.now().minusHours(1)
                ));
                repository.save(new Task(
                        null,
                        "Phase 4: Continuous Deployment & Health Verification",
                        "Automate container rollout, run post-deployment smoke tests, and monitor availability.",
                        "COMPLETED",
                        "HIGH",
                        LocalDateTime.now().minusHours(1),
                        LocalDateTime.now()
                ));
            }
        };
    }
}
