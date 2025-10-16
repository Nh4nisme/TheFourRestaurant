package com.thefourrestaurant.view;

import com.thefourrestaurant.controller.MonAnController;
import com.thefourrestaurant.view.components.LoaiMonAnBox;
import com.thefourrestaurant.view.components.MonAnBox;
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

public class MonAnCom extends VBox {

    private GridPane luoiCacMonAn;
    private List<Map<String, String>> danhSachMonAn;
    private final int soCotMoiHang = 8; // Changed to 8 to match LoaiMonAn

    private final MonAnController controller;

    public MonAnCom() {
        this.controller = new MonAnController();
        khoiTaoDuLieuGia();

        this.setAlignment(Pos.TOP_CENTER);

        NavBar navBar = new NavBar(this);
        navBar.setPrefHeight(80);
        navBar.setMinHeight(80);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        Label duongDan = new Label("Quản Lý > Món Ăn > Cơm");
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungTren = new VBox(duongDan);
        khungTren.setStyle("-fx-background-color: #673E1F;");
        khungTren.setAlignment(Pos.CENTER_LEFT);
        khungTren.setPadding(new Insets(0, 20, 0, 20));
        khungTren.setPrefHeight(30); // Đặt chiều cao ưu tiên
        khungTren.setMinHeight(30); // Đặt chiều cao tối thiểu để không bị co lại
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
        GridPane.setVgrow(khungDuoi, Priority.ALWAYS); // Đảm bảo phần dưới luôn lấp đầy không gian

        VBox dsMonAn = new VBox(20);
        dsMonAn.setStyle("-fx-background-color: #F0F2F3; -fx-background-radius: 10;");
        dsMonAn.setAlignment(Pos.TOP_CENTER);
        dsMonAn.setPadding(new Insets(20));
        khungDuoi.getChildren().add(dsMonAn);
        VBox.setVgrow(dsMonAn, Priority.ALWAYS);

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

        GridPane luoiThemMon = new GridPane();
        luoiThemMon.setAlignment(Pos.BASELINE_LEFT);
        luoiThemMon.setHgap(20);
        luoiThemMon.setVgap(20);
        luoiThemMon.getStyleClass().add("grid-pane");
        luoiThemMon.setPadding(new Insets(0, 0, 0, 5));
        luoiThemMon.setMinHeight(200); // Added to match LoaiMonAn

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
                danhSachMonAn.add(0, newItem); // Add to the beginning
                capNhatLuoiMonAn();
            }
        });

        hopThemMoi.setOnMouseClicked(event -> themMoiButton.fire());

        luoiThemMon.add(hopThemMoi, 0, 0);
        contentPane.getChildren().add(themMoiButton);

        capNhatLuoiMonAn();

        dsMonAn.getChildren().addAll(luoiThemMon, scrollPane);
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
        danhSachMonAn = new ArrayList<>();
        for (int i = 1; i <= 50; i++) { // Generate more items
            Map<String, String> item = new HashMap<>();
            item.put("name", "Cơm " + i);
            item.put("price", (30 + i) + ",000");
            item.put("imagePath", null);
            danhSachMonAn.add(item);
        }
    }

    private void capNhatLuoiMonAn() {
        luoiCacMonAn.getChildren().clear();

        for (int i = 0; i < danhSachMonAn.size(); i++) {
            Map<String, String> item = danhSachMonAn.get(i);
            MonAnBox hopMonAn = new MonAnBox(item.get("name"), item.get("price"), item.get("imagePath"));

            int col = i % soCotMoiHang;
            int row = i / soCotMoiHang;

            luoiCacMonAn.add(hopMonAn, col, row);
        }
    }
}
