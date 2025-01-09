package com.jwt.security.service;

import com.jwt.security.client.RestClient;
import com.jwt.security.exception.ResourceNotFoundException;
import com.jwt.security.model.starwars.People;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class StarWarsServiceTest {

    @Mock
    private RestClient restClient;


    private StarWarsService starWarsService;

    private final String baseUrl = "https://swapi.dev/api";

    @BeforeEach
    void setup() {
        starWarsService = new StarWarsService(restClient);
        starWarsService.url = baseUrl;
    }

    @Test
    void sendRequest_shouldReturnResponse_whenValidRequest() {
        String uri = "/people";
        int id = 1;
        People response = People.builder()
                .name( "Luke Skywalker")
                .gender("male")
                .hairColor("blond")
                .skinColor("fair")
                .eyeColor("blue")
                .birthYear("19BBY")
                .build();
        String endpoint = baseUrl + uri + "/" + id;

        when(restClient.sendRequest(endpoint, HttpMethod.GET, People.class)).thenReturn(response);

        Class<?> responseType = People.class;
        People result = (People) starWarsService.requestInfo(uri, id, responseType);

        verify(restClient, times(1)).sendRequest(endpoint, HttpMethod.GET, People.class);
        assertEquals(response, result);
    }

    @Test
    void sendRequest_shouldThrowResourceNotFoundException_whenResourceNotFound() {
        String uri = "/people";
        int id = 99;
        String endpoint = baseUrl + uri + "/" + id;
        Class<?> responseType = People.class;
        when(restClient.sendRequest(endpoint, HttpMethod.GET, responseType)).thenThrow(new ResourceNotFoundException("Not Found"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> starWarsService.requestInfo(uri, id, responseType)
        );

        assertEquals(
                String.format("The requested /people with ID %s could not be found.", id),
                exception.getMessage()
        );

        verify(restClient, times(1)).sendRequest(endpoint, HttpMethod.GET, responseType);
    }

    @Test
    void sendRequest_shouldThrowRuntimeException_whenUnexpectedErrorOccurs() {
        String uri = "/planets";
        int id = 3;
        String endpoint = baseUrl + uri + "/" + id;
        Class<?> responseType = People.class;
        when(restClient.sendRequest(endpoint, HttpMethod.GET, responseType)).thenThrow(new RuntimeException("Unexpected"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> starWarsService.requestInfo(uri, id, responseType)
        );

        assertEquals("An unexpected error occurred: Unexpected", exception.getMessage());

        verify(restClient, times(1)).sendRequest(endpoint, HttpMethod.GET, responseType);
    }
}