package com.hcl.linklite.util.shortcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Component
public class Base62RandomGenerator implements ShortCodeGenerator {
    
    private static final String BASE62_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int DEFAULT_LENGTH = 6;
    private final Random random;
    
    public Base62RandomGenerator() {
        this.random = new SecureRandom();
    }
    
    @Override
    public String generate(String longUrl) {
        return generateRandomCode(DEFAULT_LENGTH);
    }
    
    public String generate(int length) {
        return generateRandomCode(length);
    }
    
    private String generateRandomCode(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be at least 1");
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(BASE62_CHARS.length());
            sb.append(BASE62_CHARS.charAt(randomIndex));
        }
        
        String code = sb.toString();
        log.debug("Generated short code: {}", code);
        return code;
    }
}
