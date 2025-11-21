package com.delivery_api.Projeto.Delivery.API.mapper;

import org.springframework.stereotype.Component;

import com.delivery_api.Projeto.Delivery.API.dto.PedidoResponse;
import com.delivery_api.Projeto.Delivery.API.entity.Pedido;

@Component
public class PedidoMapper {
    
    public PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setNumeroPedido(pedido.getNumeroPedido());
        response.setDataPedido(pedido.getDataPedido());
        response.setStatus(pedido.getStatus());
        response.setValorTotal(pedido.getValorTotal());
        response.setObservacoes(pedido.getObservacoes());
        response.setItens(pedido.getItens());
        
        if (pedido.getCliente() != null) {
            response.setClienteId(pedido.getCliente().getId());
            response.setClienteNome(pedido.getCliente().getNome());
        }
        
        if (pedido.getRestaurante() != null) {
            response.setRestauranteId(pedido.getRestaurante().getId());
            response.setRestauranteNome(pedido.getRestaurante().getNome());
        }
        
        return response;
    }
}