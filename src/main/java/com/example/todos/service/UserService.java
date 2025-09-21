package com.example.todos.service;

import com.example.todos.api.NotFoundException;
import com.example.todos.domain.User;
import com.example.todos.dto.CreateUserRequest;
import com.example.todos.dto.UserResponse;
import com.example.todos.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserResponse create(CreateUserRequest req) {
        var u = new User();
        u.setId(UUID.randomUUID());
        u.setUsername(req.username());
        u.setEmail(req.email());
        u.setCreatedAt(Instant.now());

        var saved = repo.save(u);
        repo.flush();
        return toResp(saved);
    }

    public UserResponse get(UUID id) {
        return repo.findById(id)
                .map(this::toResp)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    public List<UserResponse> list() {
        return repo.findAll().stream().map(this::toResp).toList();
    }

    private UserResponse toResp(User u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getCreatedAt());
    }
}


