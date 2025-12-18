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
import java.util.Set;
import java.util.stream.Collectors;

public class GiaoDienKhuyenMai extends VBox {

    private final KhuyenMaiController boDieuKhien;
    private List<KhuyenMai> danhSachKhuyenMaiGoc = new ArrayList<>();
    private List<KhuyenMai> danhSachKhuyenMaiHienThi = new ArrayList<>();
    private final TableView<KhuyenMai> bangKhuyenMai = new TableView<>();

    private VBox khuyenMaiViewContainer;
    private KhuyenMaiGrid gridView;
    private Node listView;
    private final Label lblItemCount = new Label();
    private final ComboBox<String> hopLocKieu = new ComboBox<>();

    public GiaoDienKhuyenMai() {
        this.boDieuKhien = new KhuyenMaiController();
        this.setAlignment(Pos.TOP_CENTER);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");
        contentPane.add(taoKhungGiua(), 0, 1);

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

        caiDatBangKhuyenMai();
        listView = bangKhuyenMai;
        gridView = new KhuyenMaiGrid(this);

        khuyenMaiViewContainer.getChildren().add(gridView);

        contentPane.add(taoThanhTrangThai(), 0, 3);

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        }

        this.getChildren().add(contentPane);
        lamMoiGiaoDien();
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

        ButtonSample btnKhoiPhuc = new ButtonSample("Khôi phục", 35, 14, 3);
        btnKhoiPhuc.setOnAction(event -> {
            List<KhuyenMai> danhSachKMDaXoa = boDieuKhien.layKhuyenMaiDaXoa();
            if (danhSachKMDaXoa.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Không có khuyến mãi nào đã bị xóa.");
                alert.initOwner((Stage) getScene().getWindow());
                alert.showAndWait();
                return;
            }

            KhoiPhucKhuyenMai dialog = new KhoiPhucKhuyenMai(danhSachKMDaXoa);
            dialog.initOwner((Stage) getScene().getWindow());
            dialog.showAndWait();

            Set<KhuyenMai> cacKMCamKhoiPhuc = dialog.getCacKMDaChon();
            if (cacKMCamKhoiPhuc != null && !cacKMCamKhoiPhuc.isEmpty()) {
                Stage owner = (Stage) getScene().getWindow();
                if (boDieuKhien.khoiPhucKhuyenMai(owner, cacKMCamKhoiPhuc)) {
                    lamMoiGiaoDien();
                }
            }
        });

        hopLocKieu.setItems(FXCollections.observableArrayList("Tất cả", "Sự kiện (Tự động)", "Mã giảm giá (Nhập mã)"));
        hopLocKieu.setValue("Tất cả");
        hopLocKieu.setStyle("-fx-background-color: #DDB248;\n" +
                "    -fx-border-color: #DDB248;\n" +
                "    -fx-text-fill: #1E424D;\n" +
                "    -fx-border-radius: 5;\n" +
                "    -fx-background-radius: 7;\n" +
                "    -fx-border-width: 2;\n" +
                "    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.35), 10, 0, 0, 4);\n" +
                "    -fx-background-insets: 0;");
        hopLocKieu.setOnAction(e -> locTheoKieu());

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm theo mã, tên, mã code...");
        txtTimKiem.setPrefWidth(300);

        ButtonSample btnTim = new ButtonSample("Tìm", "", 35, 13, 3);
        btnTim.setOnAction(event -> locVaCapNhatKhuyenMai(txtTimKiem.getText()));
        txtTimKiem.setOnAction(event -> locVaCapNhatKhuyenMai(txtTimKiem.getText()));

        khungGiua.getChildren().addAll(btnList, btnGrid, hopLocKieu, space, txtTimKiem, btnTim, btnKhoiPhuc);
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
        tenKMCol.setPrefWidth(150);

