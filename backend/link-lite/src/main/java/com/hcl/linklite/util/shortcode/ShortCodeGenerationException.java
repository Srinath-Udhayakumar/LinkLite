package com.hcl.linklite.util.shortcode;

/**
 * Exception thrown when short code generation fails.
 */
public class ShortCodeGenerationException extends RuntimeException {
    
    public ShortCodeGenerationException(String message) {
        super(message);
    }
    
    public ShortCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
