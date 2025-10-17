package com.thefourrestaurant.view.thoigiansukien;

import com.thefourrestaurant.controller.ThoiGianSuKienController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThoiGianSuKienGrid extends VBox {

    private GridPane luoiCacSuKien;
    private List<Map<String, Object>> danhSachSuKien;
    private final int soCotMoiHang = 5;
    private final ThoiGianSuKienController controller;

    public ThoiGianSuKienGrid() {
        this.controller = new ThoiGianSuKienController();
        khoiTaoDuLieuGia();

        this.setSpacing(20);
        VBox.setVgrow(this, Priority.ALWAYS);

        luoiCacSuKien = new GridPane();
        luoiCacSuKien.setAlignment(Pos.CENTER);
        luoiCacSuKien.setHgap(20);
        luoiCacSuKien.setVgap(20);
        luoiCacSuKien.getStyleClass().add("grid-pane");

        ScrollPane scrollPane = new ScrollPane(luoiCacSuKien);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        GridPane luoiThemMon = new GridPane();
        luoiThemMon.setAlignment(Pos.BASELINE_LEFT);
        luoiThemMon.setHgap(20);
        luoiThemMon.setVgap(20);
        luoiThemMon.getStyleClass().add("grid-pane");
        luoiThemMon.setPadding(new Insets(0, 0, 0, 15));
        luoiThemMon.setMinHeight(280); // Adjusted for new box height

        VBox hopThemMoi = ThoiGianSuKienBox.createThemMoiBox();

        Button themMoiButton = new Button();
        themMoiButton.setVisible(false);
        themMoiButton.setManaged(false);

        themMoiButton.setOnAction(event -> {
            Map<String, Object> result = controller.themMoiSuKien();
            if (result != null) {
                danhSachSuKien.add(0, result);
                capNhatLuoiSuKien();
            }
        });

        hopThemMoi.setOnMouseClicked(event -> themMoiButton.fire());

        luoiThemMon.add(hopThemMoi, 0, 0);
        this.getChildren().add(themMoiButton);

        capNhatLuoiSuKien();

        this.getChildren().addAll(luoiThemMon, scrollPane);
    }

    private void khoiTaoDuLieuGia() {
        danhSachSuKien = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("tenSuKien", "Trưa ngon rẻ " + i);
            item.put("giamGia", (i * 10) + "%");
            item.put("thoiGianMa", "Vô thời hạn");
            item.put("thoiGianBatDau", "01/09");
            item.put("thoiGianKetThuc", "30/09");
            item.put("ngayApDung", new String[]{"T2", "T3", "T4", "T5", "T6"});
            item.put("gioApDung", "14:00        -        17:00");
            danhSachSuKien.add(item);
        }
    }

    private void capNhatLuoiSuKien() {
        if (luoiCacSuKien == null) return;
        luoiCacSuKien.getChildren().clear();

        for (int i = 0; i < danhSachSuKien.size(); i++) {
            Map<String, Object> item = danhSachSuKien.get(i);

            Object ngayApDungObj = item.get("ngayApDung");
            String[] ngayApDung;
            if (ngayApDungObj instanceof List) {
                ngayApDung = ((List<?>) ngayApDungObj).toArray(new String[0]);
            } else {
                ngayApDung = (String[]) ngayApDungObj;
            }

            ThoiGianSuKienBox hopSuKien = new ThoiGianSuKienBox(
                    (String) item.get("tenSuKien"),
                    (String) item.get("giamGia"),
                    (String) item.get("thoiGianMa"),
                    (String) item.get("thoiGianBatDau"),
                    (String) item.get("thoiGianKetThuc"),
                    ngayApDung,
                    (String) item.get("gioApDung")
            );

            final int index = i;
            hopSuKien.setOnMouseClicked(event -> {
                ThoiGianTuyChinh tuyChinh = new ThoiGianTuyChinh();
                tuyChinh.showAndWait();

                Map<String, Object> ketQua = tuyChinh.layKetQua();
                if (ketQua != null) {
                    danhSachSuKien.set(index, ketQua);
                    capNhatLuoiSuKien();
                }
            });
            int col = i % soCotMoiHang;
            int row = i / soCotMoiHang;

            luoiCacSuKien.add(hopSuKien, col, row);
        }
    }
}
