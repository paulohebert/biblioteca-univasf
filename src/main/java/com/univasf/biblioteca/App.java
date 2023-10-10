package com.univasf.biblioteca;

import java.io.IOException;

import javafx.stage.Stage;
import javafx.application.Application;

import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Window.init(stage, FXMLResource.LOGIN);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}