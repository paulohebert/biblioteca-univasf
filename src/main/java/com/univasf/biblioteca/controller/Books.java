package com.univasf.biblioteca.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.BookService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.util.Session;
import com.univasf.biblioteca.util.TableUtil;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class Books implements Initializable {
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ObservableList<Book> data = FXCollections.observableArrayList();
    private User user;
    @FXML
    private MFXTableView<Book> table;
    @FXML
    private MFXTextField searchField;
    @FXML
    private MFXComboBox<SEARCH> dropdown;
    @FXML
    private Text results;
    @FXML
    private MFXButton addBookButton;

    public enum SEARCH {
        ISBN,
        Título,
        Autor,
        Categoria;
    };

    private EventHandler<ActionEvent> seeEvent = (e) -> {
        try {
            BookInfo bookInfo = Window.<BookInfo>createWithController(FXMLResource.BOOK_INFO, 800, 500);
            if (bookInfo != null) {
                String isbnStr = ((Labeled) ((MFXButton) e.getSource()).getParent().getParent()).getText();
                Book book = BookService.getLivro(isbnStr);
                if (book != null) {
                    bookInfo.setBook(book);
                } else {
                    throw new IOException("O Livro não existe");
                }
            } else {
                throw new IOException("Não é possível visualizar o livro");
            }
        } catch (IOException err) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro ao Visualizar o Livro", err.getMessage());
            err.printStackTrace();
        }
    };

    private EventHandler<ActionEvent> editEvent = (e) -> {
        try {
            EditBook editBook = Window.<EditBook>createWithController(FXMLResource.EDIT_BOOK, 700, 500);
            if (editBook != null) {
                String isbnStr = ((Labeled) ((MFXButton) e.getSource()).getParent()).getText();
                Book book = BookService.getLivro(isbnStr);
                if (book != null) {
                    editBook.setBook(book);
                } else {
                    throw new IOException("O Livro não existe");
                }
            } else {
                throw new IOException("Não é possível editar o livro");
            }
        } catch (IOException err) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro ao Editar o Livro", err.getMessage());
            err.printStackTrace();
        }
    };

    private EventHandler<ActionEvent> delEvent = (e) -> {
        String isbnStr = ((Labeled) ((MFXButton) e.getSource()).getParent().getParent()).getText();
        Book book = BookService.getLivro(isbnStr);
        if (book != null) {
            String msg = """
                    Tem certeza de que deseja excluir o Livro?

                    ISBN: %d
                    Título: %s
                    Autor: %s

                    """.formatted(book.getISBN(), book.getTitulo(), book.getAutor());

            EventHandler<MouseEvent> eventConfirm = (event) -> {
                if (BookService.deleteLivro(book)) {
                    DialogFactory.showDialog(DialogType.INFO, "Excluir Livro",
                            "O Livro foi excluído com sucesso");
                } else {
                    DialogFactory.showDialog(DialogType.ERROR, "Excluir Livro",
                            "\n\tNão foi possível excluir o livro.\n\n\t\f\f\fCertifique que o Livro\n não possua histórico de empréstimo");
                }
            };

            DialogFactory.showDialog(DialogType.WARNING, "Excluir Livro", msg, eventConfirm);
        }
    };

    private void tableInit() {
        TableUtil<Book> tableUtil = new TableUtil<>();

        var columns = table.getTableColumns();
        columns.add(tableUtil.createColumn("ISBN", Comparator.comparing(Book::getISBN), Book::getISBN));
        columns.add(tableUtil.createColumn("Título", Comparator.comparing(Book::getTitulo), Book::getTitulo));
        columns.add(tableUtil.createColumn("Autor", Comparator.comparing(Book::getAutor), Book::getAutor));
        columns.add(tableUtil.createColumn("Editora", Comparator.comparing(Book::getEditora), (bookModel) -> {
            String publisher = bookModel.getEditora();
            return publisher != null ? publisher : "";
        }));
        columns.add(tableUtil.createColumn("Data de Publicação", Comparator.comparing(Book::getAno_publicacao),
                (bookModel) -> {
                    LocalDate date = bookModel.getAno_publicacao();
                    return date != null ? date.format(format) : "";
                }));
        columns.add(tableUtil.createActions(Book::getISBN, seeEvent,
                user.getTipoAdministrador() ? editEvent : null,
                user.getTipoAdministrador() ? delEvent : null));

        table.setItems(data);
        table.autosizeColumnsOnInitialization();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = Session.getUser();
        if (user == null) {
            try {
                Window.change(FXMLResource.LOGIN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (user.getTipoAdministrador() == false) {
                addBookButton.setVisible(false);
                addBookButton.setManaged(false);
            }

            dropdown.setItems(FXCollections.observableArrayList(SEARCH.values()));
            dropdown.getSelectionModel().selectItem(SEARCH.Título);

            search();
            if (data.isEmpty()) {
                data.setAll(new Book[1]);
            }
            tableInit();
        }
    }

    @FXML
    public void addBook() throws IOException {
        Window.create(FXMLResource.ADD_BOOK, 700, 500);
    }

    @FXML
    public void search() {
        SEARCH type = dropdown.getSelectedItem();
        data.clear();
        switch (type) {
            case ISBN:
                List<Book> booksByISBN = BookService.getAllBooksByISBN(searchField.getText());
                data.setAll(booksByISBN);
                break;
            case Título:
                List<Book> booksByTitle = BookService.getLivrosPorTitulo(searchField.getText());
                data.setAll(booksByTitle);
                break;
            case Autor:
                List<Book> booksByAuthor = BookService.getLivrosPorAutor(searchField.getText());
                data.setAll(booksByAuthor);
                break;
            case Categoria:
                List<Book> booksByCategory = BookService.getLivrosPorCategoria(searchField.getText());
                data.setAll(booksByCategory);
                break;
        }

        results.setText(Integer.toString(data.size()));
    }
}
