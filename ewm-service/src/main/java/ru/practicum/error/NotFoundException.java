package ru.practicum.error;

public class NotFoundException extends Throwable {
    public NotFoundException(final String message) {
        super(message);
    }
}

