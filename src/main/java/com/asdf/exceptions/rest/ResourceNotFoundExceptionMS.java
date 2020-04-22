package com.asdf.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundExceptionMS extends RuntimeException {
    public ResourceNotFoundExceptionMS(String message) {
        super(message);
    }

    public ResourceNotFoundExceptionMS(Throwable cause) {
        super(cause);
    }
}
