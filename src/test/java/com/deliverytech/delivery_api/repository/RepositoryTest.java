package com.deliverytech.delivery_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.enums.StatusPedido;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoryTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private ClienteRepository clienteRepository;

        @Autowired
        private RestauranteRepository restauranteRepository;

        @Autowired
        private ProdutoRepository produtoRepository;

        @Autowired
        private PedidoRepository pedidoRepository;

        @Test
        public void testClienteRepository() {
                Cliente cliente = new Cliente();
                cliente.setNome("Test Client");
                cliente.setEmail("test@example.com");
                cliente.setAtivo(true);
                entityManager.persist(cliente);
                entityManager.flush();

                Optional<Cliente> found = clienteRepository.findByEmail("test@example.com");
                assertThat(found).isPresent();
                assertThat(found.get().getNome()).isEqualTo("Test Client");

                List<Cliente> ativos = clienteRepository.findByAtivoTrue();
                assertThat(ativos).isNotEmpty();

                List<Cliente> byName = clienteRepository.findByNomeContainingIgnoreCase("Test");
                assertThat(byName).isNotEmpty();

                boolean exists = clienteRepository.existsByEmail("test@example.com");
                assertThat(exists).isTrue();
        }

        @Test
        public void testRestauranteRepository() {
                Restaurante restaurante = new Restaurante();
                restaurante.setNome("Test Restaurant");
                restaurante.setCategoria("Pizza");
                restaurante.setTaxaEntrega(new BigDecimal("5.00"));
                restaurante.setAtivo(true);
                entityManager.persist(restaurante);
                entityManager.flush();

                List<Restaurante> byCategoria = restauranteRepository.findByCategoria("Pizza");
                assertThat(byCategoria).isNotEmpty();

                List<Restaurante> ativos = restauranteRepository.findByAtivoTrue();
                assertThat(ativos).isNotEmpty();

                List<Restaurante> byTaxa = restauranteRepository
                                .findByTaxaEntregaLessThanEqual(new BigDecimal("10.00"));
                assertThat(byTaxa).isNotEmpty();

                List<Restaurante> top5 = restauranteRepository.findTop5ByOrderByNomeAsc();
                assertThat(top5).isNotEmpty();
        }

        @Test
        public void testProdutoRepository() {
                Restaurante restaurante = new Restaurante();
                restaurante.setNome("Burger Place");
                entityManager.persist(restaurante);

                Produto produto = new Produto();
                produto.setNome("Burger");
                produto.setPreco(new BigDecimal("15.00"));
                produto.setCategoria("Lanches");
                produto.setDisponivel(true);
                produto.setRestauranteId(restaurante.getId());
                entityManager.persist(produto);
                entityManager.flush();

                List<Produto> byRestaurante = produtoRepository.findByRestauranteId(restaurante.getId());
                assertThat(byRestaurante).isNotEmpty();

                List<Produto> disponiveis = produtoRepository.findByDisponivelTrue();
                assertThat(disponiveis).isNotEmpty();

                List<Produto> byCategoria = produtoRepository.findByCategoria("Lanches");
                assertThat(byCategoria).isNotEmpty();

                List<Produto> byPreco = produtoRepository.findByPrecoLessThanEqual(new BigDecimal("20.00"));
                assertThat(byPreco).isNotEmpty();
        }

        @Test
        public void testPedidoRepository() {
                Cliente cliente = new Cliente();
                cliente.setNome("Order Client");
                entityManager.persist(cliente);

                Restaurante restaurante = new Restaurante();
                restaurante.setNome("Order Restaurant");
                entityManager.persist(restaurante);

                Pedido pedido = new Pedido();
                pedido.setCliente(cliente);
                pedido.setRestaurante(restaurante);
                pedido.setStatus(StatusPedido.PENDENTE);
                pedido.setDataPedido(LocalDateTime.now());
                pedido.setValorTotal(new BigDecimal("50.00"));
                entityManager.persist(pedido);
                entityManager.flush();

                List<Pedido> byCliente = pedidoRepository.findByClienteId(cliente.getId());
                assertThat(byCliente).isNotEmpty();

                List<Pedido> byStatus = pedidoRepository.findByStatus(StatusPedido.PENDENTE);
                assertThat(byStatus).isNotEmpty();

                List<Pedido> top10 = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
                assertThat(top10).isNotEmpty();

                List<Pedido> byDate = pedidoRepository.findByDataPedidoBetween(LocalDateTime.now().minusDays(1),
                                LocalDateTime.now().plusDays(1));
                assertThat(byDate).isNotEmpty();
        }
}
