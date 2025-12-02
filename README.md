# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias
- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## ⚡ Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run`
4. Acesse: http://localhost:8080/health

## 📋 Endpoints
- GET /health - Status da aplicação (inclui versão Java)
- GET /info - Informações da aplicação
- GET /h2-console - Console do banco H2

## 🧪 Testes

### Testes Manuais com H2 Console
Com a aplicação em execução, acesse o console do H2 para interagir diretamente com o banco de dados em memória.

1.  Acesse: `http://localhost:8080/h2-console`
2.  Use as seguintes configurações para conectar:
    - **Driver Class**: `org.h2.Driver`
    - **JDBC URL**: `jdbc:h2:mem:deliverydb`
    - **User Name**: `sa`
    - **Password**: (deixe em branco)

O banco de dados será pré-populado com os dados do arquivo `src/main/resources/data.sql`.

### Testes de API
Utilize o Swagger UI para explorar e testar os endpoints de forma interativa.

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

## 💡 Exemplos de Uso (cURL)

Aqui estão alguns exemplos de como interagir com a API usando cURL.

**1. Cadastrar um novo cliente:**
```bash
curl -X POST http://localhost:8080/clientes \
-H "Content-Type: application/json" \
-d '{
  "nome": "Ana Souza",
  "email": "ana.souza@email.com",
  "telefone": "(11) 98888-7777",
  "endereco": "Rua das Flores, 10"
}'
```

**2. Listar todos os restaurantes ativos:**
```bash
curl -X GET http://localhost:8080/restaurantes
```

**3. Criar um novo pedido:**
```bash
curl -X POST http://localhost:8080/pedidos \
-H "Content-Type: application/json" \
-d '{
  "clienteId": 1,
  "restauranteId": 2,
  "itens": [
    { "produtoId": 3, "quantidade": 2 },
    { "produtoId": 5, "quantidade": 1 }
  ],
  "enderecoEntrega": "Rua A, 123 - São Paulo/SP",
  "observacoes": "Caprichar no lanche!"
}'
```

## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Profile: development

## 👨‍💻 Desenvolvedor
[Seu Nome] - [Sua Turma]  
Desenvolvido com JDK 21 e Spring Boot 3.2.x