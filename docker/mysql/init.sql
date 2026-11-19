-- Database initialization script for production environment
CREATE DATABASE IF NOT EXISTS devops_db;
USE devops_db;

CREATE TABLE IF NOT EXISTS devops_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO devops_tasks (title, description, status, priority)
SELECT 'Setup Continuous Integration', 'Automate builds and test verification using GitHub Actions', 'COMPLETED', 'HIGH'
WHERE NOT EXISTS (SELECT 1 FROM devops_tasks WHERE title = 'Setup Continuous Integration');

INSERT INTO devops_tasks (title, description, status, priority)
SELECT 'Containerize Web App', 'Create multi-stage Dockerfile and test local execution', 'COMPLETED', 'HIGH'
WHERE NOT EXISTS (SELECT 1 FROM devops_tasks WHERE title = 'Containerize Web App');

INSERT INTO devops_tasks (title, description, status, priority)
SELECT 'Health Check Verification', 'Implement /api/health and Actuator endpoints for liveness probes', 'COMPLETED', 'MEDIUM'
WHERE NOT EXISTS (SELECT 1 FROM devops_tasks WHERE title = 'Health Check Verification');
