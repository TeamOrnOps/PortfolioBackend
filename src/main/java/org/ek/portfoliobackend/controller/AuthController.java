package org.ek.portfoliobackend.controller;

import org.ek.portfoliobackend.security.filter.JwtTokenGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenGenerator jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenGenerator jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody A) {}

    Authentication authenticate(String username, String password) {}


}
