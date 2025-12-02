package com.deliverytech.delivery_api.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.ItemPedido;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

@Configuration
@Profile("!test") // Não rodar nos testes unitários
public class DataLoader implements CommandLineRunner {

        @Autowired
        private ClienteRepository clienteRepository;

        @Autowired
        private RestauranteRepository restauranteRepository;

        @Autowired
        private ProdutoRepository produtoRepository;

        @Autowired
        private PedidoRepository pedidoRepository;

        @Override
        public void run(String... args) throws Exception {
                if (clienteRepository.count() > 0) {
                        System.out.println("Dados já carregados. Pulando DataLoader.");
                        return;
                }

                System.out.println("Iniciando carga de dados...");

                // 1. Clientes
                Cliente c1 = new Cliente(null, "João Silva", "joao@email.com", "11999990001", "Rua A, 100",
                                LocalDateTime.now(), true);
                Cliente c2 = new Cliente(null, "Maria Souza", "maria@email.com", "11999990002", "Rua B, 200",
                                LocalDateTime.now(), true);
                Cliente c3 = new Cliente(null, "Carlos Lima", "carlos@email.com", "11999990003", "Rua C, 300",
                                LocalDateTime.now(), true);
                clienteRepository.saveAll(List.of(c1, c2, c3));

                // 2. Restaurantes
                Restaurante r1 = new Restaurante(null, "Pizza Express", "Pizza", "Av Paulista, 1000", "1133330001",
                                new BigDecimal("5.00"), new BigDecimal("4.5"), true);
                Restaurante r2 = new Restaurante(null, "Burger King", "Hamburguer", "Av Faria Lima, 2000", "1133330002",
                                new BigDecimal("7.00"), new BigDecimal("4.2"), true);
                restauranteRepository.saveAll(List.of(r1, r2));

                // 3. Produtos
                Produto p1 = new Produto(null, "Pizza Calabresa", "Queijo e calabresa", new BigDecimal("40.00"),
                                "Pizza", true, r1.getId());
                Produto p2 = new Produto(null, "Pizza 4 Queijos", "Mozzarella, provolone, gorgonzola, parmesão",
                                new BigDecimal("45.00"), "Pizza", true, r1.getId());
                Produto p3 = new Produto(null, "Coca Cola 2L", "Refrigerante", new BigDecimal("12.00"), "Bebida", true,
                                r1.getId());

                Produto p4 = new Produto(null, "Whopper", "Hamburguer grelhado", new BigDecimal("25.00"), "Lanche",
                                true, r2.getId());
                Produto p5 = new Produto(null, "Batata Frita", "Batata crocante", new BigDecimal("10.00"),
                                "Acompanhamento", true, r2.getId());
                produtoRepository.saveAll(List.of(p1, p2, p3, p4, p5));

                // 4. Pedidos
                // Pedido 1: João pede Pizza Calabresa e 2 Coca Colas no Pizza Express
                BigDecimal totalPedido1 = p1.getPreco().add(p3.getPreco().multiply(new BigDecimal("2")))
                                .add(r1.getTaxaEntrega());

                Pedido ped1 = new Pedido();
                ped1.setCliente(c1);
                ped1.setRestaurante(r1);
                ped1.setStatus(StatusPedido.PENDENTE);
                ped1.setDataPedido(LocalDateTime.now().minusHours(1));
                ped1.setNumeroPedido("PED-001");
                ped1.setValorTotal(totalPedido1);
                ped1.setObservacoes("Sem cebola");

                ItemPedido item1 = new ItemPedido();
                item1.setProduto(p1);
                item1.setQuantidade(1);
                ped1.adicionarItem(item1);

                ItemPedido item2 = new ItemPedido();
                item2.setProduto(p3);
                item2.setQuantidade(2);
                ped1.adicionarItem(item2);

                pedidoRepository.save(ped1);

                // Pedido 2: Maria pede 2 Whoppers no Burger King
                BigDecimal totalPedido2 = p4.getPreco().multiply(new BigDecimal("2")).add(r2.getTaxaEntrega());

                Pedido ped2 = new Pedido();
                ped2.setCliente(c2);
                ped2.setRestaurante(r2);
                ped2.setStatus(StatusPedido.ENTREGUE);
                ped2.setDataPedido(LocalDateTime.now().minusDays(1));
                ped2.setNumeroPedido("PED-002");
                ped2.setValorTotal(totalPedido2);
                ped2.setObservacoes("Capricha no molho");

                ItemPedido item3 = new ItemPedido();
                item3.setProduto(p4);
                item3.setQuantidade(2);
                ped2.adicionarItem(item3);

                pedidoRepository.save(ped2);

                System.out.println("Carga de dados finalizada com sucesso!");

                // ========================================
                // 2.2 VALIDAÇÃO DAS CONSULTAS DERIVADAS
                // ========================================
                System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║          VALIDAÇÃO DAS CONSULTAS DERIVADAS                     ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

                // ========== CLIENTE REPOSITORY ==========
                System.out.println("┌─────────────────────────────────────────────────────────────┐");
                System.out.println("│ 📋 CLIENTE REPOSITORY - Consultas Derivadas                 │");
                System.out.println("└─────────────────────────────────────────────────────────────┘");

                System.out.println("✓ findByAtivoTrue(): " + clienteRepository.findByAtivoTrue().size()
                                + " clientes ativos");
                clienteRepository.findByAtivoTrue()
                                .forEach(c -> System.out.println("  → " + c.getNome() + " (" + c.getEmail() + ")"));

                System.out.println("\n✓ findByEmail('joao@email.com'): " +
                                (clienteRepository.findByEmail("joao@email.com").isPresent() ? "Encontrado!"
                                                : "Não encontrado"));
                clienteRepository.findByEmail("joao@email.com").ifPresent(
                                c -> System.out.println("  → " + c.getNome() + " - Tel: " + c.getTelefone()));

                System.out.println("\n✓ existsByEmail('maria@email.com'): " +
                                clienteRepository.existsByEmail("maria@email.com"));

                System.out.println("\n✓ findByNomeContainingIgnoreCase('silva'): " +
                                clienteRepository.findByNomeContainingIgnoreCase("silva").size() + " resultado(s)");
                clienteRepository.findByNomeContainingIgnoreCase("silva")
                                .forEach(c -> System.out.println("  → " + c.getNome()));

                // ========== RESTAURANTE REPOSITORY ==========
                System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
                System.out.println("│ 🍽️  RESTAURANTE REPOSITORY - Consultas Derivadas            │");
                System.out.println("└─────────────────────────────────────────────────────────────┘");

                System.out.println("✓ findByAtivoTrue(): " + restauranteRepository.findByAtivoTrue().size()
                                + " restaurantes ativos");
                restauranteRepository.findByAtivoTrue().forEach(r -> System.out.println("  → " + r.getNome() + " - "
                                + r.getCategoria() + " (Taxa: R$ " + r.getTaxaEntrega() + ")"));

                System.out.println("\n✓ findByCategoriaIgnoreCase('pizza'): " +
                                restauranteRepository.findByCategoriaIgnoreCase("pizza").size() + " resultado(s)");
                restauranteRepository.findByCategoriaIgnoreCase("pizza").forEach(
                                r -> System.out.println("  → " + r.getNome() + " - Avaliação: " + r.getAvaliacao()));

                System.out.println("\n✓ findByTaxaEntregaLessThanEqual(6.00): " +
                                restauranteRepository.findByTaxaEntregaLessThanEqual(new BigDecimal("6.00")).size()
                                + " resultado(s)");
                restauranteRepository.findByTaxaEntregaLessThanEqual(new BigDecimal("6.00")).forEach(
                                r -> System.out.println("  → " + r.getNome() + " - Taxa: R$ " + r.getTaxaEntrega()));

                System.out.println("\n✓ findTop5ByOrderByNomeAsc(): " +
                                restauranteRepository.findTop5ByOrderByNomeAsc().size() + " resultado(s)");
                restauranteRepository.findTop5ByOrderByNomeAsc().forEach(r -> System.out.println("  → " + r.getNome()));

                // ========== PRODUTO REPOSITORY ==========
                System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
                System.out.println("│ 🍕 PRODUTO REPOSITORY - Consultas Derivadas                 │");
                System.out.println("└─────────────────────────────────────────────────────────────┘");

                System.out.println("✓ findByRestauranteId(" + r1.getId() + "): " +
                                produtoRepository.findByRestauranteId(r1.getId()).size() + " produtos");
                produtoRepository.findByRestauranteId(r1.getId())
                                .forEach(p -> System.out.println("  → " + p.getNome() + " - R$ " + p.getPreco()));

                System.out.println("\n✓ findByDisponivelTrue(): " +
                                produtoRepository.findByDisponivelTrue().size() + " produtos disponíveis");

                System.out.println("\n✓ findByCategoriaIgnoreCase('pizza'): " +
                                produtoRepository.findByCategoriaIgnoreCase("pizza").size() + " resultado(s)");
                produtoRepository.findByCategoriaIgnoreCase("pizza")
                                .forEach(p -> System.out.println("  → " + p.getNome() + " - " + p.getDescricao()));

                System.out.println("\n✓ findByPrecoLessThanEqual(15.00): " +
                                produtoRepository.findByPrecoLessThanEqual(new BigDecimal("15.00")).size()
                                + " resultado(s)");
                produtoRepository.findByPrecoLessThanEqual(new BigDecimal("15.00"))
                                .forEach(p -> System.out.println("  → " + p.getNome() + " - R$ " + p.getPreco()));

                // ========== PEDIDO REPOSITORY ==========
                System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
                System.out.println("│ 📦 PEDIDO REPOSITORY - Consultas Derivadas                  │");
                System.out.println("└─────────────────────────────────────────────────────────────┘");

                System.out.println("✓ findByClienteOrderByDataPedidoDesc(c1): " +
                                pedidoRepository.findByClienteOrderByDataPedidoDesc(c1).size() + " pedido(s)");
                pedidoRepository.findByClienteOrderByDataPedidoDesc(c1).forEach(p -> System.out.println(
                                "  → " + p.getNumeroPedido() + " - R$ " + p.getValorTotal() + " - " + p.getStatus()));

                System.out.println("\n✓ findByStatus(PENDENTE): " +
                                pedidoRepository.findByStatus(StatusPedido.PENDENTE).size() + " pedido(s)");
                pedidoRepository.findByStatus(StatusPedido.PENDENTE).forEach(p -> System.out
                                .println("  → " + p.getNumeroPedido() + " - Cliente: " + p.getCliente().getNome()));

                System.out.println("\n✓ findByStatus(ENTREGUE): " +
                                pedidoRepository.findByStatus(StatusPedido.ENTREGUE).size() + " pedido(s)");

                System.out.println("\n✓ findByClienteId(" + c1.getId() + "): " +
                                pedidoRepository.findByClienteId(c1.getId()).size() + " pedido(s)");

                System.out.println("\n✓ findTop10ByOrderByDataPedidoDesc(): " +
                                pedidoRepository.findTop10ByOrderByDataPedidoDesc().size()
                                + " pedido(s) mais recentes");
                pedidoRepository.findTop10ByOrderByDataPedidoDesc().forEach(
                                p -> System.out.println("  → " + p.getNumeroPedido() + " - " + p.getDataPedido()));

                // ========== CONSULTAS CUSTOMIZADAS (@Query) ==========
                System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
                System.out.println("│ 🔍 CONSULTAS CUSTOMIZADAS (@Query)                          │");
                System.out.println("└─────────────────────────────────────────────────────────────┘");

                System.out.println("\n✓ totalVendasPorRestaurante():");
                pedidoRepository.totalVendasPorRestaurante().forEach(
                                p -> System.out.println("  → " + p.getRestaurante() + ": R$ " + p.getTotalVendas()));

                System.out.println("\n✓ findProdutosMaisVendidos():");
                pedidoRepository.findProdutosMaisVendidos().forEach(
                                p -> System.out.println(
                                                "  → " + p.getProduto() + ": " + p.getQuantidadeTotal() + " unidades"));

                System.out.println("\n✓ findRankingClientes():");
                pedidoRepository.findRankingClientes().forEach(
                                m -> System.out.println("  → " + m.get("CLIENTE") + ": " + m.get("TOTALPEDIDOS")
                                                + " pedido(s)"));

                System.out.println("\n✓ findFaturamentoPorCategoria():");
                pedidoRepository.findFaturamentoPorCategoria().forEach(
                                m -> System.out.println("  → " + m.get("CATEGORIA") + ": R$ " + m.get("FATURAMENTO")));

                System.out.println("\n✓ buscarPedidosComValorAcima(50.00): " +
                                pedidoRepository.buscarPedidosComValorAcima(new BigDecimal("50.00")).size()
                                + " pedido(s)");
                pedidoRepository.buscarPedidosComValorAcima(new BigDecimal("50.00")).forEach(
                                p -> System.out.println("  → " + p.getNumeroPedido() + " - R$ " + p.getValorTotal()));

                // ========== VERIFICAÇÃO DE RELACIONAMENTOS ==========
                System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
                System.out.println("│ 🔗 VERIFICAÇÃO DE RELACIONAMENTOS                           │");
                System.out.println("└─────────────────────────────────────────────────────────────┘");

                System.out.println("\n✓ Pedido 1 - Itens:");
                Pedido pedido1 = pedidoRepository.findById(ped1.getId()).orElse(null);
                if (pedido1 != null) {
                        System.out.println("  Cliente: " + pedido1.getCliente().getNome());
                        System.out.println("  Restaurante: " + pedido1.getRestaurante().getNome());
                        System.out.println("  Total de itens: " + pedido1.getItens().size());
                        pedido1.getItens().forEach(item -> System.out
                                        .println("    → " + item.getProduto().getNome() + " x" + item.getQuantidade()));
                }

                System.out.println("\n✓ Pedido 2 - Itens:");
                Pedido pedido2 = pedidoRepository.findById(ped2.getId()).orElse(null);
                if (pedido2 != null) {
                        System.out.println("  Cliente: " + pedido2.getCliente().getNome());
                        System.out.println("  Restaurante: " + pedido2.getRestaurante().getNome());
                        System.out.println("  Total de itens: " + pedido2.getItens().size());
                        pedido2.getItens().forEach(item -> System.out
                                        .println("    → " + item.getProduto().getNome() + " x" + item.getQuantidade()));
                }

                System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║  ✅ TODAS AS CONSULTAS VALIDADAS COM SUCESSO!                 ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        }
}
