package com.delivery_api.Projeto.Delivery.API.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_pedido", unique = true, nullable = false)
    @Size(min = 1, max = 50)
    private String numeroPedido;
    
    @Column(name = "data_pedido", nullable = false)
    @NotNull
    private LocalDateTime dataPedido;
    
    @Column(nullable = false)
    @Size(min = 1, max = 50)
    @NotNull
    private String status;
    
    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    @NotNull
    private BigDecimal valorTotal;
    
    @Column(length = 500)
    private String observacoes;
    
    @Column(length = 1000)
    private String itens;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurante_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Restaurante restaurante;
    
    public Pedido() {}
    
    public Pedido(String numeroPedido, LocalDateTime dataPedido, String status, 
                 BigDecimal valorTotal, String observacoes, String itens,
                 Cliente cliente, Restaurante restaurante) {
        this.numeroPedido = numeroPedido;
        this.dataPedido = dataPedido;
        this.status = status;
        this.valorTotal = valorTotal;
        this.observacoes = observacoes;
        this.itens = itens;
        this.cliente = cliente;
        this.restaurante = restaurante;
    }
    
    @PrePersist
    protected void onCreate() {
        if (dataPedido == null) {
            dataPedido = LocalDateTime.now();
        }
        if (status == null || status.trim().isEmpty()) {
            status = "PENDENTE";
        }
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }
    
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public String getItens() { return itens; }
    public void setItens(String itens) { this.itens = itens; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Restaurante getRestaurante() { return restaurante; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }
}