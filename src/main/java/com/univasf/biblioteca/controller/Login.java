package com.univasf.biblioteca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.UserService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.util.Session;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Login implements Initializable {

    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

    @FXML
    private MFXTextField cpfUsername;
    @FXML
    private MFXPasswordField password;
    @FXML
    private ImageView univasfLogo;
    @FXML
    private VBox box;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        double width = Window.getWidth();

        cpfUsername.setPrefWidth(140 + width * 0.1);
        password.setPrefWidth(140 + width * 0.1);

        univasfLogo.setFitWidth(200 + width * 0.1);

        box.setPrefWidth(200 + width * 0.14);
        double padding = width * 0.02;
        box.setPadding(new Insets(padding, padding / 2, padding, padding / 2));

        var children = box.getChildren();

        ((MFXFontIcon) children.get(0)).setSize((int) (width * 0.055));
        ((Text) children.get(1)).setFont(new Font(10 + width * 0.02));
        ((HBox) children.get(4)).setPrefHeight(width * 0.03);
    }

    private void validate(boolean valid) {
        cpfUsername.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !valid);
        password.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !valid);
    }

    @FXML
    public void signIn(Event e) throws IOException {
        User user;
        try {
            long cpfLong = Long.parseLong(cpfUsername.getText());
            user = UserService.getUser(cpfLong);
        } catch (NumberFormatException numErr) {
            try {
                user = UserService.getUserByUserName(cpfUsername.getText());
            } catch (Exception err) {
                user = null;
            }
        }

        if (user != null && UserService.checkPassword(password.getText(), user)) {
            DialogFactory.showDialog(DialogType.INFO, "Login Realizado com sucesso",
                    "Seja bem-vindo, " + user.getNome() + "!");

            Session.setUser(user);

            if (user.getTipoAdministrador()) {
                Window.change(FXMLResource.ADMIN);
            } else {
                Window.change(FXMLResource.USER);
            }
        } else {
            validate(false);

            DialogFactory.showDialog(DialogType.ERROR, "Falha no login", "A Senha ou CPF/Username são Inválidos");
        }
    }
}
