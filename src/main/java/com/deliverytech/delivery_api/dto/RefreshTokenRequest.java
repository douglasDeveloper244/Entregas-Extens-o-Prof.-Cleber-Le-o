package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para renovação de token")
public class RefreshTokenRequest {
        @Schema(description = "Token de refresh válido")
        private String refreshToken;

        public String getRefreshToken() {
                return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
                this.refreshToken = refreshToken;
        }
}
