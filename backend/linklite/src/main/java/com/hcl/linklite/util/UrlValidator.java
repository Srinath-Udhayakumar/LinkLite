package com.hcl.linklite.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class UrlValidator {
    
    // Only allow http and https
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    );
    
    // Blocked schemes
    private static final Pattern BLOCKED_SCHEMES = Pattern.compile(
            "^(javascript|data|file|vbscript|about):.*",
            Pattern.CASE_INSENSITIVE
    );
    
    public static boolean isValid(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        
        String trimmedUrl = url.trim();
        
        // Check for blocked schemes
        if (BLOCKED_SCHEMES.matcher(trimmedUrl).matches()) {
            return false;
        }
        
        // Check URL pattern
        if (!URL_PATTERN.matcher(trimmedUrl).matches()) {
            return false;
        }
        
        try {
            URI uri = new URI(trimmedUrl);
            // Only allow http and https schemes
            if (uri.getScheme() == null || 
                (!uri.getScheme().equalsIgnoreCase("http") && 
                 !uri.getScheme().equalsIgnoreCase("https"))) {
                return false;
            }
            return uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }
    
    public static String normalize(String url) {
        if (url == null) {
            return null;
        }
        
        String normalized = url.trim();
        if (!normalized.isEmpty() && !normalized.matches("^[a-zA-Z]+://.*")) {
            normalized = "https://" + normalized;
        }
        
        return normalized;
    }
}

