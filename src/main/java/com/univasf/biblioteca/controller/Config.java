package com.univasf.biblioteca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.UserService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.util.Session;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Config implements Initializable {
    private User user;
    @FXML
    private HBox cpf, username, name, email, address, password;

    private void addText(HBox node, String text) {
        ObservableList<Node> children = node.getChildren();
        children.remove(1);
        children.add(new Text(text));
    }

    private void addField(HBox node, String text, Consumer<String> func) {
        ObservableList<Node> children = node.getChildren();
        if (children.get(1) instanceof Text) {
            MFXTextField field = new MFXTextField(text);
            field.getStyleClass().add("field");
            field.setFloatMode(FloatMode.DISABLED);
            field.setPrefWidth(200);
            field.setOnAction(e -> {
                String content = ((MFXTextField) e.getSource()).getText();
                addText(node, content);
                func.accept(content);
            });

            children.remove(1);
            children.add(field);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = Session.getUser();
        if (user != null) {
            cpf.getChildren().add(new Text(user.getCpf().toString()));
            username.getChildren().add(new Text(user.getNomeUsuario()));
            name.getChildren().add(new Text(user.getNome()));
            email.getChildren().add(new Text(user.getEmail()));
            address.getChildren().add(new Text(user.getEndereco()));
        } else {
            try {
                Window.change(FXMLResource.LOGIN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void editName(Event event) {
        addField(name, user.getNome(), (text) -> {
            try {
                user.setNome(text);
                UserService.updateUser(user);
            } catch (Exception err) {
                DialogFactory.showDialog(DialogType.ERROR, "Editar Dados",
                        "Não foi possível alterar o \"Nome\" do Usuário");
            }
        });
    }

    @FXML
    public void editEmail(Event event) {
        addField(email, user.getEmail(), (text) -> {
            try {
                user.setEmail(text);
                UserService.updateUser(user);
            } catch (Exception err) {
                DialogFactory.showDialog(DialogType.ERROR, "Editar Dados",
                        "Não foi possível alterar o \"Email\" do Usuário");
            }
        });
    }

    @FXML
    public void editAddress(Event event) {
        addField(address, user.getEndereco(), (text) -> {
            try {
                user.setEndereco(text);
                UserService.updateUser(user);
            } catch (Exception err) {
                DialogFactory.showDialog(DialogType.ERROR, "Editar Dados",
                        "Não foi possível alterar o \"Endereço\" do Usuário");
            }
        });
    }

    @FXML
    public void changePassword() {
        if (password.getChildren().size() == 0) {
            MFXPasswordField passwordField = new MFXPasswordField();
            MFXPasswordField passwordField2 = new MFXPasswordField();
            MFXButton submitBtn = new MFXButton("Confirmar");

            passwordField.getStyleClass().add("field");
            passwordField.setFloatMode(FloatMode.DISABLED);
            passwordField.setPrefWidth(200);
            passwordField.setPromptText("Senha");

            passwordField2.getStyleClass().add("field");
            passwordField2.setFloatMode(FloatMode.DISABLED);
            passwordField2.setPrefWidth(200);
            passwordField2.setPromptText("Confirme a Senha");

            submitBtn.getStyleClass().add("button-secondary");
            submitBtn.setOnAction(e -> {
                if (passwordField.getText().equals(passwordField2.getText())) {
                    try {
                        user.setSenha(passwordField.getText());
                        UserService.updateUser(user);

                        DialogFactory.showDialog(DialogType.INFO, "Alterar Senha",
                                "A Senha foi alterada com sucesso");

                        password.getChildren().clear();
                    } catch (Exception err) {
                        DialogFactory.showDialog(DialogType.ERROR, "Erro ao Alterar Senha",
                                "Não foi possível alterar a Senha");
                    }
                } else {
                    DialogFactory.showDialog(DialogType.ERROR, "Erro ao Alterar Senha",
                            "As senhas devem ser iguais");
                }
            });

            password.getChildren().setAll(passwordField, passwordField2, submitBtn);
        }
    }

    @FXML
    public void deleteAccount() {
        EventHandler<MouseEvent> eventConfirm = (e) -> {
            if (UserService.deleteUser(user)) {
                DialogFactory.showDialog(DialogType.INFO,
                        "Excluir Conta",
                        "Sua conta foi excluída com sucesso");
            } else {
                DialogFactory.showDialog(DialogType.ERROR,
                        "Excluir Conta",
                        "Não foi possível excluir sua conta");
            }
        };

        DialogFactory.showDialog(DialogType.WARNING, "Excluir Conta", "Tem certeza de que deseja excluir sua conta?",
                eventConfirm);

    }
}
