package dev.hust.simpleasia.port;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface RestTemplateClient {
    <T> ResponseEntity<T> get(String url, Class<T> responseType, HttpHeaders headers, Object... uriVariables);

    <T> ResponseEntity<T> post(String url, Class<T> responseType, Object body, HttpHeaders headers, Object... uriVariables);

    <T> ResponseEntity<T> delete(String url, Class<T> responseType, HttpHeaders headers, Object... uriVariables);

    <T> ResponseEntity<T> put(String url, Class<T> responseType, Object body, HttpHeaders headers, Object... uriVariables);

    <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType, HttpHeaders headers, Object... uriVariables);

    <T> ResponseEntity<T> post(String url, ParameterizedTypeReference<T> responseType, Object body, HttpHeaders headers, Object... uriVariables);

    <T> ResponseEntity<T> delete(String url, ParameterizedTypeReference<T> responseType, HttpHeaders headers, Object... uriVariables);

    <T> ResponseEntity<T> put(String url, ParameterizedTypeReference<T> responseType, Object body, HttpHeaders headers, Object... uriVariables);
}
