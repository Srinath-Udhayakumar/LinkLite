package com.hcl.linklite.repository;

import com.hcl.linklite.entity.UrlClick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UrlClickRepository extends JpaRepository<UrlClick, Long> {

    // Total clicks for a shortCode
    long countByUrl_ShortCode(String shortCode);

    // Clicks within time range (e.g., last 24 hours)
    @Query("""
        SELECT COUNT(c)
        FROM UrlClick c
        WHERE c.url.shortCode = :shortCode
        AND c.clickedAt >= :fromTime
    """)
    long countClicksInRange(@Param("shortCode") String shortCode,
                            @Param("fromTime") LocalDateTime fromTime);

    // Paginated click history
    Page<UrlClick> findByUrl_ShortCodeOrderByClickedAtDesc(
            String shortCode,
            Pageable pageable
    );
}