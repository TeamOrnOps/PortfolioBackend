package org.ek.portfoliobackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// ---- CHECKS EVERY HTTP-REQUEST FROM CLIENT FOR JWT-TOKEN ----
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenGenerator jwtUtil;

    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenGenerator jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            // Extract token
            String jwt = header.substring("Bearer ".length()); // Fjerner "Bearer "

            // Extract username from token
            String username = jwtUtil.extractUsername(jwt);

            // Load user fra UserDetails
            UserDetails user = userDetailsService.loadUserByUsername(username);

            // Create Authentication object
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            // Store Authentication in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        // Continue filter chain with or without token
        filterChain.doFilter(request, response);
    }
}
