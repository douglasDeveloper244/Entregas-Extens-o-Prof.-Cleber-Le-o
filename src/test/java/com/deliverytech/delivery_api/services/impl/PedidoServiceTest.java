package com.deliverytech.delivery_api.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

        @InjectMocks
        private PedidoServiceImpl pedidoService;

        @Mock
        private PedidoRepository pedidoRepository;

        @Mock
        private ClienteRepository clienteRepository;

        @Mock
        private RestauranteRepository restauranteRepository;

        @Mock
        private ProdutoRepository produtoRepository;

        @Mock
        private ModelMapper modelMapper;

        private PedidoRequestDTO pedidoRequestDTO;
        private Cliente cliente;
        private Restaurante restaurante;
        private Produto produto;
        private Pedido pedido;
        private PedidoResponseDTO pedidoResponseDTO;

        @BeforeEach
        void setUp() {
                cliente = new Cliente();
                cliente.setId(1L);
                cliente.setAtivo(true);

                restaurante = new Restaurante();
                restaurante.setId(1L);
                restaurante.setAtivo(true);
                restaurante.setTaxaEntrega(BigDecimal.valueOf(10.0));

                produto = new Produto();
                produto.setId(1L);
                produto.setDisponivel(true); // Usando disponivel como proxy para estoque
                produto.setPreco(BigDecimal.valueOf(50.0));
                produto.setRestaurante(restaurante);

                ItemPedidoRequestDTO itemDTO = new ItemPedidoRequestDTO();
                itemDTO.setProdutoId(1L);
                itemDTO.setQuantidade(2);

                List<ItemPedidoRequestDTO> itens = new ArrayList<>();
                itens.add(itemDTO);

                pedidoRequestDTO = new PedidoRequestDTO();
                pedidoRequestDTO.setClienteId(1L);
                pedidoRequestDTO.setRestauranteId(1L);
                pedidoRequestDTO.setItens(itens);
                pedidoRequestDTO.setEnderecoEntrega("Rua Teste");

                pedido = new Pedido();
                pedido.setId(1L);
                pedido.setStatus(StatusPedido.PENDENTE.name());
                pedido.setValorTotal(BigDecimal.valueOf(110.0)); // 2*50 + 10

                pedidoResponseDTO = new PedidoResponseDTO();
                pedidoResponseDTO.setId(1L);
                pedidoResponseDTO.setStatus(StatusPedido.PENDENTE.name());
                pedidoResponseDTO.setValorTotal(BigDecimal.valueOf(110.0));
        }

        @Test
        void criarPedido_ComDadosValidos_DeveRetornarPedido() {
                when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
                when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));
                when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produto));
                when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
                when(modelMapper.map(any(Pedido.class), eq(PedidoResponseDTO.class))).thenReturn(pedidoResponseDTO);

                PedidoResponseDTO result = pedidoService.criarPedido(pedidoRequestDTO);

                assertNotNull(result);
                assertEquals(pedidoResponseDTO.getValorTotal(), result.getValorTotal());
                verify(pedidoRepository).save(any(Pedido.class));
        }

        @Test
        void criarPedido_ComClienteInativo_DeveLancarException() {
                cliente.setAtivo(false);
                when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));

                assertThrows(BusinessException.class, () -> pedidoService.criarPedido(pedidoRequestDTO));
                verify(pedidoRepository, never()).save(any(Pedido.class));
        }

        @Test
        void criarPedido_ComProdutoIndisponivel_DeveLancarException() {
                produto.setDisponivel(false); // Simula estoque insuficiente/indisponível

                when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
                when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));
                when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produto));

                assertThrows(BusinessException.class, () -> pedidoService.criarPedido(pedidoRequestDTO));
                verify(pedidoRepository, never()).save(any(Pedido.class));
        }

        @Test
        void criarPedido_ComProdutoDeOutroRestaurante_DeveLancarException() {
                Restaurante outroRestaurante = new Restaurante();
                outroRestaurante.setId(2L);
                produto.setRestaurante(outroRestaurante);

                when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
                when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));
                when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produto));

                assertThrows(BusinessException.class, () -> pedidoService.criarPedido(pedidoRequestDTO));
                verify(pedidoRepository, never()).save(any(Pedido.class));
        }

        @Test
        void atualizarStatus_TransicaoValida_DeveAtualizarStatus() {
                when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
                when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
                when(modelMapper.map(any(Pedido.class), eq(PedidoResponseDTO.class))).thenReturn(pedidoResponseDTO);

                // PENDENTE -> CONFIRMADO
                pedidoResponseDTO.setStatus(StatusPedido.CONFIRMADO.name());
                PedidoResponseDTO result = pedidoService.atualizarStatusPedido(1L, StatusPedido.CONFIRMADO);

                assertNotNull(result);
                assertEquals(StatusPedido.CONFIRMADO.name(), result.getStatus());
        }

        @Test
        void atualizarStatus_TransicaoInvalida_DeveLancarException() {
                pedido.setStatus(StatusPedido.PENDENTE.name());
                when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedido));

                // PENDENTE -> ENTREGUE (Inválido direto)
                assertThrows(BusinessException.class,
                                () -> pedidoService.atualizarStatusPedido(1L, StatusPedido.ENTREGUE));
                verify(pedidoRepository, never()).save(any(Pedido.class));
        }

        @Test
        void calcularValorTotal_DeveRetornarValorCorreto() {
                when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produto));

                BigDecimal total = pedidoService.calcularValorTotalPedido(pedidoRequestDTO.getItens());

                assertEquals(BigDecimal.valueOf(100.0), total); // 2 * 50
        }
}
