package com.hcl.linklite.dto;

public class ClickCountResponseDto{

    private final String shortCode;
    private final String range;
    private final long clickCount;

    public ClickCountResponseDto(String shortCode,
                                 String range,
                                 long clickCount) {
        this.shortCode = shortCode;
        this.range = range;
        this.clickCount = clickCount;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getRange() {
        return range;
    }

    public long getClickCount() {
        return clickCount;
    }
}