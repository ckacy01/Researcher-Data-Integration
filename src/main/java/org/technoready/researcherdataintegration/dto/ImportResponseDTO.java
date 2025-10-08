package org.technoready.researcherdataintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.technoready.researcherdataintegration.entity.Article;

import java.util.List;

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
