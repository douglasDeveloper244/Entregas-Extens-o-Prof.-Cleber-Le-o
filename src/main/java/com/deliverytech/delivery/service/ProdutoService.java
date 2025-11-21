package com.deliverytech.delivery.service;

import java.util.List;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;

public interface ProdutoService {
    ProdutoResponseDTO cadastrarProduto(ProdutoDTO dto);

    List<ProdutoResponseDTO> buscarProdutosPorRestaurante(Long restauranteId);

    ProdutoResponseDTO buscarProdutoPorId(Long id);

    ProdutoResponseDTO atualizarProduto(Long id, ProdutoDTO dto);

    void alterarDisponibilidade(Long id, boolean disponivel);

    List<ProdutoResponseDTO> buscarProdutosPorCategoria(String categoria);

    List<ProdutoResponseDTO> listarTodos();
}
