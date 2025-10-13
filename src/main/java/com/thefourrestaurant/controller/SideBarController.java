package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.components.sidebar.*;
import javafx.animation.*;
import javafx.scene.layout.*;

public class SideBarController {
    private final SideBar sideBar;
    private final HBox mainContainer;
    private Pane danhMucPanel;

    public SideBarController(SideBar sideBar, HBox mainContainer) {
        this.sideBar = sideBar;
        this.mainContainer = mainContainer;
        khoiTaoSuKien();
    }

    private void khoiTaoSuKien() {
        sideBar.getButton("DanhMuc").setOnAction(e -> moHoacDongDanhMuc());
    }

    private void moHoacDongDanhMuc() {
        if (danhMucPanel != null) {
            mainContainer.getChildren().remove(danhMucPanel);
            danhMucPanel = null;
        } else {
            danhMucPanel = new SideBarDanhMuc();
            HBox.setHgrow(danhMucPanel, Priority.ALWAYS);
            danhMucPanel.setMaxWidth(300);
            mainContainer.getChildren().add(1, danhMucPanel);
        }
    }
}
