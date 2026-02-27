package com.hcl.linklite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UrlNotFoundException extends RuntimeException {
    
    public UrlNotFoundException(String shortCode) {
        super("URL not found for short code: " + shortCode);
    }
    
    public UrlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
