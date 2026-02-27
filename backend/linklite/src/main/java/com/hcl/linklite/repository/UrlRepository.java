package com.hcl.linklite.repository;

import com.hcl.linklite.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    @Modifying
    @Query("UPDATE Url u SET u.totalClicks = u.totalClicks + 1 WHERE u.shortCode = :shortCode")
    void incrementTotalClicks(@Param("shortCode") String shortCode);

    @Query("SELECT u FROM Url u WHERE u.createdAt >= :since ORDER BY u.totalClicks DESC")
    List<Url> findTopUrlsByClicksSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(u) FROM Url u WHERE u.createdAt >= :since")
    long countUrlsCreatedSince(@Param("since") LocalDateTime since);
}
