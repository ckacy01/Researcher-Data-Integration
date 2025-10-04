package org.technoready.researcherdataintegration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
/**
 * Class to create an object to get the application properties.
 * DATE: 04 - October - 2025
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "scholar.api")
public class ScholarApiConfiguration {
    private String baseUrl;
    private String apiKey;
    private Integer timeout;
}
