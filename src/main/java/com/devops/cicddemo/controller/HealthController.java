package com.devops.cicddemo.controller;

import com.devops.cicddemo.model.HealthResponse;
import com.devops.cicddemo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final TaskRepository taskRepository;
    private final Environment environment;

    @Value("${spring.application.name:devops-cicd-app}")
    private String applicationName;

    @Value("${app.version:1.0.0}")
    private String applicationVersion;

    public HealthController(TaskRepository taskRepository, Environment environment) {
        this.taskRepository = taskRepository;
        this.environment = environment;
    }

    /**
     * Comprehensive health check endpoint returning JSON payload.
     * Evaluates application state, uptime, active profiles, and database connectivity.
     */
    @GetMapping
    public ResponseEntity<HealthResponse> getHealth() {
        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        long uptimeSeconds = uptimeMillis / 1000;

        String[] activeProfiles = environment.getActiveProfiles();
        String currentEnv = activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default (dev/h2)";

        Map<String, Object> details = new LinkedHashMap<>();
        
        // Database connectivity probe
        boolean dbHealthy;
        try {
            long count = taskRepository.count();
            details.put("databaseStatus", "UP");
            details.put("databaseRecordCount", count);
            dbHealthy = true;
        } catch (Exception ex) {
            details.put("databaseStatus", "DOWN");
            details.put("databaseError", ex.getMessage());
            dbHealthy = false;
        }

        // System & Container metrics
        details.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        details.put("freeMemoryMb", Runtime.getRuntime().freeMemory() / (1024 * 1024));
        details.put("totalMemoryMb", Runtime.getRuntime().totalMemory() / (1024 * 1024));
        details.put("javaVersion", System.getProperty("java.version"));
        details.put("osName", System.getProperty("os.name"));
        details.put("dockerRunning", System.getenv("IS_DOCKER_CONTAINER") != null);

        String overallStatus = dbHealthy ? "UP" : "DEGRADED";

        HealthResponse response = new HealthResponse(
                overallStatus,
                applicationName,
                applicationVersion,
                currentEnv,
                LocalDateTime.now(),
                uptimeSeconds,
                details
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Fast, lightweight ping check for Kubernetes/Docker container probes.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("PONG");
    }
}
