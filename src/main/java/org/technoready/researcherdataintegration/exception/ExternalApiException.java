package org.technoready.researcherdataintegration.exception;

/**
 * Custom exception thrown when an error occurs while communicating with an external API.
 * This may include network errors, invalid responses, timeouts, or unexpected data formats.
 * DATE: 04 - October - 2025
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
public class ExternalApiException extends RuntimeException {

    /**
     * Constructs a new ExternalApiException with a specified error message.
     *
     * @param message String - The detail message describing the error
     */
    public ExternalApiException(String message) {
        super(message);
    }

    /**
     * Constructs a new ExternalApiException with a specified error message and cause.
     *
     * @param message String - The detail message describing the error
     * @param cause   Throwable - The cause of the exception (e.g., a lower-level exception)
     */
    public ExternalApiException(String message, Throwable cause) {
    }
}
