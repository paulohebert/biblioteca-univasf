package com.univasf.biblioteca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.util.Session;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class UserPanel implements Initializable {
    private ToggleGroup toggleGroup;

    @FXML
    private Label greeting;
    @FXML
    private MFXRectangleToggleNode booksBtn, loansBtn, configBtn;
    @FXML
    private VBox main;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toggleGroup = new ToggleGroup();

        booksBtn.setToggleGroup(toggleGroup);
        loansBtn.setToggleGroup(toggleGroup);
        configBtn.setToggleGroup(toggleGroup);

        ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup);

        User user = Session.getUser();
        if (user != null) {
            greeting.setText("Olá, " + Session.getUser().getNome());
        }

        try {
            Window.changeNode(main, FXMLResource.BOOKS);
        } catch (IOException err) {
            err.printStackTrace();
        }

    }

    @FXML
    public void signOut() {
        EventHandler<MouseEvent> eventConfirm = (e) -> {
            try {
                Session.setUser(null);
                Window.change(FXMLResource.LOGIN);
            } catch (IOException err) {
                err.printStackTrace();
            }
        };
        DialogFactory.showDialog(DialogType.WARNING, "Encerrar Sessão", "Tem certeza de que deseja sair?",
                eventConfirm);
    }

    @FXML
    public void loadBooksPanel(Event e) throws IOException {
        Window.changeNode(main, FXMLResource.BOOKS);
    }

    @FXML
    public void loadLoansPanel(Event e) throws IOException {
        Window.changeNode(main, FXMLResource.LOANS);
    }

    @FXML
    public void loadConfigPanel(Event e) throws IOException {
        Window.changeNode(main, FXMLResource.CONFIG);
    }

    @FXML
    public void addBook() throws IOException {
        Window.create(FXMLResource.ADD_LOAN, 700, 400);
    }

}
