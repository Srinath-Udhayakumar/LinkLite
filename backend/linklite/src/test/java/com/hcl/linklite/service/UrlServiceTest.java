package com.hcl.linklite.service;

import com.hcl.linklite.dto.UrlShortenResponse;
import com.hcl.linklite.entity.Url;
import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.exception.UrlNotFoundException;
import com.hcl.linklite.repository.UrlRepository;
import com.hcl.linklite.repository.UrlClickRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {
    
    @Mock
    private UrlRepository urlRepository;
    
    @Mock
    private UrlClickRepository urlClickRepository;
    
    @Mock
    private ShortCodeService shortCodeService;
    
    @Mock
    private HttpServletRequest request;
    
    @InjectMocks
    private UrlService urlService;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(urlService, "baseUrl", "http://localhost:8080");
    }
    
    @Test
    @DisplayName("Should create short URL successfully")
    void shouldCreateShortUrlSuccessfully() {
        String longUrl = "https://example.com";
        String shortCode = "abc123";
        
        when(shortCodeService.generateUniqueShortCode(longUrl)).thenReturn(shortCode);
        when(urlRepository.save(any(Url.class))).thenAnswer(invocation -> {
            Url url = invocation.getArgument(0);
            url.setId(1L);
            return url;
        });
        
        UrlShortenResponse response = urlService.createShortUrl(longUrl);
        
        assertThat(response.getShortCode()).isEqualTo(shortCode);
        assertThat(response.getShortUrl()).isEqualTo("http://localhost:8080/" + shortCode);
        assertThat(response.getLongUrl()).isEqualTo(longUrl);
        assertThat(response.getCreatedAt()).isNotNull();
        
        verify(shortCodeService).generateUniqueShortCode(longUrl);
        verify(urlRepository).save(any(Url.class));
    }
    
    @Test
    @DisplayName("Should get original URL by short code")
    void shouldGetOriginalUrlByShortCode() {
        String shortCode = "abc123";
        String longUrl = "https://example.com";
        Url url = Url.builder()
                .id(1L)
                .longUrl(longUrl)
                .shortCode(shortCode)
                .totalClicks(0L)
                .createdAt(LocalDateTime.now())
                .build();
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));
        
        String result = urlService.getOriginalUrl(shortCode);
        
        assertThat(result).isEqualTo(longUrl);
        verify(urlRepository).findByShortCode(shortCode);
    }
    
    @Test
    @DisplayName("Should throw exception when URL not found")
    void shouldThrowExceptionWhenUrlNotFound() {
        String shortCode = "nonexistent";
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> urlService.getOriginalUrl(shortCode))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessage("URL not found for short code: nonexistent");
    }
    
    @Test
    @DisplayName("Should increment clicks and log click data")
    void shouldIncrementClicksAndLogClickData() {
        String shortCode = "abc123";
        String longUrl = "https://example.com";
        String ipAddress = "192.168.1.1";
        
        Url url = Url.builder()
                .id(1L)
                .longUrl(longUrl)
                .shortCode(shortCode)
                .totalClicks(5L)
                .createdAt(LocalDateTime.now())
                .build();
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));
        when(urlRepository.save(any(Url.class))).thenReturn(url);
        when(urlClickRepository.save(any(UrlClick.class))).thenReturn(new UrlClick());
        when(request.getRemoteAddr()).thenReturn(ipAddress);
        
        urlService.incrementClicks(shortCode, request);
        
        assertThat(url.getTotalClicks()).isEqualTo(6L);
        verify(urlRepository).save(url);
        verify(urlClickRepository).save(argThat(click -> 
                click.getUrl() == url && 
                click.getIpAddress().equals(ipAddress) &&
                click.getClickedAt() != null
        ));
    }
    
    @Test
    @DisplayName("Should get URL by short code")
    void shouldGetUrlByShortCode() {
        String shortCode = "abc123";
        Url url = Url.builder()
                .id(1L)
                .longUrl("https://example.com")
                .shortCode(shortCode)
                .build();
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));
        
        Url result = urlService.getUrlByShortCode(shortCode);
        
        assertThat(result).isEqualTo(url);
        verify(urlRepository).findByShortCode(shortCode);
    }
    
    @Test
    @DisplayName("Should get clicks since specific time")
    void shouldGetClicksSinceSpecificTime() {
        Url url = Url.builder().id(1L).build();
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        List<UrlClick> clicks = Arrays.asList(
                UrlClick.builder().id(1L).url(url).clickedAt(LocalDateTime.now()).build(),
                UrlClick.builder().id(2L).url(url).clickedAt(LocalDateTime.now()).build()
        );
        
        when(urlClickRepository.findByUrlAndClickedAtAfter(url, since)).thenReturn(clicks);
        
        List<UrlClick> result = urlService.getClicksSince(url, since);
        
        assertThat(result).hasSize(2);
        verify(urlClickRepository).findByUrlAndClickedAtAfter(url, since);
    }
    
    @Test
    @DisplayName("Should get recent clicks with limit")
    void shouldGetRecentClicksWithLimit() {
        Url url = Url.builder().id(1L).build();
        List<UrlClick> clicks = Arrays.asList(
                UrlClick.builder().id(1L).url(url).clickedAt(LocalDateTime.now()).build(),
                UrlClick.builder().id(2L).url(url).clickedAt(LocalDateTime.now()).build(),
                UrlClick.builder().id(3L).url(url).clickedAt(LocalDateTime.now()).build()
        );
        
        when(urlClickRepository.findRecentClicksByUrl(url)).thenReturn(clicks);
        
        List<UrlClick> result = urlService.getRecentClicks(url, 2);
        
        assertThat(result).hasSize(2);
        verify(urlClickRepository).findRecentClicksByUrl(url);
    }
    
    @Test
    @DisplayName("Should extract X-Forwarded-For IP address")
    void shouldExtractXForwardedForIpAddress() {
        String shortCode = "abc123";
        Url url = Url.builder().id(1L).build();
        String forwardedIp = "203.0.113.195";
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));
        when(urlRepository.save(any(Url.class))).thenReturn(url);
        when(urlClickRepository.save(any(UrlClick.class))).thenReturn(new UrlClick());
        when(request.getHeader("X-Forwarded-For")).thenReturn(forwardedIp);
        
        urlService.incrementClicks(shortCode, request);
        
        verify(urlClickRepository).save(argThat(click -> 
                click.getIpAddress().equals(forwardedIp)
        ));
    }
}
