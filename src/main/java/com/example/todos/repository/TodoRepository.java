package com.example.todos.repository;

import com.example.todos.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
    List<Todo> findByUserId(UUID userId);
    List<Todo> findByUserIdAndStatus(UUID userId, Todo.Status status);
    List<Todo> findByUserIdAndDueDateBefore(UUID userId, LocalDate dueBefore);
    List<Todo> findByUserIdAndStatusAndDueDateBefore(UUID userId, Todo.Status status, LocalDate dueBefore);
}

