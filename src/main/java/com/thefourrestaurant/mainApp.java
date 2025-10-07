package com.thefourrestaurant;

import com.thefourrestaurant.view.GiaoDienChinh;
import com.thefourrestaurant.view.GiaoDienDangNhap;
import javafx.application.Application;
import javafx.stage.Stage;

public class mainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GiaoDienDangNhap gd = new GiaoDienDangNhap();
        // GiaoDienChinh gd = new GiaoDienChinh();
        gd.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
