package com.jwt.security.controller;

import com.jwt.security.request.AuthenticationRequest;
import com.jwt.security.request.RegisterRequest;
import com.jwt.security.response.AuthenticationResponse;
import com.jwt.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth security service", description = "Allow to register a new user and login into the app")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @Operation(summary = "Register a new user", description = "Register a new user and create the token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User registered",
                    content = @Content(
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }


    @Operation(summary = "Login user", description = "The user can login")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User authenticated",
                    content = @Content(
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )
            )
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }


}
