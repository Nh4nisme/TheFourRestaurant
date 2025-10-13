package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NavBar extends HBox {

    private final DropDownButton btnHeThong, btnTimKiem,btnXuLi,btnDanhMucNav;

    public NavBar(VBox rightBox) {
        Font montserrat = Font.loadFont(
                Objects.requireNonNull(getClass().getResourceAsStream(
                        "/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf")),
                16 // kích thước mặc định
        );

        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 30, 0, 30));
        setPrefHeight(80);
        setSpacing(10);
        setStyle("-fx-background-color: #E5D595");
        ButtonSample btnTKDN = new ButtonSample("QL: Tâm ", "/com/thefourrestaurant/images/icon/accountIcon.png", 45, 16);

        btnDanhMucNav = new DropDownButton(
                "Danh mục",
                List.of("Món ăn", "Phiếu đặt bàn", "Khách hàng","Hóa đơn","Bàn","Tài khoản"),
                "/com/thefourrestaurant/images/icon/danhMucNavIcon.png",
                45,
                16
        );

        btnXuLi = new DropDownButton(
                "Xử lí",
                List.of("Đặt bàn","Đặt món"),
                "/com/thefourrestaurant/images/icon/xuLyIcon.png",
                45,
                16
        );

        btnTimKiem = new DropDownButton(
                "Tìm kiếm",
                List.of("Món ăn", "Phiếu đặt bàn", "Khách hàng","Hóa đơn","Bàn","Tài khoản"),
                "/com/thefourrestaurant/images/icon/timKiemIcon.png",
                45,
                16
        );

        btnHeThong = new DropDownButton(
                "Hệ thống",
                List.of("Trang chủ","Trợ giúp","Thoát","Đăng xuất"),
                "/com/thefourrestaurant/images/icon/heThongIcon.png",
                45,
                16
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(btnDanhMucNav,btnXuLi,btnTimKiem,btnHeThong,spacer,btnTKDN);
    }
}
