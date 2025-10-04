package org.technoready.researcherdataintegration.exception;

/**
 * Custom exception class used to indicate issues related to API Key validation or authorization.
 * This exception is typically thrown when an API key is missing, invalid, or unauthorized.
 * DATE: 04 - October - 2025
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
public class ApiKeyException extends RuntimeException {

    /**
     * Constructor that accepts a specific error message related to the API Key issue.
     *
     * @param message String - The error message to be associated with the exception
     */
    public ApiKeyException(String message) {
        super(message);
    }
}

