package org.technoready.researcherdataintegration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.technoready.researcherdataintegration.model.Author;
import org.technoready.researcherdataintegration.service.AuthorService;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/{authorId}")
    public Author getAuthor(@PathVariable String authorId) {
        return authorService.getAuthor(authorId);
    }
}

