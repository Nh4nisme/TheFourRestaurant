package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GiaoDienLapHoaDon extends VBox {

    private final Stage stage;
    private final Label lblMaPDB = new Label();
    private final Label lblTenKH = new Label();
    private final Label lblSDT = new Label();
    private final Label lblGioNhan = new Label();
    private final Label lblGioTra = new Label();
    private final Label lblTongTien = new Label("0 đ");
    private final TableView<ChiTietHoaDon> bangMon = new TableView<>();

    private HoaDon hoaDonHienTai;

    public GiaoDienLapHoaDon(Stage stage) {
        this.stage = stage;
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        setPadding(new Insets(15));
        setSpacing(12);

        Label tieuDe = new Label("LẬP HÓA ĐƠN THANH TOÁN");
        tieuDe.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane thongTin = new GridPane();
        thongTin.setHgap(10);
        thongTin.setVgap(8);

        thongTin.addRow(0, new Label("Mã PĐB:"), lblMaPDB);
        thongTin.addRow(1, new Label("Tên KH:"), lblTenKH);
        thongTin.addRow(2, new Label("SĐT KH:"), lblSDT);
        thongTin.addRow(3, new Label("Giờ nhận:"), lblGioNhan);
        thongTin.addRow(4, new Label("Giờ trả:"), lblGioTra);

        khoiTaoBangMon();

        HBox tongTienBox = new HBox(10, new Label("Tổng tiền:"), lblTongTien);
        tongTienBox.setPadding(new Insets(10));
        lblTongTien.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red;");

        Button btnXacNhan = new Button("💾 Lưu hóa đơn");
        btnXacNhan.setOnAction(e -> luuHoaDon());

        VBox vbox = new VBox(10, tieuDe, thongTin, bangMon, tongTienBox, btnXacNhan);
        getChildren().add(vbox);

        stage.setTitle("Lập hóa đơn");
        stage.setScene(new Scene(this, 750, 600));
        stage.show();
    }

    private void khoiTaoBangMon() {
        bangMon.setPrefHeight(250);

        TableColumn<ChiTietHoaDon, Integer> cotSTT = new TableColumn<>("STT");
        TableColumn<ChiTietHoaDon, String> cotTenMon = new TableColumn<>("Tên món");
        TableColumn<ChiTietHoaDon, BigDecimal> cotDonGia = new TableColumn<>("Đơn giá");
        TableColumn<ChiTietHoaDon, Integer> cotSoLuong = new TableColumn<>("Số lượng");
        TableColumn<ChiTietHoaDon, BigDecimal> cotThanhTien = new TableColumn<>("Thành tiền");

        // ✅ Dùng lambda cho tất cả cột (không dùng PropertyValueFactory)
        cotSTT.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(bangMon.getItems().indexOf(param.getValue()) + 1)
        );
        cotTenMon.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getMonAn().getTenMon())
        );
        cotDonGia.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getDonGia())
        );
        cotSoLuong.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getSoLuong())
        );
        cotThanhTien.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue().getThanhTien())
        );

        cotSTT.setPrefWidth(60);
        cotTenMon.setPrefWidth(200);
        cotDonGia.setPrefWidth(120);
        cotSoLuong.setPrefWidth(100);
        cotThanhTien.setPrefWidth(140);

        bangMon.getColumns().addAll(cotSTT, cotTenMon, cotDonGia, cotSoLuong, cotThanhTien);
    }

    /**
     * Hiển thị thông tin từ phiếu đặt bàn lên giao diện lập hóa đơn
     */
    public void hienThiThongTin(PhieuDatBan pdb) {
        lblMaPDB.setText(pdb.getMaPDB());
        lblTenKH.setText(pdb.getKhachHang() != null ? pdb.getKhachHang().getHoTen() : "");
        lblSDT.setText(pdb.getKhachHang() != null ? pdb.getKhachHang().getSoDT() : "");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblGioNhan.setText(pdb.getNgayDat() != null ? pdb.getNgayDat().format(fmt) : "");
//        lblGioTra.setText(pdb.getNgayTra() != null ? pdb.getNgayTra().format(fmt) : "");

        // 🧾 Tạo hóa đơn tạm
        hoaDonHienTai = new HoaDon();
        hoaDonHienTai.setPhieuDatBan(pdb);

        List<ChiTietHoaDon> dsCTHD = new ArrayList<>();
        if (pdb.getChiTietPDB() != null) {
            for (ChiTietPDB ctpdb : pdb.getChiTietPDB()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon(
                        hoaDonHienTai,
                        ctpdb.getMonAn(),
                        ctpdb.getSoLuong(),
                        ctpdb.getMonAn().getDonGia()
                );
                dsCTHD.add(cthd);
            }
        }

        hoaDonHienTai.setChiTietHoaDon(dsCTHD);

        BigDecimal tongTien = hoaDonHienTai.getTongTien();
        lblTongTien.setText(String.format("%,.0f đ", tongTien));

        bangMon.setItems(FXCollections.observableArrayList(dsCTHD));
    }

    private void luuHoaDon() {
        if (hoaDonHienTai == null) {
            thongBao("Chưa có dữ liệu hóa đơn để lưu.", Alert.AlertType.WARNING);
            return;
        }

        System.out.println("Đã lưu hóa đơn: " + hoaDonHienTai.getMaHD());
        thongBao("Lưu hóa đơn thành công!", Alert.AlertType.INFORMATION);
        stage.close();
    }

    private void thongBao(String noiDung, Alert.AlertType loai) {
        Alert alert = new Alert(loai);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.initOwner(stage);
        alert.showAndWait();
    }
}
