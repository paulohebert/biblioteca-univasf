package com.univasf.biblioteca.view;

import java.io.IOException;

import com.univasf.biblioteca.App;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
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

    private static void setTitle(FXMLResource fxml) {
        mainStage.setTitle("BIBLIOTECA UNIVASF | " + fxml.getTitle());
    }

    private static Parent loadFXML(FXMLResource fxml) throws IOException {
        return FXMLLoader.load(App.class.getResource(fxml.getFXMLPath()));
    }

    public static FXMLLoader loadFXMLResource(FXMLResource fxml) {
        return new FXMLLoader(App.class.getResource(fxml.getFXMLPath()));
    }

    public static void init(Stage stage, FXMLResource fxml) throws IOException {
        mainStage = stage;

        Rectangle2D screen = Screen.getPrimary().getBounds();
        width = screen.getWidth();
        height = screen.getHeight();

        Parent root = loadFXML(fxml);
        Scene scene = new Scene(root, width, height);
        MFXThemeManager.addOn(scene, Themes.DEFAULT);

        setTitle(fxml);
        mainStage.getIcons().add(new Image(App.class.getResourceAsStream("/img/icon.png")));
        mainStage.setMaximized(true);
        mainStage.setScene(scene);
        mainStage.setOnCloseRequest(event -> {
            event.consume();
            Platform.exit();
        });
        mainStage.show();
    }

    public static Stage create(FXMLResource fxml, double width, double height) throws IOException {
        Parent root = loadFXML(fxml);
        return create(root, fxml.getTitle(), width, height);
    }

    public static <T> T createWithController(FXMLResource fxml, double width, double height)
            throws IOException {
        FXMLLoader loader = loadFXMLResource(fxml);
        Parent root = loader.load();
        T controller = loader.getController();
        if (controller != null) {
            create(root, fxml.getTitle(), width, height);
        }
        return controller;
    }

    public static Stage create(Parent root, String title, double width, double height) {
        Stage newStage = new Stage();

        Scene scene = new Scene(root, width, height);
        MFXThemeManager.addOn(scene, Themes.DEFAULT);

        newStage.setScene(scene);
        newStage.setTitle(title);
        newStage.setResizable(false);
        newStage.getIcons().add(new Image(App.class.getResourceAsStream("/img/icon.png")));

        newStage.setX(mainStage.getX() + (Window.width - width) / 2);
        newStage.setY(mainStage.getY() + (Window.height - height) / 2);

        newStage.show();
        return newStage;
    }

    public static void change(FXMLResource fxml) throws IOException {
        Parent root = loadFXML(fxml);
        mainStage.getScene().setRoot(root);
        setTitle(fxml);
    }

    public static void changeNode(Pane root, FXMLResource fxml) throws IOException {
        Parent children = loadFXML(fxml);
        root.getChildren().setAll(children);
    }
}
