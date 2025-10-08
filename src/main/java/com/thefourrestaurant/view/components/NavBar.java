package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.util.Objects;

public class NavBar extends HBox {

    private final ButtonSample btnHeThong, btnTimKiem, btnDanhMucNav, btnXuLi, btnTKDN;

    public NavBar() {
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 30, 0, 30));
        setPrefHeight(80);
        setSpacing(10);
        setStyle("-fx-background-color: #E5D595");

        btnHeThong = new ButtonSample("Hệ Thống","/com/thefourrestaurant/images/icon/heThongIcon.png", 45, 16);
        btnTimKiem = new ButtonSample("Tìm Kiếm","/com/thefourrestaurant/images/icon/timKiemIcon.png", 45, 16);
        btnDanhMucNav = new ButtonSample("Danh mục","/com/thefourrestaurant/images/icon/danhMucNavIcon.png", 45, 16);
        btnXuLi = new ButtonSample("Xử lí","/com/thefourrestaurant/images/icon/xuLyIcon.png", 45, 16);
        btnTKDN = new ButtonSample("QL: Tâm GAY LORD", "/com/thefourrestaurant/images/icon/accountIcon.png", 45, 16);

        // Cho phép thanh điều hướng mở rộng chiều rộng

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(btnHeThong,btnTimKiem,btnDanhMucNav,btnXuLi,spacer,btnTKDN);
    }
}
