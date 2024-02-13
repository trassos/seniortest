package com.trassos.domain.exception;

public class SeniorStandardException extends RuntimeException {
    public SeniorStandardException(String message) {
        super(message);
    }

    public SeniorStandardException(String message, Throwable cause) {
        super(message, cause);
    }
}
