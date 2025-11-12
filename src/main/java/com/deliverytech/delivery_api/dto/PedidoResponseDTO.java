package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliverytech.delivery_api.enums.StatusPedido;

/**
 * DTO de resposta para o Pedido, retornado aos clientes da API.
 * Contém todas as informações relevantes, sem expor entidades diretamente.
 */
public class PedidoResponseDTO {

    private Long id;
    private String numeroPedido;

    private LocalDateTime dataPedido;
    private StatusPedido status;

    private BigDecimal subtotal;
    private BigDecimal taxaEntrega;
    private BigDecimal total;

    private String enderecoEntrega;
    private String observacoes;

    private ClienteResponseDTO cliente;
    private RestauranteResponseDTO restaurante;

    public PedidoResponseDTO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }
    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }
    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }
    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }
    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }
    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getObservacoes() {
        return observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public ClienteResponseDTO getCliente() {
        return cliente;
    }
    public void setCliente(ClienteResponseDTO cliente) {
        this.cliente = cliente;
    }

    public RestauranteResponseDTO getRestaurante() {
        return restaurante;
    }
    public void setRestaurante(RestauranteResponseDTO restaurante) {
        this.restaurante = restaurante;
    }
}
