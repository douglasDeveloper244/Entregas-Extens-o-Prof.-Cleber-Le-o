package com.deliverytech.delivery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.ItemPedido;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.ItemPedidoRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public DataLoader(ClienteRepository clienteRepository, RestauranteRepository restauranteRepository,
                      ProdutoRepository produtoRepository, PedidoRepository pedidoRepository,
                      ItemPedidoRepository itemPedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // clientes diferentes
        Cliente cliente1 = new Cliente();
        cliente1.setNome("João Silva");
        cliente1.setEmail("joao@email.com");
        cliente1.setAtivo(true);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Maria Oliveira");
        cliente2.setEmail("maria@email.com");
        cliente2.setAtivo(true);

        Cliente cliente3 = new Cliente();
        cliente3.setNome("Pedro Inativo");
        cliente3.setEmail("pedro@email.com");
        cliente3.setAtivo(false);

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));
        System.out.println("Clientes inseridos: " + clienteRepository.count());

        // 2 restaurantes de categorias distintas
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNome("Pizzaria do Chef");
        restaurante1.setCategoria("Pizza");
        restaurante1.setAtivo(true);
        restaurante1.setTaxaEntrega(new BigDecimal("10.00"));

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Hamburgueria Gourmet");
        restaurante2.setCategoria("Lanche");
        restaurante2.setAtivo(true);
        restaurante2.setTaxaEntrega(new BigDecimal("4.50"));

        restauranteRepository.saveAll(Arrays.asList(restaurante1, restaurante2));
        System.out.println("Restaurantes inseridos: " + restauranteRepository.count());

        // 5 produtos variados
        Produto produto1 = new Produto();
        produto1.setNome("Pizza Calabresa");
        produto1.setPreco(new BigDecimal("45.00"));
        produto1.setDisponivel(true);
        produto1.setCategoria("Salgada");
        produto1.setRestaurante(restaurante1);

        Produto produto2 = new Produto();
        produto2.setNome("Coca-Cola 2L");
        produto2.setPreco(new BigDecimal("10.00"));
        produto2.setDisponivel(true);
        produto2.setCategoria("Bebida");
        produto2.setRestaurante(restaurante1);

        Produto produto3 = new Produto();
        produto3.setNome("X-Bacon");
        produto3.setPreco(new BigDecimal("25.00"));
        produto3.setDisponivel(true);
        produto3.setCategoria("Sanduíche");
        produto3.setRestaurante(restaurante2);

        Produto produto4 = new Produto();
        produto4.setNome("Batata Frita");
        produto4.setPreco(new BigDecimal("15.00"));
        produto4.setDisponivel(true);
        produto4.setCategoria("Acompanhamento");
        produto4.setRestaurante(restaurante2);

        Produto produto5 = new Produto();
        produto5.setNome("Produto Indisponível");
        produto5.setPreco(new BigDecimal("5.00"));
        produto5.setDisponivel(false);
        produto5.setCategoria("Outro");
        produto5.setRestaurante(restaurante1);

        produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3, produto4, produto5));
        System.out.println("Produtos inseridos: " + produtoRepository.count());

        // 2 pedidos com itens
        // Pedido 1 - João
        Pedido pedido1 = new Pedido();
        pedido1.setCliente(cliente1);
        pedido1.setRestaurante(restaurante1);
        pedido1.setStatus(StatusPedido.CONFIRMADO);
        pedido1.setDataPedido(LocalDateTime.now().minusHours(2));
        pedido1.setEnderecoEntrega("Rua A, 123");
        pedido1.setValorTotal(new BigDecimal("65.00").add(restaurante1.getTaxaEntrega())); // 45 + 10 + 10 = 65

        ItemPedido item1_1 = new ItemPedido();
        item1_1.setPedido(pedido1);
        item1_1.setProduto(produto1);
        item1_1.setQuantidade(1);
        item1_1.setPrecoUnitario(produto1.getPreco());
        item1_1.setPrecoTotal(produto1.getPreco());

        ItemPedido item1_2 = new ItemPedido();
        item1_2.setPedido(pedido1);
        item1_2.setProduto(produto2);
        item1_2.setQuantidade(2);
        item1_2.setPrecoUnitario(produto2.getPreco());
        item1_2.setPrecoTotal(produto2.getPreco().multiply(new BigDecimal(2)));

        pedido1.setItens(Arrays.asList(item1_1, item1_2));
        pedidoRepository.save(pedido1);
        itemPedidoRepository.saveAll(Arrays.asList(item1_1, item1_2));

        // Pedido 2 - Maria
        Pedido pedido2 = new Pedido();
        pedido2.setCliente(cliente2);
        pedido2.setRestaurante(restaurante2);
        pedido2.setStatus(StatusPedido.ENTREGUE);
        pedido2.setDataPedido(LocalDateTime.now().minusDays(1));
        pedido2.setEnderecoEntrega("Rua B, 456");
        pedido2.setValorTotal(new BigDecimal("40.00").add(restaurante2.getTaxaEntrega())); // 25 + 15 + 4.50 = 44.50

        ItemPedido item2_1 = new ItemPedido();
        item2_1.setPedido(pedido2);
        item2_1.setProduto(produto3);
        item2_1.setQuantidade(1);
        item2_1.setPrecoUnitario(produto3.getPreco());
        item2_1.setPrecoTotal(produto3.getPreco());

        ItemPedido item2_2 = new ItemPedido();
        item2_2.setPedido(pedido2);
        item2_2.setProduto(produto4);
        item2_2.setQuantidade(1);
        item2_2.setPrecoUnitario(produto4.getPreco());
        item2_2.setPrecoTotal(produto4.getPreco());

        pedido2.setItens(Arrays.asList(item2_1, item2_2));
        pedidoRepository.save(pedido2);
        itemPedidoRepository.saveAll(Arrays.asList(item2_1, item2_2));

        System.out.println("Pedidos inseridos: " + pedidoRepository.count());
        System.out.println("Itens de Pedido inseridos: " + itemPedidoRepository.count());

        // Cenário 1: Busca de Cliente por Email
        System.out.println("\nCenário 1: Busca de Cliente por Email (joao@email.com)");
        clienteRepository.findByEmail("joao@email.com").ifPresentOrElse(
                c -> System.out.println("Resultado: Cliente encontrado: " + c.getNome()),
                () -> System.out.println("Resultado: Cliente não encontrado.")
        );

        // Cenário 2: Produtos por Restaurante
        System.out.println("\nCenário 2: Produtos por Restaurante (ID 1 - Pizzaria do Chef)");
        List<Produto> produtosRestaurante1 = produtoRepository.findByRestauranteId(restaurante1.getId());
        System.out.println("Resultado: " + produtosRestaurante1.size() + " produtos encontrados.");
        produtosRestaurante1.forEach(p -> System.out.println("- " + p.getNome() + " (Disponível: " + p.isDisponivel() + ")"));

        // Cenário 3: Pedidos Recentes
        System.out.println("\nCenário 3: Pedidos Recentes (Top 10)");
        List<Pedido> pedidosRecentes = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        System.out.println("Resultado: " + pedidosRecentes.size() + " pedidos encontrados.");
        pedidosRecentes.forEach(p -> System.out.println("- Pedido #" + p.getId() + " - Cliente: " + p.getCliente().getNome() + " - Data: " + p.getDataPedido()));

        // Cenário 4: Restaurantes por Taxa
        System.out.println("\nCenário 4: Restaurantes por Taxa (Taxa <= R$ 5.00)");
        List<Restaurante> restaurantesTaxaBaixa = restauranteRepository.findByTaxaEntregaLessThanEqual(new BigDecimal("5.00"));
        System.out.println("Resultado: " + restaurantesTaxaBaixa.size() + " restaurantes encontrados.");
        restaurantesTaxaBaixa.forEach(r -> System.out.println("- " + r.getNome() + " (Taxa: R$" + r.getTaxaEntrega() + ")"));

        // Total de vendas por restaurante
        System.out.println("\nTotal de vendas por restaurante:");
        List<Object[]> vendasPorRestaurante = pedidoRepository.totalVendasPorRestaurante();
        vendasPorRestaurante.forEach(obj -> System.out.println("- Restaurante: " + obj[0] + " | Total: R$" + obj[1]));

        // Pedidos com valor acima de X (Ex: R$ 50.00)
        System.out.println("\nPedidos com valor acima de R$ 50.00:");
        List<Pedido> pedidosAcima50 = pedidoRepository.pedidosComValorAcimaDe(new BigDecimal("50.00"));
        pedidosAcima50.forEach(p -> System.out.println("- Pedido #" + p.getId() + " | Total: R$" + p.getValorTotal()));

        // Relatório por período e status (Ex: Últimos 3 dias, Status CONFIRMADO)
        System.out.println("\nRelatório por período e status (CONFIRMADO):");
        List<Pedido> relatorioConfirmado = pedidoRepository.relatorioPorPeriodoEStatus(
                LocalDateTime.now().minusDays(3), LocalDateTime.now(), StatusPedido.CONFIRMADO);
        relatorioConfirmado.forEach(p -> System.out.println("- Pedido #" + p.getId() + " | Cliente: " + p.getCliente().getNome() + " | Status: " + p.getStatus()));
    }
}
