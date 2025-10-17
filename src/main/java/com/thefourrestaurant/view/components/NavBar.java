package com.thefourrestaurant.view.components;

import com.thefourrestaurant.view.hoadon.GiaoDienHoaDon;
import com.thefourrestaurant.view.ban.GiaoDienChiTietBan;
import com.thefourrestaurant.view.ban.GiaoDienDatBan;
import com.thefourrestaurant.view.ban.GiaoDienDatBanTruoc;
import com.thefourrestaurant.view.GiaoDienTaoThucDon;
import com.thefourrestaurant.view.GiaoDienThemKhachHang;
import com.thefourrestaurant.view.loaimonan.LoaiMonAn;
import com.thefourrestaurant.view.PhieuGoiMon;
import com.thefourrestaurant.view.taikhoan.GiaoDienTaiKhoan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Objects;

public class NavBar extends HBox {

    private final DropDownButton btnHeThong, btnTimKiem,btnXuLi,btnDanhMucNav;
    private final VBox mainContainer;

    public NavBar(VBox mainContainer) {
        this.mainContainer = mainContainer;
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
        ButtonSample btnTKDN = new ButtonSample("QL: Tâm ", "/com/thefourrestaurant/images/icon/accountIcon.png",45, 16, 1);

        btnDanhMucNav = new DropDownButton(
                "Danh mục",
                List.of("Thực đơn", "Món ăn", "Phiếu đặt bàn", "Khách hàng","Hóa đơn","Bàn","Tài khoản"),
                "/com/thefourrestaurant/images/icon/danhMucNavIcon.png",
                45,
                16,
                1
        );

        btnXuLi = new DropDownButton(
                "Xử lí",
                List.of("Đặt bàn","Đặt món", "Đặt bàn trước", "Thêm khách hàng", "Chi tiết bàn"),
                "/com/thefourrestaurant/images/icon/xuLyIcon.png",
                45,
                16,
                1
        );

        btnTimKiem = new DropDownButton(
                "Tìm kiếm",
                List.of("Món ăn", "Phiếu đặt bàn", "Khách hàng","Hóa đơn","Bàn","Tài khoản"),
                "/com/thefourrestaurant/images/icon/timKiemIcon.png",
                45,
                16,
                1
        );

        btnHeThong = new DropDownButton(
                "Hệ thống",
                List.of("Trang chủ","Trợ giúp","Thoát","Đăng xuất"),
                "/com/thefourrestaurant/images/icon/heThongIcon.png",
                45,
                16,
                1
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(btnDanhMucNav,btnXuLi,btnTimKiem,btnHeThong,spacer,btnTKDN);

        btnDanhMucNav.setOnItemSelected(this::showPanel);
        btnXuLi.setOnItemSelected(this::showPanel);
    }

    private void showPanel(String s) {
        mainContainer.getChildren().clear();

        switch (s) {
            case "Thực đơn" -> mainContainer.getChildren().add(new GiaoDienTaoThucDon());
            case "Món ăn" -> mainContainer.getChildren().add(new LoaiMonAn());
            case "Đặt món" -> mainContainer.getChildren().add(new PhieuGoiMon());
            case "Đặt bàn" -> mainContainer.getChildren().add(new GiaoDienDatBan());
            case "Đặt bàn trước" -> mainContainer.getChildren().add(new GiaoDienDatBanTruoc());
            case "Thêm khách hàng" -> mainContainer.getChildren().add(new GiaoDienThemKhachHang());
            case "Chi tiết bàn" -> mainContainer.getChildren().add(new GiaoDienChiTietBan());
            case "Hóa đơn" -> mainContainer.getChildren().add(new GiaoDienHoaDon());
            case "Tài khoản" -> mainContainer.getChildren().add(new GiaoDienTaiKhoan());
//            case "Nguyên liệu" -> mainContainer.getChildren().add(new IngredientPanel());
            default -> System.out.println("Không tìm thấy panel: " + s);
        }
    }
}
