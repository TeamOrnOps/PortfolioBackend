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

    @Value("${JWT_SECRET}")
    private String secret;


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes()); // Secret must be of 32 chars for HS256
    }

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .claim("roles", user.getAuthorities())
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token expires after 24 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // JJWT signing
                .compact();

    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

}
