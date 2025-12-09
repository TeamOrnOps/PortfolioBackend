package org.ek.portfoliobackend.security;

import org.ek.portfoliobackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JwtTokenGeneratorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    // Helper: generate ADMIN token
    private String generateAdminToken() {
        UserDetails admin = User.withUsername("admin")
                .password("testpassword")
                .roles("ADMIN")
                .build();

        return jwtTokenGenerator.generateToken(admin);
    }


    // ---- No token = forbidden access ----
    @Test
    void whenNoTokenOnAdminEndpoint_thenUnauthorized() throws Exception {
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"title":"Test","description":"Test"}
                        """))
                .andExpect(status().isForbidden());
    }




}
