package com.thefourrestaurant.view;

import java.util.Objects;

import com.thefourrestaurant.controller.SideBarController;
import com.thefourrestaurant.view.components.NavBar;
import com.thefourrestaurant.view.components.sidebar.SideBar;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GiaoDienChinh {
    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        // === Background trung tâm ===
        Image bgImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/com/thefourrestaurant/images/GiaoDienChinhImg.png")));
        BackgroundImage bg = new BackgroundImage(bgImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        // === RIGHT: gồm NavBar + mainContent ===
        VBox rightSection = new VBox();
        rightSection.setAlignment(Pos.TOP_CENTER);

        // Tạo StackPane mainContent (chứa nội dung chính)
        StackPane mainContent = new StackPane();
        mainContent.setBackground(new Background(bg));
        VBox.setVgrow(mainContent, Priority.ALWAYS);


        // === LEFT: gồm SideBar + SideBar mở rộng ===
        SideBar sideBar = new SideBar();

        VBox sideBarExtended = new VBox();
        sideBarExtended.setVisible(false); // ban đầu ẩn
        sideBarExtended.setManaged(false);
        VBox.setVgrow(sideBarExtended, Priority.ALWAYS);

        // === LEFT Container (gom 2 phần) ===
        HBox leftSection = new HBox(sideBar, sideBarExtended);
        VBox.setVgrow(leftSection, Priority.ALWAYS);

        // === Tổng thể: LEFT + RIGHT ===
        HBox mainLayout = new HBox(leftSection, rightSection);
        HBox.setHgrow(rightSection, Priority.ALWAYS);
        root.setCenter(mainLayout);

        //Tạo NavBar, truyền đúng mainContent
        NavBar navBar = new NavBar(mainContent,sideBar,sideBarExtended);
        navBar.setPrefHeight(80);
        navBar.setMinHeight(80);

        //Thêm NavBar + mainContent vào VBox
        rightSection.getChildren().addAll(navBar, mainContent);

        // === Controller quản lý sidebar (ẩn/hiện) ===
        new SideBarController(sideBar, sideBarExtended, mainContent);

        // === Scene ===
        Scene scene = new Scene(root, 1366, 768);
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/thefourrestaurant/css/Application.css")).toExternalForm());
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
}
