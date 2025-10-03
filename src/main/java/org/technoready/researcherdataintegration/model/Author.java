package org.technoready.researcherdataintegration.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Author(Author author) {
        this.name = author.getName();
        this.affiliations = author.getAffiliations();
        this.email = author.getEmail();
        this.interests = author.getInterests();
        this.thumbnail = author.getThumbnail();
    }
}
