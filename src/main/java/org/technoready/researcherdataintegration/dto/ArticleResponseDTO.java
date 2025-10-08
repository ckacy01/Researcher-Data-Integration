package org.technoready.researcherdataintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for mapping article search responses from external API (SerpAPI).
 * Represents the complete response structure including search metadata and organic results.
 * DATE: 08 - October - 2025
 *
 * This DTO uses Jackson annotations to map JSON responses from SerpAPI's Google Scholar search.
 * Unknown JSON properties are ignored to maintain flexibility with API response changes.
 *
 * Response structure:
 * - Search metadata: Status and creation timestamp
 * - Organic results: List of articles with details, authors, and citations
 *
 * External API: https://serpapi.com/
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleResponseDTO {

    @JsonProperty("search_metadata")
    private SearchMetadata searchMetadata;

    @JsonProperty("organic_results")
    private List<OrganicResult> organicResults;

    /**
     * Inner class representing search metadata information from the API response.
     * Contains status and timestamp of the search operation.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchMetadata {

        /** Status of the search request (e.g., "Success") */
        private String status;

        /** ISO 8601 timestamp when the search was created */
        @JsonProperty("created_at")
        private String createdAt;
    }

    /**
     * Inner class representing a single article result from the search.
     * Contains all relevant article information including title, authors, and citations.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrganicResult {

        /** Unique identifier for the search result */
        @JsonProperty("result_id")
        private String resultId;

        /** Title of the article */
        private String title;

        /** Brief excerpt or abstract of the article */
        private String snippet;

        /** URL link to the article */
        private String link;

        /** Publication information including authors */
        @JsonProperty("publication_info")
        private PublicationInfo publicationInfo;

        /** Inline links including citation information */
        @JsonProperty("inline_links")
        private InlineLinks inlineLinks;
    }

    /**
     * Inner class representing publication information for an article.
     * Contains publication summary and list of authors.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PublicationInfo {

        /** Summary of publication details (e.g., journal, year) */
        private String summary;

        /** List of authors who contributed to the article */
        private List<Author> authors;
    }

    /**
     * Inner class representing an individual author of an article.
     * Contains author name and unique identifier.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {

        /** Full name of the author */
        private String name;

        /** Unique Google Scholar author identifier */
        @JsonProperty("author_id")
        private String authorId;
    }

    /**
     * Inner class representing inline links associated with an article.
     * Currently contains citation information.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InlineLinks {

        /** Citation information for the article */
        @JsonProperty("cited_by")
        private CitedBy citedBy;
    }

    /**
     * Inner class representing citation information for an article.
     * Contains the total number of times the article has been cited.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CitedBy {

        /** Total number of citations the article has received */
        private Integer total;
    }
}