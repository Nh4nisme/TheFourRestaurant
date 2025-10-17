package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class GiaoDienThucThe extends VBox {

    protected TableView<?> tableChinh;
    protected Node chiTietNode;
    protected Label lblTieuDe;
    protected ButtonSample btnTimKiem, btnLamMoi;
    protected TextField txtTimKiem;

    public GiaoDienThucThe(String tieuDe, Node chiTietNode) {
        this.chiTietNode = chiTietNode;
        this.getStyleClass().add("giao-dien-co-chi-tiet");
        setVgrow(this, Priority.ALWAYS);

        getStylesheets().add(getClass().getResource("/com/thefourrestaurant/css/Application.css").toExternalForm());
        this.getStyleClass().add("giaodienthucthe");

        // === NavBar ===
        NavBar navBar = new NavBar(this);

        // === Toolbar ===
        HBox toolbar = taoToolbar(tieuDe);

        // === SplitPane ===
        SplitPane splitPane = taoSplitPane();

        VBox.setVgrow(splitPane, Priority.ALWAYS);
        getChildren().addAll(navBar, toolbar, splitPane);
    }

    /** Tạo Toolbar chung */
    private HBox taoToolbar(String tieuDe) {
        HBox toolbar = new HBox();
        toolbar.getStyleClass().add("toolbar");
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10));

        lblTieuDe = new Label(tieuDe);
        lblTieuDe.getStyleClass().add("toolbar-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox thanhTimKiem = new HBox(10); // khoảng cách giữa các phần tử = 10px
        thanhTimKiem.setAlignment(Pos.CENTER_LEFT);
        thanhTimKiem.setPadding(new Insets(0,10,0,10));
        txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Nhập từ khóa tìm kiếm...");
        txtTimKiem.setPrefHeight(45);
        txtTimKiem.setStyle("-fx-font-size: 16; -fx-background-radius: 8;");

        btnTimKiem = new ButtonSample("Tìm kiếm", 45, 16, 3);

        thanhTimKiem.getChildren().addAll(txtTimKiem, btnTimKiem);

        btnLamMoi = new ButtonSample("Làm mới",45,16,3);

        toolbar.getChildren().addAll(lblTieuDe, spacer, thanhTimKiem,btnLamMoi);
        return toolbar;
    }

    /** Tạo SplitPane chứa bảng và chi tiết */
    private SplitPane taoSplitPane() {
        SplitPane splitPane = new SplitPane();
        splitPane.setPadding(new Insets(10));
        splitPane.setDividerPositions(0.6);
        splitPane.getStyleClass().add("split-pane");

        // Bảng chính
        VBox bangDuLieu = new VBox();
        tableChinh = taoBangChinh();
        VBox.setVgrow(tableChinh, Priority.ALWAYS);
        bangDuLieu.getChildren().add(tableChinh);

        // Chi tiết
        VBox bangChiTiet = new VBox();
        bangChiTiet.getChildren().add(chiTietNode);
        VBox.setVgrow(bangChiTiet, Priority.ALWAYS);

        splitPane.getItems().addAll(bangDuLieu, bangChiTiet);
        return splitPane;
    }

    /** Hàm con cần override để tạo bảng tương ứng */
    protected abstract TableView<?> taoBangChinh();
}
