# Sistema de Gerenciamento de Biblioteca

## Funções

- [x] Autenticação de Usuário e Administrador
- [ ] Cadastro de Usuário (username, nome, e-mail e senha)
- [ ] Cadastro de Livros (título, autor, ISBN, editora, ano de publicação, categoria e número de cópias disponíveis)
- [ ] Pesquisa de Livros (título, autor, categoria ou ISBN)
- [ ] Empréstimo de Livros (rastrear as datas de empréstimo e devolução)
- [ ] Devolução de Livros
- [ ] Editar e Excluir informações de usuários
- [ ] Relatórios de livros emprestados, reservados e disponíveis, bem como o histórico de empréstimos de um usuário

## Dependências

- JDK 17
- Maven
- JavaFX
- MaterialFX
- Hibernate
- PostgreSQL

## Instalar Dependências

```sh
mvn install
#ou ./mvnw clean install (Linux)
#ou mvnw.cmd clean install (Windows)
```

## Executar o Projeto

```sh
mvn clean javafx:run
#ou ./mvnw clean javafx:run (Linux)
#ou mvnw.cmd clean javafx:run (Windows)
```

## Gerar Ambiente de Execução do Projeto

```sh
mvn clean javafx:jlink
#ou ./mvnw clean javafx:jlink (Linux)
#ou mvnw.cmd clean javafx:jlink (Windows)
```