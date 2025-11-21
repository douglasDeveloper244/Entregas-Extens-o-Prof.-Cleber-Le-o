package com.delivery_api.Projeto.Delivery.API.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_api.Projeto.Delivery.API.dto.PedidoRequest;
import com.delivery_api.Projeto.Delivery.API.entity.Cliente;
import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.repository.ClienteRepository;
import com.delivery_api.Projeto.Delivery.API.repository.PedidoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Transactional
    public Pedido criarPedido(PedidoRequest request) {
        validarPedidoRequest(request);
        
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + request.getClienteId()));
                
        Restaurante restaurante = restauranteRepository.findById(request.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + request.getRestauranteId()));

        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(gerarNumeroPedido());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");
        pedido.setValorTotal(request.getValorTotal());
        pedido.setObservacoes(request.getObservacoes());
        pedido.setItens(request.getItens());
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido criarPedidoSimples(Pedido pedido) {
        validarPedidoParaCriacao(pedido);

        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().trim().isEmpty()) {
            pedido.setNumeroPedido(gerarNumeroPedido());
        }

        if (pedido.getDataPedido() == null) {
            pedido.setDataPedido(LocalDateTime.now());
        }

        if (pedido.getStatus() == null || pedido.getStatus().trim().isEmpty()) {
            pedido.setStatus("PENDENTE");
        } else {
            pedido.setStatus(pedido.getStatus().trim().toUpperCase());
        }

        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorNumeroPedido(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorClienteEStatus(Long clienteId, String status) {
        if (status != null) {
            status = status.trim().toUpperCase();
        }
        return pedidoRepository.findByClienteIdAndStatus(clienteId, status);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorStatus(String status) {
        if (status != null) {
            status = status.trim().toUpperCase();
        }
        return pedidoRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public Optional<Pedido> atualizarStatus(Long id, String novoStatus) {
        if (novoStatus == null || novoStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode ser vazio");
        }

        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(novoStatus.trim().toUpperCase());
                    return pedidoRepository.save(pedido);
                });
    }

    @Transactional
    public Optional<Pedido> atualizarPedido(Long id, PedidoRequest request) {
        validarPedidoRequest(request);
        
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    Cliente cliente = clienteRepository.findById(request.getClienteId())
                            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + request.getClienteId()));
                            
                    Restaurante restaurante = restauranteRepository.findById(request.getRestauranteId())
                            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + request.getRestauranteId()));

                    pedido.setValorTotal(request.getValorTotal());
                    pedido.setObservacoes(request.getObservacoes());
                    pedido.setItens(request.getItens());
                    pedido.setCliente(cliente);
                    pedido.setRestaurante(restaurante);

                    return pedidoRepository.save(pedido);
                });
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        if (pedido.getId() != null && pedidoRepository.findById(pedido.getId()).isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado para atualização");
        }
        return pedidoRepository.save(pedido);
    }

    private String gerarNumeroPedido() {
        return "PED" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void validarPedidoParaCriacao(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        if (pedido.getRestaurante() == null || pedido.getRestaurante().getId() == null) {
            throw new IllegalArgumentException("Restaurante é obrigatório");
        }

        if (pedido.getValorTotal() == null || pedido.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor total deve ser maior que zero");
        }
    }
    
    private void validarPedidoRequest(PedidoRequest request) {
        if (request.getClienteId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório");
        }
        
        if (request.getRestauranteId() == null) {
            throw new IllegalArgumentException("ID do restaurante é obrigatório");
        }
        
        if (request.getValorTotal() == null || request.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor total deve ser maior que zero");
        }
    }
}