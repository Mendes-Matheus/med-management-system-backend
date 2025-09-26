# 🏥 Med Management System

Este projeto é uma aplicação backend desenvolvida em **Java com Spring Boot** para gerenciar **usuários, pacientes, estabelecimentos e procedimentos médicos**.  
Projetado para facilitar o cadastro, agendamento e controle de procedimentos em estabelecimentos de saúde.

---

## ⚙️ Funcionalidades Principais

- **Gestão de Usuários**

    - Autenticação e autorização baseadas em **Spring Security**

    - Controle de acesso por **roles** (`ADMIN`, `USER`, etc.)

    - Cadastro de informações pessoais e de contato

- **Gestão de Pacientes**

    - Cadastro completo com CPF, RG, CNS, contatos e endereço

    - Associação de múltiplos procedimentos médicos

    - Histórico e observações clínicas

- **Gestão de Estabelecimentos**

    - Cadastro de clínicas/hospitais

    - Observações específicas sobre cada unidade

- **Gestão de Procedimentos**

    - Cadastro de procedimentos vinculados a estabelecimentos

    - Orientações médicas e observações

    - Associação com pacientes via agendamento

- **Agendamento de Procedimentos (ProcedimentoPaciente)**

    - Registro de solicitações e agendamentos

    - Controle de status (`PENDENTE`, `AGENDADO`, `REALIZADO`, etc.)

    - Relacionamento entre **Paciente** e **Procedimento**

---

## 🏗️ Tecnologias Utilizadas

- **Java 21+**

- **Spring Boot 3+**

- **Spring Data JPA / Hibernate**

- **Spring Security**

- **PostgreSQL**

---

## 🚀 Como Executar

1. Clone o repositório:

   ```bash
   git clone git@github.com:Mendes-Matheus/med-management-system-backend.git
   cd med-management-system-backend
   ```

2. Configure o arquivo `application.yml`:

   ```yaml
   server:
     port: ${SERVER_PORT}
   
   spring:
     datasource:
       url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
       username: ${DB_USER}
       password: ${DB_PASSWORD}
       driver-class-name: org.postgresql.Driver
   
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
       properties:
         hibernate:
           dialect: org.hibernate.dialect.PostgreSQLDialect
           format_sql: true
           jdbc:
             batch_size: 20
             order_inserts: true
             order_updates: true
             batch_versioned_data: true
   
     web:
       cors:
         allowed-origins: ${WEB_CORS_ORIGINS}
         allowed-methods: "GET,POST,PUT,DELETE,OPTIONS"
         allowed-headers: "*"
         allow-credentials: true
   
     messages:
       basename: messages
       encoding: UTF-8
   
   logging:
     level:
       com.example: DEBUG
       org.hibernate.SQL: DEBUG
       org.hibernate.type.descriptor.sql.BasicBinder: TRACE
   
   api:
     security:
       token:
         secret: ${TOKEN.SECRET} # chave gerada com o comando: "openssl rand -hex 32"
         issuer: ${TOKEN_ISSUER}
   ```

3. Crie um arquivo .env com as variáveis de ambiente e configure a IDE para o application.yml ler esse arquivo.

   ```env
   DB_URL=
   DB_USER=
   DB_PASSWORD=
   DB_HOST=
   DB_PORT=
   DB_NAME=
   
   SERVER_PORT=
   
   WEB_CORS_ORIGINS=
   
   TOKEN.SECRET=
   TOKEN_ISSUER=
   
   ```

4. Execute o projeto:

   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🔒 Segurança

- Autenticação baseada em **JWT** (suportado via `Usuario`)

- Perfis de acesso controlados pelo campo `role`

- Endpoints protegidos de acordo com permissões

---
