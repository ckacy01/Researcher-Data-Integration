package org.technoready.researcherdataintegration.models;

import java.util.List;

import lombok.Data;

@Data
public class Author {
    private String name;
    private String affiliations;
    private String email;
    private List<Interest> interests;
    private String thumbnail;
}
