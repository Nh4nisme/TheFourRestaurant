package com.thefourrestaurant.view;

import com.thefourrestaurant.view.components.NavBar;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.thefourrestaurant.view.components.SideBar;

import java.util.Objects;

public class GiaoDienChinh {
    public void show(Stage stage) {
        BorderPane borderPane = new BorderPane();

// Right VBox
        Image giaoDienChinhIMG = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/thefourrestaurant/images/GiaoDienChinhImg.png")));
        BackgroundSize giaoDienChinhSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage giaoDienChinhBackgroundIMG = new BackgroundImage(giaoDienChinhIMG,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                giaoDienChinhSize
                );
        VBox rightBox = new VBox();
        NavBar navBar = new NavBar(rightBox);
        navBar.setMinHeight(80);
        navBar.setMaxHeight(80);

        Pane backgroundCenter =  new Pane();
        VBox.setVgrow(backgroundCenter, Priority.ALWAYS);

        backgroundCenter.setBackground(new Background(giaoDienChinhBackgroundIMG));

        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.getChildren().addAll(navBar,backgroundCenter);
        borderPane.setCenter(rightBox); // dùng center thay vì right nếu muốn tỷ lệ dễ bind

// SideBar
        SideBar sideBar = new SideBar(rightBox);
        borderPane.setLeft(sideBar);


// Scene
        Scene scene = new Scene(borderPane,1366,768);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/thefourrestaurant/css/Application.css")).toExternalForm());
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();

// Bind width theo Stage
        sideBar.prefWidthProperty().bind(scene.widthProperty().multiply(0.05));
    }
}
