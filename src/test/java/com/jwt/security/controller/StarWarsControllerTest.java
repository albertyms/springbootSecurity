package com.jwt.security.controller;

import com.jwt.security.client.RestClient;
import com.jwt.security.config.JwtAuthenticationFilter;
import com.jwt.security.config.JwtService;
import com.jwt.security.model.starwars.*;
import com.jwt.security.model.token.Token;
import com.jwt.security.repository.TokenRepository;
import com.jwt.security.service.StarWarsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StarWarsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestClient restClient;

    @MockitoBean
    private StarWarsService starWarsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String baseUrl = "https://swapi.dev/api/";

    @BeforeEach
    void setup() {
        starWarsService = new StarWarsService(restClient);
        starWarsService.url = baseUrl;
    }

    @Test
    void testGetStarWarsWithFilmsUri() throws Exception {
        Film responseFilm = Film.builder()
                .title("A New Hope")
                .build();

        UserDetails mockUserDetails = org.springframework.security.core.userdetails.User
                .withUsername("test_user")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        when(jwtService.isTokenValid("testToken", mockUserDetails)).thenReturn(true);
        when(jwtService.extractUsername("testToken")).thenReturn("test_user");
        when(tokenRepository.findByToken("testToken")).thenReturn(Optional.of(
                Token.builder()
                        .token("testToken")
                        .revoked(false)
                        .expired(false)
                        .build()
        ));

        when(starWarsService.requestInfo("films", 1, Film.class)).thenReturn(responseFilm);
        when(restClient.sendRequest(baseUrl + "/films/1", HttpMethod.GET, Film.class)).thenReturn(responseFilm);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/star-wars")
                        .header("Authorization", "Bearer testToken")
                        .param("uri", "films")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.title").value("A New Hope"));
    }

    @Test
    void testGetStarWarsWithPeopleUri() throws Exception {
        People mockPeople = new People();
        mockPeople.setName("Luke Skywalker");

        UserDetails mockUserDetails = org.springframework.security.core.userdetails.User
                .withUsername("test_user")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        when(jwtService.isTokenValid("testToken", mockUserDetails)).thenReturn(true);
        when(jwtService.extractUsername("testToken")).thenReturn("test_user");
        when(tokenRepository.findByToken("testToken")).thenReturn(Optional.of(
                Token.builder()
                        .token("testToken")
                        .revoked(false)
                        .expired(false)
                        .build()
        ));

        when(restClient.sendRequest(baseUrl + "/films/1", HttpMethod.GET, People.class)).thenReturn(mockPeople);

        when(starWarsService.requestInfo("people", 1, People.class)).thenReturn(mockPeople);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/star-wars")
                        .param("uri", "people")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.name").value("Luke Skywalker"));
    }

//    @Test
//    void testGetStarWarsWithInvalidUri() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/star-wars")
//                        .param("uri", "invalid")
//                        .param("id", "1")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    void testGetStarWarsWithStarshipsUri() throws Exception {
//        Starship mockStarship = new Starship();
//        mockStarship.setName("Millennium Falcon");
//
//        when(starWarsService.requestInfo("starships", 1, Starship.class)).thenReturn(mockStarship);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/star-wars")
//                        .param("uri", "starships")
//                        .param("id", "1")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Millennium Falcon"));
//    }
//
//    @Test
//    void testGetStarWarsWithVehiclesUri() throws Exception {
//        Vehicle mockVehicle = new Vehicle();
//        mockVehicle.setName("Speeder");
//
//        when(starWarsService.requestInfo("vehicles", 1, Vehicle.class)).thenReturn(mockVehicle);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/star-wars")
//                        .param("uri", "vehicles")
//                        .param("id", "1")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Speeder"));
//    }
//
//    @Test
//    void testGetStarWarsWithPlanetsUri() throws Exception {
//        Planet mockPlanet = new Planet();
//        mockPlanet.setName("Tatooine");
//
//        when(starWarsService.requestInfo("planets", 1, Planet.class)).thenReturn(mockPlanet);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/star-wars")
//                        .param("uri", "planets")
//                        .param("id", "1")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Tatooine"));
//    }
//
//    @Test
//    void testGetStarWarsWithSpeciesUri() throws Exception {
//        Specie mockSpecie = new Specie();
//        mockSpecie.setName("Wookiee");
//
//        when(starWarsService.requestInfo("species", 1, Specie.class)).thenReturn(mockSpecie);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/star-wars")
//                        .param("uri", "species")
//                        .param("id", "1")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Wookiee"));
//    }
}