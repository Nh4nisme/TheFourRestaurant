package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.DAO.*;
import com.thefourrestaurant.controller.HoaDonController;
import com.thefourrestaurant.controller.PhuongThucThanhToanController;
import com.thefourrestaurant.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiaoDienLapHoaDon extends VBox {

    private final Stage stage;

    // ===== Label hiển thị thông tin =====
    private final Label lblMaHD = new Label();
    private final Label lblMaPDB = new Label();
    private final Label lblTenKH = new Label();
    private final Label lblSDT = new Label();
    private final Label lblGioNhan = new Label();
    private final Label lblGioTra = new Label();

    // ===== Thông tin thanh toán =====
    private final Label lblTongTien = new Label("0 đ");
    private final Label lblVAT = new Label("10%");
    private final Label lblChietKhau = new Label("0%");
    private final Label lblThanhToan = new Label("0 đ");
    private final Label lblTienThua = new Label("0 đ");

    private final TextField txtKhuyenMai = new TextField();
    private final TextField txtTienKhachDua = new TextField();

    private final Button btnKiemTraKM = new Button("Kiểm tra");
    private ComboBox<PhuongThucThanhToan> cboPTTT = new ComboBox<>();
    private final CheckBox chkXuatHoaDon = new CheckBox("Xuất hóa đơn");

    private final TableView<ChiTietPDB> bangMon = new TableView<>();
    private KhuyenMai kmHienTai;

    private final HoaDonController hoaDonController = new HoaDonController();
    private final PhuongThucThanhToanController phuongThucThanhToanController = new PhuongThucThanhToanController();
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

    public GiaoDienLapHoaDon(Stage stage) {
        this.stage = stage;
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        setPadding(new Insets(15));
        setSpacing(12);

        // ===== Header =====
        Label tieuDe = new Label("LẬP HÓA ĐƠN THANH TOÁN");
        tieuDe.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #DDB248;");
        HBox headerBox = new HBox(tieuDe);
        headerBox.setPadding(new Insets(10));
        headerBox.setStyle("-fx-background-color: #1E424D; -fx-alignment: center; -fx-background-radius: 8;");

        // ===== Thông tin khách hàng =====
        GridPane thongTin = new GridPane();
        thongTin.setHgap(10);
        thongTin.setVgap(8);
        thongTin.setPadding(new Insets(10));
        thongTin.setStyle("-fx-background-color: #F6F6F6; -fx-border-color: #DDB248; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label lbl1 = new Label("Mã HD:");
        Label lbl2 = new Label("Mã PĐB:");
        Label lbl3 = new Label("Tên KH:");
        Label lbl4 = new Label("SĐT KH:");
        Label lbl5 = new Label("Giờ nhận:");

        for (Label lbl : new Label[]{lblMaHD, lblMaPDB, lblTenKH, lblSDT, lblGioNhan, lblGioTra}) {
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D; -fx-font-size: 13px;");
        }

        thongTin.addRow(0, lbl1, lblMaHD, lbl2, lblMaPDB);
        thongTin.addRow(1, lbl3, lblTenKH, lbl4, lblSDT);
        thongTin.addRow(2, lbl5, lblGioNhan);

        // ===== Bảng món =====
        bangMon.setPrefHeight(250);
        khoiTaoBangMon();

        // ===== Phần thanh toán =====
        GridPane thanhToanPane = new GridPane();
        thanhToanPane.setHgap(10);
        thanhToanPane.setVgap(10);
        thanhToanPane.setPadding(new Insets(10));
        thanhToanPane.setStyle("-fx-background-color: #F6F6F6; -fx-border-color: #DDB248; -fx-border-radius: 8; -fx-background-radius: 8;");

        String inputStyle = """
            -fx-font-weight: bold;
            -fx-font-size: 13px;
            -fx-background-color: #FFFFFF;
            -fx-border-color: #DDB248;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
            -fx-prompt-text-fill: derive(#1E424D, -30%);
            -fx-text-fill: #1E424D;
        """;

        //cbo PTTT
        txtKhuyenMai.setStyle(inputStyle);
        txtTienKhachDua.setStyle(inputStyle);
        txtTienKhachDua.textProperty().addListener((obs, oldText, newText) -> {
            capNhatTienThua();
        });

        List<PhuongThucThanhToan> dsPTTT = phuongThucThanhToanController.layPhuongThucThanhToan();
        cboPTTT.getItems().clear();
        cboPTTT.getItems().addAll(dsPTTT);

        cboPTTT.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(PhuongThucThanhToan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getLoaiPTTT().getTenHienThi());
            }
        });

        cboPTTT.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(PhuongThucThanhToan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getLoaiPTTT().getTenHienThi());
            }
        });

        cboPTTT.getSelectionModel().selectFirst();

        btnKiemTraKM.setStyle("-fx-background-color: #1E424D; -fx-text-fill: #DDB248; -fx-font-weight: bold; -fx-cursor: hand;");
        btnKiemTraKM.setOnAction(e -> {
            String input = txtKhuyenMai.getText().trim();
            if (input.isEmpty()) {
                thongBao("Vui lòng nhập mã khuyến mãi!", Alert.AlertType.WARNING);
                return;
            }

            kmHienTai = khuyenMaiDAO.timKhuyenMaiTheoMaHoacTen(input);

            if (kmHienTai == null) {
                thongBao("Mã hoặc tên khuyến mãi không hợp lệ hoặc hết hạn!", Alert.AlertType.WARNING);
                lblChietKhau.setText("0%");
            } else {
                // Nếu KM theo % hoặc tiền
                if (kmHienTai.getTyLe() != null) {
                    lblChietKhau.setText(kmHienTai.getTyLe() + "%");
                } else if (kmHienTai.getSoTien() != null) {
                    lblChietKhau.setText(formatTien(kmHienTai.getSoTien()) + " đ");
                } else {
                    lblChietKhau.setText("KM tặng món: " + kmHienTai.getMoTa());
                }

                capNhatThanhToan(); // cập nhật lại tổng tiền
            }
        });

        lblTongTien.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1E424D;");
        lblVAT.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
        lblChietKhau.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
        lblThanhToan.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #DDB248;");
        lblTienThua.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #E04F4F;");

        txtKhuyenMai.setPromptText("Nhập mã khuyến mãi...");
        HBox khuyenMaiBox = new HBox(8, txtKhuyenMai, btnKiemTraKM);
        khuyenMaiBox.setStyle("-fx-alignment: center-left;");

        thanhToanPane.addRow(0, new Label("Mã khuyến mãi:"), khuyenMaiBox);
        thanhToanPane.addRow(1, new Label("Chiết khấu:"), lblChietKhau);
        thanhToanPane.addRow(2, new Label("VAT:"), lblVAT);
        thanhToanPane.addRow(3, new Label("Tổng tiền:"), lblTongTien);
        thanhToanPane.addRow(4, new Label("Phương thức TT:"), cboPTTT);
        thanhToanPane.addRow(5, new Label("Tiền khách đưa:"), txtTienKhachDua);
        thanhToanPane.addRow(6, new Label("Tiền thừa:"), lblTienThua);
        thanhToanPane.addRow(7, new Label("Thành tiền:"), lblThanhToan);
        thanhToanPane.add(chkXuatHoaDon, 2, 7);

        // ===== QR + footer =====
        ImageView qrView = new ImageView(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/Logo.png")));
        qrView.setFitWidth(120);
        qrView.setFitHeight(120);
        VBox qrBox = new VBox(5, new Label("QR Thanh toán"), qrView);
        qrBox.setStyle("-fx-alignment: top-center;");

        HBox contentBox = new HBox(30, thanhToanPane, qrBox);

        // Footer
        Button btnQuayLai = new Button("Quay lại");
        btnQuayLai.setStyle("-fx-background-color: #ccc; -fx-font-weight: bold; -fx-pref-width: 150; -fx-cursor: hand;");

        Button btnXacNhan = new Button("Xác nhận thanh toán");
        btnXacNhan.setStyle("-fx-background-color: #1E424D; -fx-text-fill: #DDB248; -fx-font-weight: bold; -fx-pref-width: 200; -fx-cursor: hand;");
        btnXacNhan.setOnAction(e -> {
            taoHoaDonMoi();
        });

        HBox footer = new HBox(20, btnQuayLai, btnXacNhan);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-alignment: center-right;");

        VBox vbox = new VBox(15, headerBox, thongTin, bangMon, contentBox, footer);
        getChildren().add(vbox);

        stage.setScene(new Scene(this, 900, 740));
        stage.setTitle("Lập hóa đơn");
        stage.show();
    }

    private void khoiTaoBangMon() {
        bangMon.getColumns().clear();
        // Cột Mã món
        TableColumn<ChiTietPDB, String> colMaMon = new TableColumn<>("Mã món");
        colMaMon.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMonAn().getMaMonAn())
        );
        colMaMon.setPrefWidth(100);

        // Cột Tên món
        TableColumn<ChiTietPDB, String> colTenMon = new TableColumn<>("Tên món");
        colTenMon.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMonAn().getTenMon())
        );
        colTenMon.setPrefWidth(200);

        // Cột Số lượng
        TableColumn<ChiTietPDB, Integer> colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getSoLuong()).asObject()
        );
        colSoLuong.setPrefWidth(80);

        // Cột Đơn giá
        TableColumn<ChiTietPDB, BigDecimal> colDonGia = new TableColumn<>("Đơn giá");
        colDonGia.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getMonAn().getDonGia())
        );
        colDonGia.setPrefWidth(100);

        // Cột Thành tiền
        TableColumn<ChiTietPDB, BigDecimal> colThanhTien = new TableColumn<>("Thành tiền");
        colThanhTien.setCellValueFactory(data -> {
            BigDecimal thanhTien = data.getValue().getMonAn().getDonGia()
                    .multiply(BigDecimal.valueOf(data.getValue().getSoLuong()));
            return new SimpleObjectProperty<>(thanhTien);
        });
        colThanhTien.setPrefWidth(120);

        bangMon.getColumns().addAll(colMaMon, colTenMon, colSoLuong, colDonGia, colThanhTien);
    }

    public void hienThiThongTin(PhieuDatBan pdb) {
        if (pdb == null) return;

        // Hiển thị thông tin khách hàng
        lblMaPDB.setText(pdb.getMaPDB());       // mã phiếu đặt bàn
        lblTenKH.setText(pdb.getKhachHang().getHoTen());       // tên khách
        lblSDT.setText(pdb.getKhachHang().getSoDT());           // số điện thoại
        lblGioNhan.setText(pdb.getNgayDat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Tạo mã hóa đơn tự động
        String maHD = hoaDonController.taoMaHD();
        lblMaHD.setText(maHD);

        // Load món từ chi tiết phiếu đặt bàn
        List<ChiTietPDB> chiTietList = pdb.getChiTietPDB(); // danh sách chi tiết
        if (chiTietList != null) {
            bangMon.getItems().clear();
            bangMon.getItems().addAll(chiTietList);
            capNhatThanhToan();

            for (ChiTietPDB ct : chiTietList) {
                MonAn mon = ct.getMonAn(); // giả sử ChiTietPDB có phương thức getMon()
                int soLuong = ct.getSoLuong();
                BigDecimal thanhTien = mon.getDonGia().multiply(BigDecimal.valueOf(soLuong));
            }
        }

        // Reset các thông tin thanh toán
        lblVAT.setText("10%");
        chkXuatHoaDon.setSelected(false);
    }

    private void capNhatThanhToan() {
        List<ChiTietPDB> chiTietList = bangMon.getItems();
        if (chiTietList == null || chiTietList.isEmpty()) {
            lblTongTien.setText("0 đ");
            lblVAT.setText("10%");
            lblThanhToan.setText("0 đ");
            return;
        }

        BigDecimal tongTien = BigDecimal.ZERO;

        for (ChiTietPDB ct : chiTietList) {
            BigDecimal donGia = ct.getMonAn().getDonGia();
            int soLuong = ct.getSoLuong();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
            tongTien = tongTien.add(thanhTien);
        }

        // Trừ khuyến mãi nếu có
        BigDecimal giam = BigDecimal.ZERO;
        if (kmHienTai != null) {
            if (kmHienTai.getTyLe() != null) {
                giam = tongTien.multiply(kmHienTai.getTyLe().divide(BigDecimal.valueOf(100)));
            } else if (kmHienTai.getSoTien() != null) {
                giam = kmHienTai.getSoTien();
            }
            tongTien = tongTien.subtract(giam);
        }

        // VAT 10%
        BigDecimal vat = tongTien.multiply(BigDecimal.valueOf(0.1));
        BigDecimal thanhToan = tongTien.add(vat);

        // Hiển thị
        lblTongTien.setText(formatTien(tongTien) + " đ");
        lblVAT.setText("10%");
        lblThanhToan.setText(formatTien(thanhToan) + " đ");
    }

    /**
     * Hàm format tiền để đẹp hơn
     */
    private String formatTien(BigDecimal tien) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(tien);
    }

    private BigDecimal layTongTien() {
        BigDecimal tongTien = BigDecimal.ZERO;
        for (ChiTietPDB ct : bangMon.getItems()) {
            BigDecimal thanhTien = ct.getMonAn().getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong()));
            tongTien = tongTien.add(thanhTien);
        }
        return tongTien;
    }

    private void capNhatTienThua() {
        String thanhToanStr = lblThanhToan.getText().replaceAll("[^\\d]", ""); // bỏ ký tự đ
        BigDecimal thanhToan = BigDecimal.ZERO;
        if (!thanhToanStr.isEmpty()) {
            thanhToan = new BigDecimal(thanhToanStr);
        }

        String tienKhachDuaStr = txtTienKhachDua.getText().replaceAll("[^\\d]", "");
        BigDecimal tienKhachDua = BigDecimal.ZERO;
        if (!tienKhachDuaStr.isEmpty()) {
            try {
                tienKhachDua = new BigDecimal(tienKhachDuaStr);
            } catch (NumberFormatException e) {
                lblTienThua.setText("0 đ");
                return;
            }
        }

        BigDecimal tienThua = tienKhachDua.subtract(thanhToan);
        if (tienThua.compareTo(BigDecimal.ZERO) < 0) {
            lblTienThua.setText("0 đ"); // khách chưa đủ tiền
        } else {
            lblTienThua.setText(formatTien(tienThua) + " đ");
        }
    }

    private void thongBao(String noiDung, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null); // không hiện header
        alert.setContentText(noiDung);
        alert.showAndWait();
    }

