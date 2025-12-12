package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Popup;

import java.util.List;

public abstract class GiaoDienTraCuu extends VBox {

    protected TableView<?> tableChinh;

    protected HBox thanhCongCu;
    protected HBox thanhTrai;
    protected HBox thanhPhai;

    protected TextField txtTimKiem;

    public GiaoDienTraCuu() {

    }

    protected void khoiTaoGiaoDien() {
        taoThanhCongCuRong();

        tableChinh = taoBangChinh();
        VBox.setVgrow(tableChinh, Priority.ALWAYS);

        getChildren().addAll(thanhCongCu, tableChinh);
    }

    // Tạo thanh công cụ rỗng (không có bộ lọc)
    private void taoThanhCongCuRong() {
        thanhCongCu = new HBox(15);
        thanhCongCu.setAlignment(Pos.CENTER_LEFT);
        thanhCongCu.setPadding(new Insets(10));
        thanhCongCu.setStyle("-fx-background-color: #1E424D;");

        thanhTrai = new HBox(10);
        thanhTrai.setAlignment(Pos.CENTER_LEFT);

        thanhPhai = new HBox(10);
        thanhPhai.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(thanhPhai, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        thanhCongCu.getChildren().addAll(thanhTrai, spacer, thanhPhai);
    }

    // Thêm ô tìm kiếm
    protected void themThanhTimKiem() {
        txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Nhập từ khóa...");
        txtTimKiem.setStyle("-fx-font-size: 16; -fx-background-radius: 8");

        ButtonSample btnTim = new ButtonSample("Tìm", 35, 16, 3);
        btnTim.setOnAction(e -> thucHienTimKiem(txtTimKiem.getText().trim()));

        HBox box = new HBox(10, txtTimKiem, btnTim);
        thanhPhai.getChildren().add(box);
    }

    // Thêm nút làm mới
    protected void themButtonLamMoi() {
        ButtonSample btnLamMoi = new ButtonSample("Làm mới", 35, 16, 3);
        btnLamMoi.setOnAction(e -> lamMoiDuLieu());

        thanhPhai.getChildren().add(btnLamMoi);
    }

    // Tạo bộ lọc A-Z dạng popup bảng chọn như DatePicker
    protected void themBoLocChuCai() {
        Button btnChon = new Button("Chọn chữ cái");
        btnChon.setPrefHeight(32);
        btnChon.setStyle("-fx-font-size: 13;");

        Popup popup = new Popup();

        GridPane grid = new GridPane();
        grid.setHgap(6);
        grid.setVgap(6);
        grid.setPadding(new Insets(8));
        grid.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ccc;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);"
        );

        int col = 0, row = 0;

        for (char c = 'A'; c <= 'Z'; c++) {
            Button btn = new Button(String.valueOf(c));
            btn.setPrefSize(36, 32);
            btn.setStyle("-fx-font-size: 13; -fx-background-radius: 4;");

            char finalC = c;
            btn.setOnAction(e -> {
                btnChon.setText("Chữ: " + finalC);
                thucHienLocTheoChuCai(String.valueOf(finalC));
                popup.hide();
            });

            grid.add(btn, col, row);

            col++;
            if (col == 6) {  // 6 cột như lịch
                col = 0;
                row++;
            }
        }

        popup.getContent().add(grid);

        btnChon.setOnAction(e -> {
            if (!popup.isShowing()) {
                popup.show(btnChon,
                        btnChon.localToScreen(0, btnChon.getHeight()).getX(),
                        btnChon.localToScreen(0, btnChon.getHeight()).getY());
            } else {
                popup.hide();
            }
        });

        thanhTrai.getChildren().add(btnChon);
    }


    // Thêm lọc bằng ComboBox
    protected ComboBox<String> themComboLoc(List<String> options) {
        ComboBox<String> cbo = new ComboBox<>();
        cbo.getItems().addAll(options);
        cbo.setOnAction(e -> thucHienLocCombo(cbo.getValue()));

        thanhTrai.getChildren().add(cbo);
        return cbo;
    }

    // Tạo bảng chính
    protected abstract TableView<?> taoBangChinh();

    // Xử lý tìm kiếm
    protected abstract void thucHienTimKiem(String tuKhoa);

    // Làm mới dữ liệu
    protected abstract void lamMoiDuLieu();

    // Lọc theo chữ cái (class con override)
    protected void thucHienLocTheoChuCai(String chuCai) {}

    // Lọc ComboBox (class con override)
    protected void thucHienLocCombo(String giaTri) {}
}

