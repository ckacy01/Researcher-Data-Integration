package org.technoready.researcherdataintegration.exception;

/**
 * Custom runtime exception for database operation errors.
 * Thrown when database operations fail, such as connection issues, query errors, or transaction failures.
 * DATE: 08 - October - 2025
 *
 * This exception extends RuntimeException to allow unchecked exception handling.
 * It should be caught by the global exception handler for consistent error responses.
 *
 * Common scenarios:
 * - Database connection failures
 * - SQL query execution errors
 * - Transaction commit/rollback failures
 * - Data integrity violations
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
public class DatabaseException extends RuntimeException {

    /**
     * Constructs a new DatabaseException with the specified error message.
     *
     * @param message String - Detailed message describing the database error
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new DatabaseException with the specified error message and cause.
     * Used when wrapping underlying database exceptions to preserve the stack trace.
     *
     * @param message String - Detailed message describing the database error
     * @param cause Throwable - The underlying exception that caused this error
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}