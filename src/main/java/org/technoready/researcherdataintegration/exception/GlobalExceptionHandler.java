package org.technoready.researcherdataintegration.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.technoready.researcherdataintegration.model.Error;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AuthorNotFoundException.class)
    public ResponseEntity<Error> handleAuthorNotFoundException(AuthorNotFoundException ex, WebRequest request) {

        log.error("AuthorNotFoundException: Author Not Found", ex);

        Error error = Error.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Author Not Found")
                .error(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ExternalApiException.class)
    public ResponseEntity<Error> handleExternalApiException(ExternalApiException ex, WebRequest request) {

        log.error("ExternalApiException: External API Error", ex);

        Error error = Error.builder()
                .code(HttpStatus.BAD_GATEWAY.value())
                .message("Error Occurred trying to access external API")
                .error(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(value = ApiKeyException.class)
    public ResponseEntity<Error> handleApiKeyException(ApiKeyException ex, WebRequest request) {

        log.error("ApiKeyException: API Key", ex);

        Error error = Error.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Unauthorized")
                .error(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    public ResponseEntity<Error> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        log.error("HttpClientErrorException: HTTP Client Error", ex);

        String message = ex.getStatusCode().value() == 401 ?  "API Key not valid or expired" : "External API Error";

        Error error = Error.builder()
                .code(ex.getStatusCode().value())
                .message(message)
                .error(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatusCode().value()));
    }

    @ExceptionHandler(value = HttpServerErrorException.class)
    public ResponseEntity<Error> handleHttpServerErrorException(HttpServerErrorException ex, WebRequest request) {
        log.error("HttpServerErrorException: HTTP Server Error", ex);
        Error error = Error.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("SerpAPI isn't available in this moment please try again later")
                .error(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Error> handleException(Exception ex, WebRequest request) {
        log.error("Unexpected Error: ", ex);
        Error error = Error.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .error(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
