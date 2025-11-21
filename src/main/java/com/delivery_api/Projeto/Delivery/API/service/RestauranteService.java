package com.delivery_api.Projeto.Delivery.API.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;

@Service
public class RestauranteService {
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    // Salvar restaurante
    public Restaurante salvar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }
    
    // Buscar todos os restaurantes
    public List<Restaurante> buscarTodos() {
        return restauranteRepository.findAll();
    }
    
    // Buscar por ID
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }
    
    // Deletar restaurante
    public void deletar(Long id) {
        restauranteRepository.deleteById(id);
    }
    
    // Métodos de busca conforme solicitado
    
    // Buscar por nome
    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    // Buscar por categoria
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }
    
    // Buscar por status ativo
    public List<Restaurante> buscarPorAtivo(Boolean ativo) {
        return restauranteRepository.findByAtivo(ativo);
    }
    
    // Buscar por taxa de entrega máxima
    public List<Restaurante> buscarPorTaxaEntregaMaxima(BigDecimal taxaMaxima) {
        return restauranteRepository.findByTaxaEntregaLessThanEqual(taxaMaxima);
    }
    
    // Buscar com entrega grátis
    public List<Restaurante> buscarComEntregaGratis() {
        return restauranteRepository.findComEntregaGratis();
    }
    
    // Buscar ativos com entrega grátis
    public List<Restaurante> buscarAtivosComEntregaGratis() {
        return restauranteRepository.findAtivosComEntregaGratis();
    }
    
    // Buscar todos ordenados por avaliação
    public List<Restaurante> buscarTodosOrdenadosPorAvaliacao() {
        return restauranteRepository.findAllOrderByAvaliacaoDesc();
    }
    
    // Buscar todos ordenados por taxa de entrega
    public List<Restaurante> buscarTodosOrdenadosPorTaxaEntrega() {
        return restauranteRepository.findAllOrderByTaxaEntregaAsc();
    }
    
    // Buscar ativos ordenados por avaliação
    public List<Restaurante> buscarAtivosOrdenadosPorAvaliacao() {
        return restauranteRepository.findAtivosOrderByAvaliacaoDesc();
    }
    
    // Buscar ativos ordenados por taxa de entrega
    public List<Restaurante> buscarAtivosOrdenadosPorTaxaEntrega() {
        return restauranteRepository.findAtivosOrderByTaxaEntregaAsc();
    }
    
    // Buscar por nome ordenados por avaliação
    public List<Restaurante> buscarPorNomeOrdenadosPorAvaliacao(String nome) {
        return restauranteRepository.findByNomeContainingOrderByAvaliacaoDesc(nome);
    }
    
    // Buscar por nome ordenados por taxa de entrega
    public List<Restaurante> buscarPorNomeOrdenadosPorTaxaEntrega(String nome) {
        return restauranteRepository.findByNomeContainingOrderByTaxaEntregaAsc(nome);
    }
    
    // Buscar por categoria ordenados por avaliação
    public List<Restaurante> buscarPorCategoriaOrdenadosPorAvaliacao(String categoria) {
        return restauranteRepository.findByCategoriaOrderByAvaliacaoDesc(categoria);
    }
    
    // Buscar por categoria ordenados por taxa de entrega
    public List<Restaurante> buscarPorCategoriaOrdenadosPorTaxaEntrega(String categoria) {
        return restauranteRepository.findByCategoriaOrderByTaxaEntregaAsc(categoria);
    }
    
    // Buscar ativos por categoria ordenados por avaliação
    public List<Restaurante> buscarAtivosPorCategoriaOrdenadosPorAvaliacao(String categoria) {
        return restauranteRepository.findByCategoriaAndAtivoOrderByAvaliacaoDesc(categoria);
    }
    
    // Buscar ativos por categoria ordenados por taxa de entrega
    public List<Restaurante> buscarAtivosPorCategoriaOrdenadosPorTaxaEntrega(String categoria) {
        return restauranteRepository.findByCategoriaAndAtivoOrderByTaxaEntregaAsc(categoria);
    }
    
    // Buscar combinando nome, categoria, ativo e taxa máxima
    public List<Restaurante> buscarComFiltros(String nome, String categoria, Boolean ativo, BigDecimal taxaMaxima) {
        // Lógica para combinar os filtros
        if (nome != null && categoria != null && ativo != null && taxaMaxima != null) {
            return restauranteRepository.findByNomeContainingIgnoreCaseAndCategoriaAndAtivo(nome, categoria, ativo)
                    .stream()
                    .filter(r -> r.getTaxaEntrega() == null || r.getTaxaEntrega().compareTo(taxaMaxima) <= 0)
                    .toList();
        } else if (nome != null && categoria != null && ativo != null) {
            return restauranteRepository.findByNomeContainingIgnoreCaseAndCategoriaAndAtivo(nome, categoria, ativo);
        } else if (nome != null && categoria != null && taxaMaxima != null) {
            return restauranteRepository.findByNomeContainingIgnoreCaseAndCategoriaAndTaxaEntregaLessThanEqual(nome, categoria, taxaMaxima);
        } else if (nome != null && ativo != null) {
            return restauranteRepository.findByNomeContainingIgnoreCaseAndAtivo(nome, ativo);
        } else if (categoria != null && ativo != null) {
            return restauranteRepository.findByCategoriaAndAtivo(categoria, ativo);
        } else if (categoria != null && taxaMaxima != null) {
            return restauranteRepository.findByCategoriaAndTaxaEntregaLessThanEqual(categoria, taxaMaxima);
        } else if (nome != null) {
            return restauranteRepository.findByNomeContainingIgnoreCase(nome);
        } else if (categoria != null) {
            return restauranteRepository.findByCategoria(categoria);
        } else if (ativo != null) {
            return restauranteRepository.findByAtivo(ativo);
        } else if (taxaMaxima != null) {
            return restauranteRepository.findByTaxaEntregaLessThanEqual(taxaMaxima);
        } else {
            return restauranteRepository.findAll();
        }
    }
}