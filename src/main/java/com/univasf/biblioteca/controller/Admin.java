package com.univasf.biblioteca.controller;

import com.univasf.biblioteca.util.Dialog;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Admin implements Initializable {
    private ToggleGroup toggleGroup;

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

        try {
            Window.changeNode(main, FXMLResource.REPORT);
        } catch (IOException err) {
            err.printStackTrace();
        }

    }

    @FXML
    public void signOut(Event e) {
        Dialog signOutDialog = new Dialog(
                Dialog.Type.WARNING, e,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            Window.change(FXMLResource.LOGIN);
                        } catch (IOException err) {
                            err.printStackTrace();
                        }
                    }
                },
                "Encerrar Sessão", "Tem certeza de que deseja sair?");

        signOutDialog.show();
    }

    @FXML
    public void loadReportPanel(Event e) throws IOException {
        Window.changeNode(main, FXMLResource.REPORT);
    }

    @FXML
    public void loadBooksPanel(Event e) throws IOException {
        System.out.println("BOOKS");
        Window.changeNode(main, FXMLResource.REPORT);
    }

    @FXML
    public void loadUsersPanel(Event e) throws IOException {
        System.out.println("USERS");
        Window.changeNode(main, FXMLResource.LOGIN);
    }
}
