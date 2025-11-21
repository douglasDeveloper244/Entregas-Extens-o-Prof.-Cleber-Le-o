# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21, implementando as camadas de persistência, serviço e controle REST conforme os roteiros.

## 🚀 Tecnologias
- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.2.4
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## ⚡ Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)
- Desenvolvido com JDK 21 e Spring Boot 3.2.4

## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 e Maven instalados.
2. Clone o repositório (Assumindo que o código será entregue em um ZIP, esta etapa é para referência).
3. Navegue até o diretório `delivery-api`.
4. Execute: `./mvnw spring-boot:run` (ou `mvn spring-boot:run` se o wrapper não estiver configurado).

## 📋 Endpoints
A API está rodando em `http://localhost:8080`.

### Health Check (Roteiro 1)
- `GET /health` - Status da aplicação (inclui versão Java)
- `GET /info` - Informações da aplicação

### Clientes (Roteiro 2)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/clientes` | Cadastra um novo cliente. |
| `GET` | `/clientes` | Lista todos os clientes. |
| `GET` | `/clientes/{id}` | Busca cliente por ID. |
| `PUT` | `/clientes/{id}` | Atualiza dados do cliente. |
| `DELETE` | `/clientes/{id}` | Inativa o cliente (soft delete). |
| `PUT` | `/clientes/{id}/ativar` | Ativa o cliente. |

### Restaurantes (Roteiro 2)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/restaurantes` | Cadastra um novo restaurante. |
| `GET` | `/restaurantes` | Lista todos os restaurantes. |
| `GET` | `/restaurantes/{id}` | Busca restaurante por ID. |
| `PUT` | `/restaurantes/{id}` | Atualiza dados do restaurante. |
| `DELETE` | `/restaurantes/{id}` | Inativa o restaurante. |
| `GET` | `/restaurantes/categoria/{categoria}` | Busca restaurantes por categoria. |
| `GET` | `/restaurantes/ativos/melhores` | Busca ativos ordenados por avaliação. |

### Produtos (Roteiro 2)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/produtos/restaurante/{restauranteId}` | Cadastra um novo produto para um restaurante. |
| `GET` | `/produtos` | Lista todos os produtos. |
| `GET` | `/produtos/{id}` | Busca produto por ID. |
| `PUT` | `/produtos/{id}` | Atualiza dados do produto. |
| `DELETE` | `/produtos/{id}` | Torna o produto indisponível. |
| `GET` | `/produtos/restaurante/{restauranteId}` | Busca produtos de um restaurante. |
| `GET` | `/produtos/restaurante/{restauranteId}/disponiveis` | Busca produtos disponíveis de um restaurante. |

### Pedidos (Roteiro 2)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/pedidos?clienteId={id}&restauranteId={id}&valorTotal={valor}` | Cria um novo pedido. |
| `GET` | `/pedidos` | Lista todos os pedidos. |
| `GET` | `/pedidos/cliente/{clienteId}` | Busca pedidos por cliente. |
| `PUT` | `/pedidos/{id}/status?status={novoStatus}` | Atualiza o status do pedido. |
| `GET` | `/pedidos/status/{status}` | Busca pedidos por status. |

## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória (acessível em `http://localhost:8080/h2-console`)

## 👨‍💻 Desenvolvedor
Douglas Ribeiro - Extensão Arquitetura de sistemas API REST Full com Java Springboot.