package com.deliverytech.delivery_api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.deliverytech.delivery_api.entity.*;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;

import com.deliverytech.delivery_api.security.SecurityUtils;
import com.deliverytech.delivery_api.services.AuthService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.services.PedidoService;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    // -----------------------------
    // CRIAR PEDIDO
    // -----------------------------
    @Override
    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        if (!cliente.isAtivo()) {
            throw new BusinessException("Cliente está inativo");
        }

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

        if (!restaurante.isAtivo()) {
            throw new BusinessException("Restaurante não está disponível");
        }

        List<ItemPedido> itensPedido = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDTO : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Produto não encontrado: " + itemDTO.getProdutoId()));

            if (!produto.isAtivo()) {
                throw new BusinessException("Produto indisponível: " + produto.getNome());
            }

            if (!produto.getRestaurante().getId().equals(dto.getRestauranteId())) {
                throw new BusinessException("Produto não pertence ao restaurante informado");
            }

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));

            itensPedido.add(item);
            subtotal = subtotal.add(item.getSubtotal());
        }

        BigDecimal taxa = restaurante.getTaxaEntrega();
        BigDecimal total = subtotal.add(taxa);

        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setObservacoes(dto.getObservacoes());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE.name());
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setTaxaEntrega(taxa);
        pedido.setValorTotal(total);

        Pedido salvo = pedidoRepository.save(pedido);

        itensPedido.forEach(i -> i.setPedido(salvo));
        salvo.setItens(itensPedido);

        return modelMapper.map(salvo, PedidoResponseDTO.class);
    }

    // -----------------------------
    // BUSCAR POR ID
    // -----------------------------
    @Override
    public PedidoResponseDTO buscarPorId(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    // -----------------------------
    // LISTAR POR CLIENTE
    // -----------------------------
    @Override
    public List<PedidoResponseDTO> listarPedidosPorCliente(Long clienteId) {

        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);

        if (pedidos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum pedido encontrado para este cliente");
        }

        return pedidos.stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .toList();
    }

    // -----------------------------
    // ATUALIZAR STATUS
    // -----------------------------
    @Override
    public PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedido novoStatus) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        StatusPedido atual = StatusPedido.valueOf(pedido.getStatus());

        if (!isTransicaoValida(atual, novoStatus)) {
            throw new BusinessException("Transição de status inválida");
        }

        pedido.setStatus(novoStatus.name());
        Pedido atualizado = pedidoRepository.save(pedido);

        return modelMapper.map(atualizado, PedidoResponseDTO.class);
    }

    private boolean isTransicaoValida(StatusPedido atual, StatusPedido novo) {
        return switch (atual) {
            case PENDENTE -> novo == StatusPedido.CONFIRMADO || novo == StatusPedido.CANCELADO;
            case CONFIRMADO -> novo == StatusPedido.PREPARANDO || novo == StatusPedido.CANCELADO;
            case PREPARANDO -> novo == StatusPedido.SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA -> novo == StatusPedido.ENTREGUE;
            default -> false;
        };
    }

    // -----------------------------
    // CALCULAR VALOR TOTAL
    // -----------------------------
    @Override
    public BigDecimal calcularValorTotalPedido(List<ItemPedidoRequestDTO> itens) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO item : itens) {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

            total = total.add(produto.getPreco()
                    .multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        return total;
    }

    // -----------------------------
    // CANCELAR PEDIDO
    // -----------------------------
    @Override
    public PedidoResponseDTO cancelarPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        StatusPedido statusAtual = StatusPedido.valueOf(pedido.getStatus());

        if (!podeSerCancelado(statusAtual)) {
            throw new BusinessException("Não é possível cancelar neste status");
        }

        pedido.setStatus(StatusPedido.CANCELADO.name());

        pedidoRepository.save(pedido);

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    private boolean podeSerCancelado(StatusPedido status) {
        return status == StatusPedido.PENDENTE || status == StatusPedido.CONFIRMADO;
    }

    // -----------------------------
    // CONTROLE DE ACESSO
    // -----------------------------
    @Override
    public boolean canAccess(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElse(null);

        if (pedido == null)
            return false;

        Usuario user = SecurityUtils.getCurrentUser();
        if (user == null)
            return false;

        if (SecurityUtils.isAdmin())
            return true;

        if (SecurityUtils.isCliente()) {
            return pedido.getCliente().getEmail().equals(user.getEmail());
        }

        if (SecurityUtils.isRestaurante()) {
            return pedido.getRestaurante().getId().equals(user.getRestauranteId());
        }

        return false;
    }

    // -----------------------------
    // LISTAR TODOS (ADMIN / DONO)
    // -----------------------------
    @Override
    public List<PedidoResponseDTO> listarTodos() {

        Usuario user = authService.getUsuarioAutenticado();

        if (user.getRole() == Role.ADMIN) {
            return pedidoRepository.findAll()
                    .stream()
                    .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                    .toList();
        }

        if (user.getRole() == Role.RESTAURANTE) {

            if (user.getRestauranteId() == null) {
                throw new BusinessException("Usuário dono não possui restaurante vinculado.");
            }

            return pedidoRepository.findByRestauranteId(user.getRestauranteId())
                    .stream()
                    .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                    .toList();
        }

        throw new BusinessException("Acesso negado.");
    }
}
