package com.devops.cicddemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemInfoController {

    private final Environment environment;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${spring.application.name:devops-cicd-app}")
    private String appName;

    public SystemInfoController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        Runtime runtime = Runtime.getRuntime();

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("application", appName);
        info.put("version", appVersion);
        info.put("activeProfiles", environment.getActiveProfiles());
        info.put("isContainerized", System.getenv("IS_DOCKER_CONTAINER") != null);
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("javaVendor", System.getProperty("java.vendor"));
        info.put("osName", osBean.getName());
        info.put("osArch", osBean.getArch());
        info.put("osVersion", osBean.getVersion());
        info.put("availableProcessors", runtime.availableProcessors());
        info.put("totalMemoryMb", runtime.totalMemory() / (1024 * 1024));
        info.put("freeMemoryMb", runtime.freeMemory() / (1024 * 1024));
        info.put("maxMemoryMb", runtime.maxMemory() / (1024 * 1024));
        info.put("jvmUptimeSeconds", ManagementFactory.getRuntimeMXBean().getUptime() / 1000);
        info.put("serverTime", LocalDateTime.now(ZoneId.systemDefault()).toString());

        return ResponseEntity.ok(info);
    }
}
