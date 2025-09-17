package com.example.todos.dto;

import com.example.todos.domain.Todo.Status;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TodoResponse(
        UUID id,
        UUID userId,
        String title,
        String description,
        Status status,
        LocalDate dueDate,
        Instant createdAt,
        Instant updatedAt
) {}

