package com.thefourrestaurant;

import com.thefourrestaurant.view.GiaoDienChinh;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GiaoDienChinh gd = new GiaoDienChinh();
        gd.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
