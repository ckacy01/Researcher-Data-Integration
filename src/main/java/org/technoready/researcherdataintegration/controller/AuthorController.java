package org.technoready.researcherdataintegration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.technoready.researcherdataintegration.model.Author;
import org.technoready.researcherdataintegration.service.AuthorService;

/**
 * Class that currently has only one Endpoint (GET)
 * DATE: 04 - October - 2025
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */


@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    /**
     * To get only the author object using the endpoint of SerpApi through authorid
     *
     * @param authorId String - The authorId to search in SerpAPI
     * @return Author - The JSON of the author
     *
     */

    @GetMapping("/{authorId}")
    public Author getAuthor(@PathVariable String authorId) {
        return authorService.getAuthor(authorId);
    }
}

