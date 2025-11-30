package org.technoready.researcherdataintegration.dto;

import lombok.Data;

import java.util.List;
/**
 * Data Transfer Object for article import requests.
 * Contains the list of researcher names and the number of articles to import per researcher.
 * DATE: 08 - October - 2025
 *
 * This DTO is used in the POST /api/article/import endpoint to specify which researchers'
 * articles should be imported and how many articles to retrieve for each researcher.
 *
 * Example JSON:
 * {
 *   "researchers": ["Andrew Ng", "Geoffrey Hinton", "Yann LeCun"],
 *   "articlesPerResearcher": 5
 * }
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
@Data
public class ImportRequestDTO {
    private List<String> researchers;
    private Integer articlesPerResearcher = 3;
}