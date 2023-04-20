package ru.practicum.error;

import ru.practicum.error.model.ewmExceptionModel;

public class ewmException extends Throwable {
    private final ewmExceptionModel exceptionModel;

    public ewmException(ewmExceptionModel exceptionModel) {
        super();
        this.exceptionModel = exceptionModel;
    }

    public ewmExceptionModel getExceptionModel() {
        return exceptionModel;
    }
}
