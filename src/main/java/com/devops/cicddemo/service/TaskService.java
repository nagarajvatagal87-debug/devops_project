package com.devops.cicddemo.service;

import com.devops.cicddemo.model.Task;
import com.devops.cicddemo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatusIgnoreCase(status);
    }

    public Task createTask(Task task) {
        if (task.getStatus() == null || task.getStatus().isBlank()) {
            task.setStatus("PENDING");
        }
        if (task.getPriority() == null || task.getPriority().isBlank()) {
            task.setPriority("MEDIUM");
        }
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(existing -> {
            if (updatedTask.getTitle() != null && !updatedTask.getTitle().isBlank()) {
                existing.setTitle(updatedTask.getTitle());
            }
            if (updatedTask.getDescription() != null) {
                existing.setDescription(updatedTask.getDescription());
            }
            if (updatedTask.getStatus() != null && !updatedTask.getStatus().isBlank()) {
                existing.setStatus(updatedTask.getStatus().toUpperCase());
            }
            if (updatedTask.getPriority() != null && !updatedTask.getPriority().isBlank()) {
                existing.setPriority(updatedTask.getPriority().toUpperCase());
            }
            return taskRepository.save(existing);
        });
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Map<String, Long> getTaskStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", taskRepository.count());
        stats.put("completed", taskRepository.countByStatusIgnoreCase("COMPLETED"));
        stats.put("inProgress", taskRepository.countByStatusIgnoreCase("IN_PROGRESS"));
        stats.put("pending", taskRepository.countByStatusIgnoreCase("PENDING"));
        return stats;
    }
}
