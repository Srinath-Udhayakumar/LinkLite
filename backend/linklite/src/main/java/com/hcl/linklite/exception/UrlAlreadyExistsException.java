package com.hcl.linklite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UrlAlreadyExistsException extends RuntimeException {
    
    public UrlAlreadyExistsException(String shortCode) {
        super("URL with short code '" + shortCode + "' already exists");
    }
    
    public UrlAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
