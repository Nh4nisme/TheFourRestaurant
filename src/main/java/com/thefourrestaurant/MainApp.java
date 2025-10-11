package com.thefourrestaurant;

import com.thefourrestaurant.view.GiaoDienChinh;
import com.thefourrestaurant.view.GiaoDienDangNhap;
import com.thefourrestaurant.view.PhieuGoiMon;
import javafx.scene.Scene;

import javafx.application.Application;

import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
    	PhieuGoiMon phieuGoiMon = new PhieuGoiMon();

    	GiaoDienChinh giaoDienChinh = new GiaoDienChinh();

        // Hiển thị giao diện
        giaoDienChinh.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
