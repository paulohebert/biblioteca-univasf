package com.univasf.biblioteca.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.service.BookService;
import com.univasf.biblioteca.util.Dialog;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;

public class Books implements Initializable {
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @FXML
    private MFXTableView<Book> table;
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
        var data = FXCollections.observableArrayList(books);

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
        publisherColumn.setRowCellFactory(book -> new MFXTableRowCell<>(Book::getEditora));
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

            delBtn.setOnAction((event) -> {
                Book bookData = BookService.getLivro(cell.getText());
                String msg = """

                        Tem certeza de que deseja excluir o Livro?

                        ISBN: %d
                        Título: %s
                        Autor: %s
                        """.formatted(bookData.getISBN(), bookData.getTitulo(), bookData.getAutor());

                Dialog deleteDialog = new Dialog(
                        Dialog.Type.WARNING, event,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mEvent) {
                                if (BookService.deleteLivro(bookData)) {
                                    Dialog successDialog = new Dialog(Dialog.Type.INFO, null,
                                            "Excluir Livro",
                                            "O Livro foi excluído com sucesso");
                                    successDialog.show();
                                } else {
                                    Dialog failureDialog = new Dialog(Dialog.Type.ERROR, null,
                                            "Excluir Livro",
                                            "Não foi possível excluir o livro");
                                    failureDialog.show();
                                }
                            }
                        },
                        "Excluir Livro", msg);

                deleteDialog.show();
            });

            cell.setLeadingGraphic(seeBtn);
            cell.setGraphic(editBtn);
            cell.setTrailingGraphic(delBtn);
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
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
        dropdown.getSelectionModel().selectItem(SEARCH.ISBN);
    }
}
