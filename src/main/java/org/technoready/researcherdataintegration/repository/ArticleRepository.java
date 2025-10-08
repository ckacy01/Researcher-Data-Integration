package org.technoready.researcherdataintegration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.technoready.researcherdataintegration.entity.Article;

import java.util.List;

public interface ArticleRepository  extends JpaRepository<Article, String> {

    List<Article> findByResearcherName(String ResearcherName);
    boolean existsByResearcherName(String id, String researcherName);
}
