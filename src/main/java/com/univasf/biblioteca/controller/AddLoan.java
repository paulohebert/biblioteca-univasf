package com.univasf.biblioteca.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.model.Loan;
import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.BookService;
import com.univasf.biblioteca.service.LoanService;
import com.univasf.biblioteca.service.UserService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AddLoan implements Initializable {
    @FXML
    private MFXComboBox<User> users;
    @FXML
    private MFXComboBox<Book> books;
    @FXML
    private Label cpf, username, name, email, isbn, title, author, publisher;

    private ObservableList<User> usersList = FXCollections.observableArrayList();
    private ObservableList<Book> booksList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        users.setItems(usersList);
        books.setItems(booksList);

        users.setConverter(FunctionalStringConverter
                .to(user -> (user != null) ? "CPF: " + String.format("%011d", user.getCpf()) : ""));
        books.setConverter(FunctionalStringConverter
                .to(book -> (book != null) ? "ISBN: " + book.getISBN() : ""));

        users.setOnCancel(str -> users.setValue(users.getSelectedItem()));
        books.setOnCancel(str -> books.setValue(books.getSelectedItem()));

        users.setOnCommit(str -> {
            if (str.matches("\\d+")) {
                usersList.setAll(UserService.getAllUsersByCPF(str));
            } else {
                usersList.setAll(UserService.getAllUsersByUsername(str));
            }
        });
        books.setOnCommit(str -> {
            if (str.matches("\\d+")) {
                booksList.setAll(BookService.getAllBooksByISBN(str));
            } else {
                booksList.setAll(BookService.getLivrosPorTitulo(str));
            }
        });

        users.setOnHidden(e -> {
            User newUser = users.getSelectedItem();
            if (newUser != null) {
                users.setText("CPF: " + String.format("%011d", newUser.getCpf()));
                cpf.setText(newUser.getCpf().toString());
                username.setText(newUser.getNomeUsuario());
                name.setText(newUser.getNome());
                email.setText(newUser.getEmail());
            }
        });
        books.setOnHidden(e -> {
            Book newBook = books.getSelectedItem();
            if (newBook != null) {
                books.setText("ISBN: " + newBook.getISBN());
                isbn.setText(newBook.getISBN().toString());
                title.setText(newBook.getTitulo());
                author.setText(newBook.getAutor());
                publisher.setText(newBook.getEditora());
            }
        });

        users.setOnKeyReleased(e -> {
            if (!users.isShowing()) {
                users.show();
            }
        });
        books.setOnKeyReleased(e -> {
            if (!books.isShowing()) {
                books.show();
            }
        });

        users.setScrollOnOpen(true);
        books.setScrollOnOpen(true);
    }

    @FXML
    public void register(Event e) {
        User userSelected = users.getSelectedItem();
        Book bookSelected = books.getSelectedItem();
        if (userSelected == null) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro no Empréstimo", "Nenhum Usuário foi Selecionado", e);
        } else if (bookSelected == null) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro no Empréstimo", "Nenhum Livro foi Selecionado", e);
        } else {
            Long booksAvailable = BookService.copiasDisponiveis(bookSelected);
            if (booksAvailable > 0) {
                Loan loan = new Loan(userSelected, bookSelected);
                if (LoanService.saveEmprestimo(loan)) {
                    DialogFactory.showDialog(DialogType.INFO, "Empréstimo Concluído",
                            "O Empréstimo foi realizado com sucesso");
                    close(e);
                } else {
                    DialogFactory.showDialog(DialogType.ERROR, "Erro no Empréstimo",
                            "Não foi possível realizar o empréstimo", e);
                }
            } else {
                DialogFactory.showDialog(DialogType.ERROR, "Erro no Empréstimo",
                        "Todos os livro já foram emprestados", e);
            }
        }
    }

    @FXML
    public void close(Event e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}
