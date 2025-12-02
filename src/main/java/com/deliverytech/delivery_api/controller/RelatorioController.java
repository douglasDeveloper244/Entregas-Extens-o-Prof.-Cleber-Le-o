package com.deliverytech.delivery_api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.repository.projection.ItemMaisVendidoProjection;
import com.deliverytech.delivery_api.repository.projection.VendaRestauranteProjection;
import com.deliverytech.delivery_api.services.RelatorioService;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    /**
     * Relatório 1 - Pedidos por período
     * Endpoint: GET
     * /api/relatorios/pedidos-por-periodo?inicio=AAAA-MM-DD&fim=AAAA-MM-DD
     */
    @GetMapping("/pedidos-por-periodo")
    public List<Pedido> pedidosPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return relatorioService.buscarPedidosPorPeriodo(inicio, fim);
    }

    /**
     * Relatório 2 - Produtos mais vendidos
     * Endpoint: GET /api/relatorios/produtos-mais-vendidos
     */
    @GetMapping("/produtos-mais-vendidos")
    public List<ItemMaisVendidoProjection> produtosMaisVendidos() {
        return relatorioService.buscarProdutosMaisVendidos();
    }

    /**
     * Relatório 3 - Clientes mais ativos
     * Endpoint: GET /api/relatorios/clientes-mais-ativos
     */
    @GetMapping("/clientes-mais-ativos")
    public List<Map<String, Object>> clientesMaisAtivos() {
        return relatorioService.buscarClientesMaisAtivos();
    }

    /**
     * Relatório 4 - Restaurantes com maior volume de vendas
     * Endpoint: GET /api/relatorios/restaurantes-mais-vendas
     */
    @GetMapping("/restaurantes-mais-vendas")
    public List<VendaRestauranteProjection> restaurantesComMaisVendas() {
        return relatorioService.buscarRestaurantesComMaisVendas();
    }
}
