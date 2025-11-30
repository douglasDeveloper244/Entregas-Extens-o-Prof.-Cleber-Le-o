
package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.*;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Autenticação", description = "Endpoints para autenticação e gestão de tokens. Utilize /login para obter o Bearer Token necessário para acessar endpoints protegidos.")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private com.deliverytech.delivery_api.repository.ClienteRepository clienteRepository;

    @io.swagger.v3.oas.annotations.Operation(summary = "Realizar login", description = "Autentica um usuário e retorna tokens de acesso e refresh")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getSenha()));
            Usuario user = (Usuario) auth.getPrincipal();
            String token = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            return ResponseEntity.ok(new LoginResponse(token, refreshToken, "Bearer",
                    jwtUtil.extractClaim(token, c -> c.getExpiration().getTime()), new UserResponse(user)));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Atualizar token", description = "Gera um novo token de acesso usando um refresh token válido")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token atualizado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh token inválido ou expirado")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest req) {
        String refreshToken = req.getRefreshToken();
        try {
            String username = jwtUtil.extractUsername(refreshToken);
            if (username != null) {
                var userOpt = usuarioRepository.findByEmail(username);
                if (userOpt.isPresent()) {
                    Usuario user = userOpt.get();
                    if (jwtUtil.validateTokenType(refreshToken, user, "refresh")) {
                        String newToken = jwtUtil.generateToken(user);
                        return ResponseEntity.ok(new LoginResponse(newToken, refreshToken, "Bearer",
                                jwtUtil.extractClaim(newToken, c -> c.getExpiration().getTime()),
                                new UserResponse(user)));
                    }
                }
            }
        } catch (Exception e) {
            // Token invalid or expired
        }
        return ResponseEntity.status(401).body("Invalid refresh token");
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário (Cliente ou Restaurante)")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos ou email já cadastrado")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }
        Usuario u = new Usuario();
        u.setNome(req.getNome());
        u.setEmail(req.getEmail());
        u.setSenha(passwordEncoder.encode(req.getSenha()));
        String roleStr = req.getRole();
        if ("DONO".equalsIgnoreCase(roleStr)) {
            roleStr = "RESTAURANTE";
        }
        try {
            u.setRole(Role.valueOf(roleStr));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Role inválida: " + req.getRole());
        }
        u.setAtivo(true);
        u.setRestauranteId(req.getRestauranteId());
        Usuario savedUser = usuarioRepository.save(u);

        if (u.getRole() == Role.CLIENTE) {
            com.deliverytech.delivery_api.entity.Cliente cliente = new com.deliverytech.delivery_api.entity.Cliente();
            cliente.setNome(u.getNome());
            cliente.setEmail(u.getEmail());
            cliente.setAtivo(true);
            cliente.setDataCadastro(java.time.LocalDateTime.now());
            clienteRepository.save(cliente);
        }

        return ResponseEntity.status(201).body(new UserResponse(savedUser));
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Obter usuário atual", description = "Retorna os dados do usuário logado")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dados do usuário retornados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Não autenticado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).build();
        var opt = usuarioRepository.findByEmail(principal.getName());
        return opt.map(u -> ResponseEntity.ok(new UserResponse(u))).orElseGet(() -> ResponseEntity.status(404).build());
    }
}
