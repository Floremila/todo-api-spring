package com.example.todos.service;

import com.example.todos.api.NotFoundException;
import com.example.todos.domain.Todo;
import com.example.todos.domain.User;
import com.example.todos.dto.CreateTodoRequest;
import com.example.todos.dto.TodoResponse;
import com.example.todos.dto.UpdateTodoRequest;
import com.example.todos.repository.TodoRepository;
import com.example.todos.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepo;
    private final UserRepository userRepo;

    public TodoService(TodoRepository todoRepo, UserRepository userRepo) {
        this.todoRepo = todoRepo;
        this.userRepo = userRepo;
    }

    /** M3: crear y lanzar 404 si el usuario no existe */
    public TodoResponse create(UUID userId, CreateTodoRequest req){
        User owner = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Todo t = new Todo();
        t.setUser(owner);
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setDueDate(req.dueDate());
        // valores por defecto / timestamps
        if (t.getStatus() == null) t.setStatus(Todo.Status.PENDING);
        t.setCreatedAt(Instant.now());
        t.setUpdatedAt(t.getCreatedAt());

        t = todoRepo.save(t);
        return toResp(t);
    }

    /** M3: obtener 1; 404 si no existe */
    @Transactional(readOnly = true)
    public TodoResponse get(UUID id){
        return todoRepo.findById(id)
                .map(this::toResp)
                .orElseThrow(() -> new NotFoundException("Todo not found: " + id));
    }

    /** Lista con filtros existentes (sin cambios) */
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

    /** M3: update parcial; 404 si no existe */
    public TodoResponse update(UUID id, UpdateTodoRequest req){
        Todo t = todoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found: " + id));

        // Si tu UpdateTodoRequest usa Optional<>, aplica como tenías:
        req.title().ifPresent(t::setTitle);
        req.description().ifPresent(t::setDescription);
        req.status().ifPresent(t::setStatus);
        req.dueDate().ifPresent(t::setDueDate);

        t.setUpdatedAt(Instant.now());
        t = todoRepo.save(t);
        return toResp(t);
    }

    /** M3: delete con 1 round-trip; 404 si no existe */
    public void delete(UUID id){
        try {
            todoRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Todo not found: " + id);
        }
    }

    private TodoResponse toResp(Todo t){
        return new TodoResponse(
                t.getId(),
                t.getUser().getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getDueDate(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}


