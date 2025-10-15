package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.ThongKe;
import com.thefourrestaurant.view.components.sidebar.*;
import javafx.animation.*;
import javafx.scene.layout.*;

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
    }

    private void moHoacDongPanel(String loaiPanel) {
        if (panelDangMo != null && panelDangMo.getUserData().equals(loaiPanel)) {
            mainContainer.getChildren().remove(panelDangMo);
            panelDangMo = null;
            return;
        }

        if (panelDangMo != null) {
            mainContainer.getChildren().remove(panelDangMo);
            panelDangMo = null;
        }

        Pane panelMoi = switch (loaiPanel) {
            case "DanhMuc" -> new SideBarDanhMuc(mainContainer);
            case "ThongKe" -> {
                SideBarThongKe tk = new SideBarThongKe();
                tk.setMainContainer(mainContainer); // truyền container chính
                yield tk;
            }
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
