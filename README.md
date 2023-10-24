# Sistema de Gerenciamento de Biblioteca

## Recursos do Sistema

- [x] Autenticação de Usuário e Administrador
- [ ] Cadastro de Usuário (username, nome, e-mail e senha)
- [ ] Cadastro de Livros (título, autor, ISBN, editora, ano de publicação, categoria e número de cópias disponíveis)
- [ ] Pesquisa de Livros (título, autor, categoria ou ISBN)
- [ ] Empréstimo de Livros (rastrear as datas de empréstimo e devolução)
- [ ] Devolução de Livros
- [ ] Editar e Excluir informações de usuários
- [ ] Relatórios de livros emprestados, reservados e disponíveis, bem como o histórico de empréstimos de um usuário
- [x] Integração com Google Books

## Pré Requisitos

- JDK 17
- Maven
- Docker (ou PostgreSQL)

## Dependências

- JavaFX
- MaterialFX
- Hibernate

## Docker

### Iniciar Container PostgreSQL

```sh
docker compose up -d
```
### Acessar o pgAdmin

`http://localhost:5050/`

### Desligar Container PostgreSQL
```sh
docker compose down
```

## Executar o Projeto

### Usando o Maven

```sh
mvn clean javafx:run
```

### Usando o Maven Wrapper

#### Linux

```sh
./mvnw clean javafx:run
```

#### Windows

```sh
mvnw.cmd clean javafx:run
```