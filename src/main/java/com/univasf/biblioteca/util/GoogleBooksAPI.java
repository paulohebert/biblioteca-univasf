package com.univasf.biblioteca.util;

import java.util.List;
import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleBooksAPI {
    private static final String API_URL = "https://www.googleapis.com/books/v1/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static BookService bookService = retrofit.create(BookService.class);

    public static interface BookService {
        @GET("/books/v1/volumes")
        Call<BookSearchResult> searchBook(@Query("q") String q);

        @GET("/books/v1/volumes/{id}")
        Call<Book> getBook(@Path("id") String id);
    }

    public static class Img {
        private String thumbnail;

        public String getThumbnail() {
            return this.thumbnail;
        }
    }

    public static class VolumeInfo {
        private String title, subtitle, publisher, publishedDate, description, language;
        private int pageCount;
        private List<String> authors, categories;
        private Img imageLinks;

        public String getTitle() {
            return this.title;
        }

        public String getSubtitle() {
            return this.subtitle;
        }

        public String getPublisher() {
            return this.publisher;
        }

        public String getPublishedDate() {
            return this.publishedDate;
        }

        public String getDescription() {
            return this.description;
        }

        public String getLanguage() {
            return this.language;
        }

        public int getPageCount() {
            return this.pageCount;
        }

        public List<String> getAuthors() {
            return this.authors;
        }

        public List<String> getCategories() {
            return this.categories;
        }

        public Img getImageLinks() {
            return this.imageLinks;
        }
    }

    public static class Book {
        private String id;
        private VolumeInfo volumeInfo;

        public String getId() {
            return this.id;
        }

        public VolumeInfo getVolumeInfo() {
            return this.volumeInfo;
        }
    }

    public static class BookSearchResult {
        private int totalItems;
        private List<Book> items;

        public int getTotalItems() {
            return this.totalItems;
        }

        public List<Book> getItems() {
            return this.items;
        }
    }

    public static Book searchBook(String isbn) throws IOException {
        Call<BookSearchResult> call = bookService.searchBook("isbn:" + isbn);
        Response<BookSearchResult> response = call.execute();
        if (response.isSuccessful()) {
            BookSearchResult result = response.body();
            if (result.getTotalItems() != 0) {
                return result.getItems().get(0);
            }
            return null;
        }
        return null;
    }

    public static Book getBook(String isbn) throws IOException {
        String id = searchBook(isbn).getId();
        Call<Book> call = bookService.getBook(id);
        Response<Book> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        return null;
    }
}
