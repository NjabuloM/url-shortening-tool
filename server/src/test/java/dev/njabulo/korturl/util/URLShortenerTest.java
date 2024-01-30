package dev.njabulo.korturl.util;

import dev.njabulo.korturl.common.HashedUrl;
import dev.njabulo.korturl.service.GenerationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class URLShortenerTest {

    private String sampleUrl;

    @BeforeEach
    void setup() {
        sampleUrl = "http://www.example.com/sample/test";
    }

    @Test
    public void testDefaultShorteningLogic() {

        HashedUrl hashedUrl = URLShortener.shortenURL(sampleUrl, GenerationService.INITIAL_HASHING_ATTEMPT);
        assert hashedUrl != null;
        String urlSuffix = hashedUrl.derivedSuffix();
        String hash = hashedUrl.hash();

        Assertions.assertEquals(hash.substring(0, 8), urlSuffix);
    }

    @Test
    public void testSecondTryShorteningLogic() {

        HashedUrl hashedUrl = URLShortener.shortenURL(sampleUrl, GenerationService.SECOND_HASHING_ATTEMPT);
        assert hashedUrl != null;
        String urlSuffix = hashedUrl.derivedSuffix();
        String hash = hashedUrl.hash();

        Assertions.assertEquals(hash.substring(1, 9), urlSuffix);
    }

    @Test
    public void testThirdShorteningLogic() {

        HashedUrl hashedUrl = URLShortener.shortenURL(sampleUrl, GenerationService.THIRD_HASHING_ATTEMPT);
        assert hashedUrl != null;
        String urlSuffix = hashedUrl.derivedSuffix();
        String hash = hashedUrl.hash();

        Assertions.assertEquals(hash.substring(2, 10), urlSuffix);
    }
}
