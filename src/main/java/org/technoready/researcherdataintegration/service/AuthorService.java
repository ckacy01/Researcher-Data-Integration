package org.technoready.researcherdataintegration.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.technoready.researcherdataintegration.config.ScholarApiConfiguration;
import org.technoready.researcherdataintegration.exception.ApiKeyException;
import org.technoready.researcherdataintegration.exception.AuthorNotFoundException;
import org.technoready.researcherdataintegration.exception.ExternalApiException;
import org.technoready.researcherdataintegration.model.Author;
import org.technoready.researcherdataintegration.model.AuthorSearchResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {
    private final RestTemplate restTemplate;
    private final ScholarApiConfiguration config;

    public Author getAuthor(String authorId) {
        validateApiKey();
        String url = String.format( config.getBaseUrl() + "?engine=google_scholar_author&author_id=%s&api_key=%s",
                authorId, config.getApiKey());

        try{
            log.info("Getting author with id {}", authorId);
            AuthorSearchResponse response = restTemplate.getForObject(url, AuthorSearchResponse.class);

            if (response == null || response.getAuthor() == null) {
                throw new AuthorNotFoundException(authorId);
            }
            return response.getAuthor();

        }catch(HttpClientErrorException e){
            if(e.getStatusCode().value() == 401){
                throw new ApiKeyException("Invalid api key");
            }
            throw new ExternalApiException("Error consulting SerpAPI");
        }catch(HttpServerErrorException e){
            throw new ExternalApiException("Error consulting SerpAPI");
        }catch (Exception e){
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void validateApiKey() {
        if (config.getApiKey() == null || config.getApiKey().isEmpty()) {
            throw new ApiKeyException("API KEY IS NOT VALID");
        }
    }
}
