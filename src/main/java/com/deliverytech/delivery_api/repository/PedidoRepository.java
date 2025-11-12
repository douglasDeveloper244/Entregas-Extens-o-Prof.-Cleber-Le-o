package com.deliverytech.delivery_api.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // 🔹 Listar pedidos por cliente
    List<Pedido> findByClienteOrderByDataPedidoDesc(Cliente cliente);

    // 🔹 Total de vendas por restaurante
    @Query("""
        SELECT new map(
            r.nome as restaurante,
            SUM(p.valorTotal) as totalVendas
        )
        FROM Pedido p
        JOIN p.restaurante r
        GROUP BY r.nome
        ORDER BY totalVendas DESC
    """)
    List<Map<String, Object>> totalVendasPorRestaurante();

    // 🔹 Pedidos com valor acima de X
    @Query("SELECT p FROM Pedido p WHERE p.valorTotal > :valorMinimo ORDER BY p.valorTotal DESC")
    List<Pedido> buscarPedidosComValorAcima(@Param("valorMinimo") BigDecimal valorMinimo);

    // 🔹 Relatório por período e status
    @Query("""
        SELECT new map(
            p.id as id,
            p.numeroPedido as numeroPedido,
            p.dataPedido as dataPedido,
            p.valorTotal as valorTotal,
            c.nome as cliente,
            r.nome as restaurante,
            p.status as status
        )
        FROM Pedido p
        JOIN p.cliente c
        JOIN p.restaurante r
        WHERE p.dataPedido BETWEEN :inicio AND :fim
        AND p.status = :status
        ORDER BY p.dataPedido
    """)
    List<Map<String, Object>> buscarRelatorioPorPeriodoEStatus(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") String status);
}
