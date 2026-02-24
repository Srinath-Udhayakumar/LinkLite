package com.hcl.linklite.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Base62Encoder utility class.
 * Tests encoding, decoding, and validation functionality.
 */
class Base62EncoderTest {

    @Test
    void testEncodeZero() {
        String result = Base62Encoder.encode(0L);
        assertEquals("0", result);
    }

    @Test
    void testEncodeOne() {
        String result = Base62Encoder.encode(1L);
        assertEquals("1", result);
    }

    @Test
    void testEncodeBase62MinusOne() {
        String result = Base62Encoder.encode(61L);
        assertEquals("Z", result);
    }

    @Test
    void testEncodeBase62() {
        String result = Base62Encoder.encode(62L);
        assertEquals("10", result);
    }

    @Test
    void testEncodeLargeNumber() {
        String result = Base62Encoder.encode(3844L); // 62 * 62
        assertEquals("100", result);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "1, 1",
        "10, a",
        "35, z",
        "36, A",
        "61, Z",
        "62, 10",
        "124, 20",
        "3843, ZZ",
        "3844, 100"
    })
    void testEncodeKnownValues(Long input, String expected) {
        String result = Base62Encoder.encode(input);
        assertEquals(expected, result);
    }

    @Test
    void testDecodeZero() {
        Long result = Base62Encoder.decode("0");
        assertEquals(0L, result);
    }

    @Test
    void testDecodeOne() {
        Long result = Base62Encoder.decode("1");
        assertEquals(1L, result);
    }

    @Test
    void testDecodeBase62MinusOne() {
        Long result = Base62Encoder.decode("Z");
        assertEquals(61L, result);
    }

    @Test
    void testDecodeBase62() {
        Long result = Base62Encoder.decode("10");
        assertEquals(62L, result);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "1, 1",
        "a, 10",
        "z, 35",
        "A, 36",
        "Z, 61",
        "10, 62",
        "20, 124",
        "ZZ, 3843",
        "100, 3844"
    })
    void testDecodeKnownValues(String input, Long expected) {
        Long result = Base62Encoder.decode(input);
        assertEquals(expected, result);
    }

    @Test
    void testEncodeDecodeRoundTrip() {
        // Test round-trip encoding/decoding for various numbers
        long[] testNumbers = {0L, 1L, 10L, 61L, 62L, 100L, 1000L, 10000L, Long.MAX_VALUE};
        
        for (long number : testNumbers) {
            String encoded = Base62Encoder.encode(number);
            Long decoded = Base62Encoder.decode(encoded);
            assertEquals(number, decoded, "Round-trip failed for number: " + number);
        }
    }

    @Test
    void testEncodeNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> Base62Encoder.encode(-1L));
    }

    @Test
    void testDecodeNullString() {
        assertThrows(IllegalArgumentException.class, () -> Base62Encoder.decode(null));
    }

    @Test
    void testDecodeEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> Base62Encoder.decode(""));
    }

    @Test
    void testDecodeInvalidCharacter() {
        assertThrows(IllegalArgumentException.class, () -> Base62Encoder.decode("@"));
        assertThrows(IllegalArgumentException.class, () -> Base62Encoder.decode("a@b"));
        assertThrows(IllegalArgumentException.class, () -> Base62Encoder.decode(" "));
    }

    @Test
    void testIsValidBase62ValidStrings() {
        assertTrue(Base62Encoder.isValidBase62("0"));
        assertTrue(Base62Encoder.isValidBase62("a"));
        assertTrue(Base62Encoder.isValidBase62("A"));
        assertTrue(Base62Encoder.isValidBase62("Z"));
        assertTrue(Base62Encoder.isValidBase62("abc123"));
        assertTrue(Base62Encoder.isValidBase62("ABC123"));
        assertTrue(Base62Encoder.isValidBase62("aBcDeF123"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "@", "a@b", " ", "abc!", "123#", "-abc"})
    void testIsValidBase62InvalidStrings(String input) {
        assertFalse(Base62Encoder.isValidBase62(input));
    }

    @Test
    void testIsValidBase62NullString() {
        assertFalse(Base62Encoder.isValidBase62(null));
    }

    @Test
    void testEncodeLargeNumberScalability() {
        // Test with a very large number to ensure scalability
        long largeNumber = 1_000_000_000L;
        String encoded = Base62Encoder.encode(largeNumber);
        
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        
        // Verify it can be decoded back
        Long decoded = Base62Encoder.decode(encoded);
        assertEquals(largeNumber, decoded);
    }

    @Test
    void testEncodeMaxLong() {
        String encoded = Base62Encoder.encode(Long.MAX_VALUE);
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        
        Long decoded = Base62Encoder.decode(encoded);
        assertEquals(Long.MAX_VALUE, decoded);
    }

    @Test
    void testPrivateConstructor() {
        // Test that utility class cannot be instantiated
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            // Use reflection to test private constructor
            var constructor = Base62Encoder.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
        
        assertTrue(exception.getMessage().contains("Utility class cannot be instantiated"));
    }
}
