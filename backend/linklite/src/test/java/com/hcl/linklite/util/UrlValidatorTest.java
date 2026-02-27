package com.hcl.linklite.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UrlValidatorTest {
    
    @Test
    @DisplayName("Should validate correct HTTP URLs")
    void shouldValidateCorrectHttpUrls() {
        assertThat(UrlValidator.isValid("http://example.com")).isTrue();
        assertThat(UrlValidator.isValid("http://www.example.com")).isTrue();
        assertThat(UrlValidator.isValid("http://example.com/path")).isTrue();
    }
    
    @Test
    @DisplayName("Should validate correct HTTPS URLs")
    void shouldValidateCorrectHttpsUrls() {
        assertThat(UrlValidator.isValid("https://example.com")).isTrue();
        assertThat(UrlValidator.isValid("https://www.example.com")).isTrue();
        assertThat(UrlValidator.isValid("https://example.com/path/to/resource")).isTrue();
    }
    
    @Test
    @DisplayName("Should validate URLs with query parameters")
    void shouldValidateUrlsWithQueryParameters() {
        assertThat(UrlValidator.isValid("https://example.com/path?key=value")).isTrue();
        assertThat(UrlValidator.isValid("https://example.com/path?key1=value1&key2=value2")).isTrue();
    }
    
    @Test
    @DisplayName("Should reject FTP URLs")
    void shouldRejectFtpUrls() {
        assertThat(UrlValidator.isValid("ftp://example.com/file.txt")).isFalse();
    }
    
    @Test
    @DisplayName("Should reject JavaScript URLs")
    void shouldRejectJavaScriptUrls() {
        assertThat(UrlValidator.isValid("javascript:alert('XSS')")).isFalse();
    }
    
    @Test
    @DisplayName("Should reject data URLs")
    void shouldRejectDataUrls() {
        assertThat(UrlValidator.isValid("data:text/html,<h1>Test</h1>")).isFalse();
    }
    
    @Test
    @DisplayName("Should reject file URLs")
    void shouldRejectFileUrls() {
        assertThat(UrlValidator.isValid("file:///C:/Windows/System32/notepad.exe")).isFalse();
    }
    
    @Test
    @DisplayName("Should reject vbscript URLs")
    void shouldRejectVbscriptUrls() {
        assertThat(UrlValidator.isValid("vbscript:msgbox('XSS')")).isFalse();
    }
    
    @Test
    @DisplayName("Should reject null URLs")
    void shouldRejectNullUrls() {
        assertThat(UrlValidator.isValid(null)).isFalse();
    }
    
    @Test
    @DisplayName("Should reject empty URLs")
    void shouldRejectEmptyUrls() {
        assertThat(UrlValidator.isValid("")).isFalse();
        assertThat(UrlValidator.isValid("   ")).isFalse();
    }
    
    @Test
    @DisplayName("Should reject URLs without host")
    void shouldRejectUrlsWithoutHost() {
        assertThat(UrlValidator.isValid("http://")).isFalse();
        assertThat(UrlValidator.isValid("https://")).isFalse();
    }
    
    @Test
    @DisplayName("Should normalize URLs with http prefix")
    void shouldNormalizeUrlsWithHttpPrefix() {
        String normalized = UrlValidator.normalize("example.com");
        assertThat(normalized).isEqualTo("https://example.com");
    }
    
    @Test
    @DisplayName("Should not modify already normalized URLs")
    void shouldNotModifyAlreadyNormalizedUrls() {
        String normalized = UrlValidator.normalize("https://example.com");
        assertThat(normalized).isEqualTo("https://example.com");
    }
    
    @Test
    @DisplayName("Should handle null in normalize")
    void shouldHandleNullInNormalize() {
        String normalized = UrlValidator.normalize(null);
        assertThat(normalized).isNull();
    }
    
    @Test
    @DisplayName("Should trim whitespace during normalization")
    void shouldTrimWhitespaceDuringNormalization() {
        String normalized = UrlValidator.normalize("  https://example.com  ");
        assertThat(normalized).isEqualTo("https://example.com");
    }
}
