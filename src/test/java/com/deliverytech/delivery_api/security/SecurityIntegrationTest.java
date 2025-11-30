package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.dto.LoginRequest;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private JwtUtil jwtUtil;

        @BeforeEach
        void setUp() {
                // Ensure we have a clean state or known users
                if (!usuarioRepository.existsByEmail("security_test@example.com")) {
                        Usuario u = new Usuario();
                        u.setEmail("security_test@example.com");
                        u.setSenha(passwordEncoder.encode("password"));
                        u.setNome("Security Test");
                        u.setRole(Role.CLIENTE);
                        u.setAtivo(true);
                        usuarioRepository.save(u);
                }
        }

        @Test
        void testUnauthorizedAccess() throws Exception {
                mockMvc.perform(get("/api/pedidos"))
                                .andExpect(status().isForbidden());
        }

        @Test
        void testAuthorizedAccess() throws Exception {
                // Login to get token
                LoginRequest login = new LoginRequest();
                login.setEmail("security_test@example.com");
                login.setSenha("password");

                String response = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(login)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                String token = objectMapper.readTree(response).get("token").asText();

                // Access protected resource
                mockMvc.perform(get("/api/auth/me")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk());
        }

        @Test
        void testRefreshTokenFlow() throws Exception {
                // Login to get tokens
                LoginRequest login = new LoginRequest();
                login.setEmail("security_test@example.com");
                login.setSenha("password");

                String response = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(login)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                String refreshToken = objectMapper.readTree(response).get("refreshToken").asText();

                // Use refresh token to get new access token
                String refreshBody = "{\"refreshToken\": \"" + refreshToken + "\"}";

                mockMvc.perform(post("/api/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(refreshBody))
                                .andExpect(status().isOk());
        }

        @Test
        void testInvalidRefreshToken() throws Exception {
                String refreshBody = "{\"refreshToken\": \"invalid_token\"}";

                mockMvc.perform(post("/api/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(refreshBody))
                                .andExpect(status().isUnauthorized());
        }
}
