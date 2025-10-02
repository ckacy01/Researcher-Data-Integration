package org.technoready.researcherdataintegration.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private String name;
    private String affiliations;
    private String email;
    private List<Interest> interests;
    private String thumbnail;

    public Author(Author author) {
        this.name = author.getName();
        this.affiliations = author.getAffiliations();
        this.email = author.getEmail();
        this.interests = author.getInterests();
        this.thumbnail = author.getThumbnail();
    }
}
