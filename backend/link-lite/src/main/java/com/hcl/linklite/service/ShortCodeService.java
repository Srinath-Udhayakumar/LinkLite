package com.hcl.linklite.service;

import com.hcl.linklite.util.Base62Encoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for generating short codes from database IDs.
 * Uses Base62 encoding to convert numeric IDs to compact, URL-safe strings.
 * This service ensures collision-free short codes since they're derived from unique database IDs.
 */
@Service
public class ShortCodeService {
    
    /**
     * Generates a short code from the given database ID using Base62 encoding.
     * 
     * @param databaseId the auto-generated database ID (must be positive)
     * @return Base62 encoded short code
     * @throws IllegalArgumentException if databaseId is null or not positive
     */
    public String generateShortCode(Long databaseId) {
        if (databaseId == null) {
            throw new IllegalArgumentException("Database ID cannot be null");
        }
        
        if (databaseId <= 0) {
            throw new IllegalArgumentException("Database ID must be positive");
        }
        
        return Base62Encoder.encode(databaseId);
    }
    
    /**
     * Validates if the given short code is properly formatted.
     * 
     * @param shortCode the short code to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidShortCode(String shortCode) {
        return Base62Encoder.isValidBase62(shortCode);
    }
    
    /**
     * Decodes a short code back to its original database ID.
     * This method is useful for lookups and debugging.
     * 
     * @param shortCode the Base62 encoded short code
     * @return the original database ID
     * @throws IllegalArgumentException if shortCode is invalid
     */
    public Long decodeShortCode(String shortCode) {
        if (shortCode == null || shortCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Short code cannot be null or empty");
        }
        
        if (!isValidShortCode(shortCode)) {
            throw new IllegalArgumentException("Invalid short code format");
        }
        
        return Base62Encoder.decode(shortCode);
    }
}
