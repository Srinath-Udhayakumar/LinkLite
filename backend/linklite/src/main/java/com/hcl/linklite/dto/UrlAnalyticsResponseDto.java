package com.hcl.linklite.dto;



import java.time.LocalDateTime;

public class UrlAnalyticsResponseDto{

    private final String shortCode;
    private final long totalClicks;
    private final LocalDateTime createdAt;

    public UrlAnalyticsResponseDto(String shortCode,
                                   long totalClicks,
                                   LocalDateTime createdAt) {
        this.shortCode = shortCode;
        this.totalClicks = totalClicks;
        this.createdAt = createdAt;
    }

    public String getShortCode() {
        return shortCode;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}