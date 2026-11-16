# CRUD Manager

Sistema web de gerenciamento de cadastros desenvolvido como atividade acadêmica do curso de **Sistemas de Informação**.

O projeto tem como objetivo aplicar conceitos de desenvolvimento web com Java, integração com banco de dados relacional, operações CRUD, autenticação de usuários, relacionamentos entre entidades e paginação de registros.

## Funcionalidades

### 1. Gerenciamento de usuários
- Cadastro de usuários.
- Listagem de usuários.
- Edição de usuários.
- Exclusão de usuários.
- Armazenamento de senhas criptografadas.
- Autenticação por e-mail e senha.

### 2. Gerenciamento de posts
- Cadastro de posts.
- Listagem de publicações.
- Edição de posts.
- Exclusão de posts.
- Associação de cada publicação ao usuário responsável.

### 3. Gerenciamento de empresas
- Cadastro de empresas.
- Listagem de empresas.
- Edição de empresas.
- Exclusão de empresas.
- Associação das empresas aos usuários responsáveis.

### 4. Gerenciamento de equipamentos
- Cadastro de equipamentos.
- Listagem de equipamentos.
- Edição de equipamentos.
- Exclusão de equipamentos.
- Registro de nome, tipo, data de aquisição, valor e descrição.
- Associação de equipamentos aos usuários responsáveis.

### 5. Gerenciamento de manutenções
- Cadastro de manutenções.
- Listagem de manutenções.
- Edição de manutenções.
- Exclusão de manutenções.
- Registro de título, tipo, data, custo e descrição.
- Associação de cada manutenção a um equipamento cadastrado.

### 6. Autenticação e controle de acesso
- Tela de login.
- Validação de credenciais.
- Criptografia de senhas utilizando SHA-256 e Base64.
- Controle de acesso às páginas por meio de um filtro de autenticação.
- Encerramento de sessão por meio da funcionalidade de logout.

### 7. Paginação

Foi implementada paginação dinâmica em todos os módulos de cadastro:

- Usuários;
- Posts;
- Empresas;
- Equipamentos;
- Manutenções.

Características:
- Exibição de 5 registros por página.
- Navegação entre páginas.
- Indicação da página atual.
- Desabilitação dos botões Anterior e Próximo nos limites da paginação.
- Consulta ao banco utilizando `LIMIT` e `OFFSET`.
- Contagem total de registros utilizando `COUNT(*)`.

## Relacionamentos entre entidades

O sistema possui relacionamentos entre as tabelas para manter a integridade dos dados.

```text
USERS
  |
  | 1:N
  |
  v
EQUIPAMENTOS
  |
  | 1:N
  |
  v
MANUTENCOES
```

**Relacionamentos implementados:**

- Um usuário pode possuir vários equipamentos.
- Um equipamento pode possuir várias manutenções.
- Cada manutenção está vinculada a um equipamento por meio de uma chave estrangeira.

Nos formulários, os relacionamentos são realizados por meio de campos de seleção (`select`), permitindo escolher registros previamente cadastrados.

## Tecnologias utilizadas

| Tecnologia | Aplicação |
|---|---|
| Java | Linguagem de programação |
| Java Servlets | Controle das requisições HTTP |
| JSP | Construção das páginas dinâmicas |
| JSTL | Iteração e controle de conteúdo nas páginas |
| MySQL | Banco de dados relacional |
| JDBC | Comunicação com o banco de dados |
| HTML5 | Estrutura das páginas |
| CSS3 | Estilização |
| Bootstrap | Interface e componentes visuais |
| JavaScript | Interações da interface |
| jQuery | Manipulação de elementos e modais |
| Apache Tomcat | Servidor de aplicação |
| Eclipse IDE | Ambiente de desenvolvimento |

## Banco de dados

O projeto utiliza MySQL para armazenar os dados.

Principais tabelas:

| Tabela | Descrição |
|---|---|
| `users` | Usuários do sistema |
| `posts` | Publicações dos usuários |
| `companies` | Empresas cadastradas |
| `equipamentos` | Equipamentos cadastrados |
| `manutencoes` | Manutenções dos equipamentos |

### Criação das tabelas de Equipamentos e Manutenções

As tabelas `equipamentos` e `manutencoes` foram desenvolvidas para permitir o gerenciamento dos equipamentos e o registro de suas respectivas manutenções.

#### 1. Tabela equipamentos

Armazena os equipamentos cadastrados e o usuário responsável por cada um.

