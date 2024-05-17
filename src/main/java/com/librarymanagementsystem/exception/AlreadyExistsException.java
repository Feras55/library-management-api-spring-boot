package com.librarymanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    private String errorMessage;

    public AlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s with %s: '%s' already exists", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public AlreadyExistsException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
