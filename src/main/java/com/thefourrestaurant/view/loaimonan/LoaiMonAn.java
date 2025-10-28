package com.thefourrestaurant.view.loaimonan;

import com.thefourrestaurant.controller.LoaiMonAnController;
import com.thefourrestaurant.model.LoaiMon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class LoaiMonAn extends VBox {

    private final LoaiMonAnController controller;
    private List<LoaiMon> danhSachLoaiMonAn;
    private final GridPane gridPane = new GridPane();
    private final int soCotMoiHang = 8;

    public LoaiMonAn() {
        this.controller = new LoaiMonAnController();

        this.setAlignment(Pos.TOP_CENTER);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        // Top Bar (Breadcrumb)
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

        // Main Content Area
        VBox khungDuoi = new VBox();
        khungDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khungDuoi.setAlignment(Pos.CENTER);
        khungDuoi.setPadding(new Insets(20));
        GridPane.setMargin(khungDuoi, new Insets(10, 10, 10, 10));
        contentPane.add(khungDuoi, 0, 1);
        GridPane.setHgrow(khungDuoi, Priority.ALWAYS);
        GridPane.setVgrow(khungDuoi, Priority.ALWAYS);

        VBox dsLoaiMonAnContainer = new VBox(20);
        dsLoaiMonAnContainer.setStyle("-fx-background-color: #F0F2F3; -fx-background-radius: 10;");
        dsLoaiMonAnContainer.setAlignment(Pos.TOP_CENTER);
        dsLoaiMonAnContainer.setPadding(new Insets(20));
        khungDuoi.getChildren().add(dsLoaiMonAnContainer);
        VBox.setVgrow(dsLoaiMonAnContainer, Priority.ALWAYS);

        // Grid for items
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // "Add New" Box
        VBox hopThemMoi = LoaiMonAnBox.createThemMoiBox();
        GridPane luoiThem = new GridPane();
        luoiThem.setAlignment(Pos.BASELINE_LEFT);
        luoiThem.setPadding(new Insets(0,0,0,15));
        luoiThem.add(hopThemMoi, 0, 0);

        Button themMoiButton = new Button(); // Hidden button to trigger action
        themMoiButton.setVisible(false);
        themMoiButton.setManaged(false);
        themMoiButton.setOnAction(event -> {
            Stage owner = (Stage) getScene().getWindow();
            if (controller.themMoiLoaiMonAn(owner)) {
                refreshGrid();
            }
        });
        hopThemMoi.setOnMouseClicked(event -> themMoiButton.fire());

        dsLoaiMonAnContainer.getChildren().addAll(luoiThem, scrollPane, themMoiButton);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Load CSS
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        }

        this.getChildren().add(contentPane);

        // Initial data load
        refreshGrid();
    }

    private void refreshGrid() {
        this.danhSachLoaiMonAn = controller.layTatCaLoaiMonAn();
        gridPane.getChildren().clear();

        for (int i = 0; i < danhSachLoaiMonAn.size(); i++) {
            LoaiMon item = danhSachLoaiMonAn.get(i);
            VBox hopLoaiMonAn = LoaiMonAnBox.createLoaiMonAnBox(item.getTenLoaiMon(), item.getHinhAnh());

            // --- Event Handlers for each box ---
            ContextMenu contextMenu = createContextMenu(item);
            hopLoaiMonAn.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Stage owner = (Stage) getScene().getWindow();
                    if (controller.tuyChinhLoaiMonAn(owner, item)) {
                        refreshGrid();
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(hopLoaiMonAn, event.getScreenX(), event.getScreenY());
                }
            });

            int col = i % soCotMoiHang;
            int row = i / soCotMoiHang;
            gridPane.add(hopLoaiMonAn, col, row);
        }
    }

    private ContextMenu createContextMenu(LoaiMon loaiMon) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Sửa");
        editItem.setOnAction(e -> {
            Stage owner = (Stage) contextMenu.getOwnerNode().getScene().getWindow();
            if (controller.tuyChinhLoaiMonAn(owner, loaiMon)) {
                refreshGrid();
            }
        });

        MenuItem deleteItem = new MenuItem("Xóa");
        deleteItem.setOnAction(e -> {
            Stage owner = (Stage) contextMenu.getOwnerNode().getScene().getWindow();
            if (controller.xoaLoaiMonAn(owner, loaiMon)) {
                refreshGrid();
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        return contextMenu;
    }
}
