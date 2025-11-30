
package com.deliverytech.delivery_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                var bearerScheme = new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization");

                return new OpenAPI()
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth", bearerScheme))
                                .info(new Info()
                                                .title("Delivery Tech API")
                                                .version("1.0.0")
                                                .description("API completa para gerenciamento de sistema de delivery (Restaurantes, Produtos, Pedidos, Clientes).")
                                                .contact(new io.swagger.v3.oas.models.info.Contact()
                                                                .name("Douglas Dev")
                                                                .email("douglas@deliverytech.com")
                                                                .url("https://deliverytech.com"))
                                                .license(new io.swagger.v3.oas.models.info.License()
                                                                .name("Apache 2.0")
                                                                .url("http://springdoc.org")))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
        }
}
