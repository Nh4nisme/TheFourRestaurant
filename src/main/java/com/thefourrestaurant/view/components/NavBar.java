package com.thefourrestaurant.view.components;

import java.util.List;
import java.util.Objects;

import com.thefourrestaurant.view.*;
import com.thefourrestaurant.view.ban.*;
import com.thefourrestaurant.view.hoadon.GiaoDienHoaDon;
import com.thefourrestaurant.view.loaimonan.LoaiMonAn;
import com.thefourrestaurant.view.taikhoan.GiaoDienTaiKhoan;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.Node;

public class NavBar extends HBox {

    private final DropDownButton btnHeThong, btnTimKiem, btnXuLi, btnDanhMucNav;
    private final Pane mainContent; // Pane trung tâm dưới navBar
    private final Pane sideBar;
    private final Pane sideBarExtended;

    public NavBar(Pane mainContent, Pane sideBar, Pane sideBarExtended) {
        this.mainContent = mainContent;
        this.sideBar = sideBar;
        this.sideBarExtended = sideBarExtended;

        Font montserrat = Font.loadFont(
                Objects.requireNonNull(getClass().getResourceAsStream(
                        "/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf")),
                16
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

        getChildren().addAll(btnDanhMucNav, btnXuLi, btnTimKiem, btnHeThong, spacer, btnTKDN);

        // Event handler load nội dung vào mainContent
        btnDanhMucNav.setOnItemSelected(this::showPanel);
        btnXuLi.setOnItemSelected(this::showPanel);
    }

    private void showPanel(String s) {
        if (mainContent == null) return;

        if ("Đặt bàn".equals(s)) {
            // Ẩn sidebar, không chiếm layout
            if (sideBar != null) {
                sideBar.setVisible(false);
                sideBar.setManaged(false);
            }
            if (sideBarExtended != null) {
                sideBarExtended.setVisible(false);
                sideBarExtended.setManaged(false);
            }
        } else {
            // Hiện lại sidebar
            if (sideBar != null) {
                sideBar.setVisible(true);
                sideBar.setManaged(true);
            }
            if (sideBarExtended != null) {
                sideBarExtended.setVisible(true);
                sideBarExtended.setManaged(true);
            }
        }

        Node newContent = switch (s) {
            case "Thực đơn" -> new QuanLyThucDon();
            case "Món ăn" -> new LoaiMonAn();
            case "Đặt món" -> new GiaoDienGoiMon();
            case "Đặt bàn" -> new GiaoDienDatBan();
            case "Đặt bàn trước" -> new GiaoDienDatBanTruoc();
            case "Thêm khách hàng" -> new GiaoDienThemKhachHang();
            case "Chi tiết bàn" -> new GiaoDienChiTietBan();
            case "Bàn" -> {
                QuanLiBan giaoDienBan = new QuanLiBan();
                giaoDienBan.hienThiBanTheoTang("TG000001");
                yield giaoDienBan;
            }
            case "Hóa đơn" -> new GiaoDienHoaDon();
            case "Tài khoản" -> new GiaoDienTaiKhoan();
            default -> null;
        };

        if (newContent != null) {
            if (newContent instanceof Region region) {
                region.prefWidthProperty().bind(mainContent.widthProperty());
                region.prefHeightProperty().bind(mainContent.heightProperty());
            }
            mainContent.getChildren().setAll(newContent);
        }
    }
}