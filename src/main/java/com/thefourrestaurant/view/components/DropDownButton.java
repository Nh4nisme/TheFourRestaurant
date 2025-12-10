package com.thefourrestaurant.view.components;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class DropDownButton extends Button {

    private final ContextMenu contextMenu = new ContextMenu();
    private String selectedValue;
    private Consumer<String> onItemSelected;

    public DropDownButton(
            String promptText,
            List<String> options,
            String iconPath,
            double height,
            double fontSize,
            int styleNumber
    ) {
        super(promptText);

        khoiTaoFont(fontSize);
        khoiTaoIcon(iconPath);
        khoiTaoMenu(options);
        khoiTaoLayout(height, styleNumber);
        khoiTaoHanhVi();
    }

    // KHởi tạo

    private void khoiTaoFont(double fontSize) {
        Font font = Font.loadFont(
                getClass().getResourceAsStream(
                        "/com/thefourrestaurant/fonts/Montserrat-Bold.ttf"),
                fontSize
        );
        if (font != null) setFont(font);
    }

    private void khoiTaoIcon(String iconPath) {
        if (iconPath == null || iconPath.isEmpty()) return;

        ImageView icon = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(iconPath))
        ));
        icon.setFitHeight(40);
        icon.setFitWidth(40);
        setGraphic(icon);
    }

    private void khoiTaoMenu(List<String> options) {
        for (String option : options) {
            Label label = taoLabelMenu(option);

            CustomMenuItem item = new CustomMenuItem(label, true);
            item.setOnAction(e -> chonGiaTri(option));

            contextMenu.getItems().add(item);
        }
    }

    private Label taoLabelMenu(String text) {
        Label label = new Label(text);
        label.setPadding(new Insets(8, 10, 8, 10));
        label.setAlignment(Pos.CENTER_LEFT);
        label.setStyle(
                "-fx-text-fill: #E5D595;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;"
        );
        return label;
    }

    private void khoiTaoLayout(double height, int styleNumber) {
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

    private void khoiTaoHanhVi() {
        contextMenu.setOnShowing(e -> capNhatDoRongMenu());
        setOnAction(e -> batTatMenu());
    }

    //Logic chính của button

    private void batTatMenu() {
        if (contextMenu.isShowing()) {
            contextMenu.hide();
        } else {
            contextMenu.show(this, Side.BOTTOM, 0, 0);
        }
    }

    private void capNhatDoRongMenu() {
        double doRongNoiDung = layDoRongNoiDungButton();

        for (MenuItem item : contextMenu.getItems()) {
            if (item instanceof CustomMenuItem custom) {
                Node node = custom.getContent();
                if (node instanceof Label label) {
                    label.setPrefWidth(doRongNoiDung);
                    label.setMaxWidth(doRongNoiDung);
                }
            }
        }
    }

    private double layDoRongNoiDungButton() {
        Insets padding = getPadding();
        return getWidth() - padding.getLeft();
    }

    private void chonGiaTri(String value) {
        selectedValue = value;
        setText(value);

        if (onItemSelected != null) {
            onItemSelected.accept(value);
        }
    }



    public void setOnItemSelected(Consumer<String> action) {
        this.onItemSelected = action;
    }

    public String getSelectedValue() {
        return selectedValue;
    }
}
