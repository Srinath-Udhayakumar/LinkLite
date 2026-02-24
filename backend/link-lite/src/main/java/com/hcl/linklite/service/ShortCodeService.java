package com.hcl.linklite.service;

import com.hcl.linklite.repository.UrlRepository;
import com.hcl.linklite.util.shortcode.ShortCodeGenerator;
import com.hcl.linklite.util.shortcode.ShortCodeGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service class for managing short code generation with collision awareness.
 * Handles uniqueness checking and retry mechanisms for collision resolution.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShortCodeService {
    
    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    
    private static final int MAX_RETRY_ATTEMPTS = 5;
    
    /**
     * Generates a unique short code for the given long URL.
     * Implements collision detection and retry mechanism.
     * 
     * @param longUrl the original long URL
     * @return unique short code
     * @throws ShortCodeGenerationException if unable to generate unique code after max retries
     */
    public String generateUniqueShortCode(String longUrl) throws ShortCodeGenerationException {
        if (!StringUtils.hasText(longUrl)) {
            throw new ShortCodeGenerationException("Long URL cannot be null or empty");
        }
        
        int attempts = 0;
        String shortCode;
        
        do {
            attempts++;
            
            try {
                shortCode = shortCodeGenerator.generate(longUrl);
                log.debug("Generated short code attempt {}: {}", attempts, shortCode);
                
                // Check for collision
                if (!urlRepository.existsByShortCode(shortCode)) {
                    log.info("Successfully generated unique short code: {} after {} attempts", shortCode, attempts);
                    return shortCode;
                }
                
                log.warn("Collision detected for short code: {}, attempt {}/{}", shortCode, attempts, MAX_RETRY_ATTEMPTS);
                
            } catch (Exception e) {
                log.error("Error generating short code on attempt {}: {}", attempts, e.getMessage());
                if (attempts == MAX_RETRY_ATTEMPTS) {
                    throw new ShortCodeGenerationException("Failed to generate short code after " + MAX_RETRY_ATTEMPTS + " attempts", e);
                }
            }
            
        } while (attempts < MAX_RETRY_ATTEMPTS);
        
        throw new ShortCodeGenerationException("Unable to generate unique short code after " + MAX_RETRY_ATTEMPTS + " attempts");
    }
    
    /**
     * Validates if a short code has the correct format.
     * 
     * @param shortCode the short code to validate
     * @return true if valid format, false otherwise
     */
    public boolean isValidShortCodeFormat(String shortCode) {
        if (!StringUtils.hasText(shortCode)) {
            return false;
        }
        
        // Base62 validation: alphanumeric only, 6-8 characters
        return shortCode.matches("^[a-zA-Z0-9]{6,8}$");
    }
}
