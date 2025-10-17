package com.thefourrestaurant.view.monan;

import com.thefourrestaurant.controller.MonAnController;
import com.thefourrestaurant.view.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonAnBun extends VBox {

    private GridPane luoiCacMonAn;
    private List<Map<String, String>> danhSachMonAn;
    private final int soCotMoiHang = 8;

    private final MonAnController controller;
    private ButtonSample btnTim, btnLamMoi;

    public MonAnBun() {
        this.controller = new MonAnController();
        khoiTaoDuLieuGia();

        this.setAlignment(Pos.TOP_CENTER);

        NavBar navBar = new NavBar(this);
        navBar.setPrefHeight(80);
        navBar.setMinHeight(80);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        Label duongDan = new Label("Quản Lý > Món Ăn > Bún");
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

        HBox khungGiua = taoKhungGiua();
        GridPane.setHgrow(khungGiua, Priority.ALWAYS);
        contentPane.add(khungGiua, 0, 1);
        khungGiua.setPrefHeight(60);
        khungGiua.setMinHeight(60);

        VBox khungDuoi = new VBox();
        khungDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khungDuoi.setAlignment(Pos.CENTER);
        khungDuoi.setPadding(new Insets(20));
        GridPane.setMargin(khungDuoi, new Insets(10, 10, 10, 10));
        contentPane.add(khungDuoi, 0, 2);
        GridPane.setHgrow(khungDuoi, Priority.ALWAYS);
        GridPane.setVgrow(khungDuoi, Priority.ALWAYS);

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

    // ==== Sửa: chỉ có một phương thức taoKhungGiua() duy nhất ====
    private HBox taoKhungGiua() {
        HBox khungGiua = new HBox(10);
        khungGiua.setPadding(new Insets(10, 20, 10, 20));
        khungGiua.setAlignment(Pos.CENTER_LEFT);
        khungGiua.setStyle("-fx-background-color: #1E424D;");

        /// === Tạo ảnh cho nút List và Grid ===
        ImageView iconList = new ImageView(getClass().getResource("/com/thefourrestaurant/images/icon/List.png").toExternalForm());
        ImageView iconGrid = new ImageView(getClass().getResource("/com/thefourrestaurant/images/icon/Grid.png").toExternalForm());

        iconList.setFitWidth(20);
        iconList.setFitHeight(20);
        iconGrid.setFitWidth(20);
        iconGrid.setFitHeight(20);

        // === Nút chuyển kiểu hiển thị ===
        ButtonSample btnList = new ButtonSample("", "", 35, 16, 3);
        ButtonSample btnGrid = new ButtonSample("", "", 35, 16, 3);

        // Gắn icon vào nút
        btnList.setGraphic(iconList);
        btnGrid.setGraphic(iconGrid);

        btnList.setPrefSize(35, 35);
        btnGrid.setPrefSize(35, 35);

        Label lblSapXep = new Label("Sắp xếp:");
        lblSapXep.setTextFill(Color.web("#E5D595"));
        lblSapXep.setFont(Font.font("System", FontWeight.BOLD, 14));

        // --- Thay đổi: dùng DropDownButton cho hai lựa chọn sắp xếp ---
        DropDownButton btnTheoChuCai = new DropDownButton(
                "Theo bảng chữ cái  ▼",
                List.of("A → Z", "Z → A"),
                null,
                35,
                16,
                3
        );

        DropDownButton btnTheoGia = new DropDownButton(
                "Theo giá  ▼",
                List.of("Tăng dần", "Giảm dần"),
                null,
                35,
                16,
                3
        );

        ButtonSample btnApDung = new ButtonSample("Áp dụng", "", 35, 13, 3);

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm...");
        txtTimKiem.setPrefWidth(300);
        txtTimKiem.setStyle("-fx-background-radius: 8;");

        // Gán cho trường lớp để dùng ở chỗ khác nếu cần
        this.btnTim = new ButtonSample("Tìm", "", 35, 13, 3);

        khungGiua.getChildren().addAll(
                btnList, btnGrid,
                lblSapXep,
                btnTheoChuCai, btnTheoGia, btnApDung,
                space,
                txtTimKiem, btnTim
        );

        return khungGiua;
    }

    private void khoiTaoDuLieuGia() {
        danhSachMonAn = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("name", "Bún " + i);
            item.put("price", (25 + i) + ",000");
            item.put("imagePath", null);
            danhSachMonAn.add(item);
        }
    }

    private void capNhatLuoiMonAn() {
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
