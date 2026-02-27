package com.hcl.linklite.dto;

import java.time.LocalDateTime;

public class ClickHistoryDto {

    private final LocalDateTime clickedAt;

    public ClickHistoryDto(LocalDateTime clickedAt) {
        this.clickedAt = clickedAt;
    }

    public LocalDateTime getClickedAt() {
        return clickedAt;
    }
}