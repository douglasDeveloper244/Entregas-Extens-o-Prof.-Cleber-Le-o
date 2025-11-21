package com.delivery_api.Projeto.Delivery.API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.delivery_api.Projeto.Delivery.API.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmail(String email);
    Optional<Cliente> findByEmail(String email);
    List<Cliente> findByAtivoTrue();
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}