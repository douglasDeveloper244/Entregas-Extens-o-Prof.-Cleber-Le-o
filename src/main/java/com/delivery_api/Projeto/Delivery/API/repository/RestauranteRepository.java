package com.delivery_api.Projeto.Delivery.API.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    // Buscar por nome (contendo o texto, case insensitive)
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
    
    // Buscar por categoria
    List<Restaurante> findByCategoria(String categoria);
    
    // Buscar por status ativo
    List<Restaurante> findByAtivo(Boolean ativo);
    
    // Buscar por taxa de entrega menor ou igual
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxaEntrega);
    
    // Buscar por taxa de entrega igual a zero (entrega grátis)
    List<Restaurante> findByTaxaEntrega(BigDecimal taxaEntrega);
    
    // Buscar por nome e categoria
    List<Restaurante> findByNomeContainingIgnoreCaseAndCategoria(String nome, String categoria);
    
    // Buscar por nome e status ativo
    List<Restaurante> findByNomeContainingIgnoreCaseAndAtivo(String nome, Boolean ativo);
    
    // Buscar por categoria e status ativo
    List<Restaurante> findByCategoriaAndAtivo(String categoria, Boolean ativo);
    
    // Buscar por categoria e taxa de entrega
    List<Restaurante> findByCategoriaAndTaxaEntregaLessThanEqual(String categoria, BigDecimal taxaEntrega);
    
    // Buscar por nome, categoria e status ativo
    List<Restaurante> findByNomeContainingIgnoreCaseAndCategoriaAndAtivo(String nome, String categoria, Boolean ativo);
    
    // Buscar por nome, categoria e taxa de entrega
    List<Restaurante> findByNomeContainingIgnoreCaseAndCategoriaAndTaxaEntregaLessThanEqual(String nome, String categoria, BigDecimal taxaEntrega);
    
    // Buscar todos ordenados por avaliação (decrescente)
    @Query("SELECT r FROM Restaurante r ORDER BY r.avaliacao DESC NULLS LAST")
    List<Restaurante> findAllOrderByAvaliacaoDesc();
    
    // Buscar todos ordenados por taxa de entrega (crescente)
    @Query("SELECT r FROM Restaurante r ORDER BY r.taxaEntrega ASC NULLS LAST")
    List<Restaurante> findAllOrderByTaxaEntregaAsc();
    
    // Buscar ativos ordenados por avaliação (decrescente)
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true ORDER BY r.avaliacao DESC NULLS LAST")
    List<Restaurante> findAtivosOrderByAvaliacaoDesc();
    
    // Buscar ativos ordenados por taxa de entrega (crescente)
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true ORDER BY r.taxaEntrega ASC NULLS LAST")
    List<Restaurante> findAtivosOrderByTaxaEntregaAsc();
    
    // Buscar por nome ordenados por avaliação
    @Query("SELECT r FROM Restaurante r WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY r.avaliacao DESC NULLS LAST")
    List<Restaurante> findByNomeContainingOrderByAvaliacaoDesc(@Param("nome") String nome);
    
    // Buscar por nome ordenados por taxa de entrega
    @Query("SELECT r FROM Restaurante r WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY r.taxaEntrega ASC NULLS LAST")
    List<Restaurante> findByNomeContainingOrderByTaxaEntregaAsc(@Param("nome") String nome);
    
    // Buscar por categoria ordenados por avaliação
    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria ORDER BY r.avaliacao DESC NULLS LAST")
    List<Restaurante> findByCategoriaOrderByAvaliacaoDesc(@Param("categoria") String categoria);
    
    // Buscar por categoria ordenados por taxa de entrega
    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria ORDER BY r.taxaEntrega ASC NULLS LAST")
    List<Restaurante> findByCategoriaOrderByTaxaEntregaAsc(@Param("categoria") String categoria);
    
    // Buscar ativos por categoria ordenados por avaliação
    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria AND r.ativo = true ORDER BY r.avaliacao DESC NULLS LAST")
    List<Restaurante> findByCategoriaAndAtivoOrderByAvaliacaoDesc(@Param("categoria") String categoria);
    
    // Buscar ativos por categoria ordenados por taxa de entrega
    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria AND r.ativo = true ORDER BY r.taxaEntrega ASC NULLS LAST")
    List<Restaurante> findByCategoriaAndAtivoOrderByTaxaEntregaAsc(@Param("categoria") String categoria);
    
    // Buscar com taxa de entrega grátis (taxa = 0)
    @Query("SELECT r FROM Restaurante r WHERE r.taxaEntrega = 0 OR r.taxaEntrega IS NULL")
    List<Restaurante> findComEntregaGratis();
    
    // Buscar ativos com taxa de entrega grátis
    @Query("SELECT r FROM Restaurante r WHERE (r.taxaEntrega = 0 OR r.taxaEntrega IS NULL) AND r.ativo = true")
    List<Restaurante> findAtivosComEntregaGratis();
}