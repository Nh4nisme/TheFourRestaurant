package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class navBar extends HBox {
    public navBar() {
        Font montserrat = Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"),16);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 30, 0, 30));
        setPrefHeight(80);
        setStyle("-fx-background-color: #E5D595");

        buttonSample btnHeThong = new buttonSample("Hệ Thống","/com/thefourrestaurant/images/icon/heThongIcon.png",140, 45, 16);
        btnHeThong.setStyle("-fx-background-color: #CD7B3E; -fx-text-fill: #E5D595; -fx-font-weight: bold; -fx-background-radius: 5");

        getChildren().add(btnHeThong);
    }
}
