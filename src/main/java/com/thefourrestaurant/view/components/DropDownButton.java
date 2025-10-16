package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import javax.swing.event.ChangeListener;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class DropDownButton extends Button {
    private final ContextMenu contextMenu = new ContextMenu();
    private String selectedValue;
    private Consumer<String> onItemSelected; // callback khi chọn item

    public DropDownButton(String promptText, List<String> options, String iconPath, double height, double fontSize, int styleNumber) {
        super(promptText);

        // Font Montserrat
        Font montserrat = Font.loadFont(
                getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-Bold.ttf"),
                fontSize
        );
        if (montserrat != null) {
            setFont(montserrat);
        }

        // Icon nếu có
        if (iconPath != null && !iconPath.isEmpty()) {
            ImageView icon = new ImageView(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(iconPath))
            ));
            icon.setFitHeight(40);
            icon.setFitWidth(40);
            setGraphic(icon);
        }

        // Tạo menu items
        for (String option : options) {
            Label label = new Label(option);
            label.setPadding(new Insets(8, 10, 8, 10));
            label.setStyle("-fx-text-fill: #E5D595; -fx-font-weight: bold; -fx-font-size: 16px;");

            CustomMenuItem item = new CustomMenuItem(label, true); // true = close on click
            item.setOnAction(e -> {
                selectedValue = option;
                setText(option);

                // Gọi callback nếu có
                if (onItemSelected != null) {
                    onItemSelected.accept(selectedValue);
                }
            });

            contextMenu.getItems().add(item);
        }

        // Hiển thị context menu khi click button
        setOnAction(e -> {
            if (!contextMenu.isShowing()) {
                double buttonWidth = getWidth();
                double fixedWidth = buttonWidth - 15; // trừ padding

                for (var menuItem : contextMenu.getItems()) {
                    if (menuItem instanceof CustomMenuItem custom) {
                        Node node = custom.getContent();
                        if (node instanceof Label lbl) {
                            lbl.setPrefWidth(fixedWidth);
                            lbl.setMaxWidth(fixedWidth);
                            lbl.setAlignment(Pos.CENTER_LEFT);
                        }
                    }
                }

                contextMenu.show(this, Side.BOTTOM, 0, 0);
            } else {
                contextMenu.hide();
            }
        });

        // Kích thước button
        setPrefHeight(height);
        setMinHeight(height);
        setMaxHeight(height);
        setPadding(new Insets(5, 10, 5, 10));
        switch (styleNumber) {
            case 1 -> getStyleClass().add("dropdown-buttonGamboge");
            case 2 -> getStyleClass().add("dropdown-buttonIndigo");
            case 3 -> getStyleClass().add("dropdown-buttonIndigoV2");
        }
    }

    // Setter callback
    public void setOnItemSelected(Consumer<String> action) {
        this.onItemSelected = action;
    }

    // Getter giá trị đang chọn
    public String getSelectedValue() {
        return selectedValue;
    }
}
