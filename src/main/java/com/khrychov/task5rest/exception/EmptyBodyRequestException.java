package com.khrychov.task5rest.exception;

public class EmptyBodyRequestException extends RuntimeException {

    public EmptyBodyRequestException(String message) {
        super(message);
    }
}
