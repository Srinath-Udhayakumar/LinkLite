package com.hcl.linklite.util.shortcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class Base62RandomGeneratorTest {
    
    private Base62RandomGenerator generator;
    
    @BeforeEach
    void setUp() {
        generator = new Base62RandomGenerator();
    }
    
    @Test
    void testGenerateValidShortCode() {
        String longUrl = "https://example.com/very/long/url";
        String shortCode = generator.generate(longUrl);
        
        assertNotNull(shortCode);
        assertTrue(shortCode.length() >= 6 && shortCode.length() <= 8);
        assertTrue(shortCode.matches("^[a-zA-Z0-9]{6,8}$"));
    }
    
    @Test
    void testGenerateDifferentCodes() {
        String longUrl = "https://example.com";
        
        String code1 = generator.generate(longUrl);
        String code2 = generator.generate(longUrl);
        String code3 = generator.generate(longUrl);
        
        // Should generate different codes due to randomness
        assertFalse(code1.equals(code2) && code2.equals(code3));
    }
    
    @Test
    void testGenerateWithNullUrl() {
        assertThrows(ShortCodeGenerationException.class, () -> generator.generate(null));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "https://example.com",
        "https://very.long.url.example.com/with/many/path/segments",
        "http://test.org",
        "ftp://files.example.com"
    })
    void testGenerateWithVariousUrls(String url) {
        String shortCode = generator.generate(url);
        
        assertNotNull(shortCode);
        assertTrue(shortCode.length() >= 6 && shortCode.length() <= 8);
        assertTrue(shortCode.matches("^[a-zA-Z0-9]{6,8}$"));
    }
    
    @Test
    void testGenerateMultipleTimes() {
        String longUrl = "https://example.com";
        
        for (int i = 0; i < 100; i++) {
            String shortCode = generator.generate(longUrl);
            
            assertNotNull(shortCode);
            assertTrue(shortCode.length() >= 6 && shortCode.length() <= 8);
            assertTrue(shortCode.matches("^[a-zA-Z0-9]{6,8}$"));
        }
    }
    
    @Test
    void testThreadSafety() throws InterruptedException {
        String longUrl = "https://example.com";
        int threadCount = 10;
        int iterationsPerThread = 50;
        
        Thread[] threads = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterationsPerThread; j++) {
                    String shortCode = generator.generate(longUrl);
                    assertNotNull(shortCode);
                    assertTrue(shortCode.length() >= 6 && shortCode.length() <= 8);
                }
            });
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
