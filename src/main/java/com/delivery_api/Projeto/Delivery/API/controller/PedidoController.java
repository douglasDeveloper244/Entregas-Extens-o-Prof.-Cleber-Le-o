package com.delivery_api.Projeto.Delivery.API.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_api.Projeto.Delivery.API.dto.PedidoRequest;
import com.delivery_api.Projeto.Delivery.API.dto.PedidoResponse;
import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.mapper.PedidoMapper;
import com.delivery_api.Projeto.Delivery.API.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private PedidoMapper pedidoMapper;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody PedidoRequest request) {
        try {
            Pedido novoPedido = pedidoService.criarPedido(request);
            PedidoResponse response = pedidoMapper.toResponse(novoPedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar pedido: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> consultarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(pedidoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numeroPedido}")
    public ResponseEntity<PedidoResponse> consultarPorNumero(@PathVariable String numeroPedido) {
        return pedidoService.buscarPorNumeroPedido(numeroPedido)
                .map(pedidoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> consultarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponse> responses = pedidoService.buscarPorCliente(clienteId)
                .stream()
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/cliente/{clienteId}/status")
    public ResponseEntity<List<PedidoResponse>> consultarPorClienteEStatus(
            @PathVariable Long clienteId,
            @RequestParam String status) {
        List<PedidoResponse> responses = pedidoService.buscarPorClienteEStatus(clienteId, status)
                .stream()
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PedidoResponse>> consultarPorStatus(@PathVariable String status) {
        List<PedidoResponse> responses = pedidoService.buscarPorStatus(status)
                .stream()
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<PedidoResponse>> consultarPorRestaurante(@PathVariable Long restauranteId) {
        List<PedidoResponse> responses = pedidoService.buscarPorRestaurante(restauranteId)
                .stream()
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        List<PedidoResponse> responses = pedidoService.buscarTodos()
                .stream()
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            return pedidoService.atualizarStatus(id, status)
                    .map(pedidoMapper::toResponse)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPedido(@PathVariable Long id, @RequestBody PedidoRequest request) {
        try {
            return pedidoService.atualizarPedido(id, request)
                    .map(pedidoMapper::toResponse)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar pedido: " + e.getMessage());
        }
    }
}