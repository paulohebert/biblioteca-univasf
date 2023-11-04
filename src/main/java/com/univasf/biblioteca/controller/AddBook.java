package com.univasf.biblioteca.controller;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.service.BookService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.util.GoogleBooksAPI;

import com.google.api.services.books.v1.model.Volume;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
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

public class AddBook implements Initializable {
    private static final Locale locale = new Locale("pt", "BR");
    private static final DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM", locale);
    private static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", locale);

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale.setDefault(locale);
        description.setWrapText(true);
        publishedDate.setLocale(locale);
    }

    @FXML
    public void register(Event e) {
        Book book = new Book();
        try {
            String isbnTxt = isbn.getText();
            int size = isbnTxt.length();
            if (size != 10 && size != 13) {
                throw new IOException("ISBN inválido");
            }

            long isbnLong = Long.parseLong(isbnTxt);
            book.setISBN(isbnLong);
        } catch (NumberFormatException isbnErr) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro no Cadastro do livro",
                    "O ISBN deve ser um valor numérico", e);
            return;
        } catch (IOException ioErr) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro no Cadastro do livro", ioErr.getMessage(), e);
            return;
        }

        book.setTitulo(title.getText());
        book.setAutor(author.getText());
        book.setEditora(publisher.getText());
        book.setDescricao(description.getText());

        try {
            int amountInt = Integer.parseInt(amount.getText());
            book.setNumero_copias_totais(amountInt);
        } catch (NumberFormatException amoutErr) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro no Cadastro do livro",
                    "O Número de Cópias deve ser um valor numérico", e);
            return;
        }

        book.setCategoria(category.getText());

        try {
            int pageCountInt = Integer.parseInt(pageCount.getText());
            book.setNumero_paginas(pageCountInt);
        } catch (NumberFormatException amoutErr) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro no Cadastro do livro",
                    "O Número de Páginas deve ser um valor numérico", e);
            return;
        }

        book.setAno_publicacao(publishedDate.getValue());

        try {
            BookService.saveLivro(book);

            DialogFactory.showDialog(DialogType.INFO, "Cadastro do livro", "O Livro foi cadastrado com Sucesso");
            close(e);
        } catch (Exception err) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro no Cadastro do livro",
                    "Não foi possível cadastrar o Livro");
        }
    }

    @FXML
    public void loadDataByISBN() {
        Volume book = GoogleBooksAPI.getBook(isbn.getText());
        if (book != null) {
            Volume.VolumeInfo vInfo = book.getVolumeInfo();

            String titleStr = vInfo.getTitle();
            title.setText(titleStr != null ? titleStr : "");

            List<String> authorsList = vInfo.getAuthors();
            author.setText(authorsList != null && authorsList.size() != 0 ? String.join(", ", authorsList) : "");

            String publisherStr = vInfo.getPublisher();
            publisher.setText(publisherStr != null ? publisherStr : "");

            String descriptionStr = vInfo.getDescription();
            description.setText(descriptionStr != null ? descriptionStr : "");

            Integer pageCountInt = vInfo.getPageCount();
            pageCount.setText(pageCountInt != null ? pageCountInt.toString() : "");

            List<String> categoriesList = vInfo.getCategories();
            category.setText(
                    categoriesList != null && categoriesList.size() != 0 ? String.join(", ", categoriesList) : "");

            String publishedDateStr = vInfo.getPublishedDate();
            if (publishedDateStr != null) {
                try {
                    LocalDate localDate = LocalDate.parse(publishedDateStr, localDateFormatter);
                    publishedDate.setValue(localDate);
                } catch (DateTimeParseException errLocalDate) {
                    try {
                        YearMonth yearMonth = YearMonth.parse(publishedDateStr, yearMonthFormatter);
                        LocalDate localDate = yearMonth.atDay(1);
                        publishedDate.setValue(localDate);
                    } catch (DateTimeParseException errYearMonth) {
                        publishedDate.setValue(null);
                    }
                }
            } else {
                publishedDate.setValue(null);
            }
        }
    }

    @FXML
    public void close(Event e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}
