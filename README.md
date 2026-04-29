# Customer Address API

API REST para cadastro de clientes com consulta automática de endereço via CEP, incluindo auditoria completa de todas as consultas realizadas.

## 📋 Sobre o Projeto

Esta API foi desenvolvida para resolver o problema de cadastro manual de endereços em sistemas de gestão de clientes. Ao fornecer apenas o CEP, a aplicação consulta automaticamente os dados de endereço através da API ViaCEP e preenche os campos de logradouro, bairro, cidade e estado.

Além disso, todas as consultas de CEP são registradas em um log de auditoria, permitindo rastreabilidade completa das operações realizadas, incluindo timestamp, payload de resposta e status HTTP.

## 🎯 Problema Resolvido

- **Redução de erros**: Elimina digitação manual de endereços
- **Agilidade**: Preenchimento automático a partir do CEP
- **Auditoria**: Rastreamento completo de todas as consultas realizadas
- **Validação**: Garante consistência dos dados de endereço
- **Experiência do usuário**: Simplifica o processo de cadastro

## 🏗️ Arquitetura da Solução

O projeto segue os princípios de **Clean Architecture** e **SOLID**, com separação clara de responsabilidades:

```
src/main/java/com/filipi/customeraddress/
├── client/              # Integração com APIs externas
│   ├── CepClient.java           # Interface (Open/Closed Principle)
│   ├── ViaCepClient.java        # Implementação concreta
│   └── dto/
│       └── CepResponse.java     # DTO de resposta da API
├── config/              # Configurações do Spring
│   ├── AppConfig.java           # Beans (RestTemplate, ObjectMapper)
│   └── OpenApiConfig.java       # Documentação Swagger
├── controller/          # Camada de apresentação
│   ├── CustomerController.java  # CRUD de clientes
│   └── CepController.java       # Consulta de CEP
├── dto/                 # Data Transfer Objects
│   ├── request/
│   │   └── CustomerRequest.java # Validações com Bean Validation
│   └── response/
│       └── CustomerResponse.java
├── exception/           # Exceções de domínio
│   ├── CepNotFoundException.java
│   ├── CepServiceException.java
│   ├── CustomerNotFoundException.java
│   ├── EmailAlreadyInUseException.java
│   └── handler/
│       └── GlobalExceptionHandler.java  # Tratamento centralizado
├── mapper/              # Mapeamento de entidades
│   └── CustomerMapper.java      # MapStruct (type-safe)
├── model/               # Entidades JPA
│   ├── Customer.java
│   └── CepConsultationLog.java  # Auditoria
├── repository/          # Camada de persistência
│   ├── CustomerRepository.java
│   └── CepConsultationLogRepository.java
└── service/             # Lógica de negócio
    ├── CustomerService.java     # Interface
    ├── CepConsultationLogService.java
    └── impl/
        ├── CustomerServiceImpl.java
        └── CepConsultationLogServiceImpl.java
```

### Princípios SOLID Aplicados

- **S**ingle Responsibility: Cada classe tem uma única responsabilidade bem definida
- **O**pen/Closed: `CepClient` é uma interface, permitindo trocar implementações sem alterar o código cliente
- **L**iskov Substitution: `ViaCepClient` pode ser substituído por qualquer implementação de `CepClient`
- **I**nterface Segregation: Interfaces focadas e específicas (`CustomerService`, `CepConsultationLogService`)
- **D**ependency Inversion: Controllers e Services dependem de abstrações, não de implementações concretas

## 🚀 Tecnologias Utilizadas

- **Java 17** - LTS com recursos modernos (Records, Pattern Matching)
- **Spring Boot 3.2.4** - Framework principal
- **Spring Data JPA** - Persistência e repositórios
- **PostgreSQL** - Banco de dados relacional (produção)
- **H2 Database** - Banco em memória (desenvolvimento)
- **Flyway** - Versionamento de schema
- **MapStruct 1.5.5** - Mapeamento type-safe de DTOs
- **Lombok** - Redução de boilerplate
- **Bean Validation** - Validações declarativas
- **Springdoc OpenAPI 3** - Documentação Swagger
- **JUnit 5 + Mockito** - Testes unitários
- **Maven** - Gerenciamento de dependências

## ✅ Requisitos Atendidos

### Funcionais
- [x] Cadastro de clientes com validação de dados
- [x] Consulta automática de endereço via CEP
- [x] Atualização de clientes (com reconsulta de CEP se alterado)
- [x] Listagem e busca de clientes
- [x] Exclusão de clientes
- [x] Validação de unicidade de e-mail
- [x] Auditoria de consultas de CEP (timestamp, payload, status)

### Não Funcionais
- [x] Arquitetura limpa e manutenível
- [x] Código testável (testes unitários)
- [x] Documentação via Swagger
- [x] Tratamento global de exceções
- [x] Validações de entrada
- [x] Logs estruturados
- [x] Suporte a múltiplos ambientes (dev/prod)

## 🔧 Como Rodar Localmente

### Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- PostgreSQL 12+ (para produção) ou usar H2 (desenvolvimento)

### Opção 1: Ambiente de Desenvolvimento (H2)

Ideal para testes rápidos sem necessidade de PostgreSQL:

```bash
# Clonar o repositório
git clone https://github.com/FilipiWanderley/customer-address-api.git
cd customer-address-api

# Compilar o projeto
mvn clean install

# Rodar com perfil de desenvolvimento (H2 em memória)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

A aplicação estará disponível em:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (deixe em branco)

### Opção 2: Ambiente de Produção (PostgreSQL)

#### Configurar PostgreSQL Local

```bash
# Criar banco de dados
createdb customer_address_db

# Ou via Docker
docker run -d \
  --name postgres-customer \
  -e POSTGRES_DB=customer_address_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15-alpine
```

#### Configurar Variáveis de Ambiente

```bash
# Copiar arquivo de exemplo
cp .env.example .env

# Editar .env com suas credenciais
nano .env
```

Exemplo de `.env`:
```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/customer_address_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=sua_senha_aqui
CEP_API_BASE_URL=https://viacep.com.br/ws
SERVER_PORT=8080
```

#### Rodar a Aplicação

```bash
mvn spring-boot:run
```

### Opção 3: Usando Neon (PostgreSQL Serverless)

[Neon](https://neon.tech) oferece PostgreSQL serverless gratuito, ideal para desenvolvimento e testes.

1. Criar conta em https://neon.tech
2. Criar novo projeto
3. Copiar a connection string fornecida
4. Configurar no `.env`:

```properties
DATABASE_URL=jdbc:postgresql://ep-xxx.us-east-2.aws.neon.tech:5432/neondb?sslmode=require
DATABASE_USERNAME=seu-usuario
DATABASE_PASSWORD=sua-senha
```

5. Rodar a aplicação:
```bash
mvn spring-boot:run
```

## 🧪 Como Testar

### Rodar Testes Unitários

```bash
mvn test
```

### Testar Endpoints via Swagger

1. Acesse http://localhost:8080/swagger-ui.html
2. Expanda o endpoint desejado
3. Clique em "Try it out"
4. Preencha os dados e execute

### Testar via cURL

#### Criar Cliente

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "phone": "11987654321",
    "zipCode": "01310100",
    "number": "100",
    "complement": "Apto 42"
  }'
```

#### Listar Clientes

```bash
curl http://localhost:8080/api/v1/customers
```

#### Buscar Cliente por ID

```bash
curl http://localhost:8080/api/v1/customers/1
```

#### Atualizar Cliente

```bash
curl -X PUT http://localhost:8080/api/v1/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Santos",
    "email": "joao@email.com",
    "phone": "11987654321",
    "zipCode": "01310100",
    "number": "200"
  }'
```

#### Deletar Cliente

```bash
curl -X DELETE http://localhost:8080/api/v1/customers/1
```

#### Consultar CEP

```bash
curl http://localhost:8080/api/v1/cep/01310100
```

## 📚 Documentação da API (Swagger)

A documentação completa da API está disponível via Swagger UI:

**URL**: http://localhost:8080/swagger-ui.html

A documentação inclui:
- Todos os endpoints disponíveis
- Schemas de request/response
- Códigos de status HTTP
- Exemplos de uso
- Possibilidade de testar diretamente no navegador

Também disponível em formato JSON:
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 📁 Estrutura de Pastas

