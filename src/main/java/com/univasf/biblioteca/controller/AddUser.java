package com.univasf.biblioteca.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.UserService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.Session;
import com.univasf.biblioteca.util.DialogFactory.DialogType;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AddUser implements Initializable {
    @FXML
    private MFXTextField cpf;
    @FXML
    private MFXTextField username;
    @FXML
    private MFXTextField name;
    @FXML
    private MFXTextField email;
    @FXML
    private MFXTextField address;
    @FXML
    private MFXPasswordField password;
    @FXML
    private MFXPasswordField password2;
    @FXML
    private MFXToggleButton admin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Session.getUser() == null) {
            admin.setSelected(false);
            admin.setVisible(false);
            admin.setManaged(false);
        }
    }

    @FXML
    public void register(Event e) {
        User user = new User();

        try {
            long cpfLong = Long.parseLong(cpf.getText());
            user.setCpf(cpfLong);
        } catch (NumberFormatException numErr) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro ao Cadastrar Usuário",
                    "O CPF deve ser um valor numérico", e);
            return;
        }

        user.setNome_usuario(username.getText());
        user.setNome(name.getText());
        user.setEmail(email.getText());
        user.setEndereco(address.getText());

        if (password.getText().equals(password2.getText())) {
            user.setSenha(password.getText());
        } else {
            DialogFactory.showDialog(DialogType.ERROR, "Erro ao Cadastrar Usuário",
                    "As senhas devem ser iguais", e);
            return;
        }

        user.setTipo_administrador(admin.isSelected());

        try {
            UserService.saveUser(user);

            DialogFactory.showDialog(DialogType.INFO, "Cadastro do Usuário", "O Usuário foi cadastrado com Sucesso");
            close(e);
        } catch (Exception err) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro ao Cadastrar Usuário",
                    "Não foi possível cadastrar o usuário", e);
        }
    }

    @FXML
    public void close(Event e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}
