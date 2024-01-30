package dev.njabulo.korturl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "url_config")
public class URLConfig {
    @Id
    private String id;
    private String name;
    private String originalUrl;
    private String shortenedSuffix;
}
