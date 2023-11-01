package com.univasf.biblioteca.controller;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.service.BookService;
import com.univasf.biblioteca.util.Dialog;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EditBook implements Initializable {
    private Book book = null;
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
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale.setDefault(locale);
        description.setWrapText(true);
        publishedDate.setLocale(locale);
    }

    @FXML
    public void update(Event e) {
        book.setTitulo(title.getText());
        book.setAutor(author.getText());
        book.setEditora(publisher.getText());
        book.setDescricao(description.getText());

        try {
            int amountInt = Integer.parseInt(amount.getText());
            book.setNumero_copias_totais(amountInt);
        } catch (NumberFormatException amoutErr) {
            Dialog errDialog = new Dialog(Dialog.Type.ERROR, e, "Erro na atualização do livro",
                    "O Número de Cópias deve ser um valor numérico");
            errDialog.show();
            return;
        }

        book.setCategoria(category.getText());

        try {
            int pageCountInt = Integer.parseInt(pageCount.getText());
            book.setNumero_paginas(pageCountInt);
        } catch (NumberFormatException amoutErr) {
            Dialog errDialog = new Dialog(Dialog.Type.ERROR, e, "Erro na atualização do livro",
                    "O Número de Páginas deve ser um valor numérico");
            errDialog.show();
            return;
        }

        book.setAno_publicacao(publishedDate.getValue());

        try {
            BookService.updateLivro(book);

            Dialog successDialog = new Dialog(Dialog.Type.INFO, null, "Atualização do livro",
                    "O Livro foi atualizado com Sucesso");
            successDialog.show();
            close(e);
        } catch (Exception err) {
            Dialog failureDialog = new Dialog(Dialog.Type.ERROR, null, "Erro na atualização do livro",
                    "Não foi possível atualizar o Livro");
            failureDialog.show();
        }
    }

    @FXML
    public void close(Event e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}
