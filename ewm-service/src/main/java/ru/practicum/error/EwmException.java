package ru.practicum.error;

import ru.practicum.error.model.EwmExceptionModel;

public class EwmException extends Throwable {
    private final EwmExceptionModel exceptionModel;

    public EwmException(EwmExceptionModel exceptionModel) {
        super();
        this.exceptionModel = exceptionModel;
    }

    public EwmExceptionModel getExceptionModel() {
        return exceptionModel;
    }
}
