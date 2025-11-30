
package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da autenticação")
public class LoginResponse {
    @Schema(description = "Token de acesso JWT")
    private String token;
    @Schema(description = "Tipo do token", example = "Bearer")
    private String type;
    @Schema(description = "Timestamp de expiração do token")
    private Long expiresAt;
    @Schema(description = "Dados do usuário autenticado")
    private UserResponse user;

    @Schema(description = "Token para renovação de acesso")
    private String refreshToken;

    public LoginResponse(String token, String refreshToken, String type, Long expiresAt, UserResponse user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.type = type;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getType() {
        return type;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public UserResponse getUser() {
        return user;
    }
}
