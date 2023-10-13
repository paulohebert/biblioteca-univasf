module com.univasf.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires MaterialFX;

    requires retrofit2;
    requires retrofit2.converter.gson;
    requires com.google.gson;

    opens com.univasf.biblioteca.controller to javafx.fxml;
    opens com.univasf.biblioteca.util to com.google.gson;

    exports com.univasf.biblioteca;
    exports com.univasf.biblioteca.controller;
}
