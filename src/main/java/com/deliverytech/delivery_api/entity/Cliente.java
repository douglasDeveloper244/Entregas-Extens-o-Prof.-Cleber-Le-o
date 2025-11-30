package com.deliverytech.delivery_api.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
@Schema(description = "Entidade que representa um cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "João da Silva")
    private String nome;

    @Schema(description = "Email do cliente", example = "joao@email.com")
    private String email;

    @Schema(description = "Telefone de contato", example = "(11) 91234-5678")
    private String telefone;

    @Schema(description = "Endereço completo", example = "Rua das Flores, 123")
    private String endereco;

    @Column(name = "data_cadastro")
    @Schema(description = "Data de cadastro do cliente")
    private LocalDateTime dataCadastro;

    @Column(nullable = true)
    @Schema(description = "Indica se o cliente está ativo", example = "true")
    private Boolean ativo;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pedido> pedidos;

    public void inativar() {
        this.ativo = false;
    }

    public boolean isAtivo() {
        return this.ativo != null && this.ativo;
    }

}
