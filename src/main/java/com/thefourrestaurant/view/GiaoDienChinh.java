package com.thefourrestaurant.view;

import java.util.Objects;

import com.thefourrestaurant.view.components.NavBar;
import com.thefourrestaurant.view.components.sidebar.SideBar;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GiaoDienChinh {

    private static final double NAV_BAR_HEIGHT = 80;
    private static final double DEFAULT_WIDTH = 1366;
    private static final double DEFAULT_HEIGHT = 768;

    private StackPane mainContent;
    private SideBar sideBar;
    private VBox sideBarExpanded;
    private VBox rightSection;

    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setCenter(taoLayoutChinh());

        Scene scene = taoScene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }


    private HBox taoLayoutChinh() {
        HBox layout = new HBox(taoKhuVucTrai(), taoKhuVucPhai());
        HBox.setHgrow(rightSection, Priority.ALWAYS);
        return layout;
    }

    private HBox taoKhuVucTrai() {
        sideBar = new SideBar();
        sideBarExpanded = taoSideBarExpanded();

        HBox leftSection = new HBox(sideBar, sideBarExpanded);
        VBox.setVgrow(leftSection, Priority.ALWAYS);
        return leftSection;
    }

    private VBox taoKhuVucPhai() {
        rightSection = new VBox();
        rightSection.setAlignment(Pos.TOP_CENTER);

        mainContent = taoMainContent();
        NavBar navBar = taoNavBar();

        rightSection.getChildren().addAll(navBar, mainContent);
        return rightSection;
    }

    // Các hàm hỗ trợ tạo layout

    private StackPane taoMainContent() {
        StackPane content = new StackPane();
        content.setBackground(taoBackgroundChinh());
        VBox.setVgrow(content, Priority.ALWAYS);
        return content;
    }

    private NavBar taoNavBar() {
        NavBar navBar = new NavBar(mainContent, sideBar, sideBarExpanded);
        navBar.setPrefHeight(NAV_BAR_HEIGHT);
        navBar.setMinHeight(NAV_BAR_HEIGHT);
        return navBar;
    }

    private VBox taoSideBarExpanded() {
        VBox expanded = new VBox();
        expanded.setVisible(false);
        expanded.setManaged(false);
        VBox.setVgrow(expanded, Priority.ALWAYS);
        return expanded;
    }

    // Các hàm tạo nền và css

    private Background taoBackgroundChinh() {
        Image bgImg = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(
                        "/com/thefourrestaurant/images/GiaoDienChinhImg.png")
        ));

        BackgroundImage bg = new BackgroundImage(
                bgImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        return new Background(bg);
    }

    private Scene taoScene(BorderPane root) {
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/thefourrestaurant/css/Application.css")
        ).toExternalForm());
        return scene;
    }
}
