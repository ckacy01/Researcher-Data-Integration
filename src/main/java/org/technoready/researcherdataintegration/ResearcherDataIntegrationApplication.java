package org.technoready.researcherdataintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Researcher Data Integration application.
 *
 * This application integrates with external APIs (e.g., SerpAPI) to retrieve and serve
 * academic author data via RESTful endpoints.
 *
 * DATE: 04 - October - 2025
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */


@SpringBootApplication
public class ResearcherDataIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResearcherDataIntegrationApplication.class, args);
    }

}