//    private void taoHoaDonMoi() {
//        try {
//            // 1. Lấy dữ liệu từ giao diện
//            String maHD = lblMaHD.getText();
//            String maPDB = lblMaPDB.getText();
//            String tenKH = lblTenKH.getText().trim();
//            String maKM = null;
//
//            BigDecimal tongTien = layTongTien(); // tổng tiền từ TableView
//            BigDecimal vat = tongTien.multiply(BigDecimal.valueOf(0.1)); // VAT 10%
//            BigDecimal thanhToan = tongTien.add(vat);
//
//            BigDecimal tienKhachDua = BigDecimal.ZERO;
//            if (!txtTienKhachDua.getText().isBlank()) {
//                try {
//                    tienKhachDua = new BigDecimal(txtTienKhachDua.getText().replaceAll(",", ""));
//                } catch (NumberFormatException e) {
//                    thongBao("Tiền khách đưa không hợp lệ", Alert.AlertType.ERROR);
//                    return;
//                }
//            }
//
//            BigDecimal tienThua = tienKhachDua.subtract(thanhToan);
//            if (tienThua.compareTo(BigDecimal.ZERO) < 0) tienThua = BigDecimal.ZERO;
//
//            System.out.println("Tien khach dua: " + tienKhachDua);
//            System.out.println("Tong thanh toan: " + thanhToan);
//            System.out.println("Tien thua tinh duoc: " + tienThua);
//
//            // 2. Lấy mã khách hàng từ tên
//            KhachHang kh = khachHangDAO.layKhachHangTheoTen(lblTenKH.getText());
//            String maKH;
//            if (kh != null) {
//                maKH = kh.getMaKH();
//            } else {
//                maKH = null; // hoặc thông báo khách hàng không tồn tại
//            }
//
//            // 3. Kiểm tra mã khuyến mãi
//            String inputKM = txtKhuyenMai.getText().trim();
//            KhuyenMai km = null;
//            if (!inputKM.isEmpty()) {
//                KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
//                km = kmDAO.timKhuyenMaiTheoMaHoacTen(inputKM);
//                if (km != null) {
//                    maKM = km.getMaKM();
//                    // Áp dụng khuyến mãi nếu có
//                    if (km.getTyLe() != null) {
//                        BigDecimal chietKhau = tongTien.multiply(km.getTyLe().divide(BigDecimal.valueOf(100)));
//                        thanhToan = thanhToan.subtract(chietKhau);
//                    } else if (km.getSoTien() != null) {
//                        thanhToan = thanhToan.subtract(km.getSoTien());
//                    }
//                    // Nếu là KM tặng món
//                    if (km.getLoaiKhuyenMai() != null &&
//                            "TangMon".equalsIgnoreCase(km.getLoaiKhuyenMai().getTenLoaiKM())) {
//                        thongBao("Khuyến mãi: Tặng món!", Alert.AlertType.INFORMATION);
//                    }
//                } else {
//                    thongBao("Mã khuyến mãi không hợp lệ", Alert.AlertType.WARNING);
//                }
//            }
//
//            // 4. Lấy mã phương thức thanh toán
//            PhuongThucThanhToan pttt = cboPTTT.getValue();
//            if (pttt == null) {
//                thongBao("Vui lòng chọn phương thức thanh toán", Alert.AlertType.WARNING);
//                return;
//            }
//
//            // 5. Tạo hóa đơn
//            HoaDon hd = new HoaDon();
//            hd.setMaHD(maHD);
//            hd.setNgayLap(LocalDateTime.now());
//            hd.setNhanVien(new NhanVien("NV000001")); // giả sử NV mặc định
//            hd.setKhachHang(new KhachHang(maKH));
//            hd.setPhieuDatBan(new PhieuDatBan(maPDB));
//            hd.setKhuyenMai(km);
//            hd.setThue(new Thue("TH000001")); // mã thuế mặc định
//            hd.setTienKhachDua(tienKhachDua);
//            hd.setTienThua(tienThua);
//            hd.setPhuongThucThanhToan(pttt);
//            hd.setDeleted(false);
//
//            boolean hdCreated = hoaDonDAO.themHoaDon(hd);
//            if (!hdCreated) {
//                thongBao("Tạo hóa đơn thất bại", Alert.AlertType.ERROR);
//                return;
//            }
//
//            // 6. Cập nhật trạng thái Phiếu Đặt Bàn
//            if (maPDB != null && !maPDB.isBlank()) {
//                PhieuDatBanDAO pdbDAO = new PhieuDatBanDAO();
//                boolean updated = pdbDAO.capNhatTrangThai(maPDB, "Đã thanh toán"); // hoặc giá trị tương ứng trong DB
//                if (!updated) {
//                    System.out.println("Cập nhật trạng thái PDB thất bại cho: " + maPDB);
//                }
//            }
//
//            // 7. Tạo chi tiết hóa đơn
//            for (ChiTietPDB ct : bangMon.getItems()) {
//                chiTietHoaDonDAO.themChiTietHD(
//                        maHD,
//                        ct.getMonAn().getMaMonAn(),
//                        ct.getSoLuong(),
//                        ct.getMonAn().getDonGia()
//                );
//            }
//
//            thongBao("Tạo hóa đơn thành công!", Alert.AlertType.INFORMATION);
//
//            if (chkXuatHoaDon.isSelected()) {
//                // gọi phương thức in hóa đơn nếu có
//            }
//
//            // 7. Refresh hoặc reset giao diện nếu cần
//            capNhatThanhToan();
//            txtTienKhachDua.clear();
//            txtKhuyenMai.clear();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            thongBao("Có lỗi xảy ra khi tạo hóa đơn", Alert.AlertType.ERROR);
//        }
//    }

    private void taoHoaDonMoi() {
        try {
            // 1. Lấy dữ liệu từ giao diện
            String maHD = lblMaHD.getText();
            String maPDB = lblMaPDB.getText();
            String tenKH = lblTenKH.getText().trim();
            String inputKM = txtKhuyenMai.getText().trim();

            BigDecimal tongTien = layTongTien(); // tổng tiền từ TableView
            BigDecimal vat = tongTien.multiply(BigDecimal.valueOf(0.1)); // VAT 10%
            BigDecimal thanhToan = tongTien.add(vat);

            BigDecimal tienKhachDua = BigDecimal.ZERO;
            if (!txtTienKhachDua.getText().isBlank()) {
                try {
                    tienKhachDua = new BigDecimal(txtTienKhachDua.getText().replaceAll(",", ""));
                } catch (NumberFormatException e) {
                    thongBao("Tiền khách đưa không hợp lệ", Alert.AlertType.ERROR);
                    return;
                }
            }

            // 2. Lấy mã khách hàng từ tên
            KhachHang kh = khachHangDAO.layKhachHangTheoTen(lblTenKH.getText());
            String maKH;
            if (kh != null) {
                maKH = kh.getMaKH();
            } else {
                maKH = null; // hoặc thông báo khách hàng không tồn tại
            }

            // 3. Kiểm tra mã khuyến mãi
            KhuyenMai km = null;
            String maKM = null;
            if (!inputKM.isEmpty()) {
                KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
                km = kmDAO.timKhuyenMaiTheoMaHoacTen(inputKM);
                if (km != null) {
                    maKM = km.getMaKM();
                    // Áp dụng khuyến mãi
                    if (km.getTyLe() != null) {
                        BigDecimal chietKhau = tongTien.multiply(km.getTyLe().divide(BigDecimal.valueOf(100)));
                        thanhToan = thanhToan.subtract(chietKhau);
                    } else if (km.getSoTien() != null) {
                        thanhToan = thanhToan.subtract(km.getSoTien());
                    }

                    if (km.getLoaiKhuyenMai() != null &&
                            "TangMon".equalsIgnoreCase(km.getLoaiKhuyenMai().getTenLoaiKM())) {
                        thongBao("Khuyến mãi: Tặng món!", Alert.AlertType.INFORMATION);
                    }
                } else {
                    thongBao("Mã khuyến mãi không hợp lệ", Alert.AlertType.WARNING);
                }
            }

            // 4. Lấy mã phương thức thanh toán
            PhuongThucThanhToan pttt = cboPTTT.getValue();
            if (pttt == null) {
                thongBao("Vui lòng chọn phương thức thanh toán", Alert.AlertType.WARNING);
                return;
            }

            // 5. Tính tiền thừa
            BigDecimal tienThua = tienKhachDua.subtract(thanhToan);
            if (tienThua.compareTo(BigDecimal.ZERO) < 0) tienThua = BigDecimal.ZERO;

            // 6. Tạo hóa đơn
            HoaDon hd = new HoaDon();
            hd.setMaHD(maHD);
            hd.setNgayLap(LocalDateTime.now());
            hd.setNhanVien(new NhanVien("NV000001")); // giả sử NV hiện tại
            hd.setKhachHang(new KhachHang(maKH));
            hd.setPhieuDatBan(new PhieuDatBan(maPDB));
            hd.setKhuyenMai(maKM != null ? km : null);
            hd.setThue(new Thue("TH000001")); // mặc định
            hd.setTienKhachDua(tienKhachDua);
            hd.setTienThua(tienThua);
            hd.setPhuongThucThanhToan(pttt);
            hd.setDeleted(false);

            // 7. Lưu hóa đơn vào DB
            HoaDonDAO hoaDonDAO = new HoaDonDAO();
            boolean hdCreated = hoaDonDAO.themHoaDon(hd);
            if (!hdCreated) {
                thongBao("Tạo hóa đơn thất bại", Alert.AlertType.ERROR);
                return;
            }
            System.out.println("Tạo hóa đơn thành công: " + maHD);

            // 8. Tạo chi tiết hóa đơn
            chiTietHoaDonDAO = new ChiTietHoaDonDAO();
            for (ChiTietPDB ct : bangMon.getItems()) {
                BigDecimal donGia = ct.getMonAn().getDonGia();
                int soLuong = ct.getSoLuong();
                chiTietHoaDonDAO.themChiTietHD(maHD, ct.getMonAn().getMaMonAn(), soLuong, donGia);
                System.out.println("Thêm chi tiết: " + ct.getMonAn().getMaMonAn() + " x " + soLuong);
            }

            // 9. Cập nhật trạng thái PhieuDatBan
            if (maPDB != null && !maPDB.isEmpty()) {
                PhieuDatBanDAO pdbDAO = new PhieuDatBanDAO();
                pdbDAO.capNhatTrangThai(maPDB, "Đã thanh toán");
                System.out.println("Cập nhật PDB thành đã thanh toán: " + maPDB);
            }

            // 10. Thông báo thành công
            thongBao("Tạo hóa đơn thành công!", Alert.AlertType.INFORMATION);

            // 11. Nếu chọn xuất hóa đơn
            if (chkXuatHoaDon.isSelected()) {
                // Gọi phương thức in hóa đơn nếu có
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            thongBao("Có lỗi xảy ra khi tạo hóa đơn: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
        Stage stage = (Stage) lblMaHD.getScene().getWindow();
        stage.close();
    }
}
