package com.librarymanagementsystem.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDateException extends RuntimeException {
    public InvalidDateException() {
        super(String.format("Return date must be in the future."));
    }
}
