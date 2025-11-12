package com.deliverytech.delivery_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.entity.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar restaurante pelo nome (único)
    Optional<Restaurante> findByNome(String nome);

    // Buscar restaurantes ativos
    List<Restaurante> findByAtivoTrue();

    // Buscar por categoria exata (sem case sensitive)
    List<Restaurante> findByCategoriaIgnoreCase(String categoria);

    // Buscar por categoria com filtro parcial + paginação
    Page<Restaurante> findByCategoriaContainingIgnoreCase(String categoria, Pageable pageable);

    // Sobrescrever findAll(Pageable) com null safety explícito
    @Override
    @NonNull
    Page<Restaurante> findAll(@NonNull Pageable pageable);
}
