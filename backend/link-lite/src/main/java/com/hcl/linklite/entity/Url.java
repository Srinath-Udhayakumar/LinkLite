package com.hcl.linklite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA Entity representing a shortened URL.
 * Contains the original URL, generated short code, and metadata.
 */
@Entity
@Table(name = "urls", indexes = {
    @Index(name = "idx_short_code", columnList = "shortCode", unique = true),
    @Index(name = "idx_original_url", columnList = "originalUrl")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Original URL cannot be blank")
    @Size(max = 2048, message = "URL length cannot exceed 2048 characters")
    @Column(name = "original_url", nullable = false, length = 2048)
    private String originalUrl;
    
    @Size(max = 10, message = "Short code length cannot exceed 10 characters")
    @Column(name = "short_code", length = 10, unique = true)
    private String shortCode;
    
    @NotNull
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @NotNull
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Builder.Default
    @Column(name = "click_count", nullable = false)
    private Long clickCount = 0L;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    /**
     * Checks if the URL has expired.
     * 
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Checks if the URL is currently valid (active and not expired).
     * 
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return Boolean.TRUE.equals(isActive) && !isExpired();
    }
    
    /**
     * Increments the click count by 1.
     */
    public void incrementClickCount() {
        this.clickCount++;
    }
}
