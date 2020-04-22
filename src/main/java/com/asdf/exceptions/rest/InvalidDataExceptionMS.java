package com.asdf.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDataExceptionMS extends RuntimeException {
    public InvalidDataExceptionMS(String message) {
        super(message);
    }

    public InvalidDataExceptionMS(Throwable cause) {
        super(cause);
    }
}