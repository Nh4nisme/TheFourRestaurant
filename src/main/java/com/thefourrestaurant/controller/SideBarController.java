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
    }

    private void moHoacDongPanel(String loaiPanel) {
        if (panelDangMo != null && panelDangMo.getUserData() != null && panelDangMo.getUserData().equals(loaiPanel)) {
            mainContainer.getChildren().remove(panelDangMo);
            panelDangMo = null;
            return;
        }

        if (panelDangMo != null) {
            mainContainer.getChildren().remove(panelDangMo);
        }

        Pane panelMoi = switch (loaiPanel) {
            case "DanhMuc" -> {
                VBox rightBox = null;
                if (mainContainer.getChildren().size() > 1) {
                    Node node = mainContainer.getChildren().get(1);
                    if (node instanceof VBox) {
                        rightBox = (VBox) node;
                    }
                }
                yield new SideBarDanhMuc(rightBox);
            }
            case "ThongKe" -> {
                SideBarThongKe tk = new SideBarThongKe();
                tk.setMainContainer(mainContainer);
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
