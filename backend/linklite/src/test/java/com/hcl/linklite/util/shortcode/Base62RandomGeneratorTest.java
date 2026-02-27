package com.hcl.linklite.util.shortcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class Base62RandomGeneratorTest {
    
    private Base62RandomGenerator generator;
    
    @BeforeEach
    void setUp() {
        generator = new Base62RandomGenerator();
    }
    
    @Test
    @DisplayName("Should generate short code of default length")
    void shouldGenerateShortCodeOfDefaultLength() {
        String longUrl = "https://example.com";
        String shortCode = generator.generate(longUrl);
        
        assertThat(shortCode).hasSize(6);
        assertThat(shortCode).matches("^[a-zA-Z0-9]{6}$");
    }
    
    @Test
    @DisplayName("Should generate short code of specified length")
    void shouldGenerateShortCodeOfSpecifiedLength() {
        int length = 8;
        String shortCode = generator.generate(length);
        
        assertThat(shortCode).hasSize(length);
        assertThat(shortCode).matches("^[a-zA-Z0-9]{" + length + "}$");
    }
    
    @Test
    @DisplayName("Should generate different codes on multiple calls")
    void shouldGenerateDifferentCodesOnMultipleCalls() {
        String longUrl = "https://example.com";
        
        String code1 = generator.generate(longUrl);
        String code2 = generator.generate(longUrl);
        String code3 = generator.generate(longUrl);
        
        assertThat(code1).isNotEqualTo(code2);
        assertThat(code2).isNotEqualTo(code3);
        assertThat(code1).isNotEqualTo(code3);
    }
    
    @Test
    @DisplayName("Should throw exception for invalid length")
    void shouldThrowExceptionForInvalidLength() {
        assertThatThrownBy(() -> generator.generate(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length must be at least 1");
        
        assertThatThrownBy(() -> generator.generate(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length must be at least 1");
    }
    
    @Test
    @DisplayName("Should generate codes with valid characters only")
    void shouldGenerateCodesWithValidCharactersOnly() {
        String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        
        for (int i = 0; i < 100; i++) {
            String code = generator.generate(6);
            for (char c : code.toCharArray()) {
                assertThat(validChars).contains(String.valueOf(c));
            }
        }
    }
    
    @Test
    @DisplayName("Should handle null longUrl gracefully")
    void shouldHandleNullLongUrlGracefully() {
        assertThatNoException().isThrownBy(() -> generator.generate(null));
    }
    
    @Test
    @DisplayName("Should handle empty longUrl gracefully")
    void shouldHandleEmptyLongUrlGracefully() {
        assertThatNoException().isThrownBy(() -> generator.generate(""));
    }
}
