package com.hcl.linklite.repository;

import com.hcl.linklite.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for URL entity operations.
 * Minimal interface for short code generation needs.
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    
    /**
     * Checks if a short code already exists in the database.
     * 
     * @param shortCode the short code to check
     * @return true if exists, false otherwise
     */
    boolean existsByShortCode(String shortCode);
}
