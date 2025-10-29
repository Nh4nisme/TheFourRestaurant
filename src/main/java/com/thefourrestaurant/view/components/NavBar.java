package com.thefourrestaurant.view.components;

import java.util.List;
import java.util.Objects;

import com.thefourrestaurant.controller.HelpController;
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
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class NavBar extends HBox {

    private final DropDownButton btnHeThong, btnTimKiem, btnXuLi;
    private final Pane mainContent; // Pane trung tâm dưới navBar
    private final Pane sideBar;
    private final Pane sideBarExtended;
    private final HelpController helpController = new HelpController(); // Khởi tạo HelpController

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
        
        ButtonSample btnTKDN = new ButtonSample(accountLabel, "/com/thefourrestaurant/images/icon/accountIcon.png", 45, 16, 1);

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
                List.of("Phiếu đặt bàn"),
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

        getChildren().addAll(btnHeThong, btnXuLi, btnTimKiem, spacer, btnTKDN);

        // Event handler load nội dung vào mainContent
        btnXuLi.setOnItemSelected(this::showPanel);
        btnTimKiem.setOnItemSelected(this::showPanel);
        btnHeThong.setOnItemSelected(this::showPanel); // Thêm xử lý cho nút Hệ thống

        if (current != null) {
            String roleRaw = current.getVaiTro().getTenVaiTro();
            boolean isManager = roleRaw != null && roleRaw.equalsIgnoreCase("QuanLy");
        }
    }

    private void showPanel(String s) {
    	
    	if ("Trang chủ".equals(s)) {
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            new GiaoDienChinh().show(currentStage); // mở lại GiaoDienChinh trên stage hiện tại
            currentStage.setFullScreen(true);
            return;
        }
    	
        if (mainContent == null) return;

        // Xử lý các mục của nút Hệ thống
        if ("Trợ giúp".equals(s)) {
            Stage owner = (Stage) getScene().getWindow();
            helpController.openHelpFile(owner);
            owner.setFullScreen(true);
            return; // Không cần thay đổi mainContent
        }

        if("Thoát".equals(s)) {
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            currentStage.close();
        }


        // Ẩn/hiện sidebar dựa trên lựa chọn
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

        boolean currentIsManager = false;
        if (Session.getCurrentUser() != null) {
            String r = Session.getCurrentUser().getVaiTro().getTenVaiTro();
            currentIsManager = r != null && r.equalsIgnoreCase("QuanLy");
        }

        List<String> managerOnly = List.of("Thực đơn", "Món ăn", "Tài khoản");
        if (managerOnly.contains(s) && !currentIsManager) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Quyền truy cập bị từ chối. Chức năng này yêu cầu quyền Quản Lý.", ButtonType.OK);
            a.initOwner((Stage) getScene().getWindow()); // Đặt owner cho Alert
            a.showAndWait();
            return;
        }

        Node newContent = switch (s) {
            case "Đặt bàn" -> new GiaoDienDatBan((StackPane) mainContent);
            case "Phiếu đặt bàn" -> new GiaoDienPhieuDatBan();
            // Thêm các case khác cho các mục menu nếu cần
            default -> null;
        };

        if (newContent != null) {
            Region region = (Region) newContent;
            region.prefWidthProperty().bind(mainContent.widthProperty());
            region.prefHeightProperty().bind(mainContent.heightProperty());
            mainContent.getChildren().setAll(newContent);
        }
    }
}