package com.univasf.biblioteca.gui;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXDialogs;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

public class Dialog {
    private MFXStageDialog dialog;

    public Dialog(Stage stage, Parent dialogContent) {
        this.dialog = MFXGenericDialogBuilder.build((MFXGenericDialog) dialogContent)
                .toStageDialogBuilder()
                .initOwner(stage)
                .initModality(Modality.NONE)
                .setDraggable(true)
                .setOwnerNode((Pane) stage.getScene().getRoot())
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimStrength(0.75)
                .setScrimOwner(true)
                .get();

        this.dialog.focusedProperty().addListener((observable, prev, curr) -> {
            if (!curr) {
                dialog.close();
            }
        });

        ((MFXGenericDialog) dialogContent).addActions(
                Map.entry(new MFXButton("OK"), (event) -> dialog.close()));
    }

    public static Dialog makeError(Stage stage, String text, String title) {
        Text contentText = new Text(text);
        VBox content = new VBox(contentText);

        contentText.setFont(new Font(17));
        content.setAlignment(Pos.CENTER);

        MFXGenericDialog dialogContent = MFXDialogs.error()
                // .setContentText(text)
                .setContent(content)
                .setShowAlwaysOnTop(false)
                .setShowMinimize(false)
                .setHeaderText(title)
                .get();

        Dialog dialog = new Dialog(stage, dialogContent);

        return dialog;
    }

    public static Dialog makeInfo(Stage stage, String text, String title) {
        Text contentText = new Text(text);
        VBox content = new VBox(contentText);

        contentText.setFont(new Font(17));
        content.setAlignment(Pos.CENTER);

        MFXGenericDialog dialogContent = MFXDialogs.info()
                // .setContentText(text)
                .setContent(content)
                .setShowAlwaysOnTop(false)
                .setShowMinimize(false)
                .setHeaderText(title)
                .get();

        Dialog dialog = new Dialog(stage, dialogContent);

        return dialog;
    }

    public void open() {
        dialog.showDialog();
    }
}
