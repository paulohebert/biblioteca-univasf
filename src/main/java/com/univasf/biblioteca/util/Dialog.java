package com.univasf.biblioteca.util;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXDialogs;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;

import java.util.Map;

import com.univasf.biblioteca.view.Window;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog {
    private MFXStageDialog box;

    public static enum Type {
        GENERIC, INFO, WARNING, ERROR
    }

    private void init(Event e) {
        Stage stage = e != null ? ((Stage) ((Node) e.getSource()).getScene().getWindow()) : Window.getStage();

        box = new MFXStageDialog();

        box.initModality(Modality.NONE);
        box.initOwner(stage);
        box.setDraggable(true);
        box.setScrimPriority(ScrimPriority.WINDOW);
        box.setScrimStrength(0.75);
        box.setScrimOwner(true);
        box.setOwnerNode((Pane) stage.getScene().getRoot());

        box.focusedProperty().addListener((observable, prev, curr) -> {
            if (!curr) {
                box.close();
            }
        });
    }

    private void setContent(Type type, String title, String text) {
        Text contentText = new Text(text);
        VBox content = new VBox(contentText);

        contentText.setFont(new Font(17));
        content.setAlignment(Pos.CENTER);

        MFXGenericDialogBuilder dialogContent = null;
        switch (type) {
            case GENERIC:
                dialogContent = MFXGenericDialogBuilder.build();
                break;
            case INFO:
                dialogContent = MFXDialogs.info();
                break;
            case WARNING:
                dialogContent = MFXDialogs.warn();
                break;
            case ERROR:
                dialogContent = MFXDialogs.error();
                break;
        }

        if (dialogContent != null) {
            MFXGenericDialog dialog = dialogContent
                    .setContent(content)
                    .setShowAlwaysOnTop(false)
                    .setShowMinimize(false)
                    .setOnClose(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            box.close();
                        }
                    })
                    .setHeaderText(title)
                    .get();

            box.setContent(dialog);
        }
    }

    @SafeVarargs
    private void addActions(Map.Entry<Node, EventHandler<MouseEvent>>... actions) {
        ((MFXGenericDialog) box.getContent()).addActions(actions);
    }

    public Dialog(Type type, Event e, String title, String text) {
        init(e);
        setContent(type, title, text);

        addActions(Map.entry(new MFXButton("OK"), event -> {
            box.close();
        }));
    }

    public Dialog(Type type, Event e, EventHandler<MouseEvent> eventConfirm, String title, String text) {
        init(e);
        setContent(type, title, text);

        addActions(
                Map.entry(new MFXButton("Confirmar"), event -> {
                    eventConfirm.handle(event);
                    box.close();
                }),
                Map.entry(new MFXButton("Cancelar"), event -> box.close()));
    }

    public void show() {
        if (box != null) {
            box.showDialog();
        }
    }
}
