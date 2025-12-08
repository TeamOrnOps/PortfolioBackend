package org.ek.portfoliobackend.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;


// ---- GENERATE TOKEN ----
@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String secret;

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .claim("roles", user.getAuthorities())
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token expires after 24 hours
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractsAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

    }
}
