package com.hcl.linklite.service;

import com.hcl.linklite.entity.Url;
import com.hcl.linklite.entity.UrlClick;
import com.hcl.linklite.repository.UrlClickRepository;
import com.hcl.linklite.repository.UrlRepository;
import com.hcl.linklite.exception.UrlNotFoundException;
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
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        String anonymizedIp = anonymizeIp(ipAddress);
        
        UrlClick urlClick = UrlClick.builder()
                .url(url)
                .ipAddress(anonymizedIp)
                .build();

        urlClickRepository.save(urlClick);
        
        // Use atomic increment instead of loading and saving
        incrementTotalClicks(shortCode);

        log.debug("Successfully logged click for shortCode: {}, anonymized IP: {}", 
                shortCode, anonymizedIp);
    }

    @Transactional
    public void incrementTotalClicks(String shortCode) {
        urlRepository.incrementTotalClicks(shortCode);
    }

    public String getLongUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(Url::getLongUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));
    }
    
    private String anonymizeIp(String ipAddress) {
        if (ipAddress == null || !ipAddress.matches(".*\\..*")) {
            return ipAddress;
        }
        String[] parts = ipAddress.split("\\.");
        if (parts.length == 4) {
            // IPv4: anonymize last octet
            return parts[0] + "." + parts[1] + "." + parts[2] + ".0";
        } else if (parts.length > 4) {
            // IPv6: anonymize last segments
            return ipAddress.substring(0, ipAddress.lastIndexOf(":")) + ":0";
        }
        return ipAddress;
    }
}

