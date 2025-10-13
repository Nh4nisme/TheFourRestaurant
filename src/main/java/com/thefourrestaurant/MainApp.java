package com.thefourrestaurant;

import com.thefourrestaurant.view.GiaoDienChinh;
import com.thefourrestaurant.view.GiaoDienDangNhap;
import com.thefourrestaurant.view.PhieuGoiMon;
import javafx.scene.Scene;

import javafx.application.Application;

import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Nạp font cho toàn bộ app
        Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-Bold.ttf"), 14);

        PhieuGoiMon phieuGoiMon = new PhieuGoiMon();

    	GiaoDienChinh giaoDienChinh = new GiaoDienChinh();

        // Hiển thị giao diện
        giaoDienChinh.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
