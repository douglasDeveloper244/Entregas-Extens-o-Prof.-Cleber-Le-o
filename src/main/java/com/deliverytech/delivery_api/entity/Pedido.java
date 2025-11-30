package com.deliverytech.delivery_api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedidos")
@Schema(description = "Entidade que representa um pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do pedido", example = "100")
    private Long id;

    @Column(name = "numero_pedido")
    @Schema(description = "Número de identificação do pedido", example = "PED-123456")
    private String numeroPedido;

    @Column(name = "data_pedido")
    @Schema(description = "Data e hora em que o pedido foi realizado")
    private LocalDateTime dataPedido;

    @Schema(description = "Status atual do pedido", example = "CONFIRMADO")
    private String status;

    @Column(name = "valor_total")
    @Schema(description = "Valor total do pedido incluindo taxas", example = "55.90")
    private BigDecimal valorTotal;

    @Schema(description = "Observações do cliente", example = "Sem cebola, por favor.")
    private String observacoes;

    @Column(name = "endereco_entrega")
    @Schema(description = "Endereço de entrega", example = "Rua das Palmeiras, 456")
    private String enderecoEntrega;

    @Column(name = "taxa_entrega")
    @Schema(description = "Taxa de entrega aplicada", example = "5.00")
    private BigDecimal taxaEntrega;

    @Schema(description = "CEP de entrega", example = "12345-678")
    private String cep;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;
}
