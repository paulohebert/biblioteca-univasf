package com.univasf.biblioteca.view;

public enum FXMLResource {
    LOGIN("/fxml/login.fxml", "Login"),
    ADMIN("/fxml/admin.fxml", "Admin"),
    USER("/fxml/user.fxml", "Usuário"),
    REPORT("/fxml/report.fxml", "Relatório"),
    ADD_USER("/fxml/addUser.fxml", "Cadastrar Usuário"),
    ADD_BOOK("/fxml/addBook.fxml", "Cadastrar Livro"),
    BOOKS("/fxml/books.fxml", "Livros");

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
