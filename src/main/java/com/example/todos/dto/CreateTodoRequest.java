package com.example.todos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateTodoRequest(
        @NotBlank @Size(min = 3, max = 140) String title,
        @Size(max = 1000) String description,
        LocalDate dueDate
) {}
