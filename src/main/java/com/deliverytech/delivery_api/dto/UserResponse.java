
package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do usuário")
public class UserResponse {
    @Schema(description = "ID do usuário", example = "1")
    private Long id;
    @Schema(description = "Nome do usuário", example = "João")
    private String nome;
    @Schema(description = "Email do usuário", example = "joao@email.com")
    private String email;
    @Schema(description = "Papel do usuário", example = "CLIENTE")
    private String role;
    @Schema(description = "ID do restaurante associado", example = "1")
    private Long restauranteId;

    public UserResponse(Usuario u) {
        this.id = u.getId();
        this.nome = u.getNome();
        this.email = u.getEmail();
        this.role = u.getRole().name();
        this.restauranteId = u.getRestauranteId();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }
}
