package dev.njabulo.korturl.service;

import dev.njabulo.korturl.common.HashedUrl;
import dev.njabulo.korturl.common.URLCaptureRequest;
import dev.njabulo.korturl.entity.URLConfig;
import dev.njabulo.korturl.util.URLShortener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;

@Service
public class GenerationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerationService.class);

    @Autowired
    private URLConfigService configService;

    public static final int INITIAL_HASHING_ATTEMPT = 0;
    public static final int SECOND_HASHING_ATTEMPT = 1;
    public static final int THIRD_HASHING_ATTEMPT = 2;

    public String captureShortUrl(URLCaptureRequest urlDetails) throws InstantiationException, GeneralSecurityException {
        String originalUrl = urlDetails.url();
        String shortUrl = makeShortUrl(originalUrl, INITIAL_HASHING_ATTEMPT);

        //Persist if not exist
        URLConfig savedConfig = configService.saveURLConfig(shortUrl, urlDetails);
        if (savedConfig == null) {
            //Then we attempt our crude retry
            shortUrl = makeShortUrl(originalUrl, SECOND_HASHING_ATTEMPT);
            savedConfig = configService.saveURLConfig(shortUrl, urlDetails);

            if (savedConfig == null) {
                //Third and final attempt
                shortUrl = makeShortUrl(originalUrl, THIRD_HASHING_ATTEMPT);
                savedConfig = configService.saveURLConfig(shortUrl, urlDetails);
            }

            if (savedConfig == null) {
                throw new GeneralSecurityException("Selected URL has been saved too many times");
            }
        }

        return shortUrl;
    }

    private static String makeShortUrl(String originalUrl, int attemptCount) throws InstantiationException {
        LOGGER.info("Generate short URL. Attempt: {}", attemptCount);
        HashedUrl hashedUrl = URLShortener.shortenURL(originalUrl, attemptCount);
        String shortUrl = null;
        if (hashedUrl != null) {
            shortUrl = hashedUrl.derivedSuffix();
        }
        if (shortUrl == null) {
            throw new InstantiationException("Internal config issue: failed to hash original url");
        }
        return shortUrl;
    }
}
