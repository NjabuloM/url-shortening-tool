package dev.njabulo.korturl.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dev.njabulo.korturl.common.HashedUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLShortener {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLShortener.class);
    private static MessageDigest messageDigest;

    private static void instantiateMessageDigestInstance() throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance("SHA-256");
    }

    public static HashedUrl shortenURL(String originalURL, int ordinance) {
        if (messageDigest == null) {
            try {
                instantiateMessageDigestInstance();
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error("Failed to instantiate the message digest", e);
                return null;
            }
        }
        String hashedValue = calculateHash(originalURL);
        LOGGER.info("Hashed value: {}", hashedValue);
        return switch (ordinance) {
            case 1 -> new HashedUrl(hashedValue, hashedValue.substring(1, 9));
            case 2 -> new HashedUrl(hashedValue, hashedValue.substring(2, 10));
            default ->
                //Grab the first 8 characters to make the short URL
                new HashedUrl(hashedValue, hashedValue.substring(0, 8));
        };
    }

    private static String calculateHash(String input) {
        byte[] hashBytes = messageDigest.digest(input.getBytes());
        StringBuilder hashStringBuilder = new StringBuilder();

        for (byte b : hashBytes) {
            hashStringBuilder.append(String.format("%02x", b));
        }

        return hashStringBuilder.toString();
    }
}
