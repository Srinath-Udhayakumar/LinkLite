package com.hcl.linklite.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AnalyticsResponse {
    
    private String shortCode;
    private String longUrl;
    private Long totalClicks;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    private List<ClickData> recentClicks;
    
    @Data
    @Builder
    public static class ClickData {
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime clickedAt;
        
        private String ipAddress;
    }
}
