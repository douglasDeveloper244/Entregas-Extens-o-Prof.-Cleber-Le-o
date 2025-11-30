package com.deliverytech.delivery_api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidade que representa um item de um pedido")
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do item", example = "50")
    private Long id;

    @Schema(description = "Quantidade do produto", example = "2")
    private int quantidade;

    @JoinColumn(name = "preco_unitario")
    @Schema(description = "Preço unitário do produto no momento do pedido", example = "25.90")
    private BigDecimal precoUnitario;

    @Schema(description = "Subtotal do item (quantidade * preço unitário)", example = "51.80")
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
}
