package com.jwt.security.controller;

import com.jwt.security.model.starwars.*;
import com.jwt.security.service.StarWarsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/star-wars")
public class StarWarsController {

    private final StarWarsService starWarsService;

    public StarWarsController(StarWarsService starWarsService) {
        this.starWarsService = starWarsService;
    }

    @GetMapping
    public ResponseEntity<Object> getStarWars(@RequestParam(required = true) String uri, @RequestParam(required = true) int id) {

        Class<?> responseType = switch (uri.toLowerCase()) {
            case "films" -> Film.class;
            case "people" -> People.class;
            case "planets" -> Planet.class;
            case "species" -> Specie.class;
            case "starships" -> Starship.class;
            case "vehicles" -> Vehicle.class;
            default -> throw new IllegalStateException("Unexpected value: " + uri);
        };

        Object result = this.starWarsService.requestInfo(uri, id, responseType);

        return ResponseEntity.ok(result);
    }

}
