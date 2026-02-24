package com.hcl.linklite.service;

import com.hcl.linklite.repository.UrlRepository;
import com.hcl.linklite.util.shortcode.ShortCodeGenerator;
import com.hcl.linklite.util.shortcode.ShortCodeGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortCodeServiceTest {
    
    @Mock
    private UrlRepository urlRepository;
    
    @Mock
    private ShortCodeGenerator shortCodeGenerator;
    
    private ShortCodeService shortCodeService;
    
    @BeforeEach
    void setUp() {
        shortCodeService = new ShortCodeService(urlRepository, shortCodeGenerator);
    }
    
    @Test
    void testGenerateUniqueShortCodeSuccess() {
        String longUrl = "https://example.com";
        String expectedShortCode = "abc123";
        
        when(shortCodeGenerator.generate(longUrl)).thenReturn(expectedShortCode);
        when(urlRepository.existsByShortCode(expectedShortCode)).thenReturn(false);
        
        String result = shortCodeService.generateUniqueShortCode(longUrl);
        
        assertEquals(expectedShortCode, result);
        verify(shortCodeGenerator).generate(longUrl);
        verify(urlRepository).existsByShortCode(expectedShortCode);
    }
    
    @Test
    void testGenerateUniqueShortCodeWithCollisionRetry() {
        String longUrl = "https://example.com";
        String firstCode = "abc123";
        String secondCode = "def456";
        
        when(shortCodeGenerator.generate(longUrl))
            .thenReturn(firstCode)
            .thenReturn(secondCode);
        when(urlRepository.existsByShortCode(firstCode)).thenReturn(true);
        when(urlRepository.existsByShortCode(secondCode)).thenReturn(false);
        
        String result = shortCodeService.generateUniqueShortCode(longUrl);
        
        assertEquals(secondCode, result);
        verify(shortCodeGenerator, times(2)).generate(longUrl);
        verify(urlRepository).existsByShortCode(firstCode);
        verify(urlRepository).existsByShortCode(secondCode);
    }
    
    @Test
    void testGenerateUniqueShortCodeExhaustedRetries() {
        String longUrl = "https://example.com";
        String shortCode = "abc123";
        
        when(shortCodeGenerator.generate(longUrl)).thenReturn(shortCode);
        when(urlRepository.existsByShortCode(shortCode)).thenReturn(true);
        
        assertThrows(ShortCodeGenerationException.class, 
            () -> shortCodeService.generateUniqueShortCode(longUrl));
        
        verify(shortCodeGenerator, times(5)).generate(longUrl);
        verify(urlRepository, times(5)).existsByShortCode(shortCode);
    }
    
    @Test
    void testGenerateUniqueShortCodeWithNullUrl() {
        assertThrows(ShortCodeGenerationException.class, 
            () -> shortCodeService.generateUniqueShortCode(null));
    }
    
    @Test
    void testGenerateUniqueShortCodeWithEmptyUrl() {
        assertThrows(ShortCodeGenerationException.class, 
            () -> shortCodeService.generateUniqueShortCode(""));
    }
    
    @Test
    void testGenerateUniqueShortCodeWithGeneratorException() {
        String longUrl = "https://example.com";
        
        when(shortCodeGenerator.generate(longUrl))
            .thenThrow(new RuntimeException("Generator error"));
        
        assertThrows(ShortCodeGenerationException.class, 
            () -> shortCodeService.generateUniqueShortCode(longUrl));
    }
    
    @Test
    void testIsValidShortCodeFormat() {
        assertTrue(shortCodeService.isValidShortCodeFormat("abc123"));
        assertTrue(shortCodeService.isValidShortCodeFormat("ABCdef"));
        assertTrue(shortCodeService.isValidShortCodeFormat("123456"));
        assertTrue(shortCodeService.isValidShortCodeFormat("a1b2c3"));
        assertTrue(shortCodeService.isValidShortCodeFormat("AbCdEf"));
        
        assertFalse(shortCodeService.isValidShortCodeFormat("abc12")); // Too short
        assertFalse(shortCodeService.isValidShortCodeFormat("abc123456")); // Too long
        assertFalse(shortCodeService.isValidShortCodeFormat("abc@123")); // Invalid character
        assertFalse(shortCodeService.isValidShortCodeFormat("")); // Empty
        assertFalse(shortCodeService.isValidShortCodeFormat(null)); // Null
    }
}
