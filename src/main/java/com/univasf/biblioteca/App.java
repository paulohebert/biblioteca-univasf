package com.univasf.biblioteca;

import java.io.IOException;

import javafx.stage.Stage;
import javafx.application.Application;

import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.util.HibernateUtil;
import com.univasf.biblioteca.util.Session;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Session.setHostServices(this.getHostServices());
        Window.init(stage, FXMLResource.LOGIN);
        try {
            HibernateUtil.initialize();
        } catch (Exception e) {
            DialogFactory.showDialog(DialogType.ERROR, "Falha no Banco de Dados",
                    "O Banco de Dados não está respondendo");
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}