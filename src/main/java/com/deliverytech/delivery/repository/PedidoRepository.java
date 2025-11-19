package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Roteiro 3 - 1.4 Repository do Pedido
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(com.deliverytech.delivery.entity.StatusPedido status);
    List<Pedido> findTop10ByOrderByDataPedidoDesc();
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

    // Roteiro 3 - 3.1 Consultas com @Query
    // Total de vendas por restaurante
    @Query("SELECT p.restaurante.nome, SUM(p.valorTotal) FROM Pedido p GROUP BY p.restaurante.nome")
    List<Object[]> totalVendasPorRestaurante();

    // Pedidos com valor acima de X
    @Query("SELECT p FROM Pedido p WHERE p.valorTotal > :valorMinimo")
    List<Pedido> pedidosComValorAcimaDe(java.math.BigDecimal valorMinimo);

    // Relatório por período e status
    @Query("SELECT p FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim AND p.status = :status")
    List<Pedido> relatorioPorPeriodoEStatus(LocalDateTime inicio, LocalDateTime fim, com.deliverytech.delivery.entity.StatusPedido status);
}
