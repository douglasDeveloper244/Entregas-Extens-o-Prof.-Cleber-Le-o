🚀 Delivery API - Sistema de Gerenciamento de Delivery
Uma API RESTful completa para gerenciamento de pedidos de delivery, desenvolvida em Java com Spring Boot.

🎯 Visão Geral
Esta API fornece um sistema completo para gerenciamento de pedidos de delivery, incluindo cadastro de clientes, restaurantes, produtos e processamento de pedidos.

🛠 Tecnologias
Java 21

Spring Boot 3.4.11

Spring Data JPA

H2 Database (desenvolvimento)

Maven

Lombok

Validation API

✨ Funcionalidades Módulos Principais:

✅ Clientes - Cadastro e gerenciamento

✅ Restaurantes - Cadastro e busca avançada

✅ Produtos - Catálogo por restaurante

✅ Pedidos - Processamento completo

✅ Health Check - Monitoramento da API

Recursos Avançados
🔍 Buscas com filtros combinados

📊 Ordenação por diversos critérios

🏪 Relacionamento Produto-Restaurante

📱 API RESTful completa

🗃️ Banco em memória (H2)

🚀 Instalação e Execução
Pré-requisitos
Java 21
Maven 3.6+

Git

1. Clone o repositório
   bash
   git clone https://github.com/seu-usuario/delivery-api.git
   cd delivery-api
2. Compile o projeto
   bash
   mvn clean compile
3. Execute a aplicação
   bash
   mvn spring-boot:run
4. Acesse a aplicação
   text
   http://localhost:8080
5. Acesse o H2 Console (Banco de Dados)
   text
   http://localhost:8080/h2-console
   Configurações H2:

JDBC URL: jdbc:h2:mem:testdb

User Name: sa

Password: (deixe em branco)

📚 Endpoints da API
👥 Clientes

1. Cadastrar Cliente
   Método: POST

Endpoint: /clientes

Descrição: Cria um novo cliente no sistema

Body:

json
{
"nome": "Douglas Ribeiro",
"email": "Douglas.Ribeiro@email.com",
"telefone": "(31) 912344567",
"endereco": "Rua das Flores, 123 - Belo Horizonte - MG"
} 2. Buscar Cliente por ID
Método: GET

Endpoint: /clientes/{id}

Descrição: Retorna os dados de um cliente específico

3. Listar Clientes Ativos
   Método: GET

Endpoint: /clientes/1

Descrição: Lista todos os clientes ativos no sistema

4. Buscar Cliente por Email
   Método: GET

Endpoint: /clientes/email/{email}

Descrição: Busca um cliente pelo endereço de email

5. Buscar Clientes por Nome
   Método: GET

Endpoint: /clientes/buscar?nome={nome}

Descrição: Busca clientes por nome (parcial ou completo)

6. Atualizar Cliente
   Método: PUT

Endpoint: /clientes/{id}

Descrição: Atualiza os dados de um cliente existente

Body:

json
{
"nome": "Douglas Rodrigues",
"email": "Douglas.Ribeiro@gmail.com",
"telefone": "(11) 98888-8888",
"endereco": "Rua das Flores, 123 - São Paulo, SP"
} 7. Inativar Cliente
Método: DELETE

Endpoint: /clientes/{id}

Descrição: Inativa um cliente no sistema (exclusão lógica)

📦 Pedidos

1. Criar Pedido
   Método: POST

Endpoint: /api/pedidos

Descrição: Cria um novo pedido no sistema

Body:

json
{
"clienteId": 1,
"restauranteId": 1,
"valorTotal": 45.90,
"observacoes": "Sem cebola, por favor",
"itens": "1x Pizza Calabresa, 1x Coca-Cola 2L"
} 2. Consultar Pedido por ID
Método: GET

Endpoint: /api/pedidos/{id}

Descrição: Retorna os dados de um pedido específico

3. Consultar Pedidos por Cliente
   Método: GET

Endpoint: /api/pedidos/cliente/{clienteId}

Descrição: Lista todos os pedidos de um cliente específico

4. Atualizar Status do Pedido
   Método: PATCH

