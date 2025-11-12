# 🍕 Delivery Tech API

API RESTful para gerenciamento de restaurantes, pedidos, clientes e relatórios — desenvolvida com **Spring Boot 3.2.x** e **Java 21**.

---

## 🚀 Tecnologias Utilizadas

- **Java 21 LTS**
- **Spring Boot 3.2.x**
- **Spring Web / Spring MVC**
- **Spring Data JPA / Hibernate**
- **H2 Database (modo memória)**
- **Lombok**
- **Springdoc OpenAPI (Swagger UI)**
- **Maven**

---

## ⚡ Recursos Modernos do Java

- ✅ **Records** — para DTOs e respostas
- ✅ **Text Blocks** — em consultas SQL e documentação
- ✅ **Pattern Matching** — em validações
- ✅ **Virtual Threads (Java 21)** — suporte a concorrência leve
- ✅ **Streams + Optional API** — para manipulação fluida de dados

---

## 🏗️ Arquitetura

A API segue o padrão **MVC + Camada de Serviço**, organizada da seguinte forma:

src/
└── main/
├── java/
│ └── com/deliverytech/delivery_api/
│ ├── controller/ # 🌐 Controladores REST
│ │ ├── ClienteController.java
│ │ ├── PedidoController.java
│ │ ├── ProdutoController.java
│ │ ├── RestauranteController.java
│ │ └── RelatorioController.java
│ │
│ ├── entity/ # 🧱 Entidades do JPA
│ │ ├── Cliente.java
│ │ ├── Pedido.java
│ │ ├── Produto.java
│ │ ├── Restaurante.java
│ │ ├── ItemPedido.java
│ │
│ ├── DTO
│ │ ├── ClienteDTO.java
│ │ ├── PedidoDTO.java
│ │ ├── ProdutoDTO.java
│ │ ├── RestauranteDTO.java
│ │ └── ItemPedidoDTO.java
│ │
│ ├── repository/ # 💾 Interfaces JPA
│ │ ├── ClienteRepository.java
│ │ ├── PedidoRepository.java
│ │ ├── ProdutoRepository.java
│ │ └── RestauranteRepository.java
│ │
│ ├── services/ # ⚙️ Lógica de Negócios
│ │ ├── ClienteService.java
│ │ ├── PedidoService.java
│ │ ├── ProdutoService.java
│ │ ├── RestauranteService.java
│ │ └── RelatorioService.java
│ │
│ ├── config/ # ⚙️ Configurações da Aplicação
│ │ ├── OpenApiConfig.java
│ │
│ ├── enums/ # 🧾 Enumerações
│ │ ├── StatusPedido.java
│ │
│ ├── exceptions/ # 🚨 Tratamento Global de Erros
│ │ ├── BusinessException.java
│ │ ├── EntityNotFoundException.java
│ │ ├── GlobalExceptionHandler.java
│ │ └── ValidationErrorResponse.java
│ │
│ └── ProjetoDeliveryApiApplication.java # 🚀 Classe principal do Spring Boot
│
└── resources/
├── application.properties # Configurações da aplicação
├── data.sql # Script de carga inicial
└── schema.sql (opcional) # Script de schema manual, se necessário

### 🧩 Pré-requisitos

- **JDK 21** instalado
- **Maven 3.9+**
- Nenhuma configuração externa necessária (usa banco em memória)

### ▶️ Execução

```bash

# Executar com Maven Wrapper
./mvnw spring-boot:run

🌐 Acesso
API: http://localhost:8081

#Dashboard Swagger
Swagger UI: http://localhost:8081/swagger-ui.html

#Banco em memoria H2
H2 Console: http://localhost:8081/h2-console


📚 Endpoints Principais


🏪 Restaurantes
Método	Endpoint	                                                Descrição
POST	/api/restaurantes	                                        Cadastrar restaurante
GET	/api/restaurantes/{id}	                                        Buscar por ID
GET	/api/restaurantes/ativos	                                Listar apenas ativos
GET	/api/restaurantes?categoria=Pizza&page=0&size=5	                Buscar com filtro e paginação
PUT	/api/restaurantes/{id}	                                        Atualizar restaurante
PATCH	/api/restaurantes/{id}/inativar	                                Inativar restaurante
DELETE	/api/restaurantes/{id}	                                        Excluir restaurante


🧾 Pedidos
Método	Endpoint	                                                                                                Descrição
POST	/api/pedidos	                                                                                                Criar novo pedido
GET	/api/pedidos/cliente/{clienteId}	                                                                        Listar pedidos por cliente
PATCH	/api/pedidos/{pedidoId}/status/{status}	                                                                        Atualizar status
GET	/api/pedidos/relatorio/vendas-restaurantes	                                                                Total de vendas por restaurante
GET	/api/pedidos/relatorio/valor-acima?valorMinimo=50	                                                        Pedidos acima de um valor
GET	/api/pedidos/relatorio/periodo-status?inicio=2024-01-01T00:00:00&fim=2024-12-31T23:59:59&status=ENTREGUE	Relatório por período e status


📊 Relatórios
Método	Endpoint	                                                        Descrição
GET	/api/relatorios/pedidos-por-periodo?inicio=2024-01-01&fim=2024-01-31	Pedidos por período
GET	/api/relatorios/produtos-mais-vendidos	                                 Produtos mais vendidos
GET	/api/relatorios/clientes-mais-ativos	                                Clientes mais ativos
GET	/api/relatorios/restaurantes-mais-vendas	                        Restaurantes com mais vendas


🧠 Exemplo de Requisição
GET /api/restaurantes?categoria=Pizza&page=0&size=5

Resposta:
{
  "content": [
    {
      "id": 1,
      "nome": "Pizza Express",
      "categoria": "Pizza",
      "taxaEntrega": 5.00,
      "tempoEntrega": "30-45 min",
      "avaliacao": 4.5,
      "ativo": true
    }
  ],
  "page": {
    "number": 0,
    "size": 5,
    "totalElements": 12,
    "totalPages": 3
  }
}
✅ 1. Controllers REST completos — com validações, DTOs, e HTTP Status corretos
✅ 2. Documentação Swagger — disponível em /swagger-ui.html
✅ 3. Testes e consultas com @Query — integrados nos relatórios
✅ 4. Paginação e filtros — implementados com Pageable
✅ 5. Respostas padronizadas — tratamento global de exceções
```
