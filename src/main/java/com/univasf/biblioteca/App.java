package com.univasf.biblioteca;

import com.univasf.biblioteca.util.Window;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Window.config(stage, "LOGIN", "login");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/icon.png")));

    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}