package com.hcl.linklite.util;

import java.math.BigInteger;

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
        
        // Use BigInteger to avoid overflow with large numbers
        BigInteger bigNumber = BigInteger.valueOf(number);
        BigInteger base = BigInteger.valueOf(BASE);
        StringBuilder encoded = new StringBuilder();
        
        while (bigNumber.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] quotientAndRemainder = bigNumber.divideAndRemainder(base);
            int remainder = quotientAndRemainder[1].intValue();
            encoded.append(BASE62_CHARS.charAt(remainder));
            bigNumber = quotientAndRemainder[0];
        }
        
        return encoded.reverse().toString();
    }
    
    /**
     * Decodes a Base62 string back to a long number.
     * 
     * @param encoded the Base62 encoded string
     * @return decoded long number
     * @throws IllegalArgumentException if encoded string is null, empty, contains invalid characters, or results in overflow
     */
    public static long decode(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            throw new IllegalArgumentException("Encoded string cannot be null or empty");
        }
        
        BigInteger decoded = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(BASE);
        
        for (int i = 0; i < encoded.length(); i++) {
            char c = encoded.charAt(i);
            int digit = BASE62_CHARS.indexOf(c);
            
            if (digit == -1) {
                throw new IllegalArgumentException("Invalid character in encoded string: " + c);
            }
            
            decoded = decoded.multiply(base).add(BigInteger.valueOf(digit));
        }
        
        // Check if the result fits in a long
        if (decoded.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            throw new IllegalArgumentException("Decoded number exceeds maximum long value");
        }
        
        return decoded.longValue();
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
