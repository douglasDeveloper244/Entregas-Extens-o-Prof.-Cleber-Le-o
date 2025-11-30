package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.LoginRequest;
import com.deliverytech.delivery_api.dto.RegisterRequest;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private AuthenticationManager authenticationManager;

        @MockBean
        private UsuarioRepository usuarioRepository;

        @MockBean
        private JwtUtil jwtUtil;

        @MockBean
        private PasswordEncoder passwordEncoder;

        @MockBean
        private com.deliverytech.delivery_api.repository.ClienteRepository clienteRepository;

        private Usuario usuario;

        @BeforeEach
        void setUp() {
                usuario = new Usuario();
                usuario.setId(1L);
                usuario.setEmail("test@example.com");
                usuario.setSenha("encodedPassword");
                usuario.setNome("Test User");
                usuario.setRole(Role.CLIENTE);
        }

        @Test
        void testLogin_Success() throws Exception {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("test@example.com");
                loginRequest.setSenha("password");

                Authentication authentication = Mockito.mock(Authentication.class);
                Mockito.when(authentication.getPrincipal()).thenReturn(usuario);
                Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(authentication);

                Mockito.when(jwtUtil.generateToken(any(Usuario.class))).thenReturn("mockedToken");
                Mockito.when(jwtUtil.generateRefreshToken(any(Usuario.class))).thenReturn("mockedRefreshToken");
                Mockito.when(jwtUtil.extractClaim(any(), any())).thenReturn(3600000L);

                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value("mockedToken"))
                                .andExpect(jsonPath("$.refreshToken").value("mockedRefreshToken"))
                                .andExpect(jsonPath("$.user.email").value("test@example.com"));
        }

        @Test
        void testRegister_Success() throws Exception {
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setNome("New User");
                registerRequest.setEmail("new@example.com");
                registerRequest.setSenha("password");
                registerRequest.setRole("CLIENTE");

                Mockito.when(usuarioRepository.existsByEmail("new@example.com")).thenReturn(false);
                Mockito.when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

                Usuario savedUser = new Usuario();
                savedUser.setId(2L);
                savedUser.setNome("New User");
                savedUser.setEmail("new@example.com");
                savedUser.setRole(Role.CLIENTE);

                Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUser);

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.email").value("new@example.com"))
                                .andExpect(jsonPath("$.role").value("CLIENTE"));
        }

        @Test
        void testRegister_EmailAlreadyExists() throws Exception {
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setEmail("test@example.com");

                Mockito.when(usuarioRepository.existsByEmail("test@example.com")).thenReturn(true);

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                                .andExpect(status().isBadRequest());
        }
}
