package org.technoready.researcherdataintegration.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Model representing a research interest or topic associated with an author.
 * Typically part of the author's profile returned by an external API like SerpAPI.
 * DATE: 04 - October - 2025
 *
 * Each interest may contain a title, a link to more information, and a direct SerpAPI link.
 *
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */

@Getter
@Setter
@AllArgsConstructor
public class Interest {
    private String title;
    private String link;
    private String serpapi_link;
}
