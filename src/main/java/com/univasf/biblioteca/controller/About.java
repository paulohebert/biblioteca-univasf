package com.univasf.biblioteca.controller;

import com.univasf.biblioteca.util.Session;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class About {
    @FXML
    public void openLink(Event e) {
        Hyperlink hyperlink = (Hyperlink) e.getSource();
        Session.getHostServices().showDocument((String) hyperlink.getUserData());
    }
}
