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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     * Imports articles for multiple researchers and returns those saved in the database.
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
                log.info("✓ {} articles saved for: {}", savedArticles.size(), researcher);
            } catch (Exception e) {
                log.error("✗ Error processing {}: {}", researcher, e.getMessage(), e);
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
     * Processes an individual researcher
     */
    private List<Article> processResearcher(String researcherName, int maxArticles) {
        // 1. Call SerpAPI
        String jsonResponse = callSerpApi(researcherName, maxArticles);

        // 2. Parse JSON with Jackson
        ArticleResponseDTO response = parseJsonResponse(jsonResponse);

        // 3. Validate response
        if (response.getOrganicResults() == null || response.getOrganicResults().isEmpty()) {
            log.warn("No results found for: {}", researcherName);
            return new ArrayList<>();
        }

        // 4. Convert to entity objects
        List<Article> articles = convertToEntities(response, researcherName);

        // 5. Save to database
        return saveArticles(articles, researcherName);
    }

    /**
     * Calls SerpAPI to get articles
     */
    private String callSerpApi(String researcherName, int maxResults) {
        String apiKey = scholarApiConfiguration.getApiKey();
        String baseUrl = scholarApiConfiguration.getBaseUrl();

        if (apiKey == null || apiKey.equals("tu_serpapi_key")) {
            throw new ValidationException("SerpAPI key not configured");
        }

        String url = String.format(
                "%s?engine=google_scholar&q=author:%%22%s%%22&api_key=%s&num=%d",
                baseUrl,
                researcherName.replace(" ", "+"),
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
     * Parses JSON response using Jackson
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
     * Converts SerpAPI results to entity objects
     */
    private List<Article> convertToEntities(ArticleResponseDTO response, String researcherName) {
        List<Article> articles = new ArrayList<>();

        for (ArticleResponseDTO.OrganicResult result : response.getOrganicResults()) {
            try {
                Article article = Article.builder()
                        .id(result.getResultId())
                        .title(extractTitle(result))
                        .authors(extractAuthors(result))
                        .publication_date(extractPublicationDate(result))
                        ._abstract(extractAbstract(result))
                        .link(extractLink(result))
                        .keywords(extractKeywords(result))
                        .cited_by(extractCitedBy(result))
                        .authors(researcherName)
                        .build();

                articles.add(article);

            } catch (Exception e) {
                log.warn("Error processing result: {}", e.getMessage());
            }
        }

        return articles;
    }

    /**
     * Saves articles to the database using Hibernate
     */
    private List<Article> saveArticles(List<Article> articles, String researcherName) {
        List<Article> savedArticles = new ArrayList<>();

        try {
            for (Article article : articles) {
                if (!articleRepository.existsByAuthors(article.getId(), researcherName)) {
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
     * Retrieves all articles from the database
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

    private String extractTitle(ArticleResponseDTO.OrganicResult result) {
        return result.getTitle() != null ? result.getTitle() : "Untitled";
    }

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

    private LocalDate extractPublicationDate(ArticleResponseDTO.OrganicResult result) {
        try {
            if (result.getPublicationInfo() != null) {
                String summary = result.getPublicationInfo().getSummary();
                if (summary != null) {
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

    private String extractAbstract(ArticleResponseDTO.OrganicResult result) {
        String snippet = result.getSnippet();
        if (snippet != null && !snippet.isEmpty()) {
            return snippet.length() > 1000 ? snippet.substring(0, 1000) + "..." : snippet;
        }
        return "No abstract available";
    }

    private String extractLink(ArticleResponseDTO.OrganicResult result) {
        return result.getLink() != null ? result.getLink() : "https://scholar.google.com";
    }

    private String extractKeywords(ArticleResponseDTO.OrganicResult result) {
        String snippet = result.getSnippet();
        if (snippet != null && !snippet.isEmpty()) {
            String[] words = snippet.split("\\s+");
            int maxWords = Math.min(5, words.length);
            List<String> keywords = new ArrayList<>();
            for (int i = 0; i < maxWords; i++) {
                String word = words[i].replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                if (word.length() > 3) {
                    keywords.add(word);
                }
            }
            return String.join(", ", keywords);
        }
        return null;
    }

    private Integer extractCitedBy(ArticleResponseDTO.OrganicResult result) {
        if (result.getInlineLinks() != null &&
                result.getInlineLinks().getCitedBy() != null) {
            return result.getInlineLinks().getCitedBy().getTotal();
        }
        return 0;
    }
}
