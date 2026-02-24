package com.hcl.linklite.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class UrlValidator {
    
    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    );
    
    public static boolean isValid(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        
        if (!URL_PATTERN.matcher(url).matches()) {
            return false;
        }
        
        try {
            URI uri = new URI(url);
            return uri.getScheme() != null && uri.getHost() != null;
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
            normalized = "http://" + normalized;
        }
        
        return normalized;
    }
}
