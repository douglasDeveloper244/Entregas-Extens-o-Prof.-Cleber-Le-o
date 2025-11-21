package com.delivery_api.Projeto.Delivery.API.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.repository.ProdutoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    // Salvar produto
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }
    
    // Buscar todos os produtos
    public List<Produto> buscarTodos() {
        return produtoRepository.findAll();
    }
    
    // Buscar por ID
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }
    
    // Buscar por ID com informações do restaurante
    public Optional<Produto> buscarPorIdComRestaurante(Long id) {
        return produtoRepository.findByIdWithRestaurante(id);
    }
    
    // Deletar produto
    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
    
    // Métodos de busca conforme solicitado
    
    // Buscar produtos por restaurante
    public List<Produto> buscarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId);
    }
    
    // Buscar produtos por categoria
    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }
    
    // Buscar produtos por disponibilidade
    public List<Produto> buscarPorDisponibilidade(Boolean disponivel) {
        return produtoRepository.findByDisponivel(disponivel);
    }
    
    // Buscar produtos disponíveis por restaurante
    public List<Produto> buscarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findDisponiveisByRestauranteId(restauranteId);
    }
    
    // Buscar produtos por restaurante e categoria
    public List<Produto> buscarPorRestauranteECategoria(Long restauranteId, String categoria) {
        return produtoRepository.findByRestauranteIdAndCategoria(restauranteId, categoria);
    }
    
    // Buscar produtos por restaurante e disponibilidade
    public List<Produto> buscarPorRestauranteEDisponibilidade(Long restauranteId, Boolean disponivel) {
        return produtoRepository.findByRestauranteIdAndDisponivel(restauranteId, disponivel);
    }
    
    // Buscar produtos por categoria e disponibilidade
    public List<Produto> buscarPorCategoriaEDisponibilidade(String categoria, Boolean disponivel) {
        return produtoRepository.findByCategoriaAndDisponivel(categoria, disponivel);
    }
    
    // Buscar produtos por restaurante, categoria e disponibilidade
    public List<Produto> buscarPorRestauranteCategoriaEDisponibilidade(Long restauranteId, String categoria, Boolean disponivel) {
        return produtoRepository.findByRestauranteIdAndCategoriaAndDisponivel(restauranteId, categoria, disponivel);
    }
    
    // Buscar produtos por nome
    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    // Buscar produtos por nome e restaurante
    public List<Produto> buscarPorNomeERestaurante(String nome, Long restauranteId) {
        return produtoRepository.findByNomeContainingIgnoreCaseAndRestauranteId(nome, restauranteId);
    }
    
    // Buscar produtos por faixa de preço
    public List<Produto> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.findByPrecoBetween(precoMin, precoMax);
    }
    
    // Buscar produtos por restaurante e faixa de preço
    public List<Produto> buscarPorRestauranteEFaixaPreco(Long restauranteId, BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.findByRestauranteIdAndPrecoBetween(restauranteId, precoMin, precoMax);
    }
    
    // Buscar todos ordenados por preço
    public List<Produto> buscarTodosOrdenadosPorPreco() {
        return produtoRepository.findAllOrderByPrecoAsc();
    }
    
    // Buscar produtos por restaurante ordenados por preço
    public List<Produto> buscarPorRestauranteOrdenadosPorPreco(Long restauranteId) {
        return produtoRepository.findByRestauranteIdOrderByPrecoAsc(restauranteId);
    }
    
    // Verificar se produto pertence ao restaurante
    public boolean produtoPertenceAoRestaurante(Long produtoId, Long restauranteId) {
        return produtoRepository.existsByIdAndRestauranteId(produtoId, restauranteId);
    }
    
    // Criar produto associado a um restaurante
    public Optional<Produto> criarProdutoParaRestaurante(Long restauranteId, Produto produto) {
        Optional<Restaurante> restauranteOpt = restauranteRepository.findById(restauranteId);
        if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();
            produto.setRestaurante(restaurante);
            return Optional.of(produtoRepository.save(produto));
        }
        return Optional.empty();
    }
    
    // Atualizar disponibilidade do produto
    public Optional<Produto> atualizarDisponibilidade(Long id, Boolean disponivel) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            produto.setDisponivel(disponivel);
            return Optional.of(produtoRepository.save(produto));
        }
        return Optional.empty();
    }
}