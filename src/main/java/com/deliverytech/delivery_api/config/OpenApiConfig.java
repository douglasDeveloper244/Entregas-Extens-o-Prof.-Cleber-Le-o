package com.deliverytech.delivery_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deliveryApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Delivery API")
                        .description("Documentação da API conforme Roteiro 5")
                        .version("1.0.0"));
    }
}
