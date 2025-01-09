package com.jwt.security.service;

import com.jwt.security.client.RestClient;
import com.jwt.security.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class StarWarsService {

    private final RestClient restClient;

    public StarWarsService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Value("${service.rest.starwars.url}")
    public String url;

    public <T> T requestInfo(String uri, int id, Class<T> responseType) {
        String endpoint = url + uri + "/" + id;
        try {
            return this.restClient.sendRequest(endpoint, HttpMethod.GET, responseType);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("The requested " + uri + " with ID " + id + " could not be found.");
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }


}
