package com.hcl.linklite.util.shortcode;

public interface ShortCodeGenerator {
    
    String generate(String longUrl);
    
    String generate(int length);
}
