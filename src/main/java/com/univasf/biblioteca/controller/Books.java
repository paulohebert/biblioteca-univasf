package com.univasf.biblioteca.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.service.BookService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;

public class Books implements Initializable {
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ObservableList<Book> data;
    @FXML
    private MFXTableView<Book> table;
    @FXML
    private MFXTextField searchField;
    @FXML
    private MFXComboBox<SEARCH> dropdown;

    public enum SEARCH {
        ISBN,
        Título,
        Autor,
        Categoria;
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        var books = BookService.getAllLivros();
        data = FXCollections.observableArrayList(books);

        MFXTableColumn<Book> isbnColumn = new MFXTableColumn<>("ISBN", true, Comparator.comparing(Book::getISBN));
        MFXTableColumn<Book> titleColumn = new MFXTableColumn<>("Título", true, Comparator.comparing(Book::getTitulo));
        MFXTableColumn<Book> authorColumn = new MFXTableColumn<>("Autor", true, Comparator.comparing(Book::getAutor));
        MFXTableColumn<Book> publisherColumn = new MFXTableColumn<>("Editora", true,
                Comparator.comparing(Book::getEditora));
        MFXTableColumn<Book> dateColumn = new MFXTableColumn<>("Data de Publicação", true,
                Comparator.comparing(Book::getAno_publicacao));

        isbnColumn.setRowCellFactory(book -> new MFXTableRowCell<>(Book::getISBN));
        titleColumn.setRowCellFactory(book -> new MFXTableRowCell<>(Book::getTitulo));
        authorColumn.setRowCellFactory(book -> new MFXTableRowCell<>(Book::getAutor));
        publisherColumn.setRowCellFactory(book -> new MFXTableRowCell<>((bookModel) -> {
            String publisher = book.getEditora();
            return publisher != null ? publisher : "";
        }));
        dateColumn.setRowCellFactory(book -> new MFXTableRowCell<>((bookModel) -> {
            LocalDate date = book.getAno_publicacao();
            return date != null ? date.format(format) : "";
        }));

        MFXTableColumn<Book> actionColumn = new MFXTableColumn<Book>("");
        actionColumn.setRowCellFactory(book -> {
            MFXTableRowCell<Book, Long> cell = new MFXTableRowCell<Book, Long>(Book::getISBN);

            MFXButton seeBtn = new MFXButton(null, new MFXFontIcon("fas-eye"));
            MFXButton editBtn = new MFXButton(null, new MFXFontIcon("fas-pen-to-square"));
            MFXButton delBtn = new MFXButton(null, new MFXFontIcon("fas-trash-can"));

            seeBtn.getStyleClass().add("button-gray");
            editBtn.getStyleClass().add("button-blue");
            delBtn.getStyleClass().add("button-red");
            seeBtn.setOnAction((event) -> {
                try {
                    Book bookData = BookService.getLivro(cell.getText());
                    if (bookData != null) {
                        FXMLLoader fxml = Window.loadFXMLResource(FXMLResource.Book_Details);
                        Parent root = fxml.load();
                        Window.create(root, FXMLResource.Book_Details.getTitle(), 800, 500);

                        BookDetails bookDetails = fxml.getController();
                        bookDetails.setBook(bookData);
                    } else {
                        throw new Exception();
                    }
                } catch (Exception err) {
                    DialogFactory.showDialog(DialogType.ERROR, "Ver Livro",
                            "Não foi possível visualizar o livro");

                    err.printStackTrace();
                }
            });
            editBtn.setOnAction((event) -> {
                try {
                    Book bookData = BookService.getLivro(cell.getText());
                    if (bookData != null) {
                        FXMLLoader fxml = Window.loadFXMLResource(FXMLResource.EDIT_BOOK);
                        Parent root = fxml.load();
                        Window.create(root, FXMLResource.EDIT_BOOK.getTitle(), 700, 500);

                        EditBook editBook = fxml.getController();
                        editBook.setBook(bookData);
                    } else {
                        throw new Exception();
                    }
                } catch (Exception err) {
                    DialogFactory.showDialog(DialogType.ERROR, "Editar Livro",
                            "Não foi possível editar o livro");

                    err.printStackTrace();
                }
            });
            delBtn.setOnAction((event) -> {
                Book bookData = BookService.getLivro(cell.getText());
                if (bookData != null) {
                    String msg = """

                            Tem certeza de que deseja excluir o Livro?

                            ISBN: %d
                            Título: %s
                            Autor: %s
                            """.formatted(bookData.getISBN(), bookData.getTitulo(), bookData.getAutor());
                    EventHandler<MouseEvent> eventConfirm = (e) -> {
                        if (BookService.deleteLivro(bookData)) {
                            DialogFactory.showDialog(DialogType.INFO, "Excluir Livro",
                                    "O Livro foi excluído com sucesso");

                            data.removeIf(bookM -> {
                                try {
                                    long isbnLong = Long.parseLong(cell.getText());
                                    return bookM.getISBN() == isbnLong;
                                } catch (NumberFormatException numErr) {
                                    return false;
                                }
                            });
                        } else {
                            DialogFactory.showDialog(DialogType.ERROR, "Excluir Livro",
                                    "Não foi possível excluir o livro");
                        }
                    };

                    DialogFactory.showDialog(DialogType.WARNING, "Excluir Livro", msg, eventConfirm);
                }
            });

            cell.setLeadingGraphic(seeBtn);
            cell.setGraphic(editBtn);
            cell.setTrailingGraphic(delBtn);
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            cell.setWrapText(true);
            return cell;
        });

        table.getTableColumns().add(isbnColumn);
        table.getTableColumns().add(titleColumn);
        table.getTableColumns().add(authorColumn);
        table.getTableColumns().add(publisherColumn);
        table.getTableColumns().add(dateColumn);
        table.getTableColumns().add(actionColumn);

        table.setItems(data);
        table.autosizeColumnsOnInitialization();

        dropdown.setItems(FXCollections.observableArrayList(SEARCH.values()));
        dropdown.getSelectionModel().selectItem(SEARCH.Título);
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
                Book booksByISBN = BookService.getLivro(searchField.getText());
                if (booksByISBN != null) {
                    data.setAll(booksByISBN);
                }
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
    }
}
