package com.deliverytech.delivery_api.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.projection.ItemMaisVendidoProjection;
import com.deliverytech.delivery_api.repository.projection.VendaRestauranteProjection;

@Service
public class RelatorioService {

    private final PedidoRepository pedidoRepository;

    public RelatorioService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // 1️⃣ Relatório de pedidos por período
    public List<Pedido> buscarPedidosPorPeriodo(LocalDate inicio, LocalDate fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio.atStartOfDay(), fim.atTime(23, 59, 59));
    }

    // 2️⃣ Relatório de produtos mais vendidos
    public List<ItemMaisVendidoProjection> buscarProdutosMaisVendidos() {
        return pedidoRepository.findProdutosMaisVendidos();
    }

    // 3️⃣ Relatório de clientes mais ativos
    public List<Map<String, Object>> buscarClientesMaisAtivos() {
        return pedidoRepository.findRankingClientes();
    }

    // 4️⃣ Relatório de restaurantes com maior volume de vendas
    public List<VendaRestauranteProjection> buscarRestaurantesComMaisVendas() {
        return pedidoRepository.totalVendasPorRestaurante();
    }
}
