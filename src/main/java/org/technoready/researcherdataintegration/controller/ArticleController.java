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

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    /**
     * GET - obtain all the articles that are stored in the database
     * GET http://localhost:8080/api/article/articles
     */
    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        log.info("GET /api/scholar/articles - Requesting all articles");

        List<Article> articles = articleService.getAllArticles();

        log.info("Returning {} articles", articles.size());
        return ResponseEntity.ok(articles);
    }

    /**
     * POST - Import articles and stored in the DB
     * POST http://localhost:8080/api/article/import
     * Body: {
     *   "researchers": ["Andrew Ng", "Geoffrey Hinton"],
     *   "articlesPerResearcher": 3
     * }
     * You can use CURL to try this
     */
    @PostMapping("/import")
    public ResponseEntity<ImportResponseDTO> importArticles(@RequestBody ImportRequestDTO request) {
        log.info("POST /api/article/import - Starting importation");
        log.info("Investigators: {}", request.getResearchers());
        log.info("Articles per investigator: {}", request.getArticlesPerResearcher());

        // Import and obtain the articles
        List<Article> savedArticles = articleService.importArticlesForResearchers(
                request.getResearchers(),
                request.getArticlesPerResearcher()
        );

        // Build the response
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
