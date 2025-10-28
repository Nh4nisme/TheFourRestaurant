package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GiaoDienKhuyenMai extends VBox {

    private final KhuyenMaiController boDieuKhien;
    private List<KhuyenMai> danhSachKhuyenMaiGoc = new ArrayList<>();
    private List<KhuyenMai> danhSachKhuyenMaiHienThi = new ArrayList<>();
    private final TableView<KhuyenMai> bangKhuyenMai = new TableView<>();

    private VBox khuyenMaiViewContainer;
    private KhuyenMaiGrid gridView;
    private Node listView;
    private final Label lblItemCount = new Label(); // Label for item count

    public GiaoDienKhuyenMai() {
        this.boDieuKhien = new KhuyenMaiController();
        this.setAlignment(Pos.TOP_CENTER);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        // Thêm các khung trên và giữa
        contentPane.add(taoKhungTren(), 0, 0);
        contentPane.add(taoKhungGiua(), 0, 1);

        // Container chính cho danh sách/lưới khuyến mãi
        VBox khuyenMaiTableContainer = new VBox(10);
        khuyenMaiTableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khuyenMaiTableContainer.setPadding(new Insets(20));
        GridPane.setMargin(khuyenMaiTableContainer, new Insets(10, 10, 10, 10));
        GridPane.setHgrow(khuyenMaiTableContainer, Priority.ALWAYS);
        GridPane.setVgrow(khuyenMaiTableContainer, Priority.ALWAYS);
        contentPane.add(khuyenMaiTableContainer, 0, 2);

        khuyenMaiViewContainer = new VBox();
        VBox.setVgrow(khuyenMaiViewContainer, Priority.ALWAYS);
        khuyenMaiTableContainer.getChildren().add(khuyenMaiViewContainer);

        // Cài đặt và khởi tạo các view
        caiDatBangKhuyenMai();
        listView = bangKhuyenMai;
        gridView = new KhuyenMaiGrid(this);

        // View mặc định là GridView
        khuyenMaiViewContainer.getChildren().add(gridView);

        contentPane.add(taoThanhTrangThai(), 0, 3);

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

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm theo mã, tên...");
        txtTimKiem.setPrefWidth(300);

        ButtonSample btnTim = new ButtonSample("Tìm", "", 35, 13, 3);
        btnTim.setOnAction(event -> locVaCapNhatKhuyenMai(txtTimKiem.getText()));
        txtTimKiem.setOnAction(event -> locVaCapNhatKhuyenMai(txtTimKiem.getText())); // Trigger search on Enter key

        khungGiua.getChildren().addAll(btnList, btnGrid, space, txtTimKiem, btnTim);
        return khungGiua;
    }

    private HBox taoThanhTrangThai() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5, 20, 5, 20));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        lblItemCount.setStyle("-fx-text-fill: #333333; -fx-font-size: 12px;");
        statusBar.getChildren().add(lblItemCount);
        return statusBar;
    }

    private void caiDatBangKhuyenMai() {
        VBox.setVgrow(bangKhuyenMai, Priority.ALWAYS);

        TableColumn<KhuyenMai, String> maKMCol = new TableColumn<>("Mã KM");
        maKMCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaKM()));
        maKMCol.setPrefWidth(80);

        TableColumn<KhuyenMai, String> tenKMCol = new TableColumn<>("Tên KM");
        tenKMCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTenKM()));
        tenKMCol.setPrefWidth(200);

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

        bangKhuyenMai.getColumns().addAll(maKMCol, tenKMCol, moTaCol, loaiKMCol, tyLeCol, soTienCol, ngayBDCol, ngayKTCol, trangThaiCol);
        bangKhuyenMai.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        bangKhuyenMai.setRowFactory(tv -> {
            TableRow<KhuyenMai> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    KhuyenMai clickedRow = row.getItem();
                    Stage owner = (Stage) getScene().getWindow();
                    if (boDieuKhien.capNhatKhuyenMai(owner, clickedRow)) {
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

    public void lamMoiGiaoDien() {
        this.danhSachKhuyenMaiGoc = boDieuKhien.layDanhSachKhuyenMai();
        this.danhSachKhuyenMaiHienThi = FXCollections.observableArrayList(danhSachKhuyenMaiGoc);
        capNhatHienThi();
    }

    private void locVaCapNhatKhuyenMai(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            danhSachKhuyenMaiHienThi = FXCollections.observableArrayList(danhSachKhuyenMaiGoc);
        } else {
            String lowerCaseTuKhoa = tuKhoa.trim().toLowerCase();
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc.stream()
                    .filter(km -> km.getMaKM().toLowerCase().contains(lowerCaseTuKhoa) ||
                                     (km.getTenKM() != null && km.getTenKM().toLowerCase().contains(lowerCaseTuKhoa)))
                    .collect(Collectors.toList());
        }
        capNhatHienThi();
    }

    private void capNhatHienThi() {
        bangKhuyenMai.setItems(FXCollections.observableArrayList(danhSachKhuyenMaiHienThi));
        bangKhuyenMai.refresh();
        if (gridView != null) {
            gridView.refresh(this);
        }
        int count = danhSachKhuyenMaiHienThi.size();
        lblItemCount.setText("Hiển thị " + count + " khuyến mãi");
    }

    private ContextMenu taoMenuNguCanh(TableRow<KhuyenMai> row) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Sửa");
        editItem.setOnAction(e -> {
            KhuyenMai selectedItem = row.getItem();
            if (selectedItem != null) {
                Stage owner = (Stage) getScene().getWindow();
                if (boDieuKhien.capNhatKhuyenMai(owner, selectedItem)) {
                    lamMoiGiaoDien();
                }
            }
        });

        MenuItem deleteItem = new MenuItem("Xóa");
        deleteItem.setOnAction(e -> {
            KhuyenMai selectedItem = row.getItem();
            if (selectedItem != null) {
                Stage owner = (Stage) getScene().getWindow();
                if (boDieuKhien.xoaKhuyenMai(owner, selectedItem)) {
                    lamMoiGiaoDien();
                }
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        return contextMenu;
    }

    public List<KhuyenMai> getDanhSachKhuyenMaiHienThi() {
        return danhSachKhuyenMaiHienThi;
    }
}
