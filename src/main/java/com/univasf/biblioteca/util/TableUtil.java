package com.univasf.biblioteca.util;

import java.util.Comparator;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;

public class TableUtil<T> {
    public MFXTableColumn<T> createColumn(String name, Comparator<T> compare,
            Function<T, MFXTableRowCell<T, ?>> rowCellFactory, Pos pos) {
        MFXTableColumn<T> column = new MFXTableColumn<T>(name, true, compare);
        column.setRowCellFactory(rowCellFactory);
        column.setAlignment(pos);
        return column;
    }

    public <R> MFXTableColumn<T> createColumn(String name, Comparator<T> compare, Function<T, R> extractor) {
        MFXTableColumn<T> column = new MFXTableColumn<T>(name, true, compare);
        column.setRowCellFactory(model -> new MFXTableRowCell<>(extractor));
        return column;
    }

    public <R> MFXTableColumn<T> createActions(Function<T, R> func, EventHandler<ActionEvent> seeEvent,
            EventHandler<ActionEvent> editEvent, EventHandler<ActionEvent> delEvent) {

        MFXTableColumn<T> actionColumn = new MFXTableColumn<T>("");

        actionColumn.setRowCellFactory(model -> {
            MFXTableRowCell<T, R> cell = new MFXTableRowCell<T, R>(func);

            MFXButton seeBtn = new MFXButton(null, new MFXFontIcon("fas-eye"));
            MFXButton editBtn = new MFXButton(null, new MFXFontIcon("fas-pen-to-square"));
            MFXButton delBtn = new MFXButton(null, new MFXFontIcon("fas-trash-can"));

            seeBtn.getStyleClass().add("button-gray");
            editBtn.getStyleClass().add("button-blue");
            delBtn.getStyleClass().add("button-red");

            seeBtn.setOnAction(seeEvent);
            editBtn.setOnAction(editEvent);
            delBtn.setOnAction(delEvent);

            cell.setLeadingGraphic(seeBtn);
            cell.setGraphic(editBtn);
            cell.setTrailingGraphic(delBtn);
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            cell.setWrapText(true);
            return cell;
        });

        return actionColumn;
    }
}
