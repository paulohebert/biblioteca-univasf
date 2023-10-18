package com.univasf.biblioteca.util;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.services.books.v1.Books;
import com.google.api.services.books.v1.BooksRequestInitializer;
import com.google.api.services.books.v1.model.Volume;
import com.google.api.services.books.v1.model.Volumes;

public class GoogleBooksAPI {
    private static Books booksService;
    private static Books.Volumes volumes;
    static {
        try {
            booksService = new Books.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(), null)
                    .setApplicationName("Biblioteca Univasf")
                    .setGoogleClientRequestInitializer(new BooksRequestInitializer())
                    .build();

            volumes = booksService.volumes();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Volume getBook(String isbn) throws IOException {
        if (volumes != null) {
            Books.Volumes.List list = volumes.list("isbn:" + isbn);
            Volumes volumes = list.execute();

            if (volumes.getTotalItems() == 1) {
                return volumes.getItems().get(0);
            }
        }
        return null;
    }
}
