package org.ek.portfoliobackend.service.impl;

import org.ek.portfoliobackend.security.JwtTokenGenerator;
import org.ek.portfoliobackend.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenGenerator jwtTokenGenerator) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Override
    public String login(String username, String password) {

        // 1. Authenticate user (Spring Security handles password check)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 2. Extract authenticated user
        UserDetails user = (UserDetails) authentication.getPrincipal();

        // 3. Generate JWT token
        return jwtTokenGenerator.generateToken(user);
    }

}

