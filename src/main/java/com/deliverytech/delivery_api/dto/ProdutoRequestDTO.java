package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProdutoRequestDTO {

        @NotBlank(message = "O nome é obrigatório")
        private String nome;

        @NotBlank(message = "A descrição é obrigatória")
        private String descricao;

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        private BigDecimal preco;

        @NotBlank(message = "A categoria é obrigatória")
        private String categoria;

        @NotNull(message = "O ID do restaurante é obrigatório")
        private Long restauranteId;

        private Boolean disponivel;
}
