package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class KhuyenMaiGrid extends VBox {
    private final KhuyenMaiController boDieuKhien = new KhuyenMaiController();
    private FlowPane grid;
    private KhuyenMaiBox selectedBox = null;

    public KhuyenMaiGrid(GiaoDienKhuyenMai mainView) {
        grid = new FlowPane(15, 15); // Horizontal and vertical gap
        grid.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.getChildren().add(scrollPane);
        this.setStyle("-fx-background-color: transparent;");

        loadKhuyenMai(mainView);
    }

    private void loadKhuyenMai(GiaoDienKhuyenMai mainView) {
        grid.getChildren().clear();

        // Add "Them moi" box
        KhuyenMaiBox themMoiBox = KhuyenMaiBox.createThemMoiBox();
        themMoiBox.setOnMouseClicked(event -> {
            if (boDieuKhien.themKhuyenMaiMoi()) {
                mainView.lamMoiGiaoDien();
            }
        });
        grid.getChildren().add(themMoiBox);

        // Load existing KhuyenMai
        List<KhuyenMai> danhSachKhuyenMai = boDieuKhien.layDanhSachKhuyenMai();
        for (KhuyenMai km : danhSachKhuyenMai) {
            KhuyenMaiBox box = new KhuyenMaiBox(km);
            box.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getClickCount() == 1) {
                    if (selectedBox != null) {
                        selectedBox.setDefaultStyle();
                    }
                    selectedBox = box;
                    box.setSelectedStyle();
                    mainView.hienThiChiTietKhuyenMai(km);
                } else if (event.getClickCount() == 2) {
                    if (boDieuKhien.capNhatKhuyenMai(km)) {
                        mainView.lamMoiGiaoDien();
                    }
                }
            });
            grid.getChildren().add(box);
        }
    }

    public void refresh(GiaoDienKhuyenMai mainView) {
        loadKhuyenMai(mainView);
    }
}
