package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery.entity.Restaurante;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Roteiro 3 - 1.2 Repository do Restaurante
    List<Restaurante> findByCategoria(String categoria);
    List<Restaurante> findByAtivoTrue();
    List<Restaurante> findByTaxaEntregaLessThanEqual(java.math.BigDecimal taxa);
    List<Restaurante> findTop5ByOrderByNomeAsc();
}
