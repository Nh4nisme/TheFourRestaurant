package com.thefourrestaurant.view.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class ButtonSample extends Button {

    private static final Map<Integer, String> styleMap = new HashMap<>();
    static {
        styleMap.put(1, "button_sampleGamboge");
        styleMap.put(2, "button_sampleIndigo");
        styleMap.put(3, "button_sampleIndigoV2");
    }

    public ButtonSample(String text, String iconPath, double height, double fontSize, int styleNum) {
        super(text);

        // === Font ===
        Font montserrat = Font.loadFont(
                getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-Bold.ttf"),
                fontSize
        );
        if (montserrat != null) {
			setFont(montserrat);
		}

        // === Icon (nếu có) ===
        if (iconPath != null && !iconPath.isBlank()) {
            try {
                ImageView icon = new ImageView(
                        new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)))
                );
                icon.setFitWidth(40);
                icon.setFitHeight(40);
                icon.setPreserveRatio(true);
                setGraphic(icon);
                setContentDisplay(ContentDisplay.LEFT);
            } catch (Exception e) {
                System.out.println("⚠ Không thể load icon: " + iconPath);
            }
        }

        // === Kích thước & padding ===
        setPrefHeight(height);
        setMinHeight(height);
        setMaxHeight(height);
        setPadding(new Insets(5, 10, 5, 10));

        // === Gắn style CSS theo số ===
        String cssClass = styleMap.getOrDefault(styleNum, "button-sample-gamboge");
        getStyleClass().add(cssClass);
    }

    /* Constructor overload không icon */
    public ButtonSample(String text, double height, double fontSize, int styleNum) {
        this(text, null, height, fontSize, styleNum);
    }
}