package com.jwt.security.client;

import com.jwt.security.exception.ResourceNotFoundException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class RestClient {

    private final WebClient webClient;

    public RestClient(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create()
                .followRedirect(true); // Enable redirect handling

        this.webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public <T> T sendRequest(String url, HttpMethod method, Class<T> responseType) {
        try {
            return this.webClient
                    .method(method)
                    .uri(url)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404,
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new ResourceNotFoundException("Resource not found at URL: " + url)))
                    )
                    .onStatus(
                            HttpStatusCode::isError,
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new RuntimeException(
                                            "HTTP Error: " + clientResponse.statusCode() + " - " + errorBody
                                    )))
                    )
                    .bodyToMono(responseType)
                    .block();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Unexpected error occurred while sending request: " + e.getMessage(), e);
        }
    }



}
