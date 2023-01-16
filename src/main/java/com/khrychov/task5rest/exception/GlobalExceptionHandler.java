package com.khrychov.task5rest.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullEntityReferenceException.class)
    public ResponseEntity<?> handleNullEntityReferenceException(NullEntityReferenceException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}


