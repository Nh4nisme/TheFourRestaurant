package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiaoDienChiTietPhieuDatBan extends VBox {

    // ===== Biến giao diện =====
    private final Label lblMaPDB = new Label();
    private final Label lblTenKH = new Label();
    private final Label lblSDT = new Label();
    private final Label lblNgayDat = new Label();
    private final TableView<Ban> tableBan = new TableView<>();
    private final TableView<ChiTietPDB> tableMonAn = new TableView<>();
    private final Map<String, Label> thongTinPhu = new HashMap<>();

    public GiaoDienChiTietPhieuDatBan() {
        setPadding(new Insets(20));
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);

        Label tieuDe = new Label("THÔNG TIN CHI TIẾT PHIẾU ĐẶT BÀN");
        tieuDe.setStyle("-fx-font-size: 24; -fx-text-fill: #DDB248; -fx-font-weight: bold;");

        getChildren().addAll(
                tieuDe,
                taoDongThongTin("Mã phiếu đặt bàn:", lblMaPDB),
                taoDongThongTin("Tên khách hàng:", lblTenKH),
                taoDongThongTin("Số điện thoại:", lblSDT),
                taoDongThongTin("Ngày đặt bàn:", lblNgayDat)
        );

        // ==== Table danh sách bàn ====
        Label lblBan = new Label("Danh sách bàn được đặt:");
        lblBan.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333;");
        tableBan.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableBan.setPrefHeight(180);

        TableColumn<Ban, String> maBanCol = new TableColumn<>("Mã bàn");
        maBanCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getMaBan()));

        TableColumn<Ban, String> loaiBanCol = new TableColumn<>("Loại bàn");
        loaiBanCol.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getLoaiBan() != null ? cd.getValue().getLoaiBan().getTenLoaiBan() : ""
        ));

        tableBan.getColumns().addAll(maBanCol, loaiBanCol);

        // ==== Table danh sách món ăn ====
        Label lblMon = new Label("Danh sách món ăn trong phiếu:");
        lblMon.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333;");
        tableMonAn.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableMonAn.setPrefHeight(200);

        TableColumn<ChiTietPDB, String> tenMonCol = new TableColumn<>("Tên món");
        tenMonCol.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getMonAn() != null ? cd.getValue().getMonAn().getTenMon() : "(Không có)"
        ));

        TableColumn<ChiTietPDB, String> soLuongCol = new TableColumn<>("Số lượng");
        soLuongCol.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getSoLuong())));

        TableColumn<ChiTietPDB, String> donGiaCol = new TableColumn<>("Đơn giá");
        donGiaCol.setCellValueFactory(cd -> new SimpleStringProperty(
                String.format("%,.0f đ", cd.getValue().getDonGia())
        ));

        TableColumn<ChiTietPDB, String> ghiChuCol = new TableColumn<>("Ghi chú");
        ghiChuCol.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getGhiChu() != null ? cd.getValue().getGhiChu() : ""
        ));

        tableMonAn.getColumns().addAll(tenMonCol, soLuongCol, donGiaCol, ghiChuCol);

        // ==== Thông tin phụ ====
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(5);
        infoGrid.setPadding(new Insets(10, 0, 0, 0));

        String[] labels = {"Số người:", "Nhân viên lập:", "Trạng thái:"};
        for (int i = 0; i < labels.length; i++) {
            Label left = new Label(labels[i]);
            Label right = new Label();
            thongTinPhu.put(labels[i], right);
            infoGrid.add(left, 0, i);
            infoGrid.add(right, 1, i);
        }

        getChildren().addAll(lblBan, tableBan, lblMon, tableMonAn, infoGrid);
    }

    // ===== Hàm hiển thị thông tin =====
    public void hienThiThongTin(PhieuDatBan pdb) {
        lblMaPDB.setText(pdb.getMaPDB());
        lblTenKH.setText(pdb.getKhachHang() != null ? pdb.getKhachHang().getHoTen() : "");
        lblSDT.setText(pdb.getKhachHang() != null ? pdb.getKhachHang().getSoDT() : "");
        lblNgayDat.setText(pdb.getNgayDat() != null
                ? pdb.getNgayDat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "");

        // Lọc danh sách bàn
        tableBan.getItems().setAll(pdb.getBan() != null ? List.of(pdb.getBan()) : List.of());

        // Lọc danh sách món
        tableMonAn.getItems().setAll(pdb.getChiTietPDB());

        // Thông tin phụ
        thongTinPhu.get("Số người:").setText(String.valueOf(pdb.getSoNguoi()));
        thongTinPhu.get("Nhân viên lập:").setText(pdb.getNhanVien() != null ? pdb.getNhanVien().getHoTen() : "");
        thongTinPhu.get("Trạng thái:").setText(pdb.getTrangThai());
    }

    private HBox taoDongThongTin(String tieuDe, Label lblGiaTri) {
        Label lblTieuDe = new Label(tieuDe);
        lblTieuDe.setMinWidth(160);
        return new HBox(10, lblTieuDe, lblGiaTri);
    }
}