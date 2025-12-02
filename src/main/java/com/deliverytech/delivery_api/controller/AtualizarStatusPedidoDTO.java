package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusPedidoDTO(
                @NotNull StatusPedido status) {
}