package org.technoready.researcherdataintegration.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.technoready.researcherdataintegration.config.ScholarApiConfiguration;
import org.technoready.researcherdataintegration.model.Author;
import org.technoready.researcherdataintegration.model.AuthorSearchResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {
    private final RestTemplate restTemplate;
    private final ScholarApiConfiguration config;

    public Author getAuthor(String authorId) {
        String url = String.format( config.getBaseUrl() + "?engine=google_scholar_author&author_id=%s&api_key=%s",
                authorId, config.getApiKey());
        log.info("Getting author with id {}", authorId);
        AuthorSearchResponse response = restTemplate.getForObject(url, AuthorSearchResponse.class);
        return response != null ? response.getAuthor() : null;
    }
}
