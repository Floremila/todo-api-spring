package com.example.todos.controller;

import com.example.todos.domain.Todo;
import com.example.todos.dto.CreateTodoRequest;
import com.example.todos.dto.TodoResponse;
import com.example.todos.dto.UpdateTodoRequest;
import com.example.todos.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class TodoController {

    private final TodoService svc;

    public TodoController(TodoService svc) {
        this.svc = svc;
    }

    @PostMapping("/users/{userId}/todos")
    public ResponseEntity<TodoResponse> create(@PathVariable UUID userId,
                                               @Valid @RequestBody CreateTodoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(userId, req));
    }


    @GetMapping("/users/{userId}/todos")
    public ResponseEntity<List<TodoResponse>> list(@PathVariable UUID userId,
                                                   @RequestParam(required = false) Todo.Status status,
                                                   @RequestParam(required = false) LocalDate dueBefore,
                                                   @RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer size,
                                                   @RequestParam(required = false, defaultValue = "dueDate,asc") String sort) {

        Page<TodoResponse> p = svc.listPage(userId, status, dueBefore, page, size, sort);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(p.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(p.getTotalPages()))
                .header("X-Page", String.valueOf(p.getNumber()))
                .header("X-Size", String.valueOf(p.getSize()))
                .body(p.getContent());
    }

    @PatchMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable UUID id,
                                               @Valid @RequestBody UpdateTodoRequest req) {
        return ResponseEntity.ok(svc.update(id, req));
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}


