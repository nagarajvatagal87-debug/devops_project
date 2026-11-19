package com.devops.cicddemo.repository;

import com.devops.cicddemo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatusIgnoreCase(String status);

    List<Task> findByPriorityIgnoreCase(String priority);

    long countByStatusIgnoreCase(String status);
}
