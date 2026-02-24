package com.hcl.linklite.util.shortcode;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Base62 random short code generator implementation.
 * Generates 6-8 character alphanumeric short codes using Base62 encoding.
 * Thread-safe implementation with high randomness.
 */
@Component
public class Base62RandomGenerator implements ShortCodeGenerator {
    
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 8;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    /**
     * Generates a random short code of 6-8 characters using Base62 encoding.
     * Thread-safe implementation using SecureRandom for high randomness.
     * 
     * @param longUrl the original long URL (not used in random generation but required by interface)
     * @return generated short code
     * @throws ShortCodeGenerationException if generation fails
     */
    @Override
    public String generate(String longUrl) throws ShortCodeGenerationException {
        if (longUrl == null) {
            throw new ShortCodeGenerationException("Long URL cannot be null");
        }
        
        try {
            // Random length between 6-8 characters
            int length = ThreadLocalRandom.current().nextInt(MIN_LENGTH, MAX_LENGTH + 1);
            
            StringBuilder shortCode = new StringBuilder(length);
            
            for (int i = 0; i < length; i++) {
                int randomIndex = SECURE_RANDOM.nextInt(BASE62_CHARS.length());
                shortCode.append(BASE62_CHARS.charAt(randomIndex));
            }
            
            return shortCode.toString();
            
        } catch (Exception e) {
            throw new ShortCodeGenerationException("Failed to generate short code", e);
        }
    }
}
