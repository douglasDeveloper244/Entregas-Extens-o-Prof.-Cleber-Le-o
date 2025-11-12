package com.deliverytech.delivery_api.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.PedidoDTO;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.services.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /** Criar novo pedido */
    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody PedidoDTO dto) {
        try {
            Pedido pedido = pedidoService.criarPedido(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    /** Listar pedidos por cliente */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> listarPorCliente(@PathVariable Long clienteId) {
        try {
            List<Pedido> pedidos = pedidoService.listarPorCliente(clienteId);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar pedidos: " + e.getMessage());
        }
    }

    /** Atualizar status do pedido */
    @PatchMapping("/{pedidoId}/status/{status}")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long pedidoId, @PathVariable StatusPedido status) {
        try {
            Pedido pedido = pedidoService.atualizarStatus(pedidoId, status);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

    // ============================================================
    // 🔹 Relatórios e consultas com @Query — Roteiro 5
    // ============================================================

    /** Total de vendas por restaurante */
    @GetMapping("/relatorio/vendas-restaurantes")
    public ResponseEntity<?> totalVendasPorRestaurante() {
        try {
            List<Map<String, Object>> resultados = pedidoService.totalVendasPorRestaurante();
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    /** Pedidos com valor acima de um valor informado */
    @GetMapping("/relatorio/valor-acima")
    public ResponseEntity<?> pedidosComValorAcima(@RequestParam BigDecimal valorMinimo) {
        try {
            List<Pedido> pedidos = pedidoService.buscarPedidosComValorAcima(valorMinimo);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar pedidos: " + e.getMessage());
        }
    }

    /** Relatório de pedidos por período e status */
    @GetMapping("/relatorio/periodo-status")
    public ResponseEntity<?> relatorioPorPeriodoEStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam String status) {

        try {
            List<Map<String, Object>> pedidos = pedidoService.buscarRelatorioPorPeriodoEStatus(inicio, fim, status);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
