package com.deliverytech.delivery_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisição de login", title = "Login Request DTO")
public class LoginRequestDTO {

    @Schema(description = "Email do usuário", example = "admin@deliverytech.com", required = true)
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @Schema(description = "Senha do usuário", example = "123456", required = true)
    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}
