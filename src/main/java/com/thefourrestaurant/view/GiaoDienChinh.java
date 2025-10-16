package com.thefourrestaurant.view;

import com.thefourrestaurant.controller.SideBarController;
import com.thefourrestaurant.view.components.NavBar;
import com.thefourrestaurant.view.components.sidebar.SideBar;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Objects;

public class GiaoDienChinh {
    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        // Background
        Image bgImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/com/thefourrestaurant/images/GiaoDienChinhImg.png")));
        BackgroundImage bg = new BackgroundImage(bgImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

     // Tạo rightBox và backgroundCenter
        VBox rightBox = new VBox();
        rightBox.setAlignment(Pos.TOP_CENTER);

        NavBar navBar = new NavBar(rightBox);
        navBar.setPrefHeight(80);

        Pane backgroundCenter = new Pane();
        backgroundCenter.setBackground(new Background(bg));
        VBox.setVgrow(backgroundCenter, Priority.ALWAYS);

        rightBox.getChildren().addAll(navBar, backgroundCenter);

        // SideBar
        SideBar sideBar = new SideBar();

        // mainContainer = sideBar + rightBox
        HBox mainContainer = new HBox(sideBar, rightBox);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        root.setCenter(mainContainer);

        // Controller
        new SideBarController(sideBar, mainContainer, backgroundCenter);


        // Scene
        Scene scene = new Scene(root, 1366, 768);
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/thefourrestaurant/css/Application.css")).toExternalForm());
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
}
