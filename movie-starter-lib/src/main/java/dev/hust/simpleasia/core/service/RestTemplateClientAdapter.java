package dev.hust.simpleasia.core.service;

import dev.hust.simpleasia.port.RestTemplateClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RestTemplateClientAdapter implements RestTemplateClient {
    private final RestTemplate restTemplate;

    public RestTemplateClientAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, HttpHeaders headers, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> post(String url, Class<T> responseType, Object body, HttpHeaders headers, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, HttpHeaders headers, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(null, headers), responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> put(String url, Class<T> responseType, Object body, HttpHeaders headers, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body, headers), responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType, HttpHeaders headers, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> post(String url, ParameterizedTypeReference<T> responseType, Object body, HttpHeaders headers, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> delete(String url, ParameterizedTypeReference<T> responseType, HttpHeaders headers, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(null, headers), responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> put(String url, ParameterizedTypeReference<T> responseType, Object body, HttpHeaders headers, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body, headers), responseType, uriVariables);
    }
}
