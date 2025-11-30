
package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para registro de usuário")
public class RegisterRequest {
    @Schema(description = "Nome completo", example = "Maria Oliveira")
    private String nome;
    @Schema(description = "Email", example = "maria@email.com")
    private String email;
    @Schema(description = "Senha", example = "senha123")
    private String senha;
    @Schema(description = "Papel do usuário (CLIENTE, RESTAURANTE, ADMIN)", example = "CLIENTE")
    private String role;
    @Schema(description = "ID do restaurante (obrigatório se role for RESTAURANTE)", example = "1")
    private Long restauranteId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }
}
