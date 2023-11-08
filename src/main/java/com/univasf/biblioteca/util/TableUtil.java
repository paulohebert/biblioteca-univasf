package com.univasf.biblioteca.util;

import java.util.Comparator;
import java.util.function.Function;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;

public class TableUtil<T> {
    public MFXTableColumn<T> createColumn(String name, Comparator<T> compare,
            Function<T, MFXTableRowCell<T, ?>> rowCellFactory, Pos pos, double width) {
        MFXTableColumn<T> column = new MFXTableColumn<T>(name, true, compare);
        column.setRowCellFactory(rowCellFactory);
        column.setAlignment(pos);
        column.setMinWidth(width);
        return column;
    }

    public <R> MFXTableColumn<T> createColumn(String name, Comparator<T> compare, Function<T, R> extractor) {
        return createColumn(name, Pos.CENTER_LEFT, compare, extractor);
    }

    public <R> MFXTableColumn<T> createColumn(String name, Pos pos, Comparator<T> compare, Function<T, R> extractor) {
        MFXTableColumn<T> column = new MFXTableColumn<T>(name, true, compare);
        column.setRowCellFactory(model -> {
            MFXTableRowCell<T, ?> cell = new MFXTableRowCell<>(extractor);
            cell.setAlignment(pos);
            return cell;
        });
        column.setAlignment(pos);
        return column;
    }

    public <R> MFXTableColumn<T> createActions(Function<T, R> func, EventHandler<ActionEvent> seeEvent,
            EventHandler<ActionEvent> editEvent, EventHandler<ActionEvent> delEvent) {

        MFXTableColumn<T> actionColumn = new MFXTableColumn<T>();

        int width = editEvent == null && delEvent == null ? 40 : 120;

        actionColumn.setMinWidth(width);
        actionColumn.setRowCellFactory(model -> {
            MFXTableRowCell<T, R> cell = new MFXTableRowCell<T, R>(func);

            if (seeEvent != null) {
                MFXButton seeBtn = new MFXButton(null, new MFXFontIcon("fas-eye"));
                seeBtn.getStyleClass().add("button-gray");
                seeBtn.setOnAction(seeEvent);
                cell.setLeadingGraphic(seeBtn);
            }

            if (editEvent != null) {
                MFXButton editBtn = new MFXButton(null, new MFXFontIcon("fas-pen-to-square"));
                editBtn.getStyleClass().add("button-blue");
                editBtn.setOnAction(editEvent);
                cell.setGraphic(editBtn);
            }

            if (delEvent != null) {
                MFXButton delBtn = new MFXButton(null, new MFXFontIcon("fas-trash-can"));
                delBtn.getStyleClass().add("button-red");
                delBtn.setOnAction(delEvent);
                cell.setTrailingGraphic(delBtn);
            }

            cell.setMinWidth(width);
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return cell;
        });

        return actionColumn;
    }
}
