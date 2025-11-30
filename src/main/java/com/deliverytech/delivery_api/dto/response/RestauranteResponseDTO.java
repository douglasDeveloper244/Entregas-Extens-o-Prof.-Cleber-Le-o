package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "DTO para resposta de dados do restaurante", title = "Restaurante Response DTO")
public class RestauranteResponseDTO {

    @Schema(description = "ID do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria Express")
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "Pizzas")
    private String categoria;

    @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123")
    private String endereco;

    @Schema(description = "Telefone do restaurante", example = "+5511999999999")
    private String telefone;

    @Schema(description = "Taxa de entrega do restaurante", example = "5.00")
    private BigDecimal taxaEntrega;

    @Schema(description = "Avaliação do restaurante", example = "4.5")
    private BigDecimal avaliacao;

    @Schema(description = "Status do restaurante", example = "true")
    private Boolean ativo;

}
