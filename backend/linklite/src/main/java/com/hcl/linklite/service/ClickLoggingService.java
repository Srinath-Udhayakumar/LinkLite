package com.hcl.linklite.service;

import com.hcl.linklite.entity.Url;
import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.exception.UrlNotFoundException;
import com.hcl.linklite.repository.UrlClickRepository;
import com.hcl.linklite.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClickLoggingService {

    private final UrlClickRepository urlClickRepository;
    private final UrlRepository urlRepository;

    @Transactional
    public void logClick(String shortCode, String ipAddress) {
        log.debug("Logging click for shortCode: {}, IP: {}", shortCode, ipAddress);
        
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for shortCode: " + shortCode));

        UrlClick urlClick = UrlClick.builder()
                .url(url)
                .ipAddress(ipAddress)
                .build();

        urlClickRepository.save(urlClick);

        // Atomically increment total clicks to prevent race conditions under concurrent access
        urlRepository.incrementTotalClicks(shortCode);

        log.debug("Successfully logged click for shortCode: {}", shortCode);
    }

    public String getLongUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(Url::getLongUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for shortCode: " + shortCode));
    }
}
