package com.thefourrestaurant.view.thucdon;

import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.NavBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;

import java.net.URL;

public class ThucDon extends VBox {

    private TableView<Object> tableThucDon;

    public ThucDon() {
        this.setAlignment(Pos.TOP_CENTER);

        // === NavBar ===
        NavBar navBar = new NavBar(this);
        navBar.setPrefHeight(80);
        navBar.setMinHeight(80);

        // === Content Pane ===
        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        // === Phần trên (Breadcrumb) ===
        Label duongDan = new Label("Quản Lý > Thực Đơn");
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox phanTren = new VBox(duongDan);
        phanTren.setStyle("-fx-background-color: #673E1F;");
        phanTren.setAlignment(Pos.CENTER_LEFT);
        phanTren.setPadding(new Insets(10, 20, 10, 20));
        phanTren.setPrefHeight(40);
        phanTren.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(phanTren, Priority.ALWAYS);
        contentPane.add(phanTren, 0, 0);

        // === Phần giữa (Toolbar) ===
        HBox phanGiua = new HBox(10);
        phanGiua.setPadding(new Insets(10, 20, 10, 20));
        phanGiua.setAlignment(Pos.CENTER_LEFT);
        phanGiua.setStyle("-fx-background-color: #1E424D;");
        ButtonSample btnTaoThucDon = new ButtonSample("Tạo Thực Đơn", 45, 16, 3);
        phanGiua.getChildren().add(btnTaoThucDon);
        contentPane.add(phanGiua, 0, 1);

        btnTaoThucDon.setOnAction(event -> {
            Pane parent = (Pane) this.getParent();
            if (parent != null) {
                int index = parent.getChildren().indexOf(this);
                if (index != -1) {
                    GiaoDienTaoThucDon newView = new GiaoDienTaoThucDon();
                    // Đảm bảo view mới cũng sẽ co giãn để lấp đầy không gian
                    HBox.setHgrow(newView, Priority.ALWAYS);
                    parent.getChildren().set(index, newView);
                }
            }
        });

        // === Phần dưới (Bảng) ===
        VBox phanDuoi = new VBox();
        phanDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        phanDuoi.setPadding(new Insets(20));
        GridPane.setMargin(phanDuoi, new Insets(10, 10, 10, 10));
        contentPane.add(phanDuoi, 0, 2);
        GridPane.setHgrow(phanDuoi, Priority.ALWAYS);
        GridPane.setVgrow(phanDuoi, Priority.ALWAYS);

        tableThucDon = taoBangThucDon();
        VBox.setVgrow(tableThucDon, Priority.ALWAYS);
        phanDuoi.getChildren().add(tableThucDon);

        // === Load CSS ===
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }

        this.getChildren().addAll(navBar, contentPane);
    }

    private TableView<Object> taoBangThucDon() {
        TableView<Object> table = new TableView<>();

        TableColumn<Object, String> tenCol = new TableColumn<>("Tên");
        TableColumn<Object, String> loaiMonAnCol = new TableColumn<>("Các Loại Món Ăn");

        table.getColumns().addAll(tenCol, loaiMonAnCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }
}
