package dev.njabulo.korturl.service;

import dev.njabulo.korturl.common.URLCaptureRequest;
import dev.njabulo.korturl.entity.URLConfig;
import dev.njabulo.korturl.repository.URLConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class URLConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLConfigService.class);
    private final URLConfigRepository configRepository;

    @Autowired
    public URLConfigService(URLConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public URLConfig getURLByShortenedSuffix(String shortUrlSuffix) {
        Optional<URLConfig> urlConfigOptional = configRepository.findUrlByShortenedSuffix(shortUrlSuffix);
        return urlConfigOptional.orElse(null);
    }

    public URLConfig saveURLConfig(String shortUrlSuffix, URLCaptureRequest urlDetails) {
        URLConfig urlConfig = new URLConfig();
        urlConfig.setOriginalUrl(urlDetails.url());
        urlConfig.setShortenedSuffix(shortUrlSuffix);
        if (urlDetails.name() != null) {
            urlConfig.setName(urlDetails.name());
        }
        //Crude check for duplicates
        if (configRepository.isGenSuffixInUse(shortUrlSuffix)) {
            LOGGER.warn("Generated suffix already exist: {}", shortUrlSuffix);
            return null;
        }
        return configRepository.save(urlConfig);
    }
}
