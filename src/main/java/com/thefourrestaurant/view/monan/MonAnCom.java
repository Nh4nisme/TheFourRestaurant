package com.thefourrestaurant.view.monan;

import java.net.URL;
import java.util.List;

import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.DropDownButton;
import com.thefourrestaurant.view.components.NavBar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonAnCom extends VBox {

    private VBox dsMonAn; // Container for grid/list view
    private Node gridView;
    private Node listView;

    public MonAnCom() {
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

        dsMonAn = new VBox(20);
        dsMonAn.setStyle("-fx-background-color: #F0F2F3; -fx-background-radius: 10;");
        dsMonAn.setAlignment(Pos.TOP_CENTER);
        dsMonAn.setPadding(new Insets(20));
        khungDuoi.getChildren().add(dsMonAn);
        VBox.setVgrow(dsMonAn, Priority.ALWAYS);

        // Initialize the views
        gridView = new MonAnComGrid();
        listView = new MonAnComList();

        // Set default view
        dsMonAn.getChildren().add(gridView);

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }

        this.getChildren().addAll(navBar, contentPane);
    }

    private HBox taoKhungGiua() {
        HBox khungGiua = new HBox(10);
        khungGiua.setPadding(new Insets(10, 20, 10, 20));
        khungGiua.setAlignment(Pos.CENTER_LEFT);
        khungGiua.setStyle("-fx-background-color: #1E424D;");

        ImageView iconList = new ImageView(getClass().getResource("/com/thefourrestaurant/images/icon/List.png").toExternalForm());
        ImageView iconGrid = new ImageView(getClass().getResource("/com/thefourrestaurant/images/icon/Grid.png").toExternalForm());
        iconList.setFitWidth(20);
        iconList.setFitHeight(20);
        iconGrid.setFitWidth(20);
        iconGrid.setFitHeight(20);

        ButtonSample btnList = new ButtonSample("", "", 35, 16, 3);
        ButtonSample btnGrid = new ButtonSample("", "", 35, 16, 3);
        btnList.setGraphic(iconList);
        btnGrid.setGraphic(iconGrid);
        btnList.setPrefSize(35, 35);
        btnGrid.setPrefSize(35, 35);

        btnList.setOnAction(event -> {
            if (!dsMonAn.getChildren().contains(listView)) {
                dsMonAn.getChildren().setAll(listView);
            }
        });

        btnGrid.setOnAction(event -> {
            if (!dsMonAn.getChildren().contains(gridView)) {
                dsMonAn.getChildren().setAll(gridView);
            }
        });

        Label lblSapXep = new Label("Sắp xếp:");
        lblSapXep.setTextFill(Color.web("#E5D595"));
        lblSapXep.setFont(Font.font("System", FontWeight.BOLD, 14));

        DropDownButton btnTheoChuCai = new DropDownButton("Theo bảng chữ cái  ▼", List.of("A → Z", "Z → A"), null, 35, 16, 3);
        DropDownButton btnTheoGia = new DropDownButton("Theo giá  ▼", List.of("Tăng dần", "Giảm dần"), null, 35, 16, 3);
        ButtonSample btnApDung = new ButtonSample("Áp dụng", "", 35, 13, 3);

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm...");
        txtTimKiem.setPrefWidth(300);
        txtTimKiem.setStyle("-fx-background-radius: 8;");

        ButtonSample btnTim = new ButtonSample("Tìm", "", 35, 13, 3);

        khungGiua.getChildren().addAll(btnList, btnGrid, lblSapXep, btnTheoChuCai, btnTheoGia, btnApDung, space, txtTimKiem, btnTim);

        return khungGiua;
    }
}
