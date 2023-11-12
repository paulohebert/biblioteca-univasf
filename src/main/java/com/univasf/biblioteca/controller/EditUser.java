package com.univasf.biblioteca.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.UserService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

public class EditUser implements Initializable {
    private User user;
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

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

    private void dataInit() {
        cpf.setText(user.getCpf().toString());
        username.setText(user.getNomeUsuario());
        name.setText(user.getNome());
        email.setText(user.getEmail());
        address.setText(user.getEndereco());
        admin.setSelected(user.getTipoAdministrador());
    }

    public void setUser(User user) {
        this.user = user;

        dataInit();

        cpf.setDisable(true);
        username.setDisable(true);
        admin.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        admin.setDisable(true);
    }

    @FXML
    public void loadUserByCPF() {
        User user = UserService.getUser(cpf.getText());
        if (user != null) {
            setUser(user);
            cpf.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        } else {
            cpf.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    @FXML
    public void loadUserByUsername() {
        User user = UserService.getUserByUsername(username.getText());
        if (user != null) {
            setUser(user);
            username.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        } else {
            username.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    @FXML
    public void update(Event e) {
        if (user != null) {
            user.setNome(name.getText());
            user.setEmail(email.getText());
            user.setEndereco(address.getText());

            String passwordTxt = password.getText();
            String password2Txt = password2.getText();
            if (!(passwordTxt.isEmpty() && password2Txt.isEmpty())) {
                if (passwordTxt.equals(password2Txt)) {
                    user.setSenha(passwordTxt);
                } else {
                    DialogFactory.showDialog(DialogType.ERROR, "Erro na atualização do Usuário",
                            "As senhas devem ser iguais", e);
                    return;
                }
            }

            user.setTipo_administrador(admin.isSelected());

            try {
                UserService.updateUser(user);

                DialogFactory.showDialog(DialogType.INFO, "Atualizar Usuário", "O Usuário foi atualizado com Sucesso");
                close(e);
            } catch (Exception err) {
                DialogFactory.showDialog(DialogType.ERROR, "Erro na atualização do Usuário",
                        "Não foi possível atualizar o usuário", e);
            }
        } else {
            DialogFactory.showDialog(DialogType.ERROR, "Erro na atualização do Usuário",
                    "Informe o CPF ou Username do usuário", e);
        }
    }

    @FXML
    public void close(Event e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}
