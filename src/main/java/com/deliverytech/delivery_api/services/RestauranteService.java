package com.deliverytech.delivery_api.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.entity.RestauranteDTO;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    // ============================================================
    // 🔹 CRUD BÁSICO
    // ============================================================

    /** Cadastrar novo restaurante */
    @Transactional
    public Restaurante cadastrar(Restaurante restaurante) {
        if (restauranteRepository.findByNome(restaurante.getNome()).isPresent()) {
            throw new IllegalArgumentException("Restaurante já cadastrado: " + restaurante.getNome());
        }

        validarDadosRestaurante(restaurante);
        restaurante.setAtivo(true);

        return restauranteRepository.save(restaurante);
    }

    /** Buscar por ID */
    @Transactional(readOnly = true)
    public Optional<RestauranteDTO> findById(Long id) {
        return restauranteRepository.findById(id)
            .map(r -> new RestauranteDTO(
                    r.getId(),
                    r.getNome(),
                    r.getCategoria(),
                    r.getEndereco(),
                    r.getTelefone(),
                    r.getTaxaEntrega(),
                    r.getAvaliacao(),
                    r.getAtivo()
            ));
    }

    /** Listar todos os restaurantes ativos */
    @Transactional(readOnly = true)
    public List<RestauranteDTO> listarAtivos() {
        return restauranteRepository.findByAtivoTrue().stream()
            .map(r -> new RestauranteDTO(
                    r.getId(),
                    r.getNome(),
                    r.getCategoria(),
                    r.getEndereco(),
                    r.getTelefone(),
                    r.getTaxaEntrega(),
                    r.getAvaliacao(),
                    r.getAtivo()
            ))
            .toList();
    }

    /** Atualizar restaurante existente */
    @Transactional
    public Restaurante atualizar(Long id, Restaurante novo) {
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        if (!restaurante.getNome().equalsIgnoreCase(novo.getNome())
            && restauranteRepository.findByNome(novo.getNome()).isPresent()) {
            throw new IllegalArgumentException("Já existe um restaurante com esse nome: " + novo.getNome());
        }

        restaurante.setNome(novo.getNome());
        restaurante.setCategoria(novo.getCategoria());
        restaurante.setEndereco(novo.getEndereco());
        restaurante.setTelefone(novo.getTelefone());
        restaurante.setTaxaEntrega(novo.getTaxaEntrega());
        restaurante.setAvaliacao(novo.getAvaliacao());

        return restauranteRepository.save(restaurante);
    }

    /** Desativar restaurante (inativar) */
    @Transactional
    public void inativar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        restaurante.setAtivo(false);
        restauranteRepository.save(restaurante);
    }

    /** Deletar restaurante */
    @Transactional
    public void deletar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));
        restauranteRepository.delete(restaurante);
    }

    // ============================================================
    // 🔹 FILTROS E PAGINAÇÃO (Roteiro 5)
    // ============================================================

    /**
     * Listar restaurantes por categoria, com suporte a paginação.
     * Exemplo de uso:
     * GET /api/restaurantes?categoria=Pizza&page=0&size=5
     */
    @Transactional(readOnly = true)
    public Page<RestauranteDTO> listarPorCategoria(String categoria, Pageable pageable) {
        Page<Restaurante> page;

        if (categoria != null && !categoria.isBlank()) {
            page = restauranteRepository.findByCategoriaContainingIgnoreCase(categoria, pageable);
        } else {
            page = restauranteRepository.findAll(pageable);
        }

        return page.map(r -> new RestauranteDTO(
                r.getId(),
                r.getNome(),
                r.getCategoria(),
                r.getEndereco(),
                r.getTelefone(),
                r.getTaxaEntrega(),
                r.getAvaliacao(),
                r.getAtivo()
        ));
    }

    // ============================================================
    // 🔹 Validação auxiliar
    // ============================================================
    private void validarDadosRestaurante(Restaurante restaurante) {
        if (restaurante.getNome() == null || restaurante.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (restaurante.getTaxaEntrega() != null
                && restaurante.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa de entrega não pode ser negativa");
        }
    }
}
