package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para resposta de dados do usuário", title = "Usuario Response DTO")
public class UsuarioResponseDTO {
    @Schema(description = "ID do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário", example = "João da Silva")
    private String nome;

    @Schema(description = "Email do usuário", example = "joao@email.com")
    private String email;

    @Schema(description = "Status do usuário", example = "true")
    private boolean ativo;

    @Schema(description = "Role do usuário", example = "CLIENTE")
    private String role;

    @Schema(description = "ID do restaurante (se aplicável)", example = "1")
    private Long restauranteId;
}
