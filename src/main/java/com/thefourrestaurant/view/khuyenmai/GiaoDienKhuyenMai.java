package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.DropDownButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiaoDienKhuyenMai extends VBox {

    private final KhuyenMaiController controller;
    private final TableView<KhuyenMai> tableView;

    public GiaoDienKhuyenMai() {
        this.controller = new KhuyenMaiController();
        this.tableView = createTableView();

        this.setAlignment(Pos.TOP_CENTER);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        // Top Bar (Breadcrumb)
        contentPane.add(createTopBar(), 0, 0);

        // Middle Bar (Controls)
        contentPane.add(createMiddleBar(), 0, 1);

        // Main Content Area
        VBox mainContentContainer = new VBox();
        mainContentContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        mainContentContainer.setPadding(new Insets(20));
        GridPane.setMargin(mainContentContainer, new Insets(10, 10, 10, 10));
        VBox.setVgrow(tableView, Priority.ALWAYS);
        mainContentContainer.getChildren().add(tableView);
        contentPane.add(mainContentContainer, 0, 2);
        GridPane.setHgrow(mainContentContainer, Priority.ALWAYS);
        GridPane.setVgrow(mainContentContainer, Priority.ALWAYS);

        this.getChildren().add(contentPane);

        refreshTable();
    }

    private VBox createTopBar() {
        Label duongDan = new Label("Quản Lý > Khuyến Mãi");
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungTren = new VBox(duongDan);
        khungTren.setStyle("-fx-background-color: #673E1F;");
        khungTren.setAlignment(Pos.CENTER_LEFT);
        khungTren.setPadding(new Insets(0, 20, 0, 20));
        khungTren.setPrefHeight(30);
        khungTren.setMinHeight(30);
        khungTren.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(khungTren, Priority.ALWAYS);
        return khungTren;
    }

    private HBox createMiddleBar() {
        HBox khungGiua = new HBox(10);
        khungGiua.setPadding(new Insets(10, 20, 10, 20));
        khungGiua.setAlignment(Pos.CENTER_LEFT);
        khungGiua.setStyle("-fx-background-color: #1E424D;");

        ButtonSample themButton = new ButtonSample("Thêm Khuyến Mãi", "", 35, 14, 3);
        themButton.setOnAction(e -> {
            if (controller.themMoiKhuyenMai()) {
                refreshTable();
            }
        });

        Label lblSapXep = new Label("Sắp xếp:");
        lblSapXep.setTextFill(Color.web("#E5D595"));
        lblSapXep.setFont(Font.font("System", FontWeight.BOLD, 14));

        DropDownButton btnTheoNgay = new DropDownButton("Theo ngày bắt đầu ▼", List.of("Mới nhất", "Cũ nhất"), null, 35, 16, 3);
        ButtonSample btnApDung = new ButtonSample("Áp dụng", "", 35, 13, 3);

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm theo mã hoặc mô tả...");
        txtTimKiem.setPrefWidth(300);

        ButtonSample btnTim = new ButtonSample("Tìm", "", 35, 13, 3);

        khungGiua.getChildren().addAll(themButton, lblSapXep, btnTheoNgay, btnApDung, space, txtTimKiem, btnTim);
        return khungGiua;
    }

    private TableView<KhuyenMai> createTableView() {
        TableView<KhuyenMai> table = new TableView<>();

        TableColumn<KhuyenMai, String> maKMCol = new TableColumn<>("Mã KM");
        maKMCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaKM()));

        TableColumn<KhuyenMai, String> moTaCol = new TableColumn<>("Mô Tả");
        moTaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMoTa()));
        moTaCol.setPrefWidth(250);

        TableColumn<KhuyenMai, String> loaiKMCol = new TableColumn<>("Loại KM");
        loaiKMCol.setCellValueFactory(cellData -> {
            String maLoaiKM = cellData.getValue().getMaLoaiKM();
            return new SimpleStringProperty(maLoaiKM);
        });

        TableColumn<KhuyenMai, String> giaTriCol = new TableColumn<>("Giá Trị");
        giaTriCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            String giaTri = "";
            if (km.getTyLe() != null) {
                giaTri = km.getTyLe() + " %";
            } else if (km.getSoTien() != null) {
                giaTri = km.getSoTien().toPlainString() + " VND";
            } else if (km.getMaMonTang() != null) {
                giaTri = "Tặng " + km.getMaMonTang();
            }
            return new SimpleStringProperty(giaTri);
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TableColumn<KhuyenMai, String> ngayBDCol = new TableColumn<>("Ngày Bắt Đầu");
        ngayBDCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getNgayBatDau();
            return new SimpleStringProperty(date == null ? "" : date.format(formatter));
        });

        TableColumn<KhuyenMai, String> ngayKTCol = new TableColumn<>("Ngày Kết Thúc");
        ngayKTCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getNgayKetThuc();
            return new SimpleStringProperty(date == null ? "" : date.format(formatter));
        });

        table.getColumns().addAll(maKMCol, moTaCol, loaiKMCol, giaTriCol, ngayBDCol, ngayKTCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        table.setRowFactory(tv -> {
            TableRow<KhuyenMai> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("Sửa");
            editItem.setOnAction(event -> {
                KhuyenMai selectedItem = row.getItem();
                if (selectedItem != null && controller.tuyChinhKhuyenMai(selectedItem)) {
                    refreshTable();
                }
            });

            MenuItem deleteItem = new MenuItem("Xóa");
            deleteItem.setOnAction(event -> {
                KhuyenMai selectedItem = row.getItem();
                if (selectedItem != null && controller.xoaKhuyenMai(selectedItem)) {
                    refreshTable();
                }
            });

            contextMenu.getItems().addAll(editItem, new SeparatorMenuItem(), deleteItem);

            row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    row.setContextMenu(null);
                } else {
                    row.setContextMenu(contextMenu);
                }
            });
            return row;
        });

        return table;
    }

    private void refreshTable() {
        List<KhuyenMai> data = controller.getAllKhuyenMai();
        tableView.setItems(FXCollections.observableArrayList(data));
    }
}
