package ru.practicum.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.model.EwmExceptionModel;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ErrorHandler {
    // 400 BAD_REQUEST, 409 CONFLICT
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleEwmException(final EwmException e) {
        EwmExceptionModel exceptionModel = e.getExceptionModel();
        Map<String, String> errorMap = Map.of(
                "status", exceptionModel.getStatus(),
                "reason", exceptionModel.getReason(),
                "message", exceptionModel.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(errorMap, exceptionModel.getHttpStatus());
    }

    // handle HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Map<String, String> errorMap = Map.of(
                "status", "BAD_REQUEST",
                "reason", "Incorrectly made request.",
                "message", Objects.requireNonNull(e.getMessage()),
                "timestamp", LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = Map.of(
                "status", "BAD_REQUEST",
                "reason", "Incorrectly made request.",
                "message", Objects.requireNonNull(e.getMessage()),
                "timestamp", LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationExceptions(DataIntegrityViolationException e) {
        Map<String, String> errorMap = Map.of(
                "status", "CONFLICT",
                "reason", "Data Integrity Violation.",
                "message", Objects.requireNonNull(e.getMessage()),
                "timestamp", LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(errorMap, HttpStatus.CONFLICT);
    }

}
