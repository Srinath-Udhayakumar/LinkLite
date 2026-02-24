package com.hcl.linklite.util;

/**
 * Utility class for encoding and decoding numbers using Base62 encoding.
 * Base62 uses characters: 0-9, a-z, A-Z (total 62 characters)
 * This is commonly used for URL shortening to create compact, readable short codes.
 */
public final class Base62Encoder {
    
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = BASE62_CHARS.length();
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private Base62Encoder() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Encodes a non-negative long number to Base62 string.
     * 
     * @param number the non-negative number to encode
     * @return Base62 encoded string
     * @throws IllegalArgumentException if number is negative
     */
    public static String encode(long number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative");
        }
        
        if (number == 0) {
            return String.valueOf(BASE62_CHARS.charAt(0));
        }
        
        StringBuilder encoded = new StringBuilder();
        
        while (number > 0) {
            int remainder = (int) (number % BASE);
            encoded.append(BASE62_CHARS.charAt(remainder));
            number /= BASE;
        }
        
        return encoded.reverse().toString();
    }
    
    /**
     * Decodes a Base62 string back to a long number.
     * 
     * @param encoded the Base62 encoded string
     * @return decoded long number
     * @throws IllegalArgumentException if encoded string is null, empty, or contains invalid characters
     */
    public static long decode(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            throw new IllegalArgumentException("Encoded string cannot be null or empty");
        }
        
        long decoded = 0;
        int power = 1;
        
        for (int i = encoded.length() - 1; i >= 0; i--) {
            char c = encoded.charAt(i);
            int digit = BASE62_CHARS.indexOf(c);
            
            if (digit == -1) {
                throw new IllegalArgumentException("Invalid character in encoded string: " + c);
            }
            
            decoded += digit * power;
            power *= BASE;
        }
        
        return decoded;
    }
    
    /**
     * Validates if a string is a valid Base62 encoded string.
     * 
     * @param encoded the string to validate
     * @return true if valid Base62 string, false otherwise
     */
    public static boolean isValidBase62(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return false;
        }
        
        return encoded.chars().allMatch(c -> BASE62_CHARS.indexOf(c) >= 0);
    }
}
