package org.technoready.researcherdataintegration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.technoready.researcherdataintegration.config.ScholarApiConfiguration;
import org.technoready.researcherdataintegration.exception.ApiKeyException;
import org.technoready.researcherdataintegration.exception.AuthorNotFoundException;
import org.technoready.researcherdataintegration.exception.ExternalApiException;
import org.technoready.researcherdataintegration.model.Author;
import org.technoready.researcherdataintegration.model.AuthorSearchResponse;

/**
 * Service class responsible for retrieving author data from an external API (e.g., SerpAPI).
 * Handles API key validation, URL construction, external API calls, and response validation.
 * DATE: 04 - October - 2025
 *
 * This service throws appropriate custom exceptions to be handled globally.
 *
 * External API used: https://serpapi.com/
 *
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final RestTemplate restTemplate;
    private final ScholarApiConfiguration config;

    /**
     * Retrieves an Author object from SerpAPI using the provided author ID.
     *
     * @param authorId String - The unique author ID
     * @return Author - The author information retrieved from the external API
     * @throws ApiKeyException if the API key is missing or invalid
     * @throws AuthorNotFoundException if no author is found for the given ID
     * @throws ExternalApiException for other API-related errors (client/server)
     */
    public Author getAuthor(String authorId) {
        validateApiKey();

        String url = String.format(
                config.getBaseUrl() +
                        "?engine=google_scholar_author&author_id=%s&api_key=%s",
                authorId, config.getApiKey());

        try {
            log.info("Getting author with id {}", authorId);

            AuthorSearchResponse response = restTemplate.getForObject(url, AuthorSearchResponse.class);

            if (response == null || response.getAuthor() == null) {
                throw new AuthorNotFoundException(authorId);
            }

            return response.getAuthor();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                throw new ApiKeyException("Invalid API key");
            }
            throw new ExternalApiException("Error consulting SerpAPI", e);

        } catch (HttpServerErrorException e) {
            throw new ExternalApiException("Error consulting SerpAPI", e);
        }
    }

    /**
     * Validates the configured API key before making external API requests.
     *
     * @throws ApiKeyException if the API key is null or empty
     */
    public void validateApiKey() {
        if (config.getApiKey() == null || config.getApiKey().isEmpty()) {
            throw new ApiKeyException("API KEY IS NOT VALID");
        }
    }
}
