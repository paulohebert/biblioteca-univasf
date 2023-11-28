module com.univasf.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires MaterialFX;

    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.books;
    requires google.api.client;

    requires org.hibernate.orm.core;
    requires org.postgresql.jdbc;
    requires jakarta.persistence;
    requires bcrypt;
    requires java.naming;

    opens com.univasf.biblioteca.controller to javafx.fxml, javafx.graphics;
    opens com.univasf.biblioteca.model to org.hibernate.orm.core;

    exports com.univasf.biblioteca;
    exports com.univasf.biblioteca.service;
    exports com.univasf.biblioteca.controller;
    exports com.univasf.biblioteca.model;
}
