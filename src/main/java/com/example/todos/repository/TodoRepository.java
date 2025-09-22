package com.example.todos.repository;

import com.example.todos.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
    List<Todo> findByUserId(UUID userId);
    List<Todo> findByUserIdAndStatus(UUID userId, Todo.Status status);
    List<Todo> findByUserIdAndDueDateBefore(UUID userId, LocalDate dueBefore);
    List<Todo> findByUserIdAndStatusAndDueDateBefore(UUID userId, Todo.Status status, LocalDate dueBefore);
    Page<Todo> findByUserId(UUID userId, Pageable pageable);
    Page<Todo> findByUserIdAndStatus(UUID userId, Todo.Status status, Pageable pageable);
    Page<Todo> findByUserIdAndDueDateBefore(UUID userId, LocalDate dueBefore, Pageable pageable);
    Page<Todo> findByUserIdAndStatusAndDueDateBefore(UUID userId, Todo.Status status, LocalDate dueBefore, Pageable pageable);

}

