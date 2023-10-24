module com.univasf.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires MaterialFX;

    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.books;
    requires google.api.client;

    requires bcrypt;
    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires org.postgresql.jdbc;

    opens com.univasf.biblioteca.controller to javafx.fxml;

    exports com.univasf.biblioteca;
    exports com.univasf.biblioteca.controller;
}
