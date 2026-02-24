package com.hcl.linklite.controller;

import com.hcl.linklite.service.ClickLoggingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class RedirectController {

    private final ClickLoggingService clickLoggingService;

    @GetMapping("/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode, HttpServletRequest request) {
        log.debug("Received redirect request for shortCode: {}", shortCode);
        
        try {
            String clientIp = getClientIpAddress(request);
            clickLoggingService.logClick(shortCode, clientIp);
            
            String longUrl = clickLoggingService.getLongUrl(shortCode);
            log.debug("Redirecting shortCode: {} to {}", shortCode, longUrl);
            
            return new RedirectView(longUrl);
            
        } catch (RuntimeException e) {
            log.warn("URL not found for shortCode: {}", shortCode);
            throw new UrlNotFoundException("Short URL not found: " + shortCode);
        }
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<String> handleUrlNotFound(UrlNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    public static class UrlNotFoundException extends RuntimeException {
        public UrlNotFoundException(String message) {
            super(message);
        }
    }
}
