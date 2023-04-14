package ru.practicum.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotFoundException(final NotFoundException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
