package com.delivery_api.Projeto.Delivery.API.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurantes")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    private String endereco;
    
    private String telefone;
        
    private String categoria;
    
    @Column(name = "taxa_entrega", precision = 10, scale = 2)
    private BigDecimal taxaEntrega;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal avaliacao;
        
    @Column(nullable = false)
    private Boolean ativo;
    
    public Restaurante() {}
    
    public Restaurante(Long id, String nome, String endereco, String telefone, String horarioFuncionamento, 
                      String categoria, BigDecimal taxaEntrega, BigDecimal avaliacao, LocalDateTime dataCadastro, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.categoria = categoria;
        this.taxaEntrega = taxaEntrega;
        this.avaliacao = avaliacao;
        this.ativo = ativo;
    }
    
    @PrePersist
    protected void onCreate() {
        if (ativo == null) {
            ativo = true;
        }
        if (taxaEntrega == null) {
            taxaEntrega = BigDecimal.ZERO;
        }
        if (avaliacao == null) {
            avaliacao = BigDecimal.ZERO;
        }
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public BigDecimal getTaxaEntrega() { return taxaEntrega; }
    public void setTaxaEntrega(BigDecimal taxaEntrega) { this.taxaEntrega = taxaEntrega; }
    
    public BigDecimal getAvaliacao() { return avaliacao; }
    public void setAvaliacao(BigDecimal avaliacao) { this.avaliacao = avaliacao; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}