package org.technoready.researcherdataintegration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.technoready.researcherdataintegration.entity.Article;

import java.util.List;
/**
 * JPA Repository interface for Article entity database operations.
 * Provides CRUD operations and custom query methods for Article entities.
 * DATE: 08 - October - 2025
 *
 * This repository extends JpaRepository to inherit standard database operations
 * (save, findById, findAll, delete, etc.) and defines custom query methods using
 * Spring Data JPA naming conventions.
 *
 * Entity: Article
 * Primary Key Type: String
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
public interface ArticleRepository  extends JpaRepository<Article, String> {

    List<Article> findByResearcherName(String ResearcherName);
    boolean existsByIdAndResearcherName(String id, String researcherName);
}
