package com.thefourrestaurant.view.components;

import java.util.List;
import java.util.Objects;

import com.thefourrestaurant.view.*;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.util.Session;
import com.thefourrestaurant.view.ban.*;
//import com.thefourrestaurant.view.hoadon.GiaoDienHoaDon;
import com.thefourrestaurant.view.hoadon.GiaoDienHoaDon;
import com.thefourrestaurant.view.khachhang.GiaoDienKhachHang;
import com.thefourrestaurant.view.loaimonan.LoaiMonAn;
import com.thefourrestaurant.view.taikhoan.GiaoDienTaiKhoan;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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

        // Hiển thị tên tài khoản + vai trò từ Session (nếu đã đăng nhập)
        String accountLabel = "";
        TaiKhoan current = Session.getCurrentUser();
        if (current != null) {
            String role = current.getVaiTro().getTenVaiTro(); // ví dụ: QuanLy, ThuNgan
            String vnRole;
            if (role == null) {
                vnRole = "Tài khoản";
            } else if (role.equalsIgnoreCase("QuanLy")) {
                vnRole = "Quản Lý";
            } else if (role.equalsIgnoreCase("ThuNgan")) {
                vnRole = "Thu Ngân";
            } else {
                vnRole = role; // fallback nguyên văn
            }
            accountLabel = vnRole + ": " + current.getTenDN();
        } else {
            accountLabel = "Tài khoản: --"; // fallback khi chưa có session
        }

        ButtonSample btnTKDN = new ButtonSample("null", "/com/thefourrestaurant/images/icon/accountIcon.png",45, 16, 1);

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
                List.of("Đặt bàn"),
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

        if (current != null) {
            String roleRaw = current.getVaiTro().getTenVaiTro();
            boolean isManager = roleRaw != null && roleRaw.equalsIgnoreCase("QuanLy");

            if (!isManager) {
                btnDanhMucNav.setVisible(false);
                btnDanhMucNav.setManaged(false);
            }
        }
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

//        boolean currentIsManager = false;
//        if (Session.getCurrentUser() != null) {
//            String r = Session.getCurrentUser().getVaiTro().getTenVaiTro();
//            currentIsManager = r != null && r.equalsIgnoreCase("QuanLy");
//        }
//
//        List<String> managerOnly = List.of("Thực đơn", "Món ăn", "Tài khoản");
////        if (managerOnly.contains(s) && !currentIsManager) {
////            Alert a = new Alert(Alert.AlertType.WARNING, "Quyền truy cập bị từ chối. Chức năng này yêu cầu quyền Quản Lý.", ButtonType.OK);
////            a.showAndWait();
////            return;
////        }

        Node newContent = switch (s) {
            case "Thực đơn" -> new QuanLyThucDon();
            case "Món ăn" -> new LoaiMonAn();
            case "Đặt bàn" -> new GiaoDienDatBan((StackPane) mainContent);
            case "Bàn" -> {
                QuanLiBan giaoDienBan = new QuanLiBan((StackPane) mainContent);
                giaoDienBan.hienThiBanTheoTang("TG000001");
                yield giaoDienBan;
            }
            case "Khách hàng" -> new GiaoDienKhachHang();
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