package org.technoready.researcherdataintegration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.technoready.researcherdataintegration.entity.Article;

import java.util.List;

public interface ArticleRepository  extends JpaRepository<Article, String> {

    List<Article> findByResearcherName(String ResearcherName);
    boolean existsByIdAndResearcherName(String id, String researcherName);
}
