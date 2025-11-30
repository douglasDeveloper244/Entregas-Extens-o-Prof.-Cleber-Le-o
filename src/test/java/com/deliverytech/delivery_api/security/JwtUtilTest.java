package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

        private JwtUtil jwtUtil;
        private Usuario usuario;

        @BeforeEach
        void setUp() {
                // Mock properties
                String secret = "mySecretKeyMustBeLongEnoughToSatisfyTheRequirementsOfTheAlgorithm";
                long expiration = 3600000; // 1 hour
                long refreshExpiration = 7200000; // 2 hours

                jwtUtil = new JwtUtil(secret, expiration, refreshExpiration);

                usuario = new Usuario();
                usuario.setId(1L);
                usuario.setEmail("test@example.com");
                usuario.setRole(Role.CLIENTE);
                usuario.setRestauranteId(null);
        }

        @Test
        void testGenerateToken() {
                String token = jwtUtil.generateToken(usuario);
                assertNotNull(token);
                assertFalse(token.isEmpty());
        }

        @Test
        void testExtractUsername() {
                String token = jwtUtil.generateToken(usuario);
                String username = jwtUtil.extractUsername(token);
                assertEquals(usuario.getEmail(), username);
        }

        @Test
        void testExtractClaims() {
                String token = jwtUtil.generateToken(usuario);
                Long userId = jwtUtil.extractUserId(token);
                String role = jwtUtil.extractClaim(token, claims -> claims.get("role", String.class));

                assertEquals(usuario.getId(), userId);
                assertEquals(usuario.getRole().name(), role);
        }

        @Test
        void testValidateToken() {
                String token = jwtUtil.generateToken(usuario);
                boolean isValid = jwtUtil.validateToken(token, usuario);
                assertTrue(isValid);
        }

        @Test
        void testValidateToken_InvalidUser() {
                String token = jwtUtil.generateToken(usuario);
                Usuario otherUser = new Usuario();
                otherUser.setEmail("other@example.com");

                boolean isValid = jwtUtil.validateToken(token, otherUser);
                assertFalse(isValid);
        }

        @Test
        void testIsTokenExpired() {
                // Create a token that is already expired
                JwtUtil expiredUtil = new JwtUtil("mySecretKeyMustBeLongEnoughToSatisfyTheRequirementsOfTheAlgorithm",
                                -1000, -1000);
                String token = expiredUtil.generateToken(usuario);

                // validateToken checks expiration internally
                boolean isValid = expiredUtil.validateToken(token, usuario);
                assertFalse(isValid);
        }
}
