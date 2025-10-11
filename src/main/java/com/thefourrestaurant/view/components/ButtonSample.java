package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

public class ButtonSample extends Button {
    public ButtonSample(String text, String iconPath, double height, double fontSize) {
        super(text);

        Font montserrat = Font.loadFont(
                getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-Bold.ttf"),
                fontSize
        );
        if (montserrat != null) {
            setFont(montserrat);
        } else {
            System.out.println("Font Montserrat load failed!");
        }

        // Load icon
        if (iconPath != null && !iconPath.isEmpty()) {
            ImageView icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
            icon.setFitWidth(40);
            icon.setFitHeight(40);
            icon.setPreserveRatio(true);

            setGraphic(icon);
            setContentDisplay(ContentDisplay.LEFT);
        }

        // Kích thước button
        setPrefHeight(height);
        setMinHeight(height);
        setMaxHeight(height);
        setPadding(new Insets(5, 10, 5, 10));
        getStyleClass().add("button_sampleGamboge");

    }
}