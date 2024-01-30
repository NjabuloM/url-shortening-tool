package dev.njabulo.korturl.service;

import dev.njabulo.korturl.common.URLCaptureRequest;
import dev.njabulo.korturl.entity.URLConfig;
import dev.njabulo.korturl.repository.URLConfigRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

@SpringBootTest
public class URLConfigServiceTest {

    private URLConfigService configService;
    @MockBean
    private URLConfigRepository configRepository;

    @BeforeEach
    void setup() {
        configService = new URLConfigService(configRepository);
    }

    @Test
    public void testSavingConfig() {
        //Sample config
        String shortUrl = "hr85goat";
        String testURL = "https://letmegooglethat.com/?q=dancing+in+the+rain";

        URLConfig urlConfig = mock(URLConfig.class);
        //Mimic saving operation
        doReturn(urlConfig).when(configRepository).save(any(URLConfig.class));

        URLConfig savedEntity = configService.saveURLConfig(shortUrl, new URLCaptureRequest(testURL, "Test Config"));

        Assertions.assertNotNull(savedEntity);
    }

    @Test
    public void testSavingADuplicateConfig() {
        String existingShortUrl = "id3rn0wo";

        doReturn(true).when(configRepository).isGenSuffixInUse(anyString());

        URLConfig notSavedEntity = configService.saveURLConfig(existingShortUrl, new URLCaptureRequest("https://example.net", "Test Config"));
        Assertions.assertNull(notSavedEntity);
    }
}
