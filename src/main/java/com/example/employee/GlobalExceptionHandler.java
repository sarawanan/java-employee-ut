package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> resourceNotFoundException(ResponseStatusException exception, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder()
                .date(new Date())
                .details(request.getDescription(false))
                .message(exception.getMessage()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> genericException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder()
                .date(new Date())
                .details(request.getDescription(false))
                .message(exception.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
