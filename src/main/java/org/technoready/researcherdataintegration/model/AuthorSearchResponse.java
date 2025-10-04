package org.technoready.researcherdataintegration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Model representing the response structure from the external API when searching for an author.
 * Typically used to deserialize the JSON response where the root object contains an "author" field.
 * DATE: 04 - October - 2025
 *
 * This model helps map nested JSON to Java objects via Jackson.
 *
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorSearchResponse {
    private Author author;
}
