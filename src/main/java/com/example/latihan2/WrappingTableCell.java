package com.example.latihan2;

import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.util.Callback;

// Custom cell factory for wrapping text and adjusting row height
public class WrappingTableCell<S, T> extends TableCell<S, T> {
    private final Text text;

    public WrappingTableCell() {
        this.text = new Text();
        this.text.wrappingWidthProperty().bind(this.widthProperty());
        this.setGraphic(this.text);
        this.setPrefHeight(Control.USE_COMPUTED_SIZE);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            this.text.setText(null);
            this.setGraphic(null);
        } else {
            this.text.setText(item.toString());
            this.setGraphic(this.text);
        }
    }
}
