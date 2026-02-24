package com.hcl.linklite.service;

import com.hcl.linklite.repository.UrlRepository;
import com.hcl.linklite.util.shortcode.ShortCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortCodeServiceTest {
    
    @Mock
    private UrlRepository urlRepository;
    
    @Mock
    private ShortCodeGenerator shortCodeGenerator;
    
    @InjectMocks
    private ShortCodeService shortCodeService;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(shortCodeService, "maxRetries", 5);
        ReflectionTestUtils.setField(shortCodeService, "shortCodeLength", 6);
    }
    
    @Test
    @DisplayName("Should generate unique short code on first attempt")
    void shouldGenerateUniqueShortCodeOnFirstAttempt() {
        String longUrl = "https://example.com";
        String expectedCode = "abc123";
        
        when(shortCodeGenerator.generate(6)).thenReturn(expectedCode);
        when(urlRepository.existsByShortCode(expectedCode)).thenReturn(false);
        
        String result = shortCodeService.generateUniqueShortCode(longUrl);
        
        assertThat(result).isEqualTo(expectedCode);
        verify(urlRepository).existsByShortCode(expectedCode);
        verify(shortCodeGenerator).generate(6);
    }
    
    @Test
    @DisplayName("Should retry on collision and return unique code")
    void shouldRetryOnCollisionAndReturnUniqueCode() {
        String longUrl = "https://example.com";
        String collisionCode = "abc123";
        String uniqueCode = "def456";
        
        when(shortCodeGenerator.generate(6))
                .thenReturn(collisionCode)
                .thenReturn(uniqueCode);
        when(urlRepository.existsByShortCode(collisionCode)).thenReturn(true);
        when(urlRepository.existsByShortCode(uniqueCode)).thenReturn(false);
        
        String result = shortCodeService.generateUniqueShortCode(longUrl);
        
        assertThat(result).isEqualTo(uniqueCode);
        verify(urlRepository, times(2)).existsByShortCode(anyString());
        verify(shortCodeGenerator, times(2)).generate(6);
    }
    
    @Test
    @DisplayName("Should throw exception after max retries")
    void shouldThrowExceptionAfterMaxRetries() {
        String longUrl = "https://example.com";
        
        when(shortCodeGenerator.generate(6)).thenReturn("collision");
        when(urlRepository.existsByShortCode("collision")).thenReturn(true);
        
        assertThatThrownBy(() -> shortCodeService.generateUniqueShortCode(longUrl))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to generate unique short code after 5 attempts");
        
        verify(urlRepository, times(5)).existsByShortCode("collision");
        verify(shortCodeGenerator, times(5)).generate(6);
    }
    
    @Test
    @DisplayName("Should work with different long URLs")
    void shouldWorkWithDifferentLongUrls() {
        String url1 = "https://example1.com";
        String url2 = "https://example2.com";
        String code1 = "abc123";
        String code2 = "def456";
        
        when(shortCodeGenerator.generate(6)).thenReturn(code1).thenReturn(code2);
        when(urlRepository.existsByShortCode(code1)).thenReturn(false);
        when(urlRepository.existsByShortCode(code2)).thenReturn(false);
        
        String result1 = shortCodeService.generateUniqueShortCode(url1);
        String result2 = shortCodeService.generateUniqueShortCode(url2);
        
        assertThat(result1).isEqualTo(code1);
        assertThat(result2).isEqualTo(code2);
    }
}
