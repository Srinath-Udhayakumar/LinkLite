package com.hcl.linklite.util.shortcode;

/**
 * Strategy interface for generating short codes from URLs.
 * Implements Strategy Pattern to allow different short code generation algorithms.
 */
public interface ShortCodeGenerator {
    
    /**
     * Generates a short code for the given long URL.
     * 
     * @param longUrl the original long URL to generate short code for
     * @return generated short code
     * @throws ShortCodeGenerationException if generation fails
     */
    String generate(String longUrl) throws ShortCodeGenerationException;
}
