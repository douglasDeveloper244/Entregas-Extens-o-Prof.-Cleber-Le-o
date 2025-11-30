package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery_api.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PedidoControllerIT {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private PedidoRepository pedidoRepository;

        @Autowired
        private ClienteRepository clienteRepository;

        @Autowired
        private RestauranteRepository restauranteRepository;

        @Autowired
        private ProdutoRepository produtoRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private Cliente cliente;
        private Restaurante restaurante;
        private Produto produto;

        @BeforeEach
        void setUp() {
                pedidoRepository.deleteAll();
                produtoRepository.deleteAll();
                restauranteRepository.deleteAll();
                clienteRepository.deleteAll();

                cliente = new Cliente();
                cliente.setNome("Cliente Pedido");
                cliente.setEmail("cliente@pedido.com");
                cliente.setAtivo(true);
                cliente = clienteRepository.save(cliente);

                restaurante = new Restaurante();
                restaurante.setNome("Restaurante Teste");
                restaurante.setTaxaEntrega(BigDecimal.valueOf(10.0));
                restaurante.setAtivo(true);
                restaurante = restauranteRepository.save(restaurante);

                produto = new Produto();
                produto.setNome("Pizza");
                produto.setPreco(BigDecimal.valueOf(50.0));
                produto.setDisponivel(true);
                produto.setRestaurante(restaurante);
                produto = produtoRepository.save(produto);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void criarPedido_ComDadosValidos_DeveRetornar201() throws Exception {
                ItemPedidoRequestDTO item = new ItemPedidoRequestDTO();
                item.setProdutoId(produto.getId());
                item.setQuantidade(2);

                List<ItemPedidoRequestDTO> itens = new ArrayList<>();
                itens.add(item);

                PedidoRequestDTO dto = new PedidoRequestDTO();
                dto.setClienteId(cliente.getId());
                dto.setRestauranteId(restaurante.getId());
                dto.setItens(itens);
                dto.setEnderecoEntrega("Rua Entrega, 100");

                mockMvc.perform(post("/api/pedidos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.valorTotal", is(110.0))) // 2*50 + 10
                                .andExpect(jsonPath("$.status", is("PENDENTE")));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void criarPedido_ComProdutoInexistente_DeveRetornar404() throws Exception {
                ItemPedidoRequestDTO item = new ItemPedidoRequestDTO();
                item.setProdutoId(999L);
                item.setQuantidade(1);

                List<ItemPedidoRequestDTO> itens = new ArrayList<>();
                itens.add(item);

                PedidoRequestDTO dto = new PedidoRequestDTO();
                dto.setClienteId(cliente.getId());
                dto.setRestauranteId(restaurante.getId());
                dto.setItens(itens);
                dto.setEnderecoEntrega("Rua Entrega, 100");

                mockMvc.perform(post("/api/pedidos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void listarPedidosPorCliente_DeveRetornar200() throws Exception {
                Pedido pedido = new Pedido();
                pedido.setCliente(cliente);
                pedido.setRestaurante(restaurante);
                pedido.setStatus(StatusPedido.PENDENTE.name());
                pedido.setValorTotal(BigDecimal.valueOf(60.0));
                pedidoRepository.save(pedido);

                mockMvc.perform(get("/api/pedidos/cliente/{id}", cliente.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void atualizarStatusPedido_DeveRetornar200() throws Exception {
                Pedido pedido = new Pedido();
                pedido.setCliente(cliente);
                pedido.setRestaurante(restaurante);
                pedido.setStatus(StatusPedido.PENDENTE.name());
                pedido.setValorTotal(BigDecimal.valueOf(60.0));
                pedido = pedidoRepository.save(pedido);

                mockMvc.perform(put("/api/pedidos/{id}/status", pedido.getId())
                                .param("status", "CONFIRMADO"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status", is("CONFIRMADO")));
        }
}
