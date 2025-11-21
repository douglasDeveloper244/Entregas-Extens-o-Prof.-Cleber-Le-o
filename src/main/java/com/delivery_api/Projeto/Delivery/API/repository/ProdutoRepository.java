package com.delivery_api.Projeto.Delivery.API.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delivery_api.Projeto.Delivery.API.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // Buscar produtos por restaurante
    List<Produto> findByRestauranteId(Long restauranteId);
    
    // Buscar produtos por categoria
    List<Produto> findByCategoria(String categoria);
    
    // Buscar produtos por disponibilidade
    List<Produto> findByDisponivel(Boolean disponivel);
    
    // Buscar produtos por restaurante e categoria
    List<Produto> findByRestauranteIdAndCategoria(Long restauranteId, String categoria);
    
    // Buscar produtos por restaurante e disponibilidade
    List<Produto> findByRestauranteIdAndDisponivel(Long restauranteId, Boolean disponivel);
    
    // Buscar produtos por categoria e disponibilidade
    List<Produto> findByCategoriaAndDisponivel(String categoria, Boolean disponivel);
    
    // Buscar produtos por restaurante, categoria e disponibilidade
    List<Produto> findByRestauranteIdAndCategoriaAndDisponivel(Long restauranteId, String categoria, Boolean disponivel);
    
    // Buscar produtos por nome (case insensitive)
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    
    // Buscar produtos por nome e restaurante
    List<Produto> findByNomeContainingIgnoreCaseAndRestauranteId(String nome, Long restauranteId);
    
    // Buscar produtos disponíveis por restaurante
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true")
    List<Produto> findDisponiveisByRestauranteId(@Param("restauranteId") Long restauranteId);
    
    // Buscar produtos por faixa de preço
    @Query("SELECT p FROM Produto p WHERE p.preco BETWEEN :precoMin AND :precoMax")
    List<Produto> findByPrecoBetween(@Param("precoMin") BigDecimal precoMin, @Param("precoMax") BigDecimal precoMax);
    
    // Buscar produtos por restaurante e faixa de preço
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.preco BETWEEN :precoMin AND :precoMax")
    List<Produto> findByRestauranteIdAndPrecoBetween(@Param("restauranteId") Long restauranteId, 
    @Param("precoMin") BigDecimal precoMin, 
    @Param("precoMax") BigDecimal precoMax);
    
    // Buscar produtos ordenados por preço (crescente)
    @Query("SELECT p FROM Produto p ORDER BY p.preco ASC")
    List<Produto> findAllOrderByPrecoAsc();
    
    // Buscar produtos por restaurante ordenados por preço
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId ORDER BY p.preco ASC")
    List<Produto> findByRestauranteIdOrderByPrecoAsc(@Param("restauranteId") Long restauranteId);
    
    // Buscar produto específico com informações do restaurante
    @Query("SELECT p FROM Produto p JOIN FETCH p.restaurante WHERE p.id = :id")
    Optional<Produto> findByIdWithRestaurante(@Param("id") Long id);
    
    // Verificar se produto existe no restaurante
    boolean existsByIdAndRestauranteId(Long id, Long restauranteId);
}