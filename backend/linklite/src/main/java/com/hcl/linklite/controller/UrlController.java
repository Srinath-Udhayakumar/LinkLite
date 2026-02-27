package com.hcl.linklite.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.linklite.dto.UrlShortenRequest;
import com.hcl.linklite.dto.UrlShortenResponse;
import com.hcl.linklite.exception.InvalidUrlException;
import com.hcl.linklite.service.UrlService;
import com.hcl.linklite.util.UrlValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/urls")
@RequiredArgsConstructor
public class UrlController {
    
    private final UrlService urlService;
    
    @PostMapping("/shorten")
    public ResponseEntity<UrlShortenResponse> shortenUrl(@Valid @RequestBody UrlShortenRequest request) {
        log.info("Received request to shorten URL: {}", request.getLongUrl());
        
        // Validate URL format
        String longUrl = request.getLongUrl().trim();
        if (!UrlValidator.isValid(longUrl)) {
            log.warn("Invalid URL format provided: {}", longUrl);
            throw new InvalidUrlException("Invalid URL format. Only http:// and https:// URLs are allowed.");
        }
        
        UrlShortenResponse response = urlService.createShortUrl(longUrl);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

