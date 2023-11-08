package com.univasf.biblioteca.util;

import java.util.Map;

import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXDialogs;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

abstract class Dialog {
    protected MFXStageDialog box;
    protected EventHandler<MouseEvent> close = (MouseEvent event) -> {
        this.box.close();
    };

    Dialog() {
        Stage stage = Window.getStage();

        this.box = new MFXStageDialog();

        this.box.initModality(Modality.NONE);
        this.box.initOwner(stage);
        this.box.setDraggable(true);
        this.box.setScrimPriority(ScrimPriority.WINDOW);
        this.box.setScrimStrength(0.75);
        this.box.setScrimOwner(true);
        this.box.setOwnerNode((Pane) stage.getScene().getRoot());

        this.box.focusedProperty().addListener((observable, prev, curr) -> {
            if (!curr) {
                this.box.close();
            }
        });
    }

    public void setOwner(Event e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        this.box.initOwner(stage);
        this.box.setOwnerNode((Pane) stage.getScene().getRoot());
    }

    public void show() {
        this.box.showDialog();
    }
}

class SimpleDialog extends Dialog {
    SimpleDialog(MFXGenericDialogBuilder dialogBuilder, String title, VBox content) {
        super();
        MFXGenericDialog dialogContent = dialogBuilder
                .setContent(content)
                .setShowAlwaysOnTop(false)
                .setShowMinimize(false)
                .setOnClose(this.close)
                .setHeaderText(title)
                .get();

        dialogContent.addActions(Map.entry(new MFXButton("OK"), this.close));

        this.box.setContent(dialogContent);
    }
}

class ConfirmDialog extends Dialog {
    ConfirmDialog(MFXGenericDialogBuilder dialogBuilder, String title, VBox content,
            EventHandler<MouseEvent> eventConfirm) {
        super();
        MFXGenericDialog dialogContent = dialogBuilder
                .setContent(content)
                .setShowAlwaysOnTop(false)
                .setShowMinimize(false)
                .setOnClose(this.close)
                .setHeaderText(title)
                .get();

        if (eventConfirm != null) {
            dialogContent.addActions(Map.entry(new MFXButton("Confirmar"), event -> {
                Platform.runLater(() -> {
                    eventConfirm.handle(event);
                });
                this.box.close();
            }));
        }

        dialogContent.addActions(Map.entry(new MFXButton("Cancelar"), this.close));

        this.box.setContent(dialogContent);
    }
}

public class DialogFactory {
    public static enum DialogType {
        GENERIC, INFO, WARNING, ERROR
    }

    public static Dialog createDialog(DialogType type, String title, String text,
            EventHandler<MouseEvent> eventConfirm) {
        Label contentText = new Label(text);
        VBox content = new VBox(contentText);

        contentText.setTextOverrun(OverrunStyle.ELLIPSIS);
        contentText.setMaxWidth(450);
        contentText.setFont(new Font(17));
        contentText.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER);

        switch (type) {
            case GENERIC:
                return new SimpleDialog(MFXGenericDialogBuilder.build(), title, content);
            case INFO:
                return new SimpleDialog(MFXDialogs.info(), title, content);
            case WARNING:
                return new ConfirmDialog(MFXDialogs.warn(), title, content, eventConfirm);
            case ERROR:
                return new SimpleDialog(MFXDialogs.error(), title, content);
            default:
                return null;
        }
    }

    public static void showDialog(DialogType type, String title, String text) {
        Dialog dialog = createDialog(type, title, text, null);
        if (dialog != null) {
            dialog.show();
        }
    }

    public static void showDialog(DialogType type, String title, String text, Event event) {
        Dialog dialog = createDialog(type, title, text, null);
        if (dialog != null) {
            if (event != null) {
                dialog.setOwner(event);
            }
            dialog.show();
        }
    }

    public static void showDialog(DialogType type, String title, String text, EventHandler<MouseEvent> eventConfirm) {
        Dialog dialog = createDialog(type, title, text, eventConfirm);
        if (dialog != null) {
            dialog.show();
        }
    }
}