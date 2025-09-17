package com.example.todos.dto;

import com.example.todos.domain.Todo.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record UpdateTodoRequest(
        Optional<String> title,
        Optional<String> description,
        Optional<Status> status,
        Optional<LocalDate> dueDate
) {}

