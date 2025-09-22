package com.example.todos.controller;

import com.example.todos.dto.CreateUserRequest;
import com.example.todos.dto.UserResponse;
import com.example.todos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService svc;

    public UserController(UserService svc) {
        this.svc = svc;
    }

    @Operation(summary = "Create user", description = "Creates a new user with unique username and email.")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "409", description = "Duplicate username or email")
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(req));
    }

    @Operation(summary = "Get user by id")
    @ApiResponse(responseCode = "200", description = "Found")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable UUID id){
        return ResponseEntity.ok(svc.get(id));
    }

    @Operation(summary = "List users", description = "Returns all users.")
    @GetMapping
    public ResponseEntity<List<UserResponse>> list() {
        return ResponseEntity.ok(svc.list());
    }
}

