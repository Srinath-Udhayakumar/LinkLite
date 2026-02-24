package com.hcl.linklite.entity;

import jakarta.persistence.*;

import lombok.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
import java.util.Objects;
import org.hibernate.Hibernate;
import java.time.LocalDateTime;

@Entity
@Table(name = "urls",
        indexes = {
                @Index(name = "idx_short_code", columnList = "short_code")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "long_url", nullable = false, columnDefinition = "TEXT")
    private String longUrl;

    @Column(name = "short_code", nullable = false, unique = true, length = 10)
    private String shortCode;

    @Column(name = "total_clicks", nullable = false)
    @Builder.Default
    private Long totalClicks = 0L;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Url url = (Url) o;
        return id != null && Objects.equals(id, url.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
