package org.technoready.researcherdataintegration.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String authorId) {
        super("Could not find author with id: " + authorId);
    }

}
