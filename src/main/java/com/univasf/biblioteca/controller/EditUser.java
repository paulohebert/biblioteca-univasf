package com.univasf.biblioteca.controller;

import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.UserService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class EditUser {
    private User user;

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
    }

    @FXML
    public void update(Event e) {
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
                    "Não foi possível atualizar o usuário");
        }
    }

    @FXML
    public void close(Event e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}
