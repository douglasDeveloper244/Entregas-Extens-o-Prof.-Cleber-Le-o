package com.deliverytech.delivery_api.entity;

import java.math.BigDecimal;
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
@Table(name = "restaurantes")
@Schema(description = "Entidade que representa um restaurante")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Restaurante Saboroso")
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "Brasileira")
    private String categoria;

    @Schema(description = "Endereço completo", example = "Rua das Flores, 123")
    private String endereco;

    @Schema(description = "CEP do restaurante", example = "12345-678")
    private String cep;

    @Schema(description = "Telefone de contato", example = "(11) 98765-4321")
    private String telefone;

    @Column(name = "taxa_entrega")
    @Schema(description = "Taxa de entrega base", example = "5.00")
    private BigDecimal taxaEntrega;

    @Schema(description = "Avaliação média do restaurante", example = "4.5")
    private BigDecimal avaliacao;

    @Schema(description = "Indica se o restaurante está ativo", example = "true")
    private Boolean ativo;

    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Produto> produtos;

    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pedido> pedidos;

    public boolean isAtivo() {
        return this.ativo != null && this.ativo;
    }

}
