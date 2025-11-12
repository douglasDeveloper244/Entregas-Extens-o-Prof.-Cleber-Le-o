package com.deliverytech.delivery_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestauranteRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    private String endereco;
    
    private String telefone;
    
    private String horarioFuncionamento;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) { this.horarioFuncionamento = horarioFuncionamento; }
}