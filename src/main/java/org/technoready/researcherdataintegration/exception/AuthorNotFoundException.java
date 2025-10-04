package org.technoready.researcherdataintegration.exception;

/**
 * Custom exception thrown when an author with the specified ID cannot be found.
 * This exception is typically used to signal that no data was retrieved from the data source (SerpAPI).
 * DATE: 04 - October - 2025
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
public class AuthorNotFoundException extends RuntimeException {

    /**
     * Constructs a new AuthorNotFoundException with a detailed message
     * including the given author ID.
     *
     * @param authorId String - The ID of the author that was not found
     */
    public AuthorNotFoundException(String authorId) {
        super("Could not find author with id: " + authorId);
    }
}

