package org.technoready.researcherdataintegration.dto;

import lombok.Data;

import java.util.List;

@Data
public class ImportRequestDTO {
    private List<String> researchers;
    private Integer articlesPerResearcher = 3;
}