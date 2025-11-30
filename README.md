# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias

- **Java 21 LTS** (versão mais recente)
- **Spring Boot 3.4.x**
- **Spring Web**
- **Spring Data JPA**
- **H2 Database** (Banco em memória para desenvolvimento)
- **MySQL Driver** (Pronto para produção)
- **Spring Security + JWT** (Autenticação e Autorização)
- **SpringDoc OpenAPI** (Swagger UI)
- **Maven** (Gerenciamento de dependências)

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado em sua máquina:

- [Java JDK 21](https://adoptium.net/)
- [Git](https://git-scm.com/)
- Maven (Opcional, pois o projeto inclui o wrapper `mvnw`)

## 🔧 Instalação e Configuração

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/delivery-api.git
   cd delivery-api
   ```

2. **Configuração do Banco de Dados:**
   O projeto está configurado por padrão para usar o banco de dados **H2 em memória**.
   
   As configurações podem ser encontradas em `src/main/resources/application.properties`:
   ```properties
   # Porta do servidor
   server.port=8080
   
   # H2 Database
   spring.datasource.url=jdbc:h2:file:C:/Users/Douglas/test;AUTO_SERVER=TRUE;IFEXISTS=FALSE
   spring.datasource.username=sa
   spring.datasource.password=
   
   # JWT Secret (Altere para produção)
   jwt.secret=seu-segredo-aqui
   jwt.expiration=86400000
   ```

## 🏃‍♂️ Como Executar

Para rodar a aplicação, utilize o Maven Wrapper incluído no projeto:

### Linux/macOS
```bash
./mvnw spring-boot:run
```

### Windows
```cmd
mvnw.cmd spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## � Documentação da API (Swagger)

A documentação interativa da API está disponível através do Swagger UI. Após iniciar a aplicação, acesse:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Principais Endpoints

- **Autenticação**: `/api/auth/*` (Login, Registro)
- **Usuários**: `/api/usuarios`
- **Restaurantes**: `/api/restaurantes`
- **Pedidos**: `/api/pedidos`
- **Produtos**: `/api/produtos`

## 🛠️ Ferramentas de Desenvolvimento

- **H2 Console**: Para acessar o banco de dados em memória.
  - URL: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:file:C:/Users/Douglas/test;AUTO_SERVER=TRUE;IFEXISTS=FALSE`
  - User: `sa`
  - Password: (vazio)

- **Actuator**:
  - Health: `http://localhost:8080/actuator/health`
  - Info: `http://localhost:8080/actuator/info`

## 🧪 Testes

Para executar os testes unitários e de integração:

```bash
./mvnw test
```

## 🔑 Credenciais de Teste

Usuários pré-cadastrados no banco de dados (senha padrão: `123456`):

| Perfil | Email | Senha |
|--------|-------|-------|
| **Admin** | `admin@delivery.com` | `123456` |
| **Cliente** | `joao@email.com` | `123456` |
| **Restaurante** | `pizza@palace.com` | `123456` |
| **Entregador** | `carlos@entrega.com` | `123456` |

## 👨‍💻 Desenvolvedor

Desenvolvido com ❤️ usando as melhores práticas de desenvolvimento Java moderno.