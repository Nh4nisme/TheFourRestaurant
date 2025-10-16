package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.GiaoDienChinh;
import com.thefourrestaurant.view.QuanLiBan;
import com.thefourrestaurant.view.components.sidebar.SideBar;
import com.thefourrestaurant.view.components.sidebar.SideBarDanhMuc;
import com.thefourrestaurant.view.components.sidebar.SideBarThongKe;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SideBarController {

    private final SideBar sideBar;
    private final HBox mainContainer;
    private Pane panelDangMo;
    private Pane backgroundCenter; // Pane trung tâm sẽ thay đổi

    public SideBarController(SideBar sideBar, HBox mainContainer, Pane backgroundCenter) {
        this.sideBar = sideBar;
        this.mainContainer = mainContainer;
        this.backgroundCenter = backgroundCenter;
        khoiTaoSuKien();
    }

    private void khoiTaoSuKien() {
        sideBar.getButton("DanhMuc").setOnAction(e -> moHoacDongPanel("DanhMuc"));
        sideBar.getButton("ThongKe").setOnAction(e -> moHoacDongPanel("ThongKe"));
        sideBar.getButton("CaiDat").setOnAction(e -> {
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            new GiaoDienChinh().show(stage); // Load lại giao diện
        });
    }

    private void moHoacDongPanel(String loaiPanel) {
        // Nếu panel đang mở và trùng loại => đóng
        if (panelDangMo != null && panelDangMo.getUserData() != null
                && panelDangMo.getUserData().equals(loaiPanel)) {
            mainContainer.getChildren().remove(panelDangMo);
            panelDangMo = null;
            return;
        }

        // Đóng panel cũ nếu có
        if (panelDangMo != null) {
            mainContainer.getChildren().remove(panelDangMo);
        }

        Pane panelMoi = switch (loaiPanel) {
            case "DanhMuc" -> new SideBarDanhMuc(mainContainer);
            case "ThongKe" -> new SideBarThongKe(mainContainer);
            default -> null;
        };

        if (panelMoi != null) {
            panelMoi.setPrefWidth(300);
            panelMoi.setMaxWidth(300);
            panelMoi.setMinWidth(300);
            panelMoi.setUserData(loaiPanel);
            mainContainer.getChildren().add(1, panelMoi); // Thêm panel trượt vào giữa sideBar và rightBox
            panelDangMo = panelMoi;
        }
    }

    // Nếu muốn thay backgroundCenter sau này (ví dụ load giao diện khác)
    public void setBackgroundCenter(Pane backgroundCenter) {
        this.backgroundCenter = backgroundCenter;
    }
}
