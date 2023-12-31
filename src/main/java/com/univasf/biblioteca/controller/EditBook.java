package com.univasf.biblioteca.controller;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.service.BookService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EditBook implements Initializable {
    private Book book = null;
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private static final Locale locale = new Locale("pt", "BR");

    @FXML
    private MFXTextField isbn;

    @FXML
    private MFXTextField title;

    @FXML
    private MFXTextField author;

    @FXML
    private MFXTextField publisher;

    @FXML
    private TextArea description;

    @FXML
    private MFXTextField amount;

    @FXML
    private MFXTextField category;

    @FXML
    private MFXTextField pageCount;

    @FXML
    private MFXDatePicker publishedDate;

    private void dataInit() {
        isbn.setText(book.getISBN().toString());
        title.setText(book.getTitulo());
        author.setText(book.getAutor());
        publisher.setText(book.getEditora());
        description.setText(book.getDescricao());
        amount.setText(Integer.toString(book.getNumero_copias_totais()));
        category.setText(book.getCategoria());
        pageCount.setText(Integer.toString(book.getNumero_paginas()));
        publishedDate.setValue(book.getAno_publicacao());
    }

    public void setBook(Book book) {
        this.book = book;

        dataInit();

        isbn.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale.setDefault(locale);
        description.setWrapText(true);
        publishedDate.setLocale(locale);
    }

    @FXML
    public void loadBook() {
        Book book = BookService.getLivro(isbn.getText());
        if (book != null) {
            setBook(book);
            isbn.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        } else {
            isbn.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    @FXML
    public void update(Event e) {
        if (book != null) {
            book.setTitulo(title.getText());
            book.setAutor(author.getText());
            book.setEditora(publisher.getText());
            book.setDescricao(description.getText());

            try {
                int amountInt = Integer.parseInt(amount.getText());
                book.setNumero_copias_totais(amountInt);
            } catch (NumberFormatException amoutErr) {
                DialogFactory.showDialog(DialogType.ERROR, "Erro na atualização do livro",
                        "O Número de Cópias deve ser um valor numérico", e);
                return;
            }

            book.setCategoria(category.getText());

            try {
                int pageCountInt = Integer.parseInt(pageCount.getText());
                book.setNumero_paginas(pageCountInt);
            } catch (NumberFormatException amoutErr) {
                DialogFactory.showDialog(DialogType.ERROR, "Erro na atualização do livro",
                        "O Número de Páginas deve ser um valor numérico", e);
                return;
            }

            book.setAno_publicacao(publishedDate.getValue());

            try {
                BookService.updateLivro(book);

                DialogFactory.showDialog(DialogType.INFO, "Atualização do livro",
                        "O Livro foi atualizado com Sucesso");
                close(e);
            } catch (Exception err) {
                DialogFactory.showDialog(DialogType.ERROR, "Erro na atualização do livro",
                        "Não foi possível atualizar o Livro", e);
            }
        } else {
            DialogFactory.showDialog(DialogType.ERROR, "Erro na atualização do livro",
                    "Informe o ISBN do Livro", e);
        }
    }

    @FXML
    public void close(Event e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}
