package com.thefourrestaurant.view;

import com.thefourrestaurant.view.components.navBar;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.thefourrestaurant.view.components.sideBar;

public class GiaoDienChinh {
    public void show(Stage stage) {
        BorderPane borderPane = new BorderPane();

// SideBar
        sideBar sideBar = new sideBar();
        borderPane.setLeft(sideBar);

// Right VBox
        Image giaoDienChinhIMG = new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/GiaoDienChinhImg.png"));
        BackgroundSize giaoDienChinhSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage giaoDienChinhBackgroundIMG = new BackgroundImage(giaoDienChinhIMG,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                giaoDienChinhSize
                );
        VBox rightBox = new VBox();
        navBar navBar = new navBar();
        Pane backgroundCenter =  new Pane();
        VBox.setVgrow(backgroundCenter, Priority.ALWAYS);

        backgroundCenter.setBackground(new Background(giaoDienChinhBackgroundIMG));

        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.getChildren().addAll(navBar,backgroundCenter);
        borderPane.setCenter(rightBox); // dùng center thay vì right nếu muốn tỷ lệ dễ bind


// Scene
        Scene scene = new Scene(borderPane,1024,768);
//        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();

// Bind width theo Stage
        sideBar.prefWidthProperty().bind(scene.widthProperty().multiply(0.05));
    }
}
