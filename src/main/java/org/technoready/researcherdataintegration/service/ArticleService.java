package org.technoready.researcherdataintegration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.technoready.researcherdataintegration.config.DatabaseConfiguration;
import org.technoready.researcherdataintegration.config.ScholarApiConfiguration;
import org.technoready.researcherdataintegration.dto.ArticleResponseDTO;
import org.technoready.researcherdataintegration.entity.Article;
import org.technoready.researcherdataintegration.exception.ApiKeyException;
import org.technoready.researcherdataintegration.exception.DatabaseException;
import org.technoready.researcherdataintegration.exception.ValidationException;
import org.technoready.researcherdataintegration.repository.ArticleRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service class responsible for article importation and management operations.
 * Handles integration with SerpAPI to retrieve articles, data transformation, and database persistence.
 * DATE: 08 - October - 2025
 *
 * This service orchestrates the complete article import workflow:
 * 1. Input validation (researchers list and article limits)
 * 2. External API calls to SerpAPI (Google Scholar)
 * 3. JSON response parsing using Jackson
 * 4. Data extraction and transformation to entity objects
 * 5. Database persistence with duplicate checking
 * 6. Article retrieval from database
 *
 * External API used: https://serpapi.com/
 *
 * Validation rules:
 * - Researchers list cannot be empty
 * - Articles per researcher must be between 1 and 20
 * - Researcher names cannot be null or empty
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final DatabaseConfiguration databaseConfiguration;
    private final ScholarApiConfiguration scholarApiConfiguration;

    /**
     * Imports articles for multiple researchers from SerpAPI and saves them to the database.
     * Processes each researcher sequentially and aggregates all saved articles.
     *
     * @param researchers List<String> - List of researcher names to search for
     * @param articlesPerResearcher int - Number of articles to retrieve per researcher (1-20)
     * @return List<Article> - All articles successfully saved to the database
     * @throws ValidationException if input parameters are invalid
     * @throws ApiKeyException if there are errors communicating with SerpAPI
     * @throws DatabaseException if database operations fail
     */
    @Transactional
    public List<Article> importArticlesForResearchers(
            List<String> researchers, int articlesPerResearcher) {

        validateInput(researchers, articlesPerResearcher);

        log.info("==================================================");
        log.info("Starting article import");
        log.info("Researchers: {}", researchers);
        log.info("Articles per researcher: {}", articlesPerResearcher);
        log.info("==================================================");

        List<Article> allSavedArticles = new ArrayList<>();

        for (String researcher : researchers) {
            try {
                log.info("Processing researcher: {}", researcher);
                List<Article> savedArticles = processResearcher(researcher, articlesPerResearcher);
                allSavedArticles.addAll(savedArticles);
                log.info("{} articles saved for: {}", savedArticles.size(), researcher);
            } catch (Exception e) {
                log.error("Error processing {}: {}", researcher, e.getMessage(), e);
                throw new ApiKeyException("Error processing researcher " + researcher + ": " + e.getMessage());
            }
        }

        log.info("==================================================");
        log.info("Import completed");
        log.info("Total articles saved: {}", allSavedArticles.size());
        log.info("==================================================");

        return allSavedArticles;
    }

    /**
     * Processes an individual researcher through the complete import workflow.
     * Orchestrates API call, parsing, conversion, and database persistence.
     *
     * @param researcherName String - Name of the researcher to process
     * @param maxArticles int - Maximum number of articles to retrieve
     * @return List<Article> - Articles saved to the database for this researcher
     */
    private List<Article> processResearcher(String researcherName, int maxArticles) {
        // 1. Call SerpAPI to get raw JSON response
        String jsonResponse = callSerpApi(researcherName, maxArticles);

        // 2. Parse JSON response with Jackson to DTO
        ArticleResponseDTO response = parseJsonResponse(jsonResponse);

        // 3. Validate response contains results
        if (response.getOrganicResults() == null || response.getOrganicResults().isEmpty()) {
            log.warn("No results found for: {}", researcherName);
            return new ArrayList<>();
        }

        // 4. Convert DTO objects to Article entities
        List<Article> articles = convertToEntities(response, researcherName);

        // 5. Save to database with duplicate checking
        return saveArticles(articles, researcherName);
    }

    /**
     * Calls SerpAPI to retrieve articles for a specific researcher.
     * Constructs the API URL with proper encoding and makes the HTTP GET request.
     *
     * @param researcherName String - Name of the researcher to search
     * @param maxResults int - Maximum number of results to retrieve
     * @return String - Raw JSON response from SerpAPI
     * @throws ApiKeyException if API call fails or returns empty response
     */
    private String callSerpApi(String researcherName, int maxResults) {
        String apiKey = scholarApiConfiguration.getApiKey();
        String baseUrl = scholarApiConfiguration.getBaseUrl();

        // Build SerpAPI URL with URL-encoded researcher name
        String url = String.format(
                "%s?engine=google_scholar&q=author:%s&api_key=%s&num=%d",
                baseUrl,
                URLEncoder.encode(researcherName, StandardCharsets.UTF_8),
                apiKey,
                maxResults
        );

        log.debug("Calling SerpAPI: {}", url.replace(apiKey, "***"));

        try {
            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.isEmpty()) {
                throw new ApiKeyException("Empty response from SerpAPI");
            }
            log.debug("SerpAPI response: {}", response);
            return response;

        } catch (HttpClientErrorException e) {
            log.error("HTTP error from SerpAPI: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ApiKeyException("Connection error with SerpAPI: " + e.getStatusText());
        } catch (Exception e) {
            log.error("Error calling SerpAPI: {}", e.getMessage());
            throw new ApiKeyException("Error connecting to SerpAPI: " + e.getMessage());
        }
    }

    /**
     * Parses the JSON response from SerpAPI using Jackson ObjectMapper.
     *
     * @param jsonResponse String - Raw JSON response from SerpAPI
     * @return ArticleResponseDTO - Parsed response object
     * @throws ApiKeyException if JSON parsing fails
     */
    private ArticleResponseDTO parseJsonResponse(String jsonResponse) {
        try {
            return objectMapper.readValue(jsonResponse, ArticleResponseDTO.class);
        } catch (Exception e) {
            log.error("Error parsing JSON: {}", e.getMessage(), e);
            throw new ApiKeyException("Error parsing SerpAPI response");
        }
    }

    /**
     * Converts SerpAPI organic results to Article entity objects.
     * Extracts and transforms all relevant fields from the DTO to the entity.
     *
     * @param response ArticleResponseDTO - Parsed SerpAPI response
     * @param researcherName String - Name of the researcher for association
     * @return List<Article> - List of converted Article entities
     */
    private List<Article> convertToEntities(ArticleResponseDTO response, String researcherName) {
        List<Article> articles = new ArrayList<>();

        for (ArticleResponseDTO.OrganicResult result : response.getOrganicResults()) {
            try {
                Article article = Article.builder()
                        .id(result.getResultId())
                        .title(extractTitle(result))
                        .authors(extractAuthors(result))
                        .publicationDate(extractPublicationDate(result))
                        .abstractText(extractAbstract(result))
                        .link(extractLink(result))
                        .keywords(extractKeywords(result))
                        .citedBy(extractCitedBy(result))
                        .researcherName(researcherName)
                        .build();

                articles.add(article);

            } catch (Exception e) {
                log.warn("Error processing result: {}", e.getMessage());
            }
        }

        return articles;
    }

    /**
     * Saves articles to the database using JPA repository.
     * Checks for duplicates before saving to avoid constraint violations.
     *
     * @param articles List<Article> - Articles to save
     * @param researcherName String - Researcher name for duplicate checking
     * @return List<Article> - Successfully saved articles (excludes duplicates)
     * @throws DatabaseException if database operations fail
     */
    private List<Article> saveArticles(List<Article> articles, String researcherName) {
        List<Article> savedArticles = new ArrayList<>();

        try {
            for (Article article : articles) {
                // Check if article already exists to avoid duplicates
                if (!articleRepository.existsByIdAndResearcherName(article.getId(), researcherName)) {
                    Article saved = articleRepository.save(article);
                    savedArticles.add(saved);
                    log.info("Saved: {}", article.getTitle());
                } else {
                    log.info("Already exists: {}", article.getTitle());
                }
            }

            return savedArticles;

        } catch (Exception e) {
            log.error("Error saving to database: {}", e.getMessage(), e);
            throw new DatabaseException("Error saving articles to the database", e);
        }
    }

    /**
     * Retrieves all articles stored in the database.
     * Uses read-only transaction for optimal performance.
     *
     * @return List<Article> - All articles in the database
     * @throws DatabaseException if retrieval fails
     */
    @Transactional(readOnly = true)
    public List<Article> getAllArticles() {
        try {
            List<Article> articles = articleRepository.findAll();
            log.info("{} articles found in the database", articles.size());
            return articles;
        } catch (Exception e) {
            log.error("Error retrieving articles: {}", e.getMessage(), e);
            throw new DatabaseException("Error retrieving articles from the database", e);
        }
    }

    // ==========================================
    // Helper methods for data extraction
    // ==========================================

    /**
     * Validates input parameters for article importation.
     *
     * @param researchers List<String> - List of researcher names
     * @param articlesPerResearcher int - Number of articles per researcher
     * @throws ValidationException if any validation rule is violated
     */
    private void validateInput(List<String> researchers, int articlesPerResearcher) {
        if (researchers == null || researchers.isEmpty()) {
            throw new ValidationException("The list of researchers cannot be empty");
        }

        if (articlesPerResearcher < 1 || articlesPerResearcher > 20) {
            throw new ValidationException("The number of articles must be between 1 and 20");
        }

        for (String researcher : researchers) {
            if (researcher == null || researcher.trim().isEmpty()) {
                throw new ValidationException("Researcher names cannot be empty");
            }
        }
    }

    /**
     * Extracts article title from organic result.
     *
     * @param result OrganicResult - SerpAPI result object
     * @return String - Article title or "Untitled" if not available
     */
    private String extractTitle(ArticleResponseDTO.OrganicResult result) {
        return result.getTitle() != null ? result.getTitle() : "Untitled";
    }

    /**
     * Extracts and concatenates all author names from organic result.
     *
     * @param result OrganicResult - SerpAPI result object
     * @return String - Comma-separated list of author names or "Unknown author" if not available
     */
    private String extractAuthors(ArticleResponseDTO.OrganicResult result) {
        if (result.getPublicationInfo() != null &&
                result.getPublicationInfo().getAuthors() != null &&
                !result.getPublicationInfo().getAuthors().isEmpty()) {

            return result.getPublicationInfo().getAuthors()
                    .stream()
                    .map(ArticleResponseDTO.Author::getName)
                    .collect(Collectors.joining(", "));
        }
        return "Unknown author";
    }

    /**
     * Extracts publication date from publication summary using regex pattern.
     * Searches for a 4-digit year (19xx or 20xx) and creates a date with January 1st.
     *
     * @param result OrganicResult - SerpAPI result object
     * @return LocalDate - Publication date or current date if extraction fails
     */
    private LocalDate extractPublicationDate(ArticleResponseDTO.OrganicResult result) {
        try {
            if (result.getPublicationInfo() != null) {
                String summary = result.getPublicationInfo().getSummary();
                if (summary != null) {
                    // Extract year using regex pattern (matches 19xx or 20xx)
                    Pattern pattern = Pattern.compile("\\b(19|20)\\d{2}\\b");
                    Matcher matcher = pattern.matcher(summary);
                    if (matcher.find()) {
                        int year = Integer.parseInt(matcher.group());
                        return LocalDate.of(year, 1, 1);
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Could not extract date: {}", e.getMessage());
        }
        return LocalDate.now();
    }

    /**
     * Extracts article abstract from snippet.
     * Truncates to 1000 characters if longer.
     *
     * @param result OrganicResult - SerpAPI result object
     * @return String - Article abstract or "No abstract available" if not present
     */
    private String extractAbstract(ArticleResponseDTO.OrganicResult result) {
        String snippet = result.getSnippet();
        if (snippet != null && !snippet.isEmpty()) {
            return snippet.length() > 1000 ? snippet.substring(0, 1000) + "..." : snippet;
        }
        return "No abstract available";
    }

    /**
     * Extracts article link from organic result.
     *
     * @param result OrganicResult - SerpAPI result object
     * @return String - Article URL or Google Scholar homepage if not available
     */
    private String extractLink(ArticleResponseDTO.OrganicResult result) {
        return result.getLink() != null ? result.getLink() : "https://scholar.google.com";
    }

    /**
     * Extracts keywords from article snippet.
     * Takes the first 5 words longer than 3 characters, cleaned and lowercased.
     *
     * @param result OrganicResult - SerpAPI result object
     * @return String - Comma-separated keywords or null if snippet is empty
     */
    private String extractKeywords(ArticleResponseDTO.OrganicResult result) {
        String snippet = result.getSnippet();
        if (snippet != null && !snippet.isEmpty()) {
            String[] words = snippet.split("\\s+");
            int maxWords = Math.min(5, words.length);
            List<String> keywords = new ArrayList<>();

            for (int i = 0; i < maxWords; i++) {
                // Remove non-alphanumeric characters and convert to lowercase
                String word = words[i].replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                if (word.length() > 3) {
                    keywords.add(word);
                }
            }
            return String.join(", ", keywords);
        }
        return null;
    }

    /**
     * Extracts citation count from inline links.
     *
     * @param result OrganicResult - SerpAPI result object
     * @return Integer - Number of citations or 0 if not available
     */
    private Integer extractCitedBy(ArticleResponseDTO.OrganicResult result) {
        if (result.getInlineLinks() != null &&
                result.getInlineLinks().getCitedBy() != null) {
            return result.getInlineLinks().getCitedBy().getTotal();
        }
        return 0;
    }
}