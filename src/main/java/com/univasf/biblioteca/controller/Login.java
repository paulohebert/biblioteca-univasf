package com.univasf.biblioteca.controller;

import com.univasf.biblioteca.util.Dialog;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;

import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.css.PseudoClass;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.event.Event;

public class Login implements Initializable {

    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

    @FXML
    private MFXTextField cpf;
    @FXML
    private MFXPasswordField password;
    @FXML
    private ImageView univasfLogo;
    @FXML
    private VBox container;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        double width = Window.getWidth();

        cpf.setPrefWidth(140 + width * 0.1);
        password.setPrefWidth(140 + width * 0.1);

        univasfLogo.setFitWidth(200 + width * 0.1);

        container.setPrefWidth(200 + width * 0.14);
        double padding = width * 0.02;
        container.setPadding(new Insets(padding, padding / 2, padding, padding / 2));

        var children = container.getChildren();

        ((MFXFontIcon) children.get(0)).setSize((int) (width * 0.055));
        ((Text) children.get(1)).setFont(new Font(10 + width * 0.02));
        ((HBox) children.get(4)).setPrefHeight(width * 0.03);
    }

    private void validate(boolean valid) {
        cpf.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !valid);
        password.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !valid);
    }

    @FXML
    public void signIn(Event e) throws IOException {
        if (password.getText().equals("123") && cpf.getText().equals("123")) {
            validate(true);

            Dialog signOutDialog = new Dialog(Dialog.Type.INFO, e, "Login Realizado com sucesso",
                    "Seja bem-vindo, Usuário XYZ!");

            signOutDialog.show();

            Window.change(FXMLResource.ADMIN);
        } else if (password.getText().equals("321") && cpf.getText().equals("321")) {
            validate(true);

            Dialog signOutDialog = new Dialog(Dialog.Type.INFO, e, "Login Realizado com sucesso",
                    "Seja bem-vindo, Usuário XYZ!");

            signOutDialog.show();

            Window.change(FXMLResource.USER);
        } else {
            validate(false);

            Dialog failureDialog = new Dialog(Dialog.Type.ERROR, e, "Falha no login", "A Senha ou CPF são Inválidos");

            failureDialog.show();
        }
    }
}
