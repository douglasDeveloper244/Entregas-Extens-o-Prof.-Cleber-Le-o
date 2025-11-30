package com.deliverytech.delivery_api.entity;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produtos")
@Schema(description = "Entidade que representa um produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do produto", example = "10")
    private Long id;

    @Schema(description = "Nome do produto", example = "Hambúrguer Artesanal")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Pão brioche, carne 180g, queijo cheddar e bacon.")
    private String descricao;

    @Schema(description = "Preço do produto", example = "25.90")
    private BigDecimal preco;

    @Schema(description = "Categoria do produto", example = "Lanches")
    private String categoria;

    @Schema(description = "Indica se o produto está disponível", example = "true")
    private Boolean disponivel;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    public boolean isAtivo() {
        return this.disponivel != null && this.disponivel;
    }

}
