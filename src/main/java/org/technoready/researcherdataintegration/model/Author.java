package org.technoready.researcherdataintegration.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model representing an academic author retrieved from an external API (SerpAPI).
 * Includes personal and professional details such as name, affiliation, email, and interests.
 * DATE: 04 - October - 2025
 *
 * Fields are mapped from JSON properties using Jackson annotations.
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @JsonProperty("name")
    private String name;

    @JsonProperty("affiliations")
    private String affiliations;

    @JsonProperty("email")
    private String email;

    @JsonProperty("interests")
    private List<Interest> interests;

    @JsonProperty("thumbnail")
    private String thumbnail;

    /**
     * Copy constructor to create a new instance based on an existing Author object.
     *
     * @param author Author - The author object to copy
     */
    public Author(Author author) {
        this.name = author.getName();
        this.affiliations = author.getAffiliations();
        this.email = author.getEmail();
        this.interests = author.getInterests();
        this.thumbnail = author.getThumbnail();
    }
}
