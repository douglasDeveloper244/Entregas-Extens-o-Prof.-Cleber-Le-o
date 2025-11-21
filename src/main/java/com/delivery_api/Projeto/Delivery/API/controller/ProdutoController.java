package com.delivery_api.Projeto.Delivery.API.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;
    
    // Criar produto
    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.salvar(produto);
        return ResponseEntity.ok(novoProduto);
    }
    
    // Criar produto para um restaurante específico
    @PostMapping("/restaurante/{restauranteId}")
    public ResponseEntity<Produto> criarProdutoParaRestaurante(
            @PathVariable Long restauranteId, 
            @RequestBody Produto produto) {
        
        Optional<Produto> novoProduto = produtoService.criarProdutoParaRestaurante(restauranteId, produto);
        return novoProduto.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    // Listar todos os produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoService.buscarTodos();
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.buscarPorId(id);
        return produto.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // Buscar produto por ID com informações do restaurante
    @GetMapping("/{id}/com-restaurante")
    public ResponseEntity<Produto> buscarPorIdComRestaurante(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.buscarPorIdComRestaurante(id);
        return produto.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // Atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        Optional<Produto> existente = produtoService.buscarPorId(id);
        if (existente.isPresent()) {
            produto.setId(id);
            Produto atualizado = produtoService.salvar(produto);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.buscarPorId(id);
        if (produto.isPresent()) {
            produtoService.deletar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // Endpoints de busca conforme solicitado
    
    // Buscar produtos por restaurante
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Produto>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        List<Produto> produtos = produtoService.buscarPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos por categoria
    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@RequestParam String categoria) {
        List<Produto> produtos = produtoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos por disponibilidade
    @GetMapping("/buscar/disponibilidade")
    public ResponseEntity<List<Produto>> buscarPorDisponibilidade(@RequestParam Boolean disponivel) {
        List<Produto> produtos = produtoService.buscarPorDisponibilidade(disponivel);
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos disponíveis por restaurante
    @GetMapping("/restaurante/{restauranteId}/disponiveis")
    public ResponseEntity<List<Produto>> buscarDisponiveisPorRestaurante(@PathVariable Long restauranteId) {
        List<Produto> produtos = produtoService.buscarDisponiveisPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos por restaurante e categoria
    @GetMapping("/restaurante/{restauranteId}/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorRestauranteECategoria(
            @PathVariable Long restauranteId, 
            @PathVariable String categoria) {
        
        List<Produto> produtos = produtoService.buscarPorRestauranteECategoria(restauranteId, categoria);
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos por categoria e disponibilidade
    @GetMapping("/buscar/categoria-disponibilidade")
    public ResponseEntity<List<Produto>> buscarPorCategoriaEDisponibilidade(
            @RequestParam String categoria, 
            @RequestParam Boolean disponivel) {
        
        List<Produto> produtos = produtoService.buscarPorCategoriaEDisponibilidade(categoria, disponivel);
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos com filtros combinados
    @GetMapping("/buscar")
    public ResponseEntity<List<Produto>> buscarComFiltros(
            @RequestParam(required = false) Long restauranteId,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean disponivel) {
        
        List<Produto> produtos;
        
        if (restauranteId != null && categoria != null && disponivel != null) {
            produtos = produtoService.buscarPorRestauranteCategoriaEDisponibilidade(restauranteId, categoria, disponivel);
        } else if (restauranteId != null && categoria != null) {
            produtos = produtoService.buscarPorRestauranteECategoria(restauranteId, categoria);
        } else if (restauranteId != null && disponivel != null) {
            produtos = produtoService.buscarPorRestauranteEDisponibilidade(restauranteId, disponivel);
        } else if (categoria != null && disponivel != null) {
            produtos = produtoService.buscarPorCategoriaEDisponibilidade(categoria, disponivel);
        } else if (restauranteId != null) {
            produtos = produtoService.buscarPorRestaurante(restauranteId);
        } else if (categoria != null) {
            produtos = produtoService.buscarPorCategoria(categoria);
        } else if (disponivel != null) {
            produtos = produtoService.buscarPorDisponibilidade(disponivel);
        } else {
            produtos = produtoService.buscarTodos();
        }
        
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos por nome
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        List<Produto> produtos = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos por nome e restaurante
    @GetMapping("/restaurante/{restauranteId}/buscar/nome")
    public ResponseEntity<List<Produto>> buscarPorNomeERestaurante(
            @PathVariable Long restauranteId, 
            @RequestParam String nome) {
        
        List<Produto> produtos = produtoService.buscarPorNomeERestaurante(nome, restauranteId);
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos por faixa de preço
    @GetMapping("/buscar/preco")
    public ResponseEntity<List<Produto>> buscarPorFaixaPreco(
            @RequestParam BigDecimal precoMin, 
            @RequestParam BigDecimal precoMax) {
        
        List<Produto> produtos = produtoService.buscarPorFaixaPreco(precoMin, precoMax);
        return ResponseEntity.ok(produtos);
    }
    
    // Buscar produtos ordenados por preço
    @GetMapping("/ordenados/preco")
    public ResponseEntity<List<Produto>> buscarOrdenadosPorPreco() {
        List<Produto> produtos = produtoService.buscarTodosOrdenadosPorPreco();
        return ResponseEntity.ok(produtos);
    }
    
    // Atualizar disponibilidade do produto
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Produto> atualizarDisponibilidade(
            @PathVariable Long id, 
            @RequestParam Boolean disponivel) {
        
        Optional<Produto> produto = produtoService.atualizarDisponibilidade(id, disponivel);
        return produto.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
}