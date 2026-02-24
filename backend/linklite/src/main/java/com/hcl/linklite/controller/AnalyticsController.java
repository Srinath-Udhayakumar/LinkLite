package com.hcl.linklite.controller;

import com.hcl.linklite.dto.AnalyticsResponse;
import com.hcl.linklite.entity.Url;
import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class AnalyticsController {
    
    private final UrlService urlService;
    
    @GetMapping("/{shortCode}/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @PathVariable String shortCode,
            @RequestParam(required = false) String range) {
        
        log.info("Analytics request for short code: {}, range: {}", shortCode, range);
        
        Url url = urlService.getUrlByShortCode(shortCode);
        
        List<UrlClick> recentClicks;
        if (range != null && range.equals("24h")) {
            LocalDateTime since = LocalDateTime.now().minusHours(24);
            recentClicks = urlService.getClicksSince(url, since);
        } else {
            recentClicks = urlService.getRecentClicks(url, 10);
        }
        
        List<AnalyticsResponse.ClickData> clickDataList = recentClicks.stream()
                .map(click -> AnalyticsResponse.ClickData.builder()
                        .clickedAt(click.getClickedAt())
                        .ipAddress(click.getIpAddress())
                        .build())
                .collect(Collectors.toList());
        
        AnalyticsResponse response = AnalyticsResponse.builder()
                .shortCode(shortCode)
                .longUrl(url.getLongUrl())
                .totalClicks(url.getTotalClicks())
                .createdAt(url.getCreatedAt())
                .recentClicks(clickDataList)
                .build();
        
        return ResponseEntity.ok(response);
    }
}
