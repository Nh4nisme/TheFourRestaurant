package com.thefourrestaurant.view.monan;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thefourrestaurant.controller.MonAnController;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.DropDownButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class GiaoDienMonAn extends VBox {

    private final String categoryName;
    private final String categoryIcon;
    private final MonAnController controller;

    private VBox dsMonAnContainer; // Container for grid/list view
    private Node gridView;
    private Node listView;

    private List<Map<String, String>> danhSachMonAn;
    private final int soCotMoiHang = 8;

    public GiaoDienMonAn(String categoryName, String categoryIcon) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.controller = new MonAnController();
        
        khoiTaoDuLieuGia();

        this.setAlignment(Pos.TOP_CENTER);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        // Top Bar (Breadcrumb)
        Label duongDan = new Label("Quản Lý > Món Ăn > " + categoryName);
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

        // Middle Bar (Controls)
        HBox khungGiua = taoKhungGiua();
        GridPane.setHgrow(khungGiua, Priority.ALWAYS);
        contentPane.add(khungGiua, 0, 1);
        khungGiua.setPrefHeight(60);
        khungGiua.setMinHeight(60);

        // Bottom container
        VBox khungDuoi = new VBox();
        khungDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khungDuoi.setAlignment(Pos.CENTER);
        khungDuoi.setPadding(new Insets(20));
        GridPane.setMargin(khungDuoi, new Insets(10, 10, 10, 10));
        contentPane.add(khungDuoi, 0, 2);
        GridPane.setHgrow(khungDuoi, Priority.ALWAYS);
        GridPane.setVgrow(khungDuoi, Priority.ALWAYS);

        dsMonAnContainer = new VBox(20);
        dsMonAnContainer.setStyle("-fx-background-color: #F0F2F3; -fx-background-radius: 10;");
        dsMonAnContainer.setAlignment(Pos.TOP_CENTER);
        dsMonAnContainer.setPadding(new Insets(20));
        khungDuoi.getChildren().add(dsMonAnContainer);
        VBox.setVgrow(dsMonAnContainer, Priority.ALWAYS);

        // Initialize the views
        gridView = createGridView();
        listView = createListView();

        // Set default view
        dsMonAnContainer.getChildren().add(gridView);

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }

        this.getChildren().addAll(contentPane);
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
            if (!dsMonAnContainer.getChildren().contains(listView)) {
                dsMonAnContainer.getChildren().setAll(listView);
            }
        });

        btnGrid.setOnAction(event -> {
            if (!dsMonAnContainer.getChildren().contains(gridView)) {
                dsMonAnContainer.getChildren().setAll(gridView);
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

    private Node createGridView() {
        VBox gridContainer = new VBox(20);
        VBox.setVgrow(gridContainer, Priority.ALWAYS);

        GridPane luoiCacMonAn = new GridPane();
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

        VBox hopThemMoi = MonAnBox.createThemMoiBox();

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
                capNhatLuoiMonAn(luoiCacMonAn);
            }
        });

        hopThemMoi.setOnMouseClicked(event -> themMoiButton.fire());

        luoiThemMon.add(hopThemMoi, 0, 0);
        gridContainer.getChildren().add(themMoiButton);

        capNhatLuoiMonAn(luoiCacMonAn);

        gridContainer.getChildren().addAll(luoiThemMon, scrollPane);
        return gridContainer;
    }

    private Node createListView() {
        TableView<Object> table = new TableView<>();
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Object, Boolean> checkBoxCol = new TableColumn<>("☑️");
        TableColumn<Object, String> danhMucCol = new TableColumn<>(categoryIcon + " Danh mục");
        TableColumn<Object, String> maMonCol = new TableColumn<>("Mã món");
        TableColumn<Object, String> tenMonAnCol = new TableColumn<>("Tên món ăn");
        TableColumn<Object, Double> donGiaCol = new TableColumn<>("Đơn giá (VND)");
        TableColumn<Object, Double> thueCol = new TableColumn<>("Thuế (%)");
        TableColumn<Object, String> trangThaiCol = new TableColumn<>("Trạng thái");
        TableColumn<Object, Integer> soLuongCol = new TableColumn<>("Số lượng");

        table.getColumns().addAll(
                checkBoxCol,
                danhMucCol,
                maMonCol,
                tenMonAnCol,
                donGiaCol,
                thueCol,
                trangThaiCol,
                soLuongCol
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // TODO: Populate table with data from danhSachMonAn
        return table;
    }

    private void khoiTaoDuLieuGia() {
        danhSachMonAn = new ArrayList<>();
        int basePrice = categoryName.equals("Bún") ? 25 : 30;
        for (int i = 1; i <= 50; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("name", categoryName + " " + i);
            item.put("price", (basePrice + i) + ",000");
            item.put("imagePath", null);
            danhSachMonAn.add(item);
        }
    }

    private void capNhatLuoiMonAn(GridPane luoiCacMonAn) {
        if (luoiCacMonAn == null) {
			return;
		}
        luoiCacMonAn.getChildren().clear();

        for (int i = 0; i < danhSachMonAn.size(); i++) {
            Map<String, String> item = danhSachMonAn.get(i);
            MonAnBox hopMonAn = new MonAnBox(item.get("name"), item.get("price"), item.get("imagePath"));

            hopMonAn.setOnMouseClicked(event -> {
                MonAnDialog tuyChinh = new MonAnDialog(item);
                tuyChinh.showAndWait();

                Map<String, Object> ketQua = tuyChinh.layKetQua();
                if (ketQua != null) {
                    item.put("name", (String) ketQua.get("ten"));
                    item.put("price", (String) ketQua.get("gia"));
                    item.put("imagePath", (String) ketQua.get("imagePath"));
                    capNhatLuoiMonAn(luoiCacMonAn);
                }
            });
            int col = i % soCotMoiHang;
            int row = i / soCotMoiHang;

            luoiCacMonAn.add(hopMonAn, col, row);
        }
    }
}
