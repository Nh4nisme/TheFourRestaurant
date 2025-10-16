package com.thefourrestaurant.view;

import com.thefourrestaurant.controller.MonAnController;
import com.thefourrestaurant.view.components.LoaiMonAnBox;
import com.thefourrestaurant.view.components.MonAnBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonAnBun extends GridPane {

    private GridPane luoiCacMonAn;
    private HBox boChuyenTrang;
    private List<Map<String, String>> danhSachMonAn;
    private final int soMucMoiTrang = 14; // 2 hàng x 7 cột
    private final int soCotMoiHang = 7;
    private int trangHienTai = 1;
    private final MonAnController controller;

    public MonAnBun() {
        this.controller = new MonAnController();
        khoiTaoDuLieuGia();

        this.setStyle("-fx-background-color: #F5F5F5;");

        RowConstraints hangTren = new RowConstraints();
        hangTren.setPercentHeight(5);
        RowConstraints hangDuoi = new RowConstraints();
        hangDuoi.setPercentHeight(95);
        this.getRowConstraints().addAll(hangTren, hangDuoi);

        Label duongDan = new Label("Quản Lý > Món Ăn > Bún");
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungTren = new VBox(duongDan);
        khungTren.setStyle("-fx-background-color: #673E1F;");
        khungTren.setAlignment(Pos.CENTER_LEFT);
        khungTren.setPadding(new Insets(0, 20, 0, 20));
        khungTren.setMaxWidth(Double.MAX_VALUE);
        khungTren.setMaxHeight(Double.MAX_VALUE);
        GridPane.setHgrow(khungTren, Priority.ALWAYS);
        GridPane.setVgrow(khungTren, Priority.ALWAYS);
        this.add(khungTren, 0, 0);

        VBox khungDuoi = new VBox();
        khungDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khungDuoi.setAlignment(Pos.CENTER);
        khungDuoi.setPadding(new Insets(20));
        GridPane.setMargin(khungDuoi, new Insets(10, 10, 10, 10));
        this.add(khungDuoi, 0, 1);

        VBox dsMonAn = new VBox(20);
        dsMonAn.setStyle("-fx-background-color: #F0F2F3; -fx-background-radius: 10;");
        dsMonAn.setAlignment(Pos.TOP_CENTER);
        dsMonAn.setPadding(new Insets(20));
        khungDuoi.getChildren().add(dsMonAn);
        dsMonAn.setPrefWidth(800);
        dsMonAn.setPrefHeight(800);

        VBox phanTren = new VBox();
        phanTren.setPrefHeight(250);
        phanTren.setAlignment(Pos.CENTER_LEFT);
        phanTren.setPadding(new Insets(10, 20, 10, 20));
        phanTren.setStyle("-fx-background-color: #F0F2F3");

        BorderPane phanDuoi = new BorderPane();
        phanDuoi.setPadding(new Insets(10, 0, 0, 0));

        luoiCacMonAn = new GridPane();
        luoiCacMonAn.setAlignment(Pos.CENTER);
        luoiCacMonAn.setHgap(20);
        luoiCacMonAn.setVgap(20);
        luoiCacMonAn.getStyleClass().add("grid-pane");

        boChuyenTrang = new HBox(10);
        boChuyenTrang.setAlignment(Pos.CENTER);
        boChuyenTrang.setPadding(new Insets(15, 0, 0, 0));

        GridPane luoiThemMon = new GridPane();
        luoiThemMon.setAlignment(Pos.BASELINE_LEFT);
        luoiThemMon.setHgap(20);
        luoiThemMon.setVgap(20);
        luoiThemMon.getStyleClass().add("grid-pane");
        luoiThemMon.setPadding(new Insets(0, 0, 0, 5));

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
                danhSachMonAn.add(newItem);
                capNhatLuoiMonAn(trangHienTai);
            }
        });

        hopThemMoi.setOnMouseClicked(event -> themMoiButton.fire());

        luoiThemMon.add(hopThemMoi, 0, 0);
        this.getChildren().add(themMoiButton);

        capNhatLuoiMonAn(trangHienTai);
        thietLapPhanTrang();
        phanTren.getChildren().add(luoiThemMon);

        phanDuoi.setCenter(luoiCacMonAn);
        phanDuoi.setBottom(boChuyenTrang);

        dsMonAn.getChildren().addAll(phanTren, phanDuoi);
        VBox.setVgrow(phanDuoi, Priority.ALWAYS);

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }
    }

    private void khoiTaoDuLieuGia() {
        danhSachMonAn = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("name", "Bún " + i);
            item.put("price", (25 + i) + ",000");
            item.put("imagePath", null); // Sử dụng imagePath và để null để hiển thị ảnh mặc định
            danhSachMonAn.add(item);
        }
    }

    private void capNhatLuoiMonAn(int trang) {
        trangHienTai = trang;
        luoiCacMonAn.getChildren().clear();

        int tongHop = danhSachMonAn.size();
        int batDau = (trang - 1) * soMucMoiTrang;
        int ketThuc = Math.min(batDau + soMucMoiTrang, tongHop);

        for (int i = batDau; i < ketThuc; i++) {
            Map<String, String> item = danhSachMonAn.get(i);
            MonAnBox hopMonAn = new MonAnBox(item.get("name"), item.get("price"), item.get("imagePath"));

            int chiSoTrongTrang = i - batDau;
            int col = chiSoTrongTrang % soCotMoiHang;
            int row = chiSoTrongTrang / soCotMoiHang;

            luoiCacMonAn.add(hopMonAn, col, row);
        }
        thietLapPhanTrang();
    }

    private void thietLapPhanTrang() {
        boChuyenTrang.getChildren().clear();
        int tongHop = danhSachMonAn.size();

        if (tongHop <= soMucMoiTrang) {
            return;
        }

        int tongSoTrang = (int) Math.ceil((double) tongHop / soMucMoiTrang);

        for (int i = 1; i <= tongSoTrang; i++) {
            final int soTrang = i;
            Label trangLabel = new Label(String.valueOf(soTrang));
            trangLabel.setPadding(new Insets(5, 10, 5, 10));
            trangLabel.setCursor(Cursor.HAND);

            if (trangHienTai == soTrang) {
                trangLabel.setStyle("-fx-background-color: #673E1F; -fx-text-fill: white; -fx-background-radius: 5;");
            } else {
                trangLabel.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: black; -fx-background-radius: 5;");
            }

            trangLabel.setOnMouseClicked(event -> {
                capNhatLuoiMonAn(soTrang);
            });

            boChuyenTrang.getChildren().add(trangLabel);
        }
    }
}
