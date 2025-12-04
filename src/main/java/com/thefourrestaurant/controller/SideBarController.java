package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.GiaoDienChinh;
import com.thefourrestaurant.view.components.sidebar.SideBar;
import com.thefourrestaurant.view.components.sidebar.SideBarDanhMuc;
import com.thefourrestaurant.view.thongke.ThongKeController;
import com.thefourrestaurant.view.thongke.ThongKeView;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SideBarController {

    private final SideBar sideBar;
    private final VBox sideBarExtended;
    private final Pane mainContent;
    private Pane panelDangMo;

    public SideBarController(SideBar sideBar, VBox sideBarExtended, Pane mainContent) {
        this.sideBar = sideBar;
        this.sideBarExtended = sideBarExtended;
        this.mainContent = mainContent;
        khoiTaoSuKien();
    }

    private void khoiTaoSuKien() {
        sideBar.getButton("DanhMuc").setOnAction(e -> moHoacDongPanel("DanhMuc"));
        sideBar.getButton("BaoCao").setOnAction(e -> hienThiViewThongKe());
        sideBar.getButton("CaiDat").setOnAction(e -> {
            Stage stage = (Stage) sideBar.getScene().getWindow();
            new GiaoDienChinh().show(stage); // Reload lại giao diện
            stage.setFullScreen(true);
        });
    }

    private void hienThiViewThongKe() {
        dongSideBarMoRong(); // Đóng sidebar mở rộng nếu có
        
        // Tạo view và controller mới cho thống kê
        ThongKeView thongKeView = new ThongKeView();
        new ThongKeController(thongKeView); // Controller sẽ tự gắn sự kiện

        // Hiển thị view trong mainContent
        mainContent.getChildren().clear();
        mainContent.getChildren().add(thongKeView);
    }

    private void moHoacDongPanel(String loaiPanel) {
        if (panelDangMo != null && panelDangMo.getUserData() != null
                && panelDangMo.getUserData().equals(loaiPanel)) {
            dongSideBarMoRong();
            return;
        }

        Pane panelMoi = switch (loaiPanel) {
            case "DanhMuc" -> new SideBarDanhMuc(mainContent);
            // Xóa trường hợp "ThongKe" cũ
            default -> null;
        };

        if (panelMoi != null) {
            panelMoi.setPrefWidth(300);
            panelMoi.setMaxWidth(300);
            panelMoi.setMinWidth(300);
            panelMoi.setUserData(loaiPanel);

            sideBarExtended.getChildren().setAll(panelMoi);
            sideBarExtended.setVisible(true);
            sideBarExtended.setManaged(true);
            panelDangMo = panelMoi;
        }
    }

    private void dongSideBarMoRong() {
        sideBarExtended.setVisible(false);
        sideBarExtended.setManaged(false);
        sideBarExtended.getChildren().clear();
        panelDangMo = null;
    }
}
