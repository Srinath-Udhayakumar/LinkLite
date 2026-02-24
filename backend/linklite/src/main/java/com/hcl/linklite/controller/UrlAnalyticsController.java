package com.hcl.linklite.controller;



import com.hcl.linklite.dto.*;
import com.hcl.linklite.service.UrlAnalyticsService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlAnalyticsController {

    private final UrlAnalyticsService analyticsService;

    public UrlAnalyticsController(UrlAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // TOTAL CLICKS
    @GetMapping("/{shortCode}/analytics")
    public Object getAnalytics(@PathVariable String shortCode,
                               @RequestParam(required = false) String range) {

        if ("24h".equalsIgnoreCase(range)) {
            return analyticsService.getLast24HoursClicks(shortCode);
        }

        return analyticsService.getTotalAnalytics(shortCode);
    }

    // CLICK HISTORY
    @GetMapping("/{shortCode}/analytics/history")
    public Page<ClickHistoryDto> getClickHistory(
            @PathVariable String shortCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return analyticsService.getClickHistory(shortCode, page, size);
    }
}