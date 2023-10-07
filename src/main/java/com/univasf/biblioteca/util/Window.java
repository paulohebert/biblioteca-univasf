package com.univasf.biblioteca.util;

import java.io.IOException;

import com.univasf.biblioteca.App;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Window {
    private static Stage mainStage;
    private static double width, height;

    public static Stage getStage() {
        return mainStage;
    }

    public static double getWidth() {
        return width;
    }

    public static double getHeight() {
        return height;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        return FXMLLoader.load(App.class.getResource("/fxml/" + fxml + ".fxml"));
    }

    public static void change(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        mainStage.getScene().setRoot(root);
    }

    public static void config(Stage stage, String name, String fxml) throws IOException {
        mainStage = stage;
        Rectangle2D screen = Screen.getPrimary().getBounds();
        width = screen.getWidth();
        height = screen.getHeight();

        Parent root = loadFXML(fxml);
        Scene scene = new Scene(root, width, height);
        MFXThemeManager.addOn(scene, Themes.DEFAULT);

        scene.getStylesheets().add(App.class.getResource("/css/styles.css").toExternalForm());
        stage.setTitle("BIBLIOTECA UNIVASF - " + name);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
