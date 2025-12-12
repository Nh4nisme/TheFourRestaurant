package com.thefourrestaurant.view.components.sidebar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.thefourrestaurant.util.ClockText;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class SideBar extends VBox {

    private final Map<String, Button> buttons = new HashMap<>();

    public SideBar() {
        Font montserrat = Font.loadFont(getClass().getResourceAsStream("com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"), 16);
        setPrefWidth(50);
        setStyle("-fx-background-color: #1E424D");
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(-10, 17, 14, 17));
        setSpacing(50);


        // Phan nay la Logo cua sideBar
        ImageView logoImg = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/logoIcon.png"))));
        logoImg.setFitWidth(100);
        logoImg.setFitHeight(100);
        logoImg.setPreserveRatio(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Tao Vbox rong
        VBox BoDemGio = new VBox();
        BoDemGio.setAlignment(Pos.BOTTOM_CENTER);
        BoDemGio.setPadding(new Insets(10, 10, 10, 10));
        BoDemGio.setPrefHeight(500);
        ClockText boDemGioText = ClockText.getInstance();
        boDemGioText.setFont(montserrat);
        boDemGioText.setStyle("-fx-fill: #DDB248; -fx-font-size: 15px; -fx-font-weight: bold;");
        BoDemGio.getChildren().add(boDemGioText);


        //Them vao VBox sidebar chinh
        getChildren().addAll(logoImg, spacer, BoDemGio);
    }
}
