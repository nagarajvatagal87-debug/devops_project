package com.devops.cicddemo.model;

import java.time.LocalDateTime;
import java.util.Map;

public class HealthResponse {

    private String status;
    private String application;
    private String version;
    private String environment;
    private LocalDateTime timestamp;
    private long uptimeSeconds;
    private Map<String, Object> details;

    public HealthResponse() {
    }

    public HealthResponse(String status, String application, String version, String environment,
                          LocalDateTime timestamp, long uptimeSeconds, Map<String, Object> details) {
        this.status = status;
        this.application = application;
        this.version = version;
        this.environment = environment;
        this.timestamp = timestamp;
        this.uptimeSeconds = uptimeSeconds;
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getUptimeSeconds() {
        return uptimeSeconds;
    }

    public void setUptimeSeconds(long uptimeSeconds) {
        this.uptimeSeconds = uptimeSeconds;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
