package com.hcl.linklite.service;

import com.hcl.linklite.dto.*;
import com.hcl.linklite.entity.Url;
import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.exception.ResourceNotFoundException;
import com.hcl.linklite.repository.UrlClickRepository;
import com.hcl.linklite.repository.UrlRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlAnalyticsService {

    private final UrlRepository urlRepository;
    private final UrlClickRepository urlClickRepository;

    public UrlAnalyticsService(UrlRepository urlRepository,
                               UrlClickRepository urlClickRepository) {
        this.urlRepository = urlRepository;
        this.urlClickRepository = urlClickRepository;
    }

    // TOTAL ANALYTICS
    public UrlAnalyticsResponseDto getTotalAnalytics(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("URL not found: " + shortCode));

        long totalClicks =
                urlClickRepository.countByUrl_ShortCode(shortCode);

        return new UrlAnalyticsResponseDto(
                shortCode,
                totalClicks,
                url.getCreatedAt()
        );
    }

    // LAST 24 HOURS ANALYTICS
    public ClickCountResponseDto getLast24HoursClicks(String shortCode) {

        LocalDateTime fromTime = LocalDateTime.now().minusHours(24);

        long clickCount =
                urlClickRepository.countClicksInRange(shortCode, fromTime);

        return new ClickCountResponseDto(
                shortCode,
                "24h",
                clickCount
        );
    }

    // PAGINATED CLICK HISTORY
    public Page<ClickHistoryDto> getClickHistory(String shortCode,
                                                 int page,
                                                 int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<UrlClick> clickPage =
                urlClickRepository
                        .findByUrl_ShortCodeOrderByClickedAtDesc(shortCode, pageable);

        return clickPage.map(click ->
                new ClickHistoryDto(click.getClickedAt())
        );
    }
}