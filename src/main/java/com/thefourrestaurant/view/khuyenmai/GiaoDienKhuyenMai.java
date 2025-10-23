package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GiaoDienKhuyenMai extends VBox {

    private final KhuyenMaiController controller;
    private List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
    private final TableView<KhuyenMai> listViewPane = new TableView<>();
    private final TableView<ChiTietKhuyenMai> chiTietListViewPane = new TableView<>();

    public GiaoDienKhuyenMai() {
        this.controller = new KhuyenMaiController();
        this.setAlignment(Pos.TOP_CENTER);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        contentPane.add(createTopBar(), 0, 0);
        contentPane.add(createMiddleBar(), 0, 1);

        // Main content area with SplitPane
        SplitPane mainSplitPane = new SplitPane();
        mainSplitPane.setDividerPositions(0.6f); // 60% for KhuyenMai, 40% for ChiTietKhuyenMai
        GridPane.setMargin(mainSplitPane, new Insets(10, 10, 10, 10));
        GridPane.setHgrow(mainSplitPane, Priority.ALWAYS);
        GridPane.setVgrow(mainSplitPane, Priority.ALWAYS);
        contentPane.add(mainSplitPane, 0, 2);

        // Left side: KhuyenMai Table
        VBox khuyenMaiTableContainer = new VBox(10);
        khuyenMaiTableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khuyenMaiTableContainer.setPadding(new Insets(20));
        khuyenMaiTableContainer.getChildren().add(listViewPane);
        VBox.setVgrow(listViewPane, Priority.ALWAYS);

        // Right side: ChiTietKhuyenMai Table
        VBox chiTietKhuyenMaiTableContainer = new VBox(10);
        chiTietKhuyenMaiTableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        chiTietKhuyenMaiTableContainer.setPadding(new Insets(20));
        chiTietKhuyenMaiTableContainer.getChildren().add(new Label("Chi tiết khuyến mãi")); // Title for the detail table
        chiTietKhuyenMaiTableContainer.getChildren().add(chiTietListViewPane);
        VBox.setVgrow(chiTietListViewPane, Priority.ALWAYS);

        mainSplitPane.getItems().addAll(khuyenMaiTableContainer, chiTietKhuyenMaiTableContainer);

        setupKhuyenMaiTableView();
        setupChiTietKhuyenMaiTableView();

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        }

        this.getChildren().add(contentPane);
        refreshView();
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
        GridPane.setHgrow(khungTren, Priority.ALWAYS);
        return khungTren;
    }

    private HBox createMiddleBar() {
        HBox khungGiua = new HBox(10);
        khungGiua.setPadding(new Insets(10, 20, 10, 20));
        khungGiua.setAlignment(Pos.CENTER_LEFT);
        khungGiua.setStyle("-fx-background-color: #1E424D;");

        ButtonSample btnThemMoi = new ButtonSample("Thêm khuyến mãi", "", 35, 14, 3);
        btnThemMoi.setOnAction(e -> {
            if (controller.themMoiKhuyenMai()) {
                refreshView();
            }
        });

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm theo mã, mô tả...");
        txtTimKiem.setPrefWidth(300);

        ButtonSample btnTim = new ButtonSample("Tìm", "", 35, 13, 3);
        btnTim.setOnAction(e -> {
            // Logic tìm kiếm sẽ được thêm vào đây sau
        });

        khungGiua.getChildren().addAll(btnThemMoi, space, txtTimKiem, btnTim);
        return khungGiua;
    }

    private void setupKhuyenMaiTableView() {
        VBox.setVgrow(listViewPane, Priority.ALWAYS);

        // maKM
        TableColumn<KhuyenMai, String> maKMCol = new TableColumn<>("Mã KM");
        maKMCol.setCellValueFactory(new PropertyValueFactory<>("maKM"));
        maKMCol.setPrefWidth(80);

        // moTa
        TableColumn<KhuyenMai, String> moTaCol = new TableColumn<>("Mô tả");
        moTaCol.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        moTaCol.setPrefWidth(200);

        // maLoaiKM (hiển thị tên loại)
        TableColumn<KhuyenMai, String> loaiKMCol = new TableColumn<>("Loại KM");
        loaiKMCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getLoaiKhuyenMai() != null) {
                return new SimpleStringProperty(cellData.getValue().getLoaiKhuyenMai().getTenLoaiKM());
            }
            return new SimpleStringProperty("");
        });
        loaiKMCol.setPrefWidth(120);

        // tyLe
        TableColumn<KhuyenMai, String> tyLeCol = new TableColumn<>("Tỷ lệ");
        tyLeCol.setCellValueFactory(cellData -> {
            BigDecimal tyLe = cellData.getValue().getTyLe();
            return new SimpleStringProperty(tyLe != null && tyLe.compareTo(BigDecimal.ZERO) > 0 ? tyLe.stripTrailingZeros().toPlainString() + "%" : "");
        });
        tyLeCol.setPrefWidth(80);

        // soTien
        TableColumn<KhuyenMai, String> soTienCol = new TableColumn<>("Số tiền");
        soTienCol.setCellValueFactory(cellData -> {
            BigDecimal soTien = cellData.getValue().getSoTien();
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            return new SimpleStringProperty(soTien != null && soTien.compareTo(BigDecimal.ZERO) > 0 ? currencyFormatter.format(soTien) : "");
        });
        soTienCol.setPrefWidth(120);

        // ngayBatDau
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        TableColumn<KhuyenMai, String> ngayBDCol = new TableColumn<>("Ngày BĐ");
        ngayBDCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getNgayBatDau();
            return new SimpleStringProperty(date != null ? date.format(dateFormatter) : "");
        });
        ngayBDCol.setPrefWidth(100);

        // ngayKetThuc
        TableColumn<KhuyenMai, String> ngayKTCol = new TableColumn<>("Ngày KT");
        ngayKTCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getNgayKetThuc();
            return new SimpleStringProperty(date != null ? date.format(dateFormatter) : "");
        });
        ngayKTCol.setPrefWidth(100);

        // Trạng thái (derived)
        TableColumn<KhuyenMai, String> trangThaiCol = new TableColumn<>("Trạng thái");
        trangThaiCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            LocalDate now = LocalDate.now();
            String status = "Chưa áp dụng";
            if (km.getNgayBatDau() != null && km.getNgayKetThuc() != null) {
                if (now.isAfter(km.getNgayKetThuc())) {
                    status = "Đã hết hạn";
                } else if (now.isBefore(km.getNgayBatDau())) {
                    status = "Sắp diễn ra";
                } else {
                    status = "Đang diễn ra";
                }
            }
            return new SimpleStringProperty(status);
        });
        trangThaiCol.setPrefWidth(100);


        listViewPane.getColumns().addAll(maKMCol, moTaCol, loaiKMCol, tyLeCol, soTienCol, ngayBDCol, ngayKTCol, trangThaiCol);
        listViewPane.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        listViewPane.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                refreshChiTietKhuyenMaiTable(newSelection.getMaKM());
            } else {
                chiTietListViewPane.getItems().clear(); // Clear details if no KhuyenMai is selected
            }
        });

        listViewPane.setRowFactory(tv -> {
            TableRow<KhuyenMai> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    KhuyenMai clickedRow = row.getItem();
                    if (controller.tuyChinhKhuyenMai(clickedRow)) {
                        refreshView();
                    }
                }
            });

            ContextMenu contextMenu = createContextMenu(row);
            row.contextMenuProperty().bind(
                row.emptyProperty().map(empty -> empty ? null : contextMenu)
            );
            return row;
        });
    }

    private void setupChiTietKhuyenMaiTableView() {
        VBox.setVgrow(chiTietListViewPane, Priority.ALWAYS);

        TableColumn<ChiTietKhuyenMai, String> maCTKMCol = new TableColumn<>("Mã CTKM");
        maCTKMCol.setCellValueFactory(new PropertyValueFactory<>("maCTKM"));
        maCTKMCol.setPrefWidth(80);

        TableColumn<ChiTietKhuyenMai, String> monApDungCol = new TableColumn<>("Món áp dụng");
        monApDungCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getMonApDung() != null) {
                return new SimpleStringProperty(cellData.getValue().getMonApDung().getTenMon());
            }
            return new SimpleStringProperty("");
        });
        monApDungCol.setPrefWidth(120);

        TableColumn<ChiTietKhuyenMai, String> monTangCol = new TableColumn<>("Món tặng");
        monTangCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getMonTang() != null) {
                return new SimpleStringProperty(cellData.getValue().getMonTang().getTenMon());
            }
            return new SimpleStringProperty("");
        });
        monTangCol.setPrefWidth(120);

        // Combined discount value column
        TableColumn<ChiTietKhuyenMai, String> giaTriGiamCol = new TableColumn<>("Giá trị giảm");
        giaTriGiamCol.setCellValueFactory(cellData -> {
            BigDecimal tyLe = cellData.getValue().getTyLeGiam();
            BigDecimal soTien = cellData.getValue().getSoTienGiam();
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            if (tyLe != null && tyLe.compareTo(BigDecimal.ZERO) > 0) {
                return new SimpleStringProperty(tyLe.stripTrailingZeros().toPlainString() + "%");
            } else if (soTien != null && soTien.compareTo(BigDecimal.ZERO) >= 0) { // Changed > to >= here
                return new SimpleStringProperty(soTien.stripTrailingZeros().toPlainString() + " VND");
            } else {
                return new SimpleStringProperty("");
            }
        });
        giaTriGiamCol.setPrefWidth(120);

        TableColumn<ChiTietKhuyenMai, String> soLuongTangCol = new TableColumn<>("SL tặng");
        soLuongTangCol.setCellValueFactory(cellData -> {
            Integer soLuong = cellData.getValue().getSoLuongTang();
            return new SimpleStringProperty(soLuong != null && soLuong > 0 ? String.valueOf(soLuong) : "");
        });
        soLuongTangCol.setPrefWidth(60);

        chiTietListViewPane.getColumns().addAll(maCTKMCol, monApDungCol, monTangCol, giaTriGiamCol, soLuongTangCol);
        chiTietListViewPane.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshView() {
        this.danhSachKhuyenMai = controller.layTatCaKhuyenMai();
        listViewPane.setItems(FXCollections.observableArrayList(danhSachKhuyenMai));
        listViewPane.refresh();
        chiTietListViewPane.getItems().clear(); // Clear details when main table refreshes
    }

    private void refreshChiTietKhuyenMaiTable(String maKM) {
        List<ChiTietKhuyenMai> chiTietList = controller.layChiTietKhuyenMaiTheoMaKM(maKM);
        chiTietListViewPane.setItems(FXCollections.observableArrayList(chiTietList));
        chiTietListViewPane.refresh();
    }

    private ContextMenu createContextMenu(TableRow<KhuyenMai> row) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Sửa");
        editItem.setOnAction(e -> {
            KhuyenMai selectedItem = row.getItem();
            if (selectedItem != null && controller.tuyChinhKhuyenMai(selectedItem)) {
                refreshView();
            }
        });

        MenuItem deleteItem = new MenuItem("Xóa");
        deleteItem.setOnAction(e -> {
            KhuyenMai selectedItem = row.getItem();
            if (selectedItem != null && controller.xoaKhuyenMai(selectedItem)) {
                refreshView();
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        return contextMenu;
    }
}
