package com.example.todos.controller;

import com.example.todos.domain.Todo;
import com.example.todos.dto.CreateTodoRequest;
import com.example.todos.dto.TodoResponse;
import com.example.todos.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class TodoController {
    private final TodoService svc;
    public TodoController(TodoService svc) { this.svc = svc; }

    @PostMapping("/users/{userId}/todos")
    public ResponseEntity<?> create(@PathVariable UUID userId, @Valid @RequestBody CreateTodoRequest req){
        Optional<TodoResponse> o = svc.create(userId, req);
        return o.<ResponseEntity<?>>map(tr -> ResponseEntity.status(HttpStatus.CREATED).body(tr))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(java.util.Map.of("error","User not found")));
    }

    @GetMapping("/users/{userId}/todos")
    public ResponseEntity<List<TodoResponse>> list(@PathVariable UUID userId,
                                                   @RequestParam(required=false) Todo.Status status,
                                                   @RequestParam(required=false) LocalDate dueBefore){
        return ResponseEntity.ok(svc.list(userId, status, dueBefore));
    }
}

