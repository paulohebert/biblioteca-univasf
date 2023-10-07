package com.univasf.biblioteca.gui;

import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;

import com.univasf.biblioteca.util.Window;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXPasswordField;

import java.net.URL;
import java.io.IOException;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.stage.Screen;

public class Login implements Initializable {

    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private static Dialog successDialog;
    private static Dialog failureDialog;

    @FXML
    private MFXTextField cpf;
    @FXML
    private MFXPasswordField password;
    @FXML
    private ImageView univasfLogo;
    @FXML
    private VBox container;
    @FXML
    private Text bibliotecaText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        double width = Window.getWidth();
        univasfLogo.setFitWidth(200 + width * 0.1);

        container.setPrefWidth(200 + width * 0.14);

        double padding = width * 0.02;
        container.setPadding(new Insets(padding, padding / 2, padding, padding / 2));

        bibliotecaText.setFont(new Font(10 + width * 0.02));
        cpf.setPrefWidth(140 + width * 0.1);
        password.setPrefWidth(140 + width * 0.1);
        ((FontIcon) container.getChildren().get(0)).setIconSize((int) (width * 0.055));
        ((HBox) container.getChildren().get(4)).setPrefHeight(width * 0.03);
    }

    @FXML
    public void signIn(Event e) throws IOException {
        if (password.getText().equals("123") && cpf.getText().equals("123")) {
            cpf.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            password.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);

            if (successDialog == null) {
                Stage stage = ((Stage) ((Node) e.getSource()).getScene().getWindow());
                successDialog = Dialog.makeInfo(stage, "Seja bem vindo, Usuário XYZ", "Login Realizado com sucesso");
            }

            successDialog.open();
            Window.change("admin");

        } else if (password.getText().equals("321") && cpf.getText().equals("321")) {
            cpf.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            password.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);

            if (successDialog == null) {
                Stage stage = ((Stage) ((Node) e.getSource()).getScene().getWindow());
                successDialog = Dialog.makeInfo(stage, "Seja bem vindo, Usuário XYZ", "Login Realizado com sucesso");
            }

            successDialog.open();
            Window.change("user");
        } else {
            cpf.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            password.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);

            if (failureDialog == null) {
                Stage stage = ((Stage) ((Node) e.getSource()).getScene().getWindow());
                failureDialog = Dialog.makeError(stage, "A Senha ou CPF são Inválidos", "Falha no login");
            }

            failureDialog.open();
        }
    }

    @FXML
    public void close(Event e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }

}
