package com.example.todos.service;

import com.example.todos.domain.User;
import com.example.todos.dto.CreateUserRequest;
import com.example.todos.dto.UserResponse;
import com.example.todos.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service @Transactional
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserResponse create(CreateUserRequest req){
        User u = new User();
        u.setUsername(req.username());
        u.setEmail(req.email());
        u = repo.save(u);
        return toResp(u);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> find(UUID id){
        return repo.findById(id).map(this::toResp);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> list(){
        return repo.findAll().stream().map(this::toResp).toList();
    }

    private UserResponse toResp(User u){
        return new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getCreatedAt());
    }
}

