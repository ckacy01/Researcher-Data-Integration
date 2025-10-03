package org.technoready.researcherdataintegration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "scholar.api")
public class ScholarApiConfiguration {
    private String baseUrl;
    private String apiKey;
    private Integer timeout;
}
