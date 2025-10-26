package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GiaoDienKhuyenMai extends VBox {

    private final KhuyenMaiController boDieuKhien;
    private List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
    private final TableView<KhuyenMai> bangKhuyenMai = new TableView<>();
    private final TableView<ChiTietKhuyenMai> bangChiTietKhuyenMai = new TableView<>();

    private VBox khuyenMaiViewContainer;
    private KhuyenMaiGrid gridView;
    private Node listView;

    public GiaoDienKhuyenMai() {
        this.boDieuKhien = new KhuyenMaiController();
        this.setAlignment(Pos.TOP_CENTER);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        contentPane.add(taoKhungTren(), 0, 0);
        contentPane.add(taoKhungGiua(), 0, 1);

        SplitPane mainSplitPane = new SplitPane();
        mainSplitPane.setDividerPositions(0.6f);
        GridPane.setMargin(mainSplitPane, new Insets(10, 10, 10, 10));
        GridPane.setHgrow(mainSplitPane, Priority.ALWAYS);
        GridPane.setVgrow(mainSplitPane, Priority.ALWAYS);
        contentPane.add(mainSplitPane, 0, 2);

        VBox khuyenMaiTableContainer = new VBox(10);
        khuyenMaiTableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khuyenMaiTableContainer.setPadding(new Insets(20));

        khuyenMaiViewContainer = new VBox();
        VBox.setVgrow(khuyenMaiViewContainer, Priority.ALWAYS);
        khuyenMaiTableContainer.getChildren().add(khuyenMaiViewContainer);

        VBox chiTietKhuyenMaiTableContainer = new VBox(10);
        chiTietKhuyenMaiTableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        chiTietKhuyenMaiTableContainer.setPadding(new Insets(20));
        chiTietKhuyenMaiTableContainer.getChildren().add(new Label("Chi tiết khuyến mãi"));
        chiTietKhuyenMaiTableContainer.getChildren().add(bangChiTietKhuyenMai);
        VBox.setVgrow(bangChiTietKhuyenMai, Priority.ALWAYS);

        mainSplitPane.getItems().addAll(khuyenMaiTableContainer, chiTietKhuyenMaiTableContainer);

        caiDatBangKhuyenMai();
        caiDatBangChiTietKhuyenMai();

        listView = bangKhuyenMai;
        gridView = new KhuyenMaiGrid(this);

        khuyenMaiViewContainer.getChildren().add(gridView);

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        }

        this.getChildren().add(contentPane);
        lamMoiGiaoDien();
    }

    private VBox taoKhungTren() {
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
            if (!khuyenMaiViewContainer.getChildren().contains(listView)) {
                khuyenMaiViewContainer.getChildren().setAll(listView);
            }
        });

        btnGrid.setOnAction(event -> {
            if (!khuyenMaiViewContainer.getChildren().contains(gridView)) {
                khuyenMaiViewContainer.getChildren().setAll(gridView);
            }
        });

        ButtonSample btnThemMoi = new ButtonSample("Thêm khuyến mãi", "", 35, 14, 3);
        btnThemMoi.setOnAction(e -> {
            if (boDieuKhien.themKhuyenMaiMoi()) {
                lamMoiGiaoDien();
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

        khungGiua.getChildren().addAll(btnList, btnGrid, btnThemMoi, space, txtTimKiem, btnTim);
        return khungGiua;
    }

    private void caiDatBangKhuyenMai() {
        VBox.setVgrow(bangKhuyenMai, Priority.ALWAYS);

        TableColumn<KhuyenMai, String> maKMCol = new TableColumn<>("Mã KM");
        maKMCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaKM()));
        maKMCol.setPrefWidth(80);

        TableColumn<KhuyenMai, String> moTaCol = new TableColumn<>("Mô tả");
        moTaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMoTa()));
        moTaCol.setPrefWidth(200);

        TableColumn<KhuyenMai, String> loaiKMCol = new TableColumn<>("Loại KM");
        loaiKMCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getLoaiKhuyenMai() != null) {
                return new SimpleStringProperty(cellData.getValue().getLoaiKhuyenMai().getTenLoaiKM());
            }
            return new SimpleStringProperty("");
        });
        loaiKMCol.setPrefWidth(120);

        TableColumn<KhuyenMai, String> tyLeCol = new TableColumn<>("Tỷ lệ");
        tyLeCol.setCellValueFactory(cellData -> {
            BigDecimal tyLe = cellData.getValue().getTyLe();
            return new SimpleStringProperty(tyLe != null && tyLe.compareTo(BigDecimal.ZERO) > 0 ? tyLe.stripTrailingZeros().toPlainString() + "%" : "");
        });
        tyLeCol.setPrefWidth(80);

        TableColumn<KhuyenMai, String> soTienCol = new TableColumn<>("Số tiền");
        soTienCol.setCellValueFactory(cellData -> {
            BigDecimal soTien = cellData.getValue().getSoTien();
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            return new SimpleStringProperty(soTien != null && soTien.compareTo(BigDecimal.ZERO) > 0 ? currencyFormatter.format(soTien) : "");
        });
        soTienCol.setPrefWidth(120);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        TableColumn<KhuyenMai, String> ngayBDCol = new TableColumn<>("Ngày BĐ");
        ngayBDCol.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getNgayBatDau();
            return new SimpleStringProperty(date != null ? date.format(dateTimeFormatter) : "");
        });
        ngayBDCol.setPrefWidth(150);

        TableColumn<KhuyenMai, String> ngayKTCol = new TableColumn<>("Ngày KT");
        ngayKTCol.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getNgayKetThuc();
            return new SimpleStringProperty(date != null ? date.format(dateTimeFormatter) : "");
        });
        ngayKTCol.setPrefWidth(150);

        TableColumn<KhuyenMai, String> trangThaiCol = new TableColumn<>("Trạng thái");
        trangThaiCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            LocalDateTime now = LocalDateTime.now();
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

        bangKhuyenMai.getColumns().addAll(maKMCol, moTaCol, loaiKMCol, tyLeCol, soTienCol, ngayBDCol, ngayKTCol, trangThaiCol);
        bangKhuyenMai.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        bangKhuyenMai.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lamMoiBangChiTietKhuyenMai(newSelection.getMaKM());
            } else {
                bangChiTietKhuyenMai.getItems().clear();
            }
        });

        bangKhuyenMai.setRowFactory(tv -> {
            TableRow<KhuyenMai> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    KhuyenMai clickedRow = row.getItem();
                    if (boDieuKhien.capNhatKhuyenMai(clickedRow)) {
                        lamMoiGiaoDien();
                    }
                }
            });

            ContextMenu contextMenu = taoMenuNguCanh(row);
            row.contextMenuProperty().bind(
                row.emptyProperty().map(empty -> empty ? null : contextMenu)
            );
            return row;
        });
    }

    private void caiDatBangChiTietKhuyenMai() {
        VBox.setVgrow(bangChiTietKhuyenMai, Priority.ALWAYS);

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

        TableColumn<ChiTietKhuyenMai, String> giaTriGiamCol = new TableColumn<>("Giá trị giảm");
        giaTriGiamCol.setCellValueFactory(cellData -> {
            BigDecimal tyLe = cellData.getValue().getTyLeGiam();
            BigDecimal soTien = cellData.getValue().getSoTienGiam();
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            if (tyLe != null && tyLe.compareTo(BigDecimal.ZERO) > 0) {
                return new SimpleStringProperty(tyLe.stripTrailingZeros().toPlainString() + "%");
            } else if (soTien != null && soTien.compareTo(BigDecimal.ZERO) >= 0) {
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

        bangChiTietKhuyenMai.getColumns().addAll(monApDungCol, monTangCol, giaTriGiamCol, soLuongTangCol);
        bangChiTietKhuyenMai.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void lamMoiGiaoDien() {
        this.danhSachKhuyenMai = boDieuKhien.layDanhSachKhuyenMai();
        bangKhuyenMai.setItems(FXCollections.observableArrayList(danhSachKhuyenMai));
        bangKhuyenMai.refresh();
        if (gridView != null) {
            gridView.refresh(this);
        }
        bangChiTietKhuyenMai.getItems().clear();
    }

    private void lamMoiBangChiTietKhuyenMai(String maKM) {
        List<ChiTietKhuyenMai> chiTietList = boDieuKhien.layChiTietKhuyenMaiTheoMaKM(maKM);
        bangChiTietKhuyenMai.setItems(FXCollections.observableArrayList(chiTietList));
        bangChiTietKhuyenMai.refresh();
    }

    public void hienThiChiTietKhuyenMai(KhuyenMai khuyenMai) {
        if (khuyenMai != null) {
            lamMoiBangChiTietKhuyenMai(khuyenMai.getMaKM());
        } else {
            bangChiTietKhuyenMai.getItems().clear();
        }
    }

    private ContextMenu taoMenuNguCanh(TableRow<KhuyenMai> row) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Sửa");
        editItem.setOnAction(e -> {
            KhuyenMai selectedItem = row.getItem();
            if (selectedItem != null && boDieuKhien.capNhatKhuyenMai(selectedItem)) {
                lamMoiGiaoDien();
            }
        });

        MenuItem deleteItem = new MenuItem("Xóa");
        deleteItem.setOnAction(e -> {
            KhuyenMai selectedItem = row.getItem();
            if (selectedItem != null && boDieuKhien.xoaKhuyenMai(selectedItem)) {
                lamMoiGiaoDien();
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        return contextMenu;
    }
}
