package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisição de criação de usuário", title = "Usuario Request DTO")
public class UsuarioRequestDTO {
    @Schema(description = "Nome do usuário", example = "João da Silva", required = true)
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ser entre 3 e 100 caracteres")
    private String nome;

    @Schema(description = "Email do usuário", example = "joao@email.com", required = true)
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @Schema(description = "Senha do usuário", example = "123456", required = true)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
    private String senha;

    @Schema(description = "Role do usuário", example = "CLIENTE", required = true)
    @NotNull(message = "O tipo de Role é obrigatório")
    private Role role;

    @Schema(description = "ID do restaurante (se aplicável)", example = "1")
    private Long restauranteId;
}
