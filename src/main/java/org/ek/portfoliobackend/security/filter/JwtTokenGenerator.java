package org.ek.portfoliobackend.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


// ---- GENERATE TOKEN ----
@Component
public class JwtTokenGenerator {

    @Value("${JWT_SECRET}") // Used for the server to sign and validate tokens
    private String secret;


    private Key getSigningKey() { // Converts the secret String into a secure HMAC-SHA-256 signing key
        return Keys.hmacShaKeyFor(secret.getBytes()); // Secret must be of 32 chars for HS256
    }

    // Token generator (creates the claims of a user)
    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .claim("roles", user.getAuthorities()) // Stores the user's authorities (ADMIN) inside the token
                .setSubject(user.getUsername())
                .setIssuedAt(new Date()) // Token generation date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token expires after 24 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signs the token with the HMAC-SHA256 algorithm
                .compact(); // Converts the token to a String for frontend

    }


    // Reads the token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // Parsing the token
                .setSigningKey(getSigningKey()) // Validates the signature
                .build()
                .parseClaimsJws(token) // Parses and validates the token
                .getBody();

    }

    // Gets the subject (user)
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Claim = subject (user), expiration time, issued at time and role

}
