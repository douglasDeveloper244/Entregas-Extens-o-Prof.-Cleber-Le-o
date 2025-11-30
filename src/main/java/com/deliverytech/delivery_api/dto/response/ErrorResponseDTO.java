package com.deliverytech.delivery_api.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para resposta de erro padrão da API")
public class ErrorResponseDTO {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp do erro")
    private LocalDateTime timestamp;

    @Schema(description = "Código de status HTTP", example = "400")
    private int status;

    @Schema(description = "Tipo do erro", example = "Bad Request")
    private String error;

    @Schema(description = "Mensagem detalhada do erro", example = "Dados inválidos")
    private String message;

    @Schema(description = "Caminho da requisição", example = "/api/produtos")
    private String path;

    @Schema(description = "Código interno de erro (opcional)", example = "ERR-001")
    private String errorCode;

    @Schema(description = "Detalhes adicionais do erro (ex: erros de validação)")
    private Map<String, String> details;

    public ErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
