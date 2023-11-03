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
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Admin implements Initializable {
    private ToggleGroup toggleGroup;

    @FXML
    private Text greeting;
    @FXML
    private MFXRectangleToggleNode reportBtn;
    @FXML
    private MFXRectangleToggleNode booksBtn;
    @FXML
    private MFXRectangleToggleNode usersBtn;
    @FXML
    private MFXRectangleToggleNode configBtn;
    @FXML
    private VBox main;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toggleGroup = new ToggleGroup();

        reportBtn.setToggleGroup(toggleGroup);
        booksBtn.setToggleGroup(toggleGroup);
        usersBtn.setToggleGroup(toggleGroup);
        configBtn.setToggleGroup(toggleGroup);

        ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup);

        User user = Session.getUser();
        if (user != null) {
            greeting.setText("Olá, " + Session.getUser().getNome());
        }

        try {
            Window.changeNode(main, FXMLResource.REPORT);
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
    public void loadReportPanel(Event e) throws IOException {
        Window.changeNode(main, FXMLResource.REPORT);
    }

    @FXML
    public void loadBooksPanel(Event e) throws IOException {
        Window.changeNode(main, FXMLResource.BOOKS);
    }

    @FXML
    public void loadUsersPanel(Event e) throws IOException {
        Window.changeNode(main, FXMLResource.USERS);
    }

    @FXML
    public void loadConfigPanel(Event e) throws IOException {
        Window.changeNode(main, FXMLResource.CONFIG);
    }

    @FXML
    public void addBook() throws IOException {
        Window.create(FXMLResource.ADD_BOOK, 700, 500);
    }

}
