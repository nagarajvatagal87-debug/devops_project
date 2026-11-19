package com.devops.cicddemo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CicdDemoApplicationTests {

    @Test
    @DisplayName("Verify Spring Boot Application Context loads successfully")
    void contextLoads() {
        assertTrue(true, "Application context initialized without errors.");
    }
}
