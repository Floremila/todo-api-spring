package com.example.todos.service;

import com.example.todos.domain.Todo;
import com.example.todos.dto.CreateTodoRequest;
import com.example.todos.dto.TodoResponse;
import com.example.todos.repository.TodoRepository;
import com.example.todos.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service @Transactional
public class TodoService {
    private final TodoRepository todoRepo;
    private final UserRepository userRepo;

    public TodoService(TodoRepository todoRepo, UserRepository userRepo) {
        this.todoRepo = todoRepo;
        this.userRepo = userRepo;
    }

    public Optional<TodoResponse> create(UUID userId, CreateTodoRequest req){
        return userRepo.findById(userId).map(user -> {
            Todo t = new Todo();
            t.setUser(user);
            t.setTitle(req.title());
            t.setDescription(req.description());
            t.setDueDate(req.dueDate());
            t = todoRepo.save(t);
            return toResp(t);
        });
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> list(UUID userId, Todo.Status status, LocalDate dueBefore){
        if (status != null && dueBefore != null) {
            return todoRepo.findByUserIdAndStatusAndDueDateBefore(userId, status, dueBefore)
                    .stream().map(this::toResp).toList();
        }
        if (status != null) {
            return todoRepo.findByUserIdAndStatus(userId, status)
                    .stream().map(this::toResp).toList();
        }
        if (dueBefore != null) {
            return todoRepo.findByUserIdAndDueDateBefore(userId, dueBefore)
                    .stream().map(this::toResp).toList();
        }
        return todoRepo.findByUserId(userId).stream().map(this::toResp).toList();
    }


    private TodoResponse toResp(Todo t){
        return new TodoResponse(
                t.getId(), t.getUser().getId(), t.getTitle(), t.getDescription(),
                t.getStatus(), t.getDueDate(), t.getCreatedAt(), t.getUpdatedAt()
        );
    }
}