        TableColumn<KhuyenMai, String> kieuKMCol = new TableColumn<>("Kiểu KM");
        kieuKMCol.setCellValueFactory(cellData -> {
            KhuyenMai km = cellData.getValue();
            String kieu = KhuyenMai.KIEU_MA_GIAM_GIA.equals(km.getKieuKM()) ? "Mã giảm giá" : "Sự kiện";
            return new SimpleStringProperty(kieu);
        });
        kieuKMCol.setPrefWidth(90);

        TableColumn<KhuyenMai, String> maCodeCol = new TableColumn<>("Mã Code");
        maCodeCol.setCellValueFactory(cellData -> {
            String maCode = cellData.getValue().getMaCode();
            return new SimpleStringProperty(maCode != null ? maCode : "-");
        });
        maCodeCol.setPrefWidth(90);

        TableColumn<KhuyenMai, String> soLuotCol = new TableColumn<>("Còn lại");
        soLuotCol.setCellValueFactory(cellData -> {
            Integer soLuot = cellData.getValue().getSoLuotSuDung();
            return new SimpleStringProperty(soLuot != null ? String.valueOf(soLuot) : "∞");
        });
        soLuotCol.setPrefWidth(60);

        TableColumn<KhuyenMai, String> loaiKMCol = new TableColumn<>("Loại KM");
        loaiKMCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getLoaiKhuyenMai() != null) {
                return new SimpleStringProperty(cellData.getValue().getLoaiKhuyenMai().getTenLoaiKM());
            }
            return new SimpleStringProperty("");
        });
        loaiKMCol.setPrefWidth(120);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        TableColumn<KhuyenMai, String> ngayBDCol = new TableColumn<>("Ngày BĐ");
        ngayBDCol.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getNgayBatDau();
            return new SimpleStringProperty(date != null ? date.format(dateTimeFormatter) : "");
        });
        ngayBDCol.setPrefWidth(90);

        TableColumn<KhuyenMai, String> ngayKTCol = new TableColumn<>("Ngày KT");
        ngayKTCol.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getNgayKetThuc();
            return new SimpleStringProperty(date != null ? date.format(dateTimeFormatter) : "");
        });
        ngayKTCol.setPrefWidth(90);

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
        trangThaiCol.setPrefWidth(90);

        bangKhuyenMai.getColumns().addAll(maKMCol, tenKMCol, kieuKMCol, maCodeCol, soLuotCol, loaiKMCol, ngayBDCol, ngayKTCol, trangThaiCol);
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
            row.contextMenuProperty().bind(row.emptyProperty().map(empty -> empty ? null : contextMenu));
            return row;
        });
    }

    public void lamMoiGiaoDien() {
        this.danhSachKhuyenMaiGoc = boDieuKhien.layDanhSachKhuyenMai();
        locTheoKieu();
    }

    private void locTheoKieu() {
        String kieuChon = hopLocKieu.getValue();
        if ("Sự kiện (Tự động)".equals(kieuChon)) {
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc.stream().filter(km -> KhuyenMai.KIEU_SU_KIEN.equals(km.getKieuKM())).collect(Collectors.toList());
        } else if ("Mã giảm giá (Nhập mã)".equals(kieuChon)) {
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiGoc.stream().filter(km -> KhuyenMai.KIEU_MA_GIAM_GIA.equals(km.getKieuKM())).collect(Collectors.toList());
        } else {
            danhSachKhuyenMaiHienThi = FXCollections.observableArrayList(danhSachKhuyenMaiGoc);
        }
        capNhatHienThi();
    }

    private void locVaCapNhatKhuyenMai(String tuKhoa) {
        locTheoKieu();
        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            String lowerCaseTuKhoa = tuKhoa.trim().toLowerCase();
            danhSachKhuyenMaiHienThi = danhSachKhuyenMaiHienThi.stream().filter(km -> km.getMaKM().toLowerCase().contains(lowerCaseTuKhoa) || (km.getTenKM() != null && km.getTenKM().toLowerCase().contains(lowerCaseTuKhoa)) || (km.getMaCode() != null && km.getMaCode().toLowerCase().contains(lowerCaseTuKhoa))).collect(Collectors.toList());
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
