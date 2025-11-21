package com.delivery_api.Projeto.Delivery.API.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.service.RestauranteService;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {
    
    @Autowired
    private RestauranteService restauranteService;
    
    // Criar restaurante
    @PostMapping
    public ResponseEntity<Restaurante> criarRestaurante(@RequestBody Restaurante restaurante) {
        Restaurante novoRestaurante = restauranteService.salvar(restaurante);
        return ResponseEntity.ok(novoRestaurante);
    }
    
    // Listar todos os restaurantes
    @GetMapping
    public ResponseEntity<List<Restaurante>> listarTodos() {
        List<Restaurante> restaurantes = restauranteService.buscarTodos();
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar restaurante por ID
    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long id) {
        Optional<Restaurante> restaurante = restauranteService.buscarPorId(id);
        return restaurante.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    // Atualizar restaurante
    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizarRestaurante(@PathVariable Long id, @RequestBody Restaurante restaurante) {
        Optional<Restaurante> existente = restauranteService.buscarPorId(id);
        if (existente.isPresent()) {
            restaurante.setId(id);
            Restaurante atualizado = restauranteService.salvar(restaurante);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Deletar restaurante
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletarRestaurante(@PathVariable Long id) {
        Optional<Restaurante> restaurante = restauranteService.buscarPorId(id);
        if (restaurante.isPresent()) {
                restauranteService.deletar(id);
                // Retorna 204 (sem conteúdo) após deletar com sucesso
                return ResponseEntity.noContent().build();
        }
        // Retorna 404 se o restaurante não existir
        return ResponseEntity.notFound().build();
        }
    
    // Endpoints de busca conforme solicitado
    
    // Buscar por nome
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<Restaurante>> buscarPorNome(@RequestParam String nome) {
        List<Restaurante> restaurantes = restauranteService.buscarPorNome(nome);
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar por categoria
    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<Restaurante>> buscarPorCategoria(@RequestParam String categoria) {
        List<Restaurante> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar por status ativo
    @GetMapping("/buscar/ativo")
    public ResponseEntity<List<Restaurante>> buscarPorAtivo(@RequestParam Boolean ativo) {
        List<Restaurante> restaurantes = restauranteService.buscarPorAtivo(ativo);
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar por taxa de entrega máxima
    @GetMapping("/buscar/taxa-entrega")
    public ResponseEntity<List<Restaurante>> buscarPorTaxaEntregaMaxima(@RequestParam BigDecimal taxaMaxima) {
        List<Restaurante> restaurantes = restauranteService.buscarPorTaxaEntregaMaxima(taxaMaxima);
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar com entrega grátis
    @GetMapping("/buscar/entrega-gratis")
    public ResponseEntity<List<Restaurante>> buscarComEntregaGratis() {
        List<Restaurante> restaurantes = restauranteService.buscarComEntregaGratis();
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar ativos com entrega grátis
    @GetMapping("/buscar/ativos/entrega-gratis")
    public ResponseEntity<List<Restaurante>> buscarAtivosComEntregaGratis() {
        List<Restaurante> restaurantes = restauranteService.buscarAtivosComEntregaGratis();
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar todos ordenados por avaliação
    @GetMapping("/ordenados/avaliacao")
    public ResponseEntity<List<Restaurante>> buscarOrdenadosPorAvaliacao() {
        List<Restaurante> restaurantes = restauranteService.buscarTodosOrdenadosPorAvaliacao();
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar todos ordenados por taxa de entrega
    @GetMapping("/ordenados/taxa-entrega")
    public ResponseEntity<List<Restaurante>> buscarOrdenadosPorTaxaEntrega() {
        List<Restaurante> restaurantes = restauranteService.buscarTodosOrdenadosPorTaxaEntrega();
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar ativos ordenados por avaliação
    @GetMapping("/ativos/ordenados/avaliacao")
    public ResponseEntity<List<Restaurante>> buscarAtivosOrdenadosPorAvaliacao() {
        List<Restaurante> restaurantes = restauranteService.buscarAtivosOrdenadosPorAvaliacao();
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar ativos ordenados por taxa de entrega
    @GetMapping("/ativos/ordenados/taxa-entrega")
    public ResponseEntity<List<Restaurante>> buscarAtivosOrdenadosPorTaxaEntrega() {
        List<Restaurante> restaurantes = restauranteService.buscarAtivosOrdenadosPorTaxaEntrega();
        return ResponseEntity.ok(restaurantes);
    }
    
    // Buscar com filtros combinados
    @GetMapping("/buscar")
    public ResponseEntity<List<Restaurante>> buscarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) BigDecimal taxaMaxima) {
        
        List<Restaurante> restaurantes = restauranteService.buscarComFiltros(nome, categoria, ativo, taxaMaxima);
        return ResponseEntity.ok(restaurantes);
    }
}