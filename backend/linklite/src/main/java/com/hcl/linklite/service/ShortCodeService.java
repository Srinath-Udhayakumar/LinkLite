package com.hcl.linklite.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcl.linklite.exception.ShortCodeGenerationException;
import com.hcl.linklite.repository.UrlRepository;
import com.hcl.linklite.util.shortcode.ShortCodeGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShortCodeService {
    
    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    
    @Value("${linklite.short-code.max-retries:5}")
    private int maxRetries;
    
    @Value("${linklite.short-code.length:6}")
    private int shortCodeLength;
    
    public String generateUniqueShortCode(String longUrl) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            String shortCode = shortCodeGenerator.generate(shortCodeLength);
            
            if (!urlRepository.existsByShortCode(shortCode)) {
                log.debug("Generated unique short code '{}' on attempt {}", shortCode, attempt);
                return shortCode;
            }
            
            log.debug("Short code '{}' already exists, retrying (attempt {})", shortCode, attempt);
        }
        
        String errorMessage = String.format("Failed to generate unique short code after %d attempts", maxRetries);
        log.error(errorMessage);
        throw new ShortCodeGenerationException(errorMessage);
    }
}

