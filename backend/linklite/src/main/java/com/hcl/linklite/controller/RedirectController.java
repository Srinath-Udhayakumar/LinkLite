package com.hcl.linklite.controller;

import com.hcl.linklite.exception.UrlNotFoundException;
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

    @GetMapping("/{shortCode:^(?!api$|health$|actuator$)[a-zA-Z0-9]+$}")
    public RedirectView redirect(@PathVariable String shortCode, HttpServletRequest request) {
        log.debug("Received redirect request for shortCode: {}", shortCode);
        
        String clientIp = getClientIpAddress(request);
        clickLoggingService.logClick(shortCode, clientIp);

        String longUrl = clickLoggingService.getLongUrl(shortCode);
        log.debug("Redirecting shortCode: {} to {}", shortCode, longUrl);
        return new RedirectView(longUrl);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<String> handleUrlNotFound(UrlNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return anonymizeIp(xForwardedFor.split(",")[0].trim());
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return anonymizeIp(xRealIp);
        }

        return anonymizeIp(request.getRemoteAddr());
    }

    private String anonymizeIp(String ipAddress) {
        if (ipAddress == null) {
            return null;
        }
        // Mask last octet for IPv4 (e.g., 192.168.1.100 -> 192.168.1.0)
        return ipAddress.replaceAll("(\\d+\\.\\d+\\.\\d+\\.)(\\d+)", "${1}0");
    }
}
