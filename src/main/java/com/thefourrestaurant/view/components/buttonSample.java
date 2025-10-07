package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class buttonSample extends Button {
    public buttonSample(String text, String iconPath, double width, double height, double fontSize) {
        super(text);

        Font montserrat = Font.loadFont(
                getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"),
                fontSize
        );
        if (montserrat != null) {
            setFont(montserrat);
        } else {
            System.out.println("Font Montserrat load failed!");
        }

        // Load icon
        if (iconPath != null && !iconPath.isEmpty()) {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            icon.setFitWidth(40);
            icon.setFitHeight(40);
            icon.setPreserveRatio(true);

            setGraphic(icon);
            setContentDisplay(ContentDisplay.LEFT);
            setGraphicTextGap(1); // khoảng cách icon và text
        }

        // Kích thước button
        setPrefWidth(width);
        setPrefHeight(height);
        setMinWidth(width);
        setMaxWidth(width);
        setMinHeight(height);
        setMaxHeight(height);
        setPadding(new Insets(5));

        // Border
        BorderStroke borderStroke = new BorderStroke(
                Color.rgb(229,213,149),
                BorderStrokeStyle.SOLID,
                new CornerRadii(5),
                new BorderWidths(2)
        );
        setBorder(new Border(borderStroke));

        // DropShadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(4);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setColor(Color.GRAY);
        setEffect(shadow);

        // Hover effect
    }
}