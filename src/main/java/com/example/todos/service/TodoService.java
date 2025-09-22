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
import org.springframework.data.domain.*;
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




    public TodoResponse create(UUID userId, CreateTodoRequest req){
        User owner = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Todo t = new Todo();
        t.setUser(owner);
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setDueDate(req.dueDate());
        if (t.getStatus() == null) t.setStatus(Todo.Status.PENDING);
        t.setCreatedAt(Instant.now());
        t.setUpdatedAt(t.getCreatedAt());

        t = todoRepo.save(t);
        return toResp(t);
    }


    @Transactional(readOnly = true)
    public TodoResponse get(UUID id){
        return todoRepo.findById(id)
                .map(this::toResp)
                .orElseThrow(() -> new NotFoundException("Todo not found: " + id));
    }


    @Transactional(readOnly = true)
    public List<TodoResponse> list(UUID userId,
                                   Todo.Status status,
                                   LocalDate dueBefore,
                                   Integer page,
                                   Integer size,
                                   String sort) {

        Page<TodoResponse> pageResult = listPage(userId, status, dueBefore, page, size, sort);
        return pageResult.getContent();
    }


    @Transactional(readOnly = true)
    public Page<TodoResponse> listPage(UUID userId,
                                       Todo.Status status,
                                       LocalDate dueBefore,
                                       Integer page,
                                       Integer size,
                                       String sort) {

        Pageable pageable = buildPageable(page, size, sort);

        Page<Todo> result;
        if (status != null && dueBefore != null) {
            result = todoRepo.findByUserIdAndStatusAndDueDateBefore(userId, status, dueBefore, pageable);
        } else if (status != null) {
            result = todoRepo.findByUserIdAndStatus(userId, status, pageable);
        } else if (dueBefore != null) {
            result = todoRepo.findByUserIdAndDueDateBefore(userId, dueBefore, pageable);
        } else {
            result = todoRepo.findByUserId(userId, pageable);
        }

        return result.map(this::toResp);
    }


    public TodoResponse update(UUID id, UpdateTodoRequest req){
        Todo t = todoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found: " + id));

        req.title().ifPresent(t::setTitle);
        req.description().ifPresent(t::setDescription);
        req.status().ifPresent(t::setStatus);
        req.dueDate().ifPresent(t::setDueDate);

        t.setUpdatedAt(Instant.now());
        t = todoRepo.save(t);
        return toResp(t);
    }


    public void delete(UUID id){
        try {
            todoRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Todo not found: " + id);
        }
    }



    private Pageable buildPageable(Integer page, Integer size, String sort) {
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0 || size > 100) ? 20 : size;


        Sort sortObj;
        if (sort != null && sort.contains(",")) {
            String[] parts = sort.split(",", 2);
            String prop = parts[0].trim();
            Sort.Direction dir = parts[1].trim().equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortObj = Sort.by(new Sort.Order(dir, prop));
        } else if (sort != null && !sort.isBlank()) {
            // Si viene solo el campo, usar ASC por defecto
            sortObj = Sort.by(Sort.Direction.ASC, sort.trim());
        } else {

            sortObj = Sort.by(Sort.Direction.ASC, "dueDate");
        }

        return PageRequest.of(p, s, sortObj);
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


    public static record PageResult<T>(
            List<T> content,
            long totalElements,
            int totalPages,
            int page,
            int size
    ) {}
}



