package com.deliverytech.delivery_api.controller;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Endpoints para verificação de saúde da aplicação")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Verificar status da API", description = "Retorna o status de saúde da aplicação")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "service", "Delivery API",
                "javaVersion", System.getProperty("java.version"));
    }

    @GetMapping("/info")
    @Operation(summary = "Informações da aplicação", description = "Retorna informações gerais sobre a API")
    public AppInfo info() {
        return new AppInfo(
                "Delivery Tech API",
                "1.0.0",
                "Douglas Dev",
                "JDK 21",
                "Spring Boot 3.2.x");
    }

    // Record para demonstrar recurso do Java 14+ (disponível no JDK 21)
    public record AppInfo(
            String application,
            String version,
            String developer,
            String javaVersion,
            String framework) {
    }
}