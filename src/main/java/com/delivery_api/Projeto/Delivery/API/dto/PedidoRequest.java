package com.delivery_api.Projeto.Delivery.API.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PedidoRequest {
    
    @NotNull(message = "ID do cliente é obrigatório")
    @Positive(message = "ID do cliente deve ser positivo")
    private Long clienteId;
    
    @NotNull(message = "ID do restaurante é obrigatório")
    @Positive(message = "ID do restaurante deve ser positivo")
    private Long restauranteId;
    
    @NotNull(message = "Valor total é obrigatório")
    @Positive(message = "Valor total deve ser maior que zero")
    private BigDecimal valorTotal;
    
    private String observacoes;
    
    private String itens;

    // Getters e Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getItens() {
        return itens;
    }

    public void setItens(String itens) {
        this.itens = itens;
    }
}