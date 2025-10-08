package org.technoready.researcherdataintegration.exception;
/**
 * Custom runtime exception for data validation errors.
 * Thrown when input data fails validation rules or business logic constraints.
 * DATE: 08 - October - 2025
 *
 * This exception extends RuntimeException to allow unchecked exception handling.
 * It should be caught by the global exception handler for consistent error responses.
 *
 * Common scenarios:
 * - Invalid request parameters orDTO fields
 * - Business rule violations
 * - Empty or null required fields
 * - Invalid data format or range values
 * - Constraint validation failures
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
    public ValidationException(String message, Throwable cause) {}
}
