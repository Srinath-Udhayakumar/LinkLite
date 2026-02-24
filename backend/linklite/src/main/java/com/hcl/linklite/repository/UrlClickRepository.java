package com.hcl.linklite.repository;

import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UrlClickRepository extends JpaRepository<UrlClick, Long> {

    long countByUrl(Url url);

    long countByUrlAndClickedAtAfter(Url url, LocalDateTime time);
}