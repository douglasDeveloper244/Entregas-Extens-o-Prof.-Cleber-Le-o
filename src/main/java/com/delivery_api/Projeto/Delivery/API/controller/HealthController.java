package com.delivery_api.Projeto.Delivery.API.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

record ProjectInfo(String nomeProjeto, String versao, String autor, String descricao, String Java, String framework) {}

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "service", "Delivery API"
        );
    }

@GetMapping(value = "/info", produces = "application/json")
public String info() {
    ProjectInfo info = new ProjectInfo(
        "Delivery API",
        "1.0.0",
        "Douglas Ribeiro",
        "API responsável por gerenciar entregas e pedidos.",
        "JDK 21.0.8",
        "Spring Boot 3.4.11"
    );

    return """
            {
              "nomeProjeto": "%s",
              "versao": "%s",
              "autor": "%s",
              "descricao": "%s",
              "Java": "%s",
              "framework": "%s"
            }
            """.formatted(
            info.nomeProjeto(),
            info.versao(),
            info.autor(),
            info.descricao(),
            info.Java(),
            info.framework()
    );
}

}
