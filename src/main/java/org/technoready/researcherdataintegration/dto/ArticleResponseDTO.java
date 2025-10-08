package org.technoready.researcherdataintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleResponseDTO {

    @JsonProperty("search_metadata")
    private SearchMetadata searchMetadata;

    @JsonProperty("organic_results")
    private List<OrganicResult> organicResults;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchMetadata {
        private String status;

        @JsonProperty("created_at")
        private String createdAt;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrganicResult {

        @JsonProperty("result_id")
        private String resultId;

        private String title;

        private String snippet;

        private String link;

        @JsonProperty("publication_info")
        private PublicationInfo publicationInfo;

        @JsonProperty("inline_links")
        private InlineLinks inlineLinks;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PublicationInfo {
        private String summary;

        private List<Author> authors;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {
        private String name;

        @JsonProperty("author_id")
        private String authorId;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InlineLinks {

        @JsonProperty("cited_by")
        private CitedBy citedBy;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CitedBy {
        private Integer total;
    }
}
