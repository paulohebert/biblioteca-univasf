# Sistema de Gerenciamento de Biblioteca

## Recursos do Sistema

- [x] Autenticação de Usuário e Administrador
- [x] Cadastro de Usuário (username, nome, e-mail e senha)
- [x] Cadastro de Livros (título, autor, ISBN, editora, ano de publicação, categoria e número de cópias disponíveis)
- [x] Pesquisa de Livros (título, autor, categoria ou ISBN)
- [x] Empréstimo de Livros (rastrear as datas de empréstimo e devolução)
- [x] Devolução de Livros
- [x] Editar e Excluir informações de usuários
- [x] Relatórios de livros emprestados e disponíveis, bem como o histórico de empréstimos de um usuário
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

`E-mail: pgadmin4@pgadmin.org`

`Senha: admin`

### Conexão com o Banco PostgreSQL

`Nome do Banco: biblioteca`

`Usuário: admin`

`Senha: admin`

`Porta: 5430`

### Desligar Container PostgreSQL
```sh
docker compose down
```

## Executar o Projeto

### Linux

```sh
./mvnw clean javafx:run
```

### Windows

```cmd
.\mvnw.cmd clean javafx:run
```

## Gerar Instalador

### Linux

#### Criar (.deb/.rpm)

```sh
./mvnw clean package
```

#### Instalar

```sh
sudo dpkg -i target/installer/biblioteca-univasf_1.0-1_amd64.deb
```

#### Desinstalar

```sh
sudo dpkg --purge biblioteca-univasf
```

### Windows

#### Pré Requisitos

- [.NET Framework 3.5](https://www.microsoft.com/pt-br/download/details.aspx?id=22)
- [WiX v3.11.2](https://github.com/wixtoolset/wix3/releases/tag/wix3112rtm)

#### Criar (.exe)

```cmd
.\mvnw.cmd clean package
```

#### Instalar

```cmd
".\target\installer\Biblioteca Univasf-1.0.exe"
```
