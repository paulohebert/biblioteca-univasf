package com.univasf.biblioteca.view;

public enum FXMLResource {
    LOGIN("/fxml/login.fxml", "Login"),
    ADMIN_PANEL("/fxml/adminPanel.fxml", "Administrador"),
    USER_PANEL("/fxml/userPanel.fxml", "Usuário"),
    REPORT("/fxml/report.fxml", "Relatório"),
    ADD_USER("/fxml/addUser.fxml", "Cadastrar Usuário"),
    ADD_BOOK("/fxml/addBook.fxml", "Cadastrar Livro"),
    ADD_LOAN("/fxml/addLoan.fxml", "Cadastrar Empréstimo"),
    EDIT_BOOK("/fxml/editBook.fxml", "Editar Livro"),
    EDIT_USER("/fxml/editUser.fxml", "Editar Usuário"),
    BOOK_INFO("/fxml/bookInfo.fxml", "Dados do Livro"),
    USER_INFO("/fxml/userInfo.fxml", "Dados do Usuário"),
    BOOKS("/fxml/books.fxml", "Livros"),
    USERS("/fxml/users.fxml", "Usuários"),
    LOANS("/fxml/loans.fxml", "Empréstimos"),
    CONFIG("/fxml/config.fxml", "Configuração"),
    ABOUT("/fxml/about.fxml", "Sobre");

    private String path;
    private String title;

    private FXMLResource(String path, String title) {
        this.path = path;
        this.title = title;
    }

    public String getFXMLPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }
}
