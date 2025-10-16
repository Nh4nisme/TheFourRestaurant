package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.components.sidebar.SideBar;
import com.thefourrestaurant.view.components.sidebar.SideBarDanhMuc;
import com.thefourrestaurant.view.components.sidebar.SideBarThongKe;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SideBarController {
    private final SideBar sideBar;
    private final HBox mainContainer;
    private Pane panelDangMo;

    public SideBarController(SideBar sideBar, HBox mainContainer) {
        this.sideBar = sideBar;
        this.mainContainer = mainContainer;
        khoiTaoSuKien();
    }

    private void khoiTaoSuKien() {
        sideBar.getButton("DanhMuc").setOnAction(e -> moHoacDongPanel("DanhMuc"));
        sideBar.getButton("ThongKe").setOnAction(e -> moHoacDongPanel("ThongKe"));
        sideBar.getButton("CaiDat").setOnAction(e -> {
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            new GiaoDienChinh().show(stage);
            // ???????????????????????????????????????
        });
    }

    private void moHoacDongPanel(String loaiPanel) {
        if (panelDangMo != null && panelDangMo.getUserData() != null && panelDangMo.getUserData().equals(loaiPanel)) {
            mainContainer.getChildren().remove(panelDangMo);
            panelDangMo = null;
            // Khi đóng panel thống kê, đóng luôn nội dung bên phải
            if (loaiPanel.equals("ThongKe") && mainContainer.getChildren().size() > 2)
                mainContainer.getChildren().remove(2);
            return;
        }

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
            panelMoi.setUserData(loaiPanel);
            mainContainer.getChildren().add(1, panelMoi);
            panelDangMo = panelMoi;
        }
    }
}
