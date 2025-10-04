package org.technoready.researcherdataintegration.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Model representing a standardized error response structure for the API.
 * This object is used in exception handlers to return consistent and informative error messages to clients.
 * DATE: 04 - October - 2025
 *
 * Fields include HTTP status code, message, detailed error description, timestamp, and request path.
 *
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */

@Data
@NoArgsConstructor
@Builder
public class Error {
    private int code;
    private String message;
    private String error;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String path;

    public Error(int code, String message, String error, LocalDateTime timestamp, String path) {
        this.code = code;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

}
