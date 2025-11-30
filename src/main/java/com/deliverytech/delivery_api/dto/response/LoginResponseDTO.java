package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para resposta de login", title = "Login Response DTO")
public class LoginResponseDTO {
    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tipo;

    @Schema(description = "Tempo de expiração em milissegundos", example = "86400000")
    private Long expiracao;

    @Schema(description = "Dados do usuário logado")
    private UsuarioResponseDTO usuario;
}
