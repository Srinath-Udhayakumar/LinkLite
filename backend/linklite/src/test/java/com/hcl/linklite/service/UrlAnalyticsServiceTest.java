package com.hcl.linklite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.hcl.linklite.dto.AnalyticsResponse;
import com.hcl.linklite.dto.ClickCountResponseDto;
import com.hcl.linklite.dto.ClickHistoryDto;
import com.hcl.linklite.entity.Url;
import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.exception.ResourceNotFoundException;
import com.hcl.linklite.repository.UrlClickRepository;
import com.hcl.linklite.repository.UrlRepository;

@ExtendWith(MockitoExtension.class)
class UrlAnalyticsServiceTest {
    
    @Mock
    private UrlRepository urlRepository;
    
    @Mock
    private UrlClickRepository urlClickRepository;
    
    @InjectMocks
    private UrlAnalyticsService analyticsService;
    
    @Test
    @DisplayName("Should get total analytics for short code")
    void shouldGetTotalAnalyticsForShortCode() {
        String shortCode = "abc123";
        Url url = Url.builder()
                .id(1L)
                .shortCode(shortCode)
                .longUrl("https://example.com")
                .totalClicks(100L)
                .createdAt(LocalDateTime.now().minusDays(10))
                .build();
        
        List<UrlClick> recentClicks = Arrays.asList(
                UrlClick.builder()
                        .id(1L)
                        .url(url)
                        .ipAddress("192.168.1.0")
                        .clickedAt(LocalDateTime.now())
                        .build(),
                UrlClick.builder()
                        .id(2L)
                        .url(url)
                        .ipAddress("203.0.113.0")
                        .clickedAt(LocalDateTime.now().minusHours(1))
                        .build()
        );
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));
        when(urlClickRepository.countByUrl_ShortCode(shortCode)).thenReturn(100L);
        when(urlClickRepository.findRecentClicksByUrl(url)).thenReturn(recentClicks);
        
        AnalyticsResponse response = analyticsService.getTotalAnalytics(shortCode);
        
        assertThat(response.getShortCode()).isEqualTo(shortCode);
        assertThat(response.getLongUrl()).isEqualTo("https://example.com");
        assertThat(response.getTotalClicks()).isEqualTo(100L);
        assertThat(response.getRecentClicks()).hasSize(2);
        
        verify(urlRepository).findByShortCode(shortCode);
        verify(urlClickRepository).countByUrl_ShortCode(shortCode);
        verify(urlClickRepository).findRecentClicksByUrl(url);
    }
    
    @Test
    @DisplayName("Should throw exception when URL not found for analytics")
    void shouldThrowExceptionWhenUrlNotFoundForAnalytics() {
        String shortCode = "nonexistent";
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> analyticsService.getTotalAnalytics(shortCode))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("URL not found: nonexistent");
    }
    
    @Test
    @DisplayName("Should get last 24 hours clicks")
    void shouldGetLast24HoursClicks() {
        String shortCode = "abc123";
        
        when(urlClickRepository.countClicksInRange(eq(shortCode), any(LocalDateTime.class)))
                .thenReturn(42L);
        
        ClickCountResponseDto response = analyticsService.getLast24HoursClicks(shortCode);
        
        assertThat(response.getShortCode()).isEqualTo(shortCode);
        assertThat(response.getRange()).isEqualTo("24h");
        assertThat(response.getClickCount()).isEqualTo(42L);
        
        verify(urlClickRepository).countClicksInRange(eq(shortCode), any(LocalDateTime.class));
    }
    
    @Test
    @DisplayName("Should get paginated click history")
    void shouldGetPaginatedClickHistory() {
        String shortCode = "abc123";
        Url url = Url.builder().id(1L).shortCode(shortCode).build();
        
        List<UrlClick> clicks = Arrays.asList(
                UrlClick.builder()
                        .id(1L)
                        .url(url)
                        .clickedAt(LocalDateTime.now())
                        .build(),
                UrlClick.builder()
                        .id(2L)
                        .url(url)
                        .clickedAt(LocalDateTime.now().minusHours(1))
                        .build()
        );
        
        Page<UrlClick> clickPage = new PageImpl<>(clicks, PageRequest.of(0, 10), 2);
        
        when(urlClickRepository.findByUrl_ShortCodeOrderByClickedAtDesc(eq(shortCode), any(Pageable.class)))
                .thenReturn(clickPage);
        
        Page<ClickHistoryDto> result = analyticsService.getClickHistory(shortCode, 0, 10);
        
        assertThat(result).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        
        verify(urlClickRepository).findByUrl_ShortCodeOrderByClickedAtDesc(eq(shortCode), any(Pageable.class));
    }
    
    @Test
    @DisplayName("Should handle empty click history")
    void shouldHandleEmptyClickHistory() {
        String shortCode = "abc123";
        Page<UrlClick> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        
        when(urlClickRepository.findByUrl_ShortCodeOrderByClickedAtDesc(eq(shortCode), any(Pageable.class)))
                .thenReturn(emptyPage);
        
        Page<ClickHistoryDto> result = analyticsService.getClickHistory(shortCode, 0, 10);
        
        assertThat(result).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Should get click history with pagination")
    void shouldGetClickHistoryWithPagination() {
        String shortCode = "abc123";
        Url url = Url.builder().id(1L).shortCode(shortCode).build();
        
        List<UrlClick> pageOneClicks = Arrays.asList(
                UrlClick.builder().id(1L).url(url).clickedAt(LocalDateTime.now()).build(),
                UrlClick.builder().id(2L).url(url).clickedAt(LocalDateTime.now()).build()
        );
        
        Page<UrlClick> pageOne = new PageImpl<>(pageOneClicks, PageRequest.of(0, 2), 5);
        
        when(urlClickRepository.findByUrl_ShortCodeOrderByClickedAtDesc(eq(shortCode), any(Pageable.class)))
                .thenReturn(pageOne);
        
        Page<ClickHistoryDto> result = analyticsService.getClickHistory(shortCode, 0, 2);
        
        assertThat(result).hasSize(2);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.isFirst()).isTrue();
    }
}
