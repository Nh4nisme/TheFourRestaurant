package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.components.sidebar.*;
import javafx.animation.*;
import javafx.scene.layout.*;

public class SideBarController {
    private final SideBar sideBar;
    private final HBox mainContainer;
    private Pane expandedPanel; // panel đang mở

    public SideBarController(SideBar sideBar, HBox mainContainer) {
        this.sideBar = sideBar;
        this.mainContainer = mainContainer;
        initialize();
    }

    private void initialize() {
        sideBar.getButton("ThongKe").setOnAction(e -> togglePanel("Thống kê"));
        sideBar.getButton("HoaDon").setOnAction(e -> togglePanel("Hóa đơn"));
        sideBar.getButton("TaiKhoan").setOnAction(e -> togglePanel("Tài khoản"));
        sideBar.getButton("KhachHang").setOnAction(e -> togglePanel("Khách hàng"));
        sideBar.getButton("DanhMuc").setOnAction(e -> togglePanel("Danh mục"));
    }

    private void togglePanel(String title) {
        // Nếu panel đang mở và là nút vừa click → đóng
        if (expandedPanel != null && expandedPanel.getUserData().equals(title)) {
            mainContainer.getChildren().remove(expandedPanel);
            expandedPanel = null;
            return;
        }

        // Nếu panel khác đang mở → remove panel cũ
        if (expandedPanel != null) {
            mainContainer.getChildren().remove(expandedPanel);
        }

        // Tạo panel mới
        expandedPanel = createPanel(title);
        mainContainer.getChildren().add(1, expandedPanel); // chèn sau sidebar
        HBox.setHgrow(expandedPanel, Priority.ALWAYS); // panel chiếm không gian còn lại
    }

    private Pane createPanel(String title) {
        Pane panel = switch (title) {
            case "Thống kê" -> new SideBarThongKe();
            case "Hóa đơn" -> new SideBarHoaDon();
            case "Tài khoản" -> new SideBarTaiKhoan();
            case "Khách hàng" -> new SideBarKhachHang();
            case "Danh mục" -> new SideBarDanhMuc();
            default -> new VBox();
        };

        panel.setUserData(title); // lưu title để nhận biết
        return panel;
    }
}