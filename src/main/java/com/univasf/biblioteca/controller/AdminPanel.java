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

public class AdminPanel implements Initializable {
    private ToggleGroup toggleGroup;

    @FXML
    private Label greeting;
    @FXML
    private MFXRectangleToggleNode reportBtn, booksBtn, usersBtn, loansBtn, configBtn;
    @FXML
    private VBox main;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toggleGroup = new ToggleGroup();

        reportBtn.setToggleGroup(toggleGroup);
        booksBtn.setToggleGroup(toggleGroup);
        usersBtn.setToggleGroup(toggleGroup);
        loansBtn.setToggleGroup(toggleGroup);
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
        if (toggleGroup.getSelectedToggle() != booksBtn) {
            toggleGroup.selectToggle(booksBtn);
        }
        Window.changeNode(main, FXMLResource.BOOKS);
    }

    @FXML
    public void loadUsersPanel(Event e) throws IOException {
        if (toggleGroup.getSelectedToggle() != usersBtn) {
            toggleGroup.selectToggle(usersBtn);
        }
        Window.changeNode(main, FXMLResource.USERS);
    }

    @FXML
    public void loadLoansPanel(Event e) throws IOException {
        if (toggleGroup.getSelectedToggle() != loansBtn) {
            toggleGroup.selectToggle(loansBtn);
        }
        Window.changeNode(main, FXMLResource.LOANS);
    }

    @FXML
    public void loadConfigPanel(Event e) throws IOException {
        Window.changeNode(main, FXMLResource.CONFIG);
    }

    @FXML
    public void addBook() throws IOException {
        Window.create(FXMLResource.ADD_BOOK, 700, 500);
    }

    @FXML
    public void addUser() throws IOException {
        Window.create(FXMLResource.ADD_USER, 595, 400);
    }

    @FXML
    public void addLoan() throws IOException {
        Window.create(FXMLResource.ADD_LOAN, 800, 400);
    }

    @FXML
    public void sourceCode() throws IOException {
        Session.getHostServices().showDocument("https://github.com/paulohebert/biblioteca-univasf");
    }

    @FXML
    public void about() throws IOException {
        Window.create(FXMLResource.ABOUT, 570, 320);
    }
}
