package com.univasf.biblioteca;

import java.io.IOException;

import javafx.stage.Stage;
import javafx.application.Application;

import com.univasf.biblioteca.util.Dialog;
import com.univasf.biblioteca.util.HibernateUtil;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Window.init(stage, FXMLResource.LOGIN);
        try {
            HibernateUtil.initialize();
        } catch (Exception e) {
            Dialog errDialog = new Dialog(Dialog.Type.ERROR, null, "Falha no Banco de Dados",
                    "O Banco de Dados não está respondendo");
            errDialog.show();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}