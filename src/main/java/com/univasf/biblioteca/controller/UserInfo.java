package com.univasf.biblioteca.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.User;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class UserInfo implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cpf.setEditable(false);
        username.setEditable(false);
        name.setEditable(false);
        email.setEditable(false);
        address.setEditable(false);
    }
}
