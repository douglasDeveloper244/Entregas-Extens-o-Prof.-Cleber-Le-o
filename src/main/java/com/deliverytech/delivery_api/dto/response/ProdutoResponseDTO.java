package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "DTO para resposta de dados do produto", title = "Produto Response DTO")
public class ProdutoResponseDTO {

    @Schema(description = "ID do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    private String nome;

    @Schema(description = "Descrição do produto", example = "Deliciosa pizza com molho de tomate, mussarela e manjericão")
    private String descricao;

    @Schema(description = "Categoria do produto", example = "Pizzas")
    private String categoria;

    @Schema(description = "Disponibilidade do produto", example = "true")
    private Boolean disponivel;

    @Schema(description = "Preço do produto", example = "29.90")
    private BigDecimal preco;

    @Schema(description = "Status do produto", example = "true")
    private boolean ativo;

}
