
package com.deliverytech.delivery_api.entity;

import com.deliverytech.delivery_api.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuario")
@Schema(description = "Entidade que representa um usuário do sistema")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do usuário", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Email do usuário (login)", example = "usuario@email.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Senha criptografada", hidden = true)
    private String senha;

    @Schema(description = "Nome do usuário", example = "Administrador")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Papel do usuário no sistema", example = "ADMIN")
    private Role role;

    @Schema(description = "Indica se o usuário está ativo", example = "true")
    private Boolean ativo = true;

    @Schema(description = "Data de criação do usuário")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Schema(description = "ID do restaurante associado (se aplicável)", example = "1")
    private Long restauranteId;

    // getters / setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    // UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + this.role.name());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.ativo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativo;
    }

}
