package com.hcl.linklite.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.linklite.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    @Query("SELECT u FROM Url u WHERE u.createdAt >= :since ORDER BY u.totalClicks DESC")
    List<Url> findTopUrlsByClicksSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(u) FROM Url u WHERE u.createdAt >= :since")
    long countUrlsCreatedSince(@Param("since") LocalDateTime since);
    
    // Get top 10 most clicked URLs
    @Query("SELECT u FROM Url u ORDER BY u.totalClicks DESC")
    List<Url> findTop10ByOrderByTotalClicksDesc(Pageable pageable);
    
    default List<Url> findTop10ByOrderByTotalClicksDesc() {
        return findTop10ByOrderByTotalClicksDesc(org.springframework.data.domain.PageRequest.of(0, 10));
    }
    
    Page<Url> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);
    
    // Atomic increment of total clicks (race-condition safe)
    @Modifying
    @Query("UPDATE Url u SET u.totalClicks = u.totalClicks + 1 WHERE u.shortCode = :shortCode")
    void incrementTotalClicks(@Param("shortCode") String shortCode);
}

