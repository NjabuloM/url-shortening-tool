package dev.njabulo.korturl.api;

import dev.njabulo.korturl.common.URLCaptureRequest;
import dev.njabulo.korturl.entity.URLConfig;
import dev.njabulo.korturl.service.GenerationService;
import dev.njabulo.korturl.service.URLConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GenerationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerationResource.class);

    private final GenerationService generationService;
    private final URLConfigService configService;

    @Autowired
    public GenerationResource(GenerationService generationService, URLConfigService configService) {
        this.generationService = generationService;
        this.configService = configService;
    }

    @PostMapping(value = "/short-url", produces = "application/json")
    Map<String, Object> shortenUrl(@RequestBody URLCaptureRequest urlDetails) {
        LOGGER.info("Received request: {}", urlDetails);
        if (!isRequestValid(urlDetails)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        String shortUrl = null;
        try {
            shortUrl = generationService.captureShortUrl(urlDetails);
        } catch (InstantiationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hashing algorithm may not be configured correctly");
        } catch (GeneralSecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Too many repeat attempts");
        }
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("short-url", shortUrl);
        return objectMap;
    }

    private boolean isRequestValid(URLCaptureRequest urlDetails) {
        //TODO: Verify the format as well
        //TODO: Check if its not already shortened
        return urlDetails.url() != null;
    }

    @GetMapping(value = "/short-url/{alias}", produces = "application/json")
    Map<String, Object> retrieveUrlByAlias(@PathVariable String alias) {
        URLConfig urlConfig = configService.getURLByShortenedSuffix(alias);
        Map<String, Object> resultHolder = new HashMap<>();
        if (urlConfig != null) {
            resultHolder.put("shortenedSuffix", urlConfig.getShortenedSuffix());
            resultHolder.put("originalUrl", urlConfig.getOriginalUrl());
            resultHolder.put("name", urlConfig.getName());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination is no longer reachable");
        }
        return resultHolder;
    }
}
