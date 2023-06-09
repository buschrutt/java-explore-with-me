package ru.practicum.error.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EwmExceptionModel {
    String message;
    String reason;
    String status;
    HttpStatus httpStatus;
}