```sql
CREATE TABLE IF NOT EXISTS equipamentos (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    data_aquisicao DATE NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    descricao TEXT,
    user_id INT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_equipamentos_users
    FOREIGN KEY (user_id)
    REFERENCES users(id)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4;
```

#### 2. Tabela manutencoes

Armazena os registros de manutenção realizados nos equipamentos.

```sql
CREATE TABLE IF NOT EXISTS manutencoes (
    id INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    data_manutencao DATE NOT NULL,
    custo DECIMAL(10,2) NOT NULL,
    descricao TEXT,
    equipamento_id INT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_manutencoes_equipamentos
    FOREIGN KEY (equipamento_id)
    REFERENCES equipamentos(id)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4;
```

#### 3. Explicação dos relacionamentos

**Relacionamento entre usuários e equipamentos:**

- A coluna `user_id` da tabela `equipamentos` referencia a coluna `id` da tabela `users`.
- Um usuário pode ser responsável por vários equipamentos.
- Cada equipamento pertence a um usuário responsável.

**Relacionamento entre equipamentos e manutenções:**

- A coluna `equipamento_id` da tabela `manutencoes` referencia a coluna `id` da tabela `equipamentos`.
- Um equipamento pode possuir várias manutenções.
- Cada manutenção pertence a um equipamento.

**Ordem de criação:** primeiro deve existir a tabela `users`, depois a tabela `equipamentos` e, por último, a tabela `manutencoes`, devido às dependências das chaves estrangeiras.

## Organização do projeto

A aplicação utiliza uma organização baseada no padrão MVC (Model-View-Controller), separando as responsabilidades do sistema.

```text
crud-manager/
│
├── src/main/java/
│   ├── controller/
│   │   ├── UsersController.java
│   │   ├── PostsController.java
│   │   ├── CompaniesController.java
│   │   ├── EquipamentosController.java
│   │   ├── ManutencoesController.java
│   │   └── LoginController.java
│   │
│   ├── filter/
│   │   └── AuthFilter.java
│   │
│   └── model/
│       ├── dao/
│       │   ├── Interfaces DAO
│       │   ├── Implementações MySQL
│       │   ├── DAOFactory.java
│       │   └── DBHandler.java
│       │
│       └── utils/
│           └── PasswordEncryptor.java
│
└── src/main/webapp/
    ├── login.jsp
    ├── index.jsp
    ├── users.jsp
    ├── companies.jsp
    ├── equipamentos.jsp
    ├── manutencoes.jsp
    ├── Formulários JSP
    ├── nav-menu.jsp
    ├── base-head.jsp
    └── Recursos CSS e JavaScript
```

*Estrutura ilustrativa dos principais arquivos e diretórios.*

## Como executar o projeto

### Pré-requisitos

- Java JDK compatível com o projeto.
- Eclipse IDE.
- Apache Tomcat.
- MySQL Server.
- Driver JDBC do MySQL.

### Configuração

1. Clone o repositório:

```bash
git clone URL_DO_REPOSITORIO
```

2. Importe o projeto no Eclipse.

3. Crie o banco de dados MySQL e configure as tabelas necessárias.

4. Crie as tabelas `equipamentos` e `manutencoes` utilizando os comandos SQL apresentados nesta documentação.

5. Configure as credenciais de acesso ao banco de dados na classe responsável pela conexão (`DBHandler`).

6. Verifique se o driver JDBC do MySQL está disponível no projeto.

7. Configure o Apache Tomcat no Eclipse.

8. Execute o projeto no servidor Tomcat.

9. Acesse pelo navegador:

```text
http://localhost:8080/crud-manager
```

10. Cadastre um usuário e utilize suas credenciais para acessar o sistema.

## Considerações finais

O desenvolvimento deste projeto permitiu aplicar conceitos importantes de programação web e banco de dados, incluindo:

- Arquitetura MVC.
- Programação orientada a objetos.
- Operações CRUD.
- Integração Java com MySQL por JDBC.
- Relacionamentos entre tabelas.
- Chaves primárias e estrangeiras.
- Autenticação e gerenciamento de sessões.
- Criptografia de senhas.
- Filtros de requisição.
- Paginação dinâmica com consultas SQL.

O resultado é uma aplicação web que centraliza o gerenciamento de usuários, publicações, empresas, equipamentos e manutenções, utilizando uma estrutura organizada e separação de responsabilidades.

---

**Projeto acadêmico — Sistemas de Informação**
