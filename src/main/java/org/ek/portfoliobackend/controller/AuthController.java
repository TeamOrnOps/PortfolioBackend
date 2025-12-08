package org.ek.portfoliobackend.controller;

import org.ek.portfoliobackend.dto.request.LoginRequest;
import org.ek.portfoliobackend.dto.response.LoginResponse;
import org.ek.portfoliobackend.service.AuthService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String token = authService.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(new LoginResponse(token));
    }


}
