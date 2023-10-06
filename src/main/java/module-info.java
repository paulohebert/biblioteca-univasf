module com.univasf.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires MaterialFX;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.univasf.biblioteca.gui to javafx.fxml;

    exports com.univasf.biblioteca;
    exports com.univasf.biblioteca.gui;
}
