package com.univasf.biblioteca.controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.util.GoogleBooksAPI;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookInfo implements Initializable {
    private Book book = null;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
    private MFXTextField publishedDate;

    @FXML
    private ImageView bookCover;

    private void dataInit() {
        isbn.setText(book.getISBN().toString());
        title.setText(book.getTitulo());
        author.setText(book.getAutor());
        publisher.setText(book.getEditora());
        description.setText(book.getDescricao());
        amount.setText(Integer.toString(book.getNumero_copias_totais()));
        category.setText(book.getCategoria());
        pageCount.setText(Integer.toString(book.getNumero_paginas()));
        publishedDate.setText(book.getAno_publicacao().format(format));

        new Thread(() -> {
            Image image = GoogleBooksAPI.getImage(book.getISBN().toString());
            if (image != null) {
                Platform.runLater(() -> bookCover.setImage(image));
            }
        }).start();
    }

    public void setBook(Book book) {
        this.book = book;
        dataInit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        description.setWrapText(true);

        isbn.setEditable(false);
        title.setEditable(false);
        author.setEditable(false);
        publisher.setEditable(false);
        description.setEditable(false);
        amount.setEditable(false);
        category.setEditable(false);
        pageCount.setEditable(false);
        publishedDate.setEditable(false);
    }
}
