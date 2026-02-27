package com.hcl.linklite.controller;

import com.hcl.linklite.dto.*;
import com.hcl.linklite.service.UrlAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/urls")
@RequiredArgsConstructor
public class UrlAnalyticsController {

    private final UrlAnalyticsService analyticsService;

    @GetMapping("/{shortCode}/analytics")
    public ResponseEntity<?> getAnalytics(@PathVariable String shortCode,
                                          @RequestParam(required = false) String range) {
        
        log.info("Analytics request for short code: {}, range: {}", shortCode, range);
        
        if ("24h".equalsIgnoreCase(range)) {
            return ResponseEntity.ok(analyticsService.getLast24HoursClicks(shortCode));
        }

        return ResponseEntity.ok(analyticsService.getTotalAnalytics(shortCode));
    }

    @GetMapping("/{shortCode}/analytics/history")
    public ResponseEntity<Page<ClickHistoryDto>> getClickHistory(
            @PathVariable String shortCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Click history request for short code: {}, page: {}, size: {}", shortCode, page, size);
        
        return ResponseEntity.ok(analyticsService.getClickHistory(shortCode, page, size));
    }
}
