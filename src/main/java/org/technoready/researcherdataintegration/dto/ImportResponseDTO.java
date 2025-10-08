package org.technoready.researcherdataintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.technoready.researcherdataintegration.entity.Article;

import java.util.List;
/**
 * Data Transfer Object for article import responses.
 * Contains the import operation summary and list of imported articles.
 * DATE: 08 - October - 2025
 *
 * This DTO is returned by the POST /api/article/import endpoint after successfully
 * importing articles from researchers. Provides detailed information about the import
 * operation including status, counts, and the actual articles imported.
 *
 * Example JSON response:
 * {
 *   "status": "success",
 *   "message": "Articles imported successfully",
 *   "totalResearchers": 2,
 *   "articlesPerResearcher": 3,
 *   "totalArticlesImported": 6,
 *   "articles": [...]
 * }
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResponseDTO {
    private String status;
    private String message;
    private Integer totalResearchers;
    private Integer articlesPerResearcher;
    private Integer totalArticlesImported;
    private List<Article> articles;
}
