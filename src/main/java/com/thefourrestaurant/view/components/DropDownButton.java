package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Objects;

public class DropDownButton extends Button {
    private final ContextMenu contextMenu = new ContextMenu();
    private String selectedValue;

    public DropDownButton(String promptText, List<String> options, String iconPath, double height, double fontSize) {
        super(promptText);

        // Font Montserrat
        Font montserrat = Font.loadFont(
                getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-Bold.ttf"),
                fontSize
        );
        if (montserrat != null) {
            setFont(montserrat);
        }

        if (iconPath != null && !iconPath.isEmpty()) {
            ImageView icon = new ImageView(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(iconPath))
            ));
            icon.setFitHeight(40);
            icon.setFitWidth(40);
            setGraphic(icon);
        }

         //Tạo menu items
        for (String option : options) {
            Label label = new Label(option);
            label.setPadding(new Insets(8, 10, 8, 10));
            label.setStyle("-fx-text-fill: #E5D595; -fx-font-weight: bold; -fx-font-size: 16px;");

            CustomMenuItem item = new CustomMenuItem(label, true); // true = close on click
            item.setOnAction(e -> {
                selectedValue = option;
                setText(option);
            });
            contextMenu.getItems().add(item);
        }

        setOnAction(e -> {
            if (!contextMenu.isShowing()) {
                double buttonWidth = getWidth();

                // 🔹 Tính trừ 2px padding do ContextMenu render
                double fixedWidth = buttonWidth - 12;

                for (MenuItem item : contextMenu.getItems()) {
                    if (item instanceof CustomMenuItem custom) {
                        Node node = custom.getContent();
                        if (node instanceof Label lbl) {
                            lbl.setPrefWidth(fixedWidth);
                            lbl.setMaxWidth(fixedWidth);
                            lbl.setAlignment(Pos.CENTER_LEFT); // canh text cho đẹp
                        }
                    }
                }

                contextMenu.show(this, Side.BOTTOM, 0, 0);
            } else {
                contextMenu.hide();
            }
        });

        setPrefHeight(height);
        setMinHeight(height);
        setMaxHeight(height);
        setPadding(new Insets(5, 10, 5, 10));
        getStyleClass().add("dropdown-button");
    }

    public String getSelectedValue() {
        return selectedValue;
    }
}
