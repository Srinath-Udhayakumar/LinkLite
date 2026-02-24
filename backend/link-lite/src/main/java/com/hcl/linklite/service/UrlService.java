package com.hcl.linklite.service;

import com.hcl.linklite.entity.Url;
import com.hcl.linklite.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service class for URL management operations.
 * Demonstrates the integration of short code generation with URL persistence.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UrlService {
    
    private final UrlRepository urlRepository;
    private final ShortCodeService shortCodeService;
    
    /**
     * Creates a shortened URL from the original URL.
     * This method demonstrates the complete workflow:
     * 1. Save URL to get database ID
     * 2. Generate short code from ID
     * 3. Update URL with short code
     * 
     * @param originalUrl the original URL to shorten
     * @return the created URL entity with short code
     * @throws IllegalArgumentException if originalUrl is invalid
     */
    @Transactional
    public Url createShortUrl(String originalUrl) {
        // Validate input
        if (!StringUtils.hasText(originalUrl)) {
            throw new IllegalArgumentException("Original URL cannot be null or empty");
        }
        
        // Check if URL already exists
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent() && existingUrl.get().isValid()) {
            log.info("Returning existing short URL for: {}", originalUrl);
            return existingUrl.get();
        }
        
        // Step 1: Save URL first to get database-generated ID
        Url newUrl = Url.builder()
                .originalUrl(originalUrl)
                .build();
        
        Url savedUrl = urlRepository.save(newUrl);
        log.debug("URL saved with ID: {}", savedUrl.getId());
        
        // Step 2: Generate short code from the database ID
        String shortCode = shortCodeService.generateShortCode(savedUrl.getId());
        log.debug("Generated short code: {} for ID: {}", shortCode, savedUrl.getId());
        
        // Step 3: Update the URL entity with the generated short code
        savedUrl.setShortCode(shortCode);
        Url finalUrl = urlRepository.save(savedUrl);
        
        log.info("Created short URL: {} -> {}", finalUrl.getShortCode(), finalUrl.getOriginalUrl());
        return finalUrl;
    }
    
    /**
     * Creates a shortened URL with custom expiration date.
     * 
     * @param originalUrl the original URL to shorten
     * @param expiresAt the expiration date
     * @return the created URL entity with short code
     */
    @Transactional
    public Url createShortUrlWithExpiration(String originalUrl, LocalDateTime expiresAt) {
        if (!StringUtils.hasText(originalUrl)) {
            throw new IllegalArgumentException("Original URL cannot be null or empty");
        }
        
        if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expiration date cannot be in the past");
        }
        
        // Save URL first to get ID
        Url newUrl = Url.builder()
                .originalUrl(originalUrl)
                .expiresAt(expiresAt)
                .build();
        
        Url savedUrl = urlRepository.save(newUrl);
        
        // Generate and set short code
        String shortCode = shortCodeService.generateShortCode(savedUrl.getId());
        savedUrl.setShortCode(shortCode);
        
        return urlRepository.save(savedUrl);
    }
    
    /**
     * Retrieves a URL by its short code, only if it's valid (active and not expired).
     * 
     * @param shortCode the short code to look up
     * @return Optional containing the URL if found and valid, empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<Url> getUrlByShortCode(String shortCode) {
        if (!StringUtils.hasText(shortCode)) {
            return Optional.empty();
        }
        
        return urlRepository.findValidUrlByShortCode(shortCode);
    }
    
    /**
     * Retrieves the original URL by short code and increments click count.
     * 
     * @param shortCode the short code to look up
     * @return Optional containing the original URL if found and valid, empty otherwise
     */
    @Transactional
    public Optional<String> getOriginalUrlAndIncrementClicks(String shortCode) {
        Optional<Url> urlOpt = getUrlByShortCode(shortCode);
        
        if (urlOpt.isPresent()) {
            Url url = urlOpt.get();
            urlRepository.incrementClickCount(url.getId());
            log.debug("Incremented click count for short code: {}", shortCode);
            return Optional.of(url.getOriginalUrl());
        }
        
        return Optional.empty();
    }
    
    /**
     * Deactivates a URL by its short code.
     * 
     * @param shortCode the short code to deactivate
     * @return true if deactivated successfully, false if not found
     */
    @Transactional
    public boolean deactivateUrl(String shortCode) {
        Optional<Url> urlOpt = urlRepository.findByShortCode(shortCode);
        
        if (urlOpt.isPresent()) {
            Url url = urlOpt.get();
            url.setIsActive(false);
            urlRepository.save(url);
            log.info("Deactivated URL with short code: {}", shortCode);
            return true;
        }
        
        return false;
    }
    
    /**
     * Validates if a short code has the correct format.
     * 
     * @param shortCode the short code to validate
     * @return true if valid format, false otherwise
     */
    public boolean isValidShortCodeFormat(String shortCode) {
        return shortCodeService.isValidShortCode(shortCode);
    }
}
