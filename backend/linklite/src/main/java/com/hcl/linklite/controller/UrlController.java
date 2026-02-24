package com.hcl.linklite.controller;

import com.hcl.linklite.dto.UrlShortenRequest;
import com.hcl.linklite.dto.UrlShortenResponse;
import com.hcl.linklite.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {
    
    private final UrlService urlService;
    
    @PostMapping("/shorten")
    public ResponseEntity<UrlShortenResponse> shortenUrl(@Valid @RequestBody UrlShortenRequest request) {
        log.info("Received request to shorten URL: {}", request.getLongUrl());
        
        UrlShortenResponse response = urlService.createShortUrl(request.getLongUrl());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
