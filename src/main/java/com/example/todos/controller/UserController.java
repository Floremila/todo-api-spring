package com.example.todos.controller;

import com.example.todos.dto.CreateUserRequest;
import com.example.todos.dto.UserResponse;
import com.example.todos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService svc;
    public UserController(UserService svc){ this.svc = svc; }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req){
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable UUID id){
        Optional<UserResponse> o = svc.find(id);
        return ResponseEntity.of(o); // 200 si existe, 404 si no
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> list(){
        return ResponseEntity.ok(svc.list());
    }
}

