package com.deliverytech.delivery_api.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.PedidoDTO;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    /** Criar novo pedido */
    @Transactional
    public Pedido criarPedido(PedidoDTO dto) throws JsonProcessingException {
        if (dto.getClienteId() == null || dto.getRestauranteId() == null) {
            throw new IllegalArgumentException("Cliente e Restaurante são obrigatórios");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + dto.getClienteId()));

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + dto.getRestauranteId()));

        if (Boolean.FALSE.equals(cliente.getAtivo())) {
            throw new IllegalArgumentException("Cliente inativo não pode fazer pedidos");
        }

        if (Boolean.FALSE.equals(restaurante.getAtivo())) {
            throw new IllegalArgumentException("Restaurante inativo");
        }

        ObjectMapper mapper = new ObjectMapper();

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.PENDENTE.name());
        pedido.setDataPedido(dto.getDataPedido());
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setObservacoes(dto.getObservacoes());
        pedido.setItens(mapper.writeValueAsString(dto.getItens()));

        return pedidoRepository.save(pedido);
    }

    /** Listar pedidos por cliente */
    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + clienteId));
        return pedidoRepository.findByClienteOrderByDataPedidoDesc(cliente);
    }

    /** Atualizar status do pedido */
    @Transactional
    public Pedido atualizarStatus(Long pedidoId, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + pedidoId));

        if (StatusPedido.ENTREGUE.name().equals(pedido.getStatus())) {
            throw new IllegalArgumentException("Pedido já finalizado: " + pedidoId);
        }

        pedido.setStatus(status.name());
        return pedidoRepository.save(pedido);
    }

    /** Relatórios e consultas */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> totalVendasPorRestaurante() {
        return pedidoRepository.totalVendasPorRestaurante();
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosComValorAcima(BigDecimal valorMinimo) {
        return pedidoRepository.buscarPedidosComValorAcima(valorMinimo);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> buscarRelatorioPorPeriodoEStatus(
            LocalDateTime inicio, LocalDateTime fim, String status) {
        return pedidoRepository.buscarRelatorioPorPeriodoEStatus(inicio, fim, status);
    }
}
