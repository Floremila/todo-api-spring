package com.example.todos.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest req) {
        Map<String, String> fields = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }
        var body = new ErrorResponse(
                Instant.now(),
                400,
                "Bad Request",
                "Validation failed",
                req.getRequestURI(),
                fields
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex,
                                                        HttpServletRequest req) {
        var body = new ErrorResponse(
                Instant.now(),
                404,
                "Not Found",
                ex.getMessage(),
                req.getRequestURI(),
                null
        );
        return ResponseEntity.status(404).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex,
                                                        HttpServletRequest req) {
        var body = new ErrorResponse(
                Instant.now(),
                409,
                "Conflict",
                "Duplicate or constraint violation",
                req.getRequestURI(),
                null
        );
        return ResponseEntity.status(409).body(body);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex, HttpServletRequest req) {
        var body = new ErrorResponse(
                Instant.now(),
                500,
                "Internal Server Error",
                ex.getMessage(),
                req.getRequestURI(),
                null
        );
        return ResponseEntity.status(500).body(body);
    }
}

