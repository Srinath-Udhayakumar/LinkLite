package com.hcl.linklite.repository;

import com.hcl.linklite.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for Url entity operations.
 * Provides database access methods for URL management.
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    
    /**
     * Finds a URL by its short code.
     * 
     * @param shortCode the short code to search for
     * @return Optional containing the URL if found, empty otherwise
     */
    Optional<Url> findByShortCode(String shortCode);
    
    /**
     * Finds a URL by its short code, only if it's active and not expired.
     * 
     * @param shortCode the short code to search for
     * @return Optional containing the valid URL if found, empty otherwise
     */
    @Query("SELECT u FROM Url u WHERE u.shortCode = :shortCode AND u.isActive = true AND (u.expiresAt IS NULL OR u.expiresAt > CURRENT_TIMESTAMP)")
    Optional<Url> findValidUrlByShortCode(@Param("shortCode") String shortCode);
    
    /**
     * Checks if a short code already exists in the database.
     * 
     * @param shortCode the short code to check
     * @return true if exists, false otherwise
     */
    boolean existsByShortCode(String shortCode);
    
    /**
     * Finds a URL by its original URL.
     * 
     * @param originalUrl the original URL to search for
     * @return Optional containing the URL if found, empty otherwise
     */
    Optional<Url> findByOriginalUrl(String originalUrl);
    
    /**
     * Increments the click count for a URL by its ID.
     * 
     * @param urlId the ID of the URL to update
     */
    @Modifying
    @Query("UPDATE Url u SET u.clickCount = u.clickCount + 1 WHERE u.id = :urlId")
    void incrementClickCount(@Param("urlId") Long urlId);
    
    /**
     * Updates the short code for a URL by its ID.
     * This is used after generating the short code from the database ID.
     * 
     * @param urlId the ID of the URL to update
     * @param shortCode the new short code
     */
    @Modifying
    @Query("UPDATE Url u SET u.shortCode = :shortCode WHERE u.id = :urlId")
    void updateShortCode(@Param("urlId") Long urlId, @Param("shortCode") String shortCode);
}
