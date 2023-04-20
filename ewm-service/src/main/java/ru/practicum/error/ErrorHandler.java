package ru.practicum.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.model.ewmExceptionModel;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    // 400 BAD_REQUEST, 409 CONFLICT
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleEwmException(final ewmException e) {
        ewmExceptionModel exceptionModel = e.getExceptionModel();
        Map<String, String> errorMap = Map.of(
                "state", exceptionModel.getStatus(),
                "reason", exceptionModel.getReason(),
                "message", exceptionModel.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(errorMap, exceptionModel.getHttpStatus());
    }

}
