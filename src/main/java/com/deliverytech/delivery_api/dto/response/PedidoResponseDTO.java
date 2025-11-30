package com.deliverytech.delivery_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Restaurante;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para resposta de dados do pedido", title = "Pedido Response DTO")
public class PedidoResponseDTO {
    @Schema(description = "ID do pedido", example = "1")
    private Long id;

    @Schema(description = "Número do pedido", example = "12345")
    private String numeroPedido;

    @Schema(description = "Data do pedido", example = "2023-10-01T12:00:00")
    private LocalDateTime dataPedido;

    @Schema(description = "Status do pedido", example = "PENDENTE")
    private String status;

    @Schema(description = "Valor total do pedido", example = "99.99")
    private BigDecimal valorTotal;

    @Schema(description = "Observações do pedido", example = "Sem cebola")
    private String observacoes;

    @Schema(description = "Dados do cliente")
    private Cliente cliente;

    @Schema(description = "Dados do restaurante")
    private Restaurante restaurante;

    @Schema(description = "Endereço de entrega", example = "Rua das Flores, 123")
    private String enderecoEntrega;

    @Schema(description = "Taxa de entrega", example = "5.00")
    private BigDecimal taxaEntrega;

    @Schema(description = "Itens do pedido")
    List<ItemPedidoRequestDTO> itens;

}