Endpoint: /api/pedidos/{id}/status?status={status}

Descrição: Atualiza o status de um pedido (ex: EM_PREPARO)

🏪 Restaurantes

1. Criar Restaurante
   Método: POST

Endpoint: /api/restaurantes

Descrição: Cadastra um novo restaurante no sistema

Body:

json
{
"nome": "Pizzaria do João",
"endereco": "Av. Paulista, 1000 - São Paulo, SP",
"telefone": "(11) 3333-3333",
"horarioFuncionamento": "18:00-23:00",
"categoria": "Pizzaria",
"taxaEntrega": 5.00,
"avaliacao": 4.5,
"ativo": true
} 2. Listar Todos os Restaurantes
Método: GET

Endpoint: /api/restaurantes

Descrição: Retorna todos os restaurantes cadastrados

3. Buscar Restaurantes por Categoria
   Método: GET

Endpoint: /api/restaurantes/buscar/categoria?categoria={categoria}

Descrição: Filtra restaurantes por categoria específica

4. Deletar Restaurante
   Método: DELETE

Endpoint: /api/restaurantes/{id}

Descrição: Remove um restaurante do sistema

5. Atualizar Restaurante
   Método: PUT

Endpoint: /api/restaurantes/{id}

Descrição: Atualiza os dados de um restaurante existente

Body:

json
{
"nome": "Pizzaria do João - Unidade Centro",
"endereco": "Av. Paulista, 1000 - São Paulo, SP",
"telefone": "(11) 3333-4444",
"horarioFuncionamento": "17:00-00:00",
"categoria": "Pizzaria",
"taxaEntrega": 6.00,
"avaliacao": 4.7,
"ativo": true
}
🍕 Produtos

1. Criar Produto
   Método: POST

Endpoint: /api/produtos

Descrição: Cadastra um novo produto no sistema

Body:

json
{
"nome": "Pizza Calabresa",
"descricao": "Pizza de calabresa com mussarela e cebola",
"preco": 39.90,
"categoria": "Pizza",
"disponivel": true,
"restaurante": {
"id": 1
}
} 2. Listar Produtos por Restaurante
Método: GET

Endpoint: /api/produtos/restaurante/{restauranteId}

Descrição: Retorna todos os produtos de um restaurante específico

3. Listar Todos os Produtos
   Método: GET

Endpoint: /api/produtos

Descrição: Retorna todos os produtos cadastrados no sistema

4. Buscar Produtos Disponíveis por Restaurante
   Método: GET

Endpoint: /api/produtos/restaurante/{restauranteId}/disponiveis

Descrição: Retorna apenas os produtos disponíveis de um restaurante

5. Atualizar Produto
   Método: PUT

Endpoint: /api/produtos/{id}

Descrição: Atualiza os dados de um produto existente

Body:

json
{
"nome": "Pizza Calabresa Especial",
"descricao": "Pizza de calabresa com mussarela, cebola e azeitonas",
"preco": 42.90,
"categoria": "Pizza",
"disponivel": true
} 6. Deletar Produto
Método: DELETE

Endpoint: /api/produtos/{id}

Descrição: Remove um produto do sistema

🔄 Fluxo de Operações Recomendado
Cadastrar Cliente → Criar Restaurante → Criar Produto → Criar Pedido

Para testes completos: Execute todas as operações CRUD em cada módulo

Utilize os IDs retornados nas respostas para operações subsequentes

Nota: Certifique-se de que o servidor está rodando em localhost:8081 antes de executar os testes.

4. Ferramentas Recomendadas para Testes
   Postman - Interface gráfica para testes de API

Insomnia - Alternativa ao Postman

VS Code REST Client - Usando os exemplos .http

# Compilar projeto

mvn clean compile

# Executar testes

mvn test

# Empacotar aplicação

mvn clean package

# Executar com perfil de desenvolvimento

mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Verificar dependências

mvn dependency:tree
Configurações
O arquivo application.properties contém:

properties

# Servidor

server.port=8080

# Banco H2 (memória)

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

Desenvolvido por Douglas Ribeiro
_API Delivery System - Versão 1.0.0_
