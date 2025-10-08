package org.technoready.researcherdataintegration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.technoready.researcherdataintegration.dto.ImportRequestDTO;
import org.technoready.researcherdataintegration.dto.ImportResponseDTO;
import org.technoready.researcherdataintegration.entity.Article;
import org.technoready.researcherdataintegration.service.ArticleService;

import java.util.List;

/**
 * REST Controller responsible for managing article-related operations.
 * Provides endpoints for retrieving stored articles and importing new articles from researchers.
 * DATE: 08 - October - 2025
 *
 * This controller exposes two main endpoints:
 * - GET /api/article/articles: Retrieves all articles stored in the database
 * - POST /api/article/import: Imports articles for specified researchers
 *
 * Base URL: /api/article
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    /**
     * Retrieves all articles stored in the database.
     *
     * Endpoint: GET /api/article/articles
     * Example: GET http://localhost:8080/api/article/articles
     *
     * @return ResponseEntity<List<Article>> - HTTP 200 OK with list of all articles
     */
    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        log.info("GET /api/article/articles - Requesting all articles");

        List<Article> articles = articleService.getAllArticles();

        log.info("Returning {} articles", articles.size());
        return ResponseEntity.ok(articles);
    }

    /**
     * Imports articles for specified researchers and stores them in the database.
     *
     * Endpoint: POST /api/article/import
     * Example: POST http://localhost:8080/api/article/import
     *
     * Request Body example:
     * {
     *   "researchers": ["Andrew Ng", "Geoffrey Hinton"],
     *   "articlesPerResearcher": 3
     * }
     *
     * CURL example:
     * curl -X POST http://localhost:8080/api/article/import \
     *   -H "Content-Type: application/json" \
     *   -d '{"researchers":["Andrew Ng"],"articlesPerResearcher":3}'
     *
     * @param request ImportRequestDTO - Contains list of researcher names and articles per researcher
     * @return ResponseEntity<ImportResponseDTO> - HTTP 201 CREATED with import summary and article list
     */
    @PostMapping("/import")
    public ResponseEntity<ImportResponseDTO> importArticles(@RequestBody ImportRequestDTO request) {
        log.info("POST /api/article/import - Starting importation");
        log.info("Investigators: {}", request.getResearchers());
        log.info("Articles per investigator: {}", request.getArticlesPerResearcher());

        // Import and obtain the articles from external API
        List<Article> savedArticles = articleService.importArticlesForResearchers(
                request.getResearchers(),
                request.getArticlesPerResearcher()
        );

        // Build the response DTO with importation summary
        ImportResponseDTO response = ImportResponseDTO.builder()
                .status("success")
                .message("Articles imported successfully")
                .totalResearchers(request.getResearchers().size())
                .articlesPerResearcher(request.getArticlesPerResearcher())
                .totalArticlesImported(savedArticles.size())
                .articles(savedArticles)
                .build();

        log.info("Importation completed: {} articles stored", savedArticles.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}