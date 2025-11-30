
package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para autenticação")
public class LoginRequest {
    @Schema(description = "Email do usuário", example = "usuario@email.com")
    private String email;
    @Schema(description = "Senha do usuário", example = "123456")
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
