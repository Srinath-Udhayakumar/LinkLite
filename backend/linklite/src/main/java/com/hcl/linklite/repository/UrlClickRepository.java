package com.hcl.linklite.repository;

import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UrlClickRepository extends JpaRepository<UrlClick, Long> {

    long countByUrl(Url url);

    long countByUrlAndClickedAtAfter(Url url, LocalDateTime time);

    @Query("SELECT uc FROM UrlClick uc WHERE uc.url = :url ORDER BY uc.clickedAt DESC")
    List<UrlClick> findRecentClicksByUrl(@Param("url") Url url, @Param("limit") int limit);
}
