package com.thefourrestaurant.view.loaimonan;

import com.thefourrestaurant.controller.LoaiMonAnController;
import com.thefourrestaurant.view.components.NavBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoaiMonAn extends VBox {

    private GridPane luoiCacMucDuoi;
    private List<Map<String, String>> danhSachLoaiMonAn;
    private final int soCotMoiHang = 8;

    private final LoaiMonAnController controller;

    public LoaiMonAn() {
        this.controller = new LoaiMonAnController();
        khoiTaoDuLieuGia();

        this.setAlignment(Pos.TOP_CENTER);

        NavBar navBar = new NavBar(this);
        navBar.setPrefHeight(80);
        navBar.setMinHeight(80);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        Label duongDan = new Label("Quản Lý > Loại Món Ăn");
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungTren = new VBox(duongDan);
        khungTren.setStyle("-fx-background-color: #673E1F;");
        khungTren.setAlignment(Pos.CENTER_LEFT);
        khungTren.setPadding(new Insets(0, 20, 0, 20));
        khungTren.setPrefHeight(30);
        khungTren.setMinHeight(30);
        khungTren.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(khungTren, Priority.ALWAYS);
        contentPane.add(khungTren, 0, 0);

        VBox khungDuoi = new VBox();
        khungDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khungDuoi.setAlignment(Pos.CENTER);
        khungDuoi.setPadding(new Insets(20));
        GridPane.setMargin(khungDuoi, new Insets(10, 10, 10, 10));
        contentPane.add(khungDuoi, 0, 1);
        GridPane.setHgrow(khungDuoi, Priority.ALWAYS);
        GridPane.setVgrow(khungDuoi, Priority.ALWAYS);

        VBox dsLoaiMonAn = new VBox(20);
        dsLoaiMonAn.setStyle("-fx-background-color: #F0F2F3; -fx-background-radius: 10;");
        dsLoaiMonAn.setAlignment(Pos.TOP_CENTER);
        dsLoaiMonAn.setPadding(new Insets(20));
        khungDuoi.getChildren().add(dsLoaiMonAn);
        VBox.setVgrow(dsLoaiMonAn, Priority.ALWAYS);

        luoiCacMucDuoi = new GridPane();
        luoiCacMucDuoi.setAlignment(Pos.CENTER);
        luoiCacMucDuoi.setHgap(20);
        luoiCacMucDuoi.setVgap(20);
        luoiCacMucDuoi.getStyleClass().add("grid-pane");

        ScrollPane scrollPane = new ScrollPane(luoiCacMucDuoi);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        GridPane luoiCacMucTren = new GridPane();
        luoiCacMucTren.setAlignment(Pos.BASELINE_LEFT);
        luoiCacMucTren.setHgap(20);
        luoiCacMucTren.setVgap(20);
        luoiCacMucTren.getStyleClass().add("grid-pane");
        luoiCacMucTren.setPadding(new Insets(0, 0, 0, 15));
        luoiCacMucTren.setMinHeight(200);

        VBox hopThemMoi = LoaiMonAnBox.createThemMoiBox();

        Button themMoiButton = new Button();
        themMoiButton.setVisible(false);
        themMoiButton.setManaged(false);

        themMoiButton.setOnAction(event -> {
            Map<String, Object> duLieuMoi = controller.themMoiLoaiMonAn();
            if (duLieuMoi != null) {
                Map<String, String> mucMoi = new HashMap<>();
                mucMoi.put("name", (String) duLieuMoi.get("name"));
                mucMoi.put("imagePath", (String) duLieuMoi.get("imagePath"));

                danhSachLoaiMonAn.add(0, mucMoi);
                capNhatLuoiMonAn();
            }
        });

        hopThemMoi.setOnMouseClicked(event -> themMoiButton.fire());

        luoiCacMucTren.add(hopThemMoi, 0, 0);
        contentPane.getChildren().add(themMoiButton);

        capNhatLuoiMonAn();

        dsLoaiMonAn.getChildren().addAll(luoiCacMucTren, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }

        this.getChildren().addAll(navBar, contentPane);
    }

    private void khoiTaoDuLieuGia() {
        danhSachLoaiMonAn = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("name", "Loại Món Ăn " + i);
            item.put("imagePath", null);
            danhSachLoaiMonAn.add(item);
        }
    }

    private void capNhatLuoiMonAn() {
        luoiCacMucDuoi.getChildren().clear();

        for (int i = 0; i < danhSachLoaiMonAn.size(); i++) {
            Map<String, String> item = danhSachLoaiMonAn.get(i);
            VBox hopLoaiMonAn = LoaiMonAnBox.createLoaiMonAnBox(item.get("name"), item.get("imagePath"));

            hopLoaiMonAn.setOnMouseClicked(event -> {
                LoaiMonAnTuyChinh tuyChinh = new LoaiMonAnTuyChinh();
                tuyChinh.showAndWait();

                Map<String, Object> ketQua = tuyChinh.layKetQua();
                if (ketQua != null) {
                    // Cập nhật dữ liệu món ăn trong danh sách
                    item.put("name", (String) ketQua.get("name"));
                    item.put("imagePath", (String) ketQua.get("imagePath"));

                    // Vẽ lại toàn bộ lưới để gán lại sự kiện
                    capNhatLuoiMonAn();
                }
            });


            int col = i % soCotMoiHang;
            int row = i / soCotMoiHang;

            luoiCacMucDuoi.add(hopLoaiMonAn, col, row);
        }
    }
}
