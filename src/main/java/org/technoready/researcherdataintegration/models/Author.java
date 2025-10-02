package org.technoready.researcherdataintegration.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Author {
    private String name;
    private String affiliations;
    private String email;
    private List<Interest> interests;
    private String thumbnail;
}
