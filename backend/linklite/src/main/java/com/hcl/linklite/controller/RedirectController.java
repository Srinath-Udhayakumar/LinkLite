package com.hcl.linklite.controller;

import com.hcl.linklite.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class RedirectController {
    
    private final UrlService urlService;
    
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @PathVariable String shortCode,
            HttpServletRequest request) {
        
        log.info("Redirect request for short code: {}", shortCode);
        
        String originalUrl = urlService.getOriginalUrl(shortCode);
        urlService.incrementClicks(shortCode, request);
        
        log.info("Redirecting to: {}", originalUrl);
        
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(java.net.URI.create(originalUrl))
                .build();
    }
}
