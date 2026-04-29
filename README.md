# Customer Address API

API REST para cadastro de clientes com consulta automática de endereço via CEP e auditoria de consultas.

## 📋 Sobre o Projeto

Sistema de cadastro de clientes que automatiza o preenchimento de endereços através da consulta ao CEP via API ViaCEP. Todas as consultas são registradas em log de auditoria para rastreabilidade.

**Principais funcionalidades:**
- Cadastro de clientes com validação de dados
- Consulta automática de endereço via CEP
- Auditoria completa de consultas CEP
- Listagem paginada de clientes
- Cache de consultas CEP para melhor performance

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.2.4
- Spring Data JPA
- PostgreSQL / H2
- Flyway
- MapStruct
- Lombok
- Springdoc OpenAPI
- Spring Cache
- JUnit 5 + Mockito

## 🔧 Como Rodar

### Desenvolvimento (H2)

```bash
git clone https://github.com/FilipiWanderley/customer-address-api.git
cd customer-address-api
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Acessos:**
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`)

### Produção (PostgreSQL)

```bash
# Configurar .env
cp .env.example .env
nano .env

# Rodar
mvn spring-boot:run
```

Exemplo `.env`:
```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/customer_address_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=sua_senha
CEP_API_BASE_URL=https://viacep.com.br/ws
```

## 📚 API Endpoints

### Customers

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/customers` | Criar cliente |
| GET | `/api/v1/customers?page=0&size=20` | Listar clientes (paginado) |
| GET | `/api/v1/customers/{id}` | Buscar por ID |
| PUT | `/api/v1/customers/{id}` | Atualizar cliente |
| DELETE | `/api/v1/customers/{id}` | Deletar cliente |

### CEP

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/cep/{zipCode}` | Consultar CEP |

**Exemplo de requisição:**

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "phone": "11987654321",
    "zipCode": "01310100",
    "number": "100"
  }'
```

## 🧪 Testes

```bash
mvn test
```

## 📖 Documentação

Swagger UI disponível em: http://localhost:8080/swagger-ui.html

## 🏗️ Arquitetura

Projeto segue Clean Architecture com separação de camadas:

```
controller → service → repository
              ↓
           client (ViaCEP)
```

**Principais componentes:**
- **Controllers**: Endpoints REST
- **Services**: Lógica de negócio
- **Repositories**: Acesso a dados
- **Clients**: Integração com APIs externas
- **DTOs**: Request/Response objects
- **Mappers**: MapStruct para conversão de entidades

## 🔒 Recursos de Resiliência

- **Cache**: Consultas CEP são cacheadas para reduzir chamadas à API externa
- **Timeout**: Configurado timeout de 5s para conexão e 10s para leitura
- **Auditoria**: Todas as consultas CEP são registradas com timestamp e payload

## 🎯 Próximos Passos

- [ ] Adicionar autenticação (Spring Security + JWT)
- [ ] Implementar retry com backoff exponencial
- [ ] Adicionar circuit breaker (Resilience4j)
- [ ] Containerização (Docker)
- [ ] CI/CD (GitHub Actions)

## 👤 Autor

**Filipi Wanderley**

- GitHub: [@FilipiWanderley](https://github.com/FilipiWanderley)
- LinkedIn: [Filipi Wanderley](https://linkedin.com/in/filipi-wanderley)
