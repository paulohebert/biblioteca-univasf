package com.univasf.biblioteca.util;

import java.io.IOException;
import java.util.function.Supplier;

import com.univasf.biblioteca.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Transition {
    private static Parent root;
    private static Parent view;

    private static Parent loadFXML(String fxml) throws IOException {
        return FXMLLoader.load(App.class.getResource("/fxml/" + fxml + ".fxml"));
    }

    private static Parent changeNode(String fxml) throws IOException {
        var a = loadFXML(fxml);
        var b = ((Pane) root).getChildren().get(((Pane) root).getChildren().size() -
                1);
        ((VBox) b).getChildren().setAll(a);
        return a;
    }

    public static void setRoot(Parent node) throws IOException {
        root = node;
    }

    public static void setRoot(String fxml) throws IOException {
        Supplier<Node> method = () -> {
            try {
                AnchorPane a = (AnchorPane) loadFXML(fxml);
                var b = ((Pane) root).getChildren().get(((Pane) root).getChildren().size() -
                        1);
                ((VBox) b).getChildren().setAll(a);
                return a;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        };

        method.get();
        /*
         * FadeTransition transition = new FadeTransition(Duration.millis(1000),
         * method.get());
         * transition.setFromValue(0);
         * transition.setToValue(1);
         * transition.setCycleCount(1);
         * transition.setAutoReverse(false);
         * transition.play();
         */
        /*
         * Stage stage = new Stage();
         * Scene scene = new Scene(loadFXML(fxml));
         * MFXThemeManager.addOn(scene, Themes.DEFAULT);
         * 
         * stage.setScene(scene);
         * stage.setTitle("Registration");
         * stage.setMaximized(true);
         * stage.show();
         */

        // scene.setRoot(loadFXML(fxml));
    }
}
