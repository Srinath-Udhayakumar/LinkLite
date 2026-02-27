package com.hcl.linklite.service;

import com.hcl.linklite.dto.UrlShortenResponse;
import com.hcl.linklite.entity.Url;
import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.exception.ShortCodeGenerationException;
import com.hcl.linklite.exception.UrlNotFoundException;
import com.hcl.linklite.repository.UrlRepository;
import com.hcl.linklite.repository.UrlClickRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    
    private final UrlRepository urlRepository;
    private final UrlClickRepository urlClickRepository;
    private final ShortCodeService shortCodeService;
    
    @Value("${linklite.short-code.base-url:http://localhost:8081}")
    private String baseUrl;
    
    @Transactional
    public UrlShortenResponse createShortUrl(String longUrl) {
        log.info("Creating short URL for: {}", longUrl);
        
        String shortCode = shortCodeService.generateUniqueShortCode(longUrl);
        
        Url url = Url.builder()
                .longUrl(longUrl)
                .shortCode(shortCode)
                .totalClicks(0L)
                .createdAt(LocalDateTime.now())
                .build();
        
        url = urlRepository.save(url);
        
        String shortUrl = baseUrl + "/" + shortCode;
        
        log.info("Created short URL: {} -> {}", shortUrl, longUrl);
        
        return UrlShortenResponse.builder()
                .shortCode(shortCode)
                .shortUrl(shortUrl)
                .longUrl(longUrl)
                .createdAt(url.getCreatedAt())
                .build();
    }
    
    @Transactional(readOnly = true)
    public String getOriginalUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));
        
        log.debug("Retrieved original URL for short code {}: {}", shortCode, url.getLongUrl());
        return url.getLongUrl();
    }
    
    @Transactional
    public void incrementClicks(String shortCode, HttpServletRequest request) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));
        
        url.setTotalClicks(url.getTotalClicks() + 1);
        urlRepository.save(url);
        
        String ipAddress = getClientIpAddress(request);
        
        UrlClick click = UrlClick.builder()
                .url(url)
                .clickedAt(LocalDateTime.now())
                .ipAddress(ipAddress)
                .build();
        
        urlClickRepository.save(click);
        
        log.debug("Incremented clicks for short code {}. Total clicks: {}", 
                 shortCode, url.getTotalClicks());
    }
    
    @Transactional(readOnly = true)
    public Url getUrlByShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));
    }
    
    @Transactional(readOnly = true)
    public List<UrlClick> getClicksSince(Url url, LocalDateTime since) {
        return urlClickRepository.findByUrlAndClickedAtAfter(url, since);
    }
    
    @Transactional(readOnly = true)
    public List<UrlClick> getRecentClicks(Url url, int limit) {
        List<UrlClick> clicks = urlClickRepository.findRecentClicksByUrl(url);
        return clicks.size() > limit ? clicks.subList(0, limit) : clicks;
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
}
