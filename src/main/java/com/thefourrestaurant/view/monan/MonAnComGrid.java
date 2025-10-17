package com.thefourrestaurant.view.monan;

import com.thefourrestaurant.controller.MonAnController;
import com.thefourrestaurant.view.loaimonan.LoaiMonAnBox;
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

public class MonAnComGrid extends VBox {

    private GridPane luoiCacMonAn;
    private List<Map<String, String>> danhSachMonAn;
    private final int soCotMoiHang = 8;
    private final MonAnController controller;

    public MonAnComGrid() {
        this.controller = new MonAnController();
        khoiTaoDuLieuGia();

        this.setSpacing(20); // Thêm khoảng cách giữa các phần tử con

        VBox.setVgrow(this, Priority.ALWAYS);

        luoiCacMonAn = new GridPane();
        luoiCacMonAn.setAlignment(Pos.CENTER);
        luoiCacMonAn.setHgap(20);
        luoiCacMonAn.setVgap(20);
        luoiCacMonAn.getStyleClass().add("grid-pane");

        ScrollPane scrollPane = new ScrollPane(luoiCacMonAn);
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
        luoiThemMon.setMinHeight(200);

        VBox hopThemMoi = LoaiMonAnBox.createThemMoiBox();

        Button themMoiButton = new Button();
        themMoiButton.setVisible(false);
        themMoiButton.setManaged(false);

        themMoiButton.setOnAction(event -> {
            Map<String, Object> result = controller.themMoiMonAn();
            if (result != null) {
                Map<String, String> newItem = new HashMap<>();
                newItem.put("name", (String) result.get("ten"));
                newItem.put("price", (String) result.get("gia"));
                newItem.put("imagePath", (String) result.get("imagePath"));
                danhSachMonAn.add(0, newItem);
                capNhatLuoiMonAn();
            }
        });

        hopThemMoi.setOnMouseClicked(event -> themMoiButton.fire());

        luoiThemMon.add(hopThemMoi, 0, 0);
        this.getChildren().add(themMoiButton); // Add to main layout to be accessible

        capNhatLuoiMonAn();

        this.getChildren().addAll(luoiThemMon, scrollPane);
    }

    private void khoiTaoDuLieuGia() {
        danhSachMonAn = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("name", "Cơm " + i);
            item.put("price", (30 + i) + ",000");
            item.put("imagePath", null);
            danhSachMonAn.add(item);
        }
    }

    private void capNhatLuoiMonAn() {
        if (luoiCacMonAn == null) return;
        luoiCacMonAn.getChildren().clear();

        for (int i = 0; i < danhSachMonAn.size(); i++) {
            Map<String, String> item = danhSachMonAn.get(i);
            MonAnBox hopMonAn = new MonAnBox(item.get("name"), item.get("price"), item.get("imagePath"));

            hopMonAn.setOnMouseClicked(event -> {
                MonAnTuyChinh tuyChinh = new MonAnTuyChinh();
                tuyChinh.showAndWait();

                Map<String, Object> ketQua = tuyChinh.layKetQua();
                if (ketQua != null) {
                    item.put("name", (String) ketQua.get("ten"));
                    item.put("price", (String) ketQua.get("gia"));
                    item.put("imagePath", (String) ketQua.get("imagePath"));
                    capNhatLuoiMonAn();
                }
            });

            int col = i % soCotMoiHang;
            int row = i / soCotMoiHang;
            luoiCacMonAn.add(hopMonAn, col, row);
        }
    }
}
