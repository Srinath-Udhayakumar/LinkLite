package com.hcl.linklite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hcl.linklite.entity.Url;
import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.exception.UrlNotFoundException;
import com.hcl.linklite.repository.UrlClickRepository;
import com.hcl.linklite.repository.UrlRepository;

@ExtendWith(MockitoExtension.class)
class ClickLoggingServiceTest {
    
    @Mock
    private UrlClickRepository urlClickRepository;
    
    @Mock
    private UrlRepository urlRepository;
    
    @InjectMocks
    private ClickLoggingService clickLoggingService;
    
    @Test
    @DisplayName("Should log click with anonymized IP address")
    void shouldLogClickWithAnonymizedIp() {
        String shortCode = "abc123";
        String ipAddress = "192.168.1.100";
        Url url = Url.builder()
                .id(1L)
                .shortCode(shortCode)
                .longUrl("https://example.com")
                .totalClicks(5L)
                .build();
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));
        when(urlClickRepository.save(any(UrlClick.class))).thenReturn(new UrlClick());
        
        clickLoggingService.logClick(shortCode, ipAddress);
        
        // Verify that click was saved with anonymized IP
        verify(urlClickRepository).save(argThat(click -> 
                click.getUrl() == url &&
                click.getIpAddress().equals("192.168.1.0") // Last octet should be .0
        ));
        
        // Verify atomic increment was called
        verify(urlRepository).incrementTotalClicks(shortCode);
    }
    
    @Test
    @DisplayName("Should throw exception when URL not found")
    void shouldThrowExceptionWhenUrlNotFound() {
        String shortCode = "nonexistent";
        String ipAddress = "192.168.1.1";
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> clickLoggingService.logClick(shortCode, ipAddress))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessage("URL not found for short code: nonexistent");
    }
    
    @Test
    @DisplayName("Should get long URL by short code")
    void shouldGetLongUrlByShortCode() {
        String shortCode = "abc123";
        String longUrl = "https://example.com/very/long/path";
        Url url = Url.builder()
                .id(1L)
                .shortCode(shortCode)
                .longUrl(longUrl)
                .build();
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));
        
        String result = clickLoggingService.getLongUrl(shortCode);
        
        assertThat(result).isEqualTo(longUrl);
        verify(urlRepository).findByShortCode(shortCode);
    }
    
    @Test
    @DisplayName("Should throw exception when getting long URL for non-existent short code")
    void shouldThrowExceptionWhenGettingLongUrlForNonExistentShortCode() {
        String shortCode = "nonexistent";
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> clickLoggingService.getLongUrl(shortCode))
                .isInstanceOf(UrlNotFoundException.class);
    }
    
    @Test
    @DisplayName("Should increment total clicks atomically")
    void shouldIncrementTotalClicksAtomically() {
        String shortCode = "abc123";
        
        clickLoggingService.incrementTotalClicks(shortCode);
        
        verify(urlRepository).incrementTotalClicks(shortCode);
    }
    
    @Test
    @DisplayName("Should anonymize IPv6 addresses correctly")
    void shouldAnonymizeIpv6Addresses() {
        String shortCode = "abc123";
        String ipv6Address = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        
        Url url = Url.builder()
                .id(1L)
                .shortCode(shortCode)
                .longUrl("https://example.com")
                .build();
        
        when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));
        when(urlClickRepository.save(any(UrlClick.class))).thenReturn(new UrlClick());
        
        clickLoggingService.logClick(shortCode, ipv6Address);
        
        verify(urlClickRepository).save(argThat(click -> 
                click.getIpAddress().endsWith(":0")
        ));
    }
}
