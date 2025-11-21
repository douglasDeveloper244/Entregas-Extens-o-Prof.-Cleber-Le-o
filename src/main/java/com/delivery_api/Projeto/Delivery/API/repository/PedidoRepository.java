package com.delivery_api.Projeto.Delivery.API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.delivery_api.Projeto.Delivery.API.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByClienteId(Long clienteId);
    
    List<Pedido> findByClienteIdAndStatus(Long clienteId, String status);
    
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    
    List<Pedido> findByStatus(String status);
    
    List<Pedido> findByRestauranteId(Long restauranteId);
    
    List<Pedido> findByRestauranteIdAndStatus(Long restauranteId, String status);
}