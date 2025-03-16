# SensorAPI - Estudo de Arquitetura Hexagonal

## Descrição do Projeto

Este projeto é uma API RESTful para monitoramento de sensores, desenvolvida com arquitetura hexagonal (Ports & Adapters) utilizando Spring Boot. A aplicação permite autenticação de usuários, processamento de métricas de sensores em tempo real via Kafka e exposição dessas métricas através de uma API streaming.

## Arquitetura

O projeto segue a arquitetura hexagonal, separando claramente:

- **Domain**: Regras de negócio e entidades principais
- **Application**: Casos de uso que implementam as operações de negócio
- **Infrastructure**: Adaptadores para tecnologias externas (Web, Kafka, Banco de Dados)

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.2**
- **Spring Security** com OAuth2 Resource Server
- **Keycloak** para autenticação e autorização
- **Spring Data JPA** para persistência de dados
- **PostgreSQL** como banco de dados
- **Flyway** para migrações de banco de dados
- **Apache Kafka** para processamento de eventos em tempo real
- **Gradle** como sistema de build
- **Docker** e **Docker Compose** para conteinerização

## Funcionalidades Principais

### Autenticação

- Login de usuários
- Registro de novos usuários
- Refresh de tokens
- Proteção de endpoints com OAuth2

### Monitoramento de Sensores

- Recebimento de métricas de sensores via Kafka
- Processamento assíncrono de métricas
- Exposição de métricas em tempo real via Server-Sent Events (SSE)

## Pré-requisitos

- Java 21
- PostgreSQL
- Keycloak
- Apache Kafka

Ou, alternativamente:

- Docker e Docker Compose

## Configuração do Ambiente

### Variáveis de Ambiente

A aplicação utiliza as seguintes variáveis de ambiente:

| Variável               | Descrição                                 | Valor Padrão   |
| ---------------------- | ----------------------------------------- | -------------- |
| DB_URL                 | URL de conexão do PostgreSQL              |                |
| DB_USERNAME            | Usuário do banco de dados                 |                |
| DB_PASSWORD            | Senha do banco de dados                   |                |
| KEYCLOAK_ISSUER_URI    | URI do emissor do Keycloak                |                |
| KEYCLOAK_JWK_SET_URI   | URI do conjunto de chaves JWK do Keycloak |                |
| KEYCLOAK_CLIENT_ID     | ID do cliente no Keycloak                 |                |
| KEYCLOAK_CLIENT_SECRET | Segredo do cliente no Keycloak            |                |
| KAFKA_HOST             | Endereço do servidor Kafka                | localhost:9092 |
| SENSOR_DATA_TOPIC      | Tópico Kafka para dados dos sensores      |                |

### Banco de Dados

A aplicação utiliza PostgreSQL e Flyway para gerenciamento de migrações. Configure as propriedades no `application.yml` ou use as variáveis de ambiente:

```yaml
spring:
  datasource:
    url: ${DB_URL:}
    username: ${DB_USERNAME:}
    password: ${DB_PASSWORD:}
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
```

### Kafka

Configure as propriedades para o Kafka:

```yaml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_HOST:localhost:9092}
    consumer:
      group-id: sensor-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer

app:
  kafka:
    topics:
      sensor-data: ${SENSOR_DATA_TOPIC}
```

### Keycloak

Configure as propriedades para o Keycloak:

```yaml
keycloak:
  client-id: ${KEYCLOAK_CLIENT_ID:}
  client-secret: ${KEYCLOAK_CLIENT_SECRET:}

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${KEYCLOAK_ISSUER_URI:}
          jwk-set-uri: ${KEYCLOAK_JWK_SET_URI:}
```

## Executando a Aplicação

A aplicação por padrão será executada na porta 8081.

### Usando Gradle

```bash
./gradlew bootRun
```

### Usando Docker Compose

O projeto inclui um arquivo `docker-compose.yml` que configura e executa todo o ambiente necessário: PostgreSQL, Keycloak, Kafka e a própria aplicação.

Para iniciar todo o ambiente:

```bash
docker-compose up -d
```

Para parar todos os serviços:

```bash
docker-compose down
```

#### Acesso aos serviços

- **Aplicação**: http://localhost:8081
- **Keycloak**: http://localhost:9090 (Admin: admin/admin123)
- **Kafdrop** (Interface Kafka): http://localhost:9000
- **PostgreSQL**: localhost:5432 (postgres/password)

### Configuração inicial

Após iniciar o ambiente pela primeira vez, você precisará:

1. Acessar o Keycloak e criar um realm chamado `sensor-realm`
2. Criar um cliente chamado `sensor-api-client` e configurar o segredo
3. Criar as necessarias (ex: `ROLE_USER`)

## Endpoints da API

### Autenticação

- **POST /api/v1/auth/login** - Autenticação de usuário
- **POST /api/v1/auth/refresh** - Renovação de token
- **POST /api/v1/auth/register** - Registro de novo usuário

### Sensores

- **GET /api/sensors/metrics/stream** - Stream de métricas de sensores em tempo real via Server-Sent Events (SSE). Este endpoint utiliza o protocolo SSE para enviar atualizações contínuas ao cliente em tempo real. Requer autenticação com role USER.

## Tratamento de Erros

A API utiliza códigos de erro padronizados:

- **AUTH001** - Credenciais inválidas
- **AUTH002** - Token expirado
- **AUTH003** - Token inválido
- **AUTH004** - Erro no servidor de autenticação
- **AUTH005** - Erro no registro de usuário

## Desenvolvimento

### Build

```bash
./gradlew build
```

### Testes

```bash
./gradlew test
```

### Docker

O projeto inclui um Dockerfile para construir a imagem da aplicação:

```bash
# Construir a imagem
docker build -t sensor-api:latest .

# Executar o contêiner
docker run -p 8081:8081 --env-file .env sensor-api:latest
```
