package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para resposta de dados do cliente", title = "Cliente Response DTO")
public class ClienteResponseDTO {
    @Schema(description = "ID do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome do cliente", example = "João da Silva")
    private String nome;

    @Schema(description = "Email do cliente", example = "email@email.com")
    private String email;

    @Schema(description = "Telefone do cliente", example = "(11) 91234-5678")
    private String telefone;

    @Schema(description = "Endereço do cliente", example = "Rua das Flores, 123, São Paulo, SP")
    private String endereco;

    @Schema(description = "Status do cliente", example = "true")
    private boolean ativo;
}