```
customer-address-api/
├── src/
│   ├── main/
│   │   ├── java/com/filipi/customeraddress/
│   │   │   ├── client/              # Integrações externas
│   │   │   ├── config/              # Configurações Spring
│   │   │   ├── controller/          # Endpoints REST
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Exceções customizadas
│   │   │   ├── mapper/              # MapStruct mappers
│   │   │   ├── model/               # Entidades JPA
│   │   │   ├── repository/          # Repositórios Spring Data
│   │   │   ├── service/             # Lógica de negócio
│   │   │   └── CustomerAddressApplication.java
│   │   └── resources/
│   │       ├── application.yml      # Configuração produção
│   │       ├── application-dev.yml  # Configuração desenvolvimento
│   │       └── db/migration/        # Scripts Flyway
│   └── test/
│       └── java/com/filipi/customeraddress/
│           ├── client/              # Testes de integração
│           ├── controller/          # Testes de controller
│           └── service/             # Testes de serviço
├── .env.example                     # Template de variáveis
├── .gitignore                       # Arquivos ignorados
├── pom.xml                          # Dependências Maven
└── README.md                        # Este arquivo
```

## 🎯 Decisões Técnicas

### 1. Separação de Interfaces e Implementações

**Decisão**: Criar interfaces para serviços (`CustomerService`, `CepConsultationLogService`) e para o cliente HTTP (`CepClient`).

**Justificativa**: 
- Facilita testes unitários (mocking)
- Permite trocar implementações sem alterar código cliente
- Segue o princípio Open/Closed do SOLID

### 2. MapStruct para Mapeamento

**Decisão**: Usar MapStruct em vez de mapeamento manual ou ModelMapper.

**Justificativa**:
- Type-safe (erros em tempo de compilação)
- Performance superior (código gerado em compile-time)
- Menos propenso a erros de runtime

### 3. Auditoria de Consultas CEP

**Decisão**: Persistir todas as consultas de CEP em tabela separada.

**Justificativa**:
- Rastreabilidade completa
- Análise de uso da API
- Debugging facilitado
- Compliance e auditoria

### 4. Validação em Múltiplas Camadas

**Decisão**: Bean Validation nos DTOs + validações de negócio no Service.

**Justificativa**:
- Bean Validation: validações sintáticas (formato, tamanho)
- Service: validações semânticas (email único, CEP válido)
- Separação clara de responsabilidades

### 5. Tratamento Global de Exceções

**Decisão**: `@RestControllerAdvice` com exceções customizadas.

**Justificativa**:
- Respostas de erro consistentes
- Código dos controllers mais limpo
- Facilita manutenção

### 6. Perfis de Ambiente (dev/prod)

**Decisão**: Perfil `dev` com H2 e perfil padrão com PostgreSQL.

**Justificativa**:
- Desenvolvimento rápido sem dependências externas
- Produção com banco robusto e persistente
- Facilita onboarding de novos desenvolvedores

### 7. Flyway para Migrations

**Decisão**: Usar Flyway para versionamento de schema.

**Justificativa**:
- Controle de versão do banco de dados
- Migrations reproduzíveis
- Facilita deploy em múltiplos ambientes

## 🔮 Melhorias Futuras

### Funcionalidades
- [ ] Paginação e ordenação na listagem de clientes
- [ ] Filtros de busca (por nome, email, cidade)
- [ ] Exportação de dados (CSV, Excel)
- [ ] Importação em lote de clientes
- [ ] Histórico de alterações de clientes
- [ ] Soft delete (exclusão lógica)

### Técnicas
- [ ] Cache de consultas CEP (Redis)
- [ ] Rate limiting para API externa
- [ ] Retry com backoff exponencial
- [ ] Circuit breaker (Resilience4j)
- [ ] Testes de integração com Testcontainers
- [ ] Métricas com Micrometer/Prometheus
- [ ] Health checks customizados
- [ ] Autenticação e autorização (Spring Security + JWT)
- [ ] API versioning
- [ ] HATEOAS para navegação de recursos

### Infraestrutura
- [ ] Containerização (Docker)
- [ ] CI/CD (GitHub Actions)
- [ ] Deploy automatizado
- [ ] Monitoramento (Grafana)
- [ ] Logs centralizados (ELK Stack)

## 📄 Licença

Este projeto foi desenvolvido como desafio técnico e está disponível para fins educacionais.

## 👤 Autor

**Filipi Wanderley**

- GitHub: [@FilipiWanderley](https://github.com/FilipiWanderley)


---

Desenvolvido com ☕ e Java
