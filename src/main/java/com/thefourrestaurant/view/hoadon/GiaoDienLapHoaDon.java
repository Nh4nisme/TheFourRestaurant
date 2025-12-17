package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.DAO.*;
import com.thefourrestaurant.controller.HoaDonController;
import com.thefourrestaurant.controller.PhuongThucThanhToanController;
import com.thefourrestaurant.model.*;
import com.thefourrestaurant.util.Session;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class GiaoDienLapHoaDon extends VBox {
    /* ================== STATE ================== */
    private final Stage stage;
    private PhieuDatBan phieuDatBan;
    private KhuyenMai kmHienTai;

    /* ================== CONTROLLER & DAO ================== */
    private final HoaDonController hoaDonController = new HoaDonController();
    private final PhuongThucThanhToanController ptttController = new PhuongThucThanhToanController();
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

    /* ================== UI COMPONENT ================== */
    private final Label lblMaHD = new Label();
    private final Label lblMaPDB = new Label();
    private final Label lblTenKH = new Label();
    private final Label lblSDT = new Label();
    private final Label lblNgayNhan = new Label();

    private final Label lblTongTien = new Label("0 đ");
    private final Label lblVAT = new Label("10%");
    private final Label lblChietKhau = new Label("0%");
    private final Label lblThanhToan = new Label("0 đ");
    private final Label lblTienThua = new Label("0 đ");
    private final Label lblTienCoc = new Label("0 đ");

    private final TextField txtKhuyenMai = new TextField();
    private final TextField txtTienKhachDua = new TextField();

    private final ComboBox<PhuongThucThanhToan> cboPTTT = new ComboBox<>();
    private final CheckBox chkXuatHoaDon = new CheckBox("Xuất hóa đơn");

    private final TableView<ChiTietPDB> tblMon = new TableView<>();

    /* ================== CONSTRUCTOR ================== */
    public GiaoDienLapHoaDon(Stage stage) {
        this.stage = stage;
        initUI();
    }

    /* ================== INIT UI ================== */
    private void initUI() {
        setPadding(new Insets(15));
        setSpacing(12);
        initComboPTTT();

        VBox root = new VBox(15,
                initHeader(),
                initThongTinKhach(),
                initBangMon(),
                initThanhToanPane(),
                initFooter()
        );

        getChildren().add(root);
        stage.setScene(new Scene(this, 950, 800));
        stage.setTitle("Lập hóa đơn");
        stage.show();
    }

    private Node initHeader() {
        Label title = new Label("LẬP HÓA ĐƠN THANH TOÁN");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold;");

        HBox box = new HBox(title);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Node initThongTinKhach() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        grid.addRow(0, new Label("Mã HD:"), lblMaHD, new Label("Mã PĐB:"), lblMaPDB);
        grid.addRow(1, new Label("Tên KH:"), lblTenKH, new Label("SĐT:"), lblSDT);
        grid.addRow(2, new Label("Ngày nhận:"), lblNgayNhan);

        return grid;
    }

    private Node initBangMon() {
        TableColumn<ChiTietPDB, String> colTenMon = new TableColumn<>("Tên món");
        colTenMon.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMonAn().getTenMon()));

        TableColumn<ChiTietPDB, Integer> colSL = new TableColumn<>("SL");
        colSL.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getSoLuong()).asObject());

        TableColumn<ChiTietPDB, BigDecimal> colThanhTien = new TableColumn<>("Thành tiền");
        colThanhTien.setCellValueFactory(c -> {
            BigDecimal tt = c.getValue().getMonAn().getDonGia()
                    .multiply(BigDecimal.valueOf(c.getValue().getSoLuong()));
            return new SimpleObjectProperty<>(tt);
        });

        tblMon.getColumns().addAll(colTenMon, colSL, colThanhTien);
        tblMon.setPrefHeight(250);
        return tblMon;
    }

    private Node initThanhToanPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        cboPTTT.getItems().addAll(ptttController.layPhuongThucThanhToan());
        cboPTTT.getSelectionModel().selectFirst();

        txtTienKhachDua.textProperty().addListener((o, a, b) -> capNhatTienThua());

        Button btnKiemTraKM = new Button("Kiểm tra");
        btnKiemTraKM.setOnAction(e -> xuLyKiemTraKhuyenMai());

        grid.addRow(0, new Label("Khuyến mãi:"), new HBox(8, txtKhuyenMai, btnKiemTraKM));
        grid.addRow(1, new Label("Chiết khấu:"), lblChietKhau);
        grid.addRow(2, new Label("VAT:"), lblVAT);
        grid.addRow(3, new Label("Tổng tiền:"), lblTongTien);
        grid.addRow(4, new Label("Tiền cọc:"), lblTienCoc);
        grid.addRow(5, new Label("PTTT:"), cboPTTT);
        grid.addRow(6, new Label("Tiền khách đưa:"), txtTienKhachDua);
        grid.addRow(7, new Label("Tiền thừa:"), lblTienThua);
        grid.addRow(8, new Label("Phải thanh toán:"), lblThanhToan, chkXuatHoaDon);

        return grid;
    }

    private Node initFooter() {
        Button btnXacNhan = new Button("Xác nhận");
        btnXacNhan.setOnAction(e -> xuLyXacNhanThanhToan());

        Button btnThoat = new Button("Thoát");
        btnThoat.setOnAction(e -> stage.close());

        HBox box = new HBox(10, btnThoat, btnXacNhan);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    private void initComboPTTT() {

        cboPTTT.getItems().setAll(ptttController.layPhuongThucThanhToan());

        cboPTTT.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(PhuongThucThanhToan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null
                        ? ""
                        : item.getLoaiPTTT().getTenHienThi());
            }
        });

        cboPTTT.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(PhuongThucThanhToan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null
                        ? ""
                        : item.getLoaiPTTT().getTenHienThi());
            }
        });

        cboPTTT.getSelectionModel().selectFirst();
    }


    /* ================== DATA ================== */
    public void hienThiThongTin(PhieuDatBan pdb) {
        this.phieuDatBan = pdb;

        lblMaPDB.setText(pdb.getMaPDB());
        lblTenKH.setText(pdb.getKhachHang().getHoTen());
        lblSDT.setText(pdb.getKhachHang().getSoDT());
        lblNgayNhan.setText(pdb.getNgayDat().toString());

        lblMaHD.setText(hoaDonController.taoMaHD());

        tblMon.getItems().setAll(pdb.getChiTietPDB());

        lblTienCoc.setText(formatTien(
                pdb.getTienCoc() == null ? BigDecimal.ZERO : pdb.getTienCoc()) + " đ");

        capNhatThanhToan();
    }

    /* ================== LOGIC ================== */
    private BigDecimal tinhTongTienMon() {
        return tblMon.getItems().stream()
                .map(ct -> ct.getMonAn().getDonGia()
                        .multiply(BigDecimal.valueOf(ct.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal tinhThanhToanSauKMVaVAT() {
        BigDecimal tong = tinhTongTienMon();

        if (kmHienTai != null) {
            if (kmHienTai.getTyLe() != null)
                tong = tong.subtract(tong.multiply(kmHienTai.getTyLe()).divide(BigDecimal.valueOf(100)));
            else if (kmHienTai.getSoTien() != null)
                tong = tong.subtract(kmHienTai.getSoTien());
        }

        tong = tong.add(tong.multiply(BigDecimal.valueOf(0.1)));

        if (phieuDatBan.getTienCoc() != null)
            tong = tong.subtract(phieuDatBan.getTienCoc());

        return tong.max(BigDecimal.ZERO);
    }

    private void capNhatThanhToan() {
        lblTongTien.setText(formatTien(tinhTongTienMon()) + " đ");
        lblThanhToan.setText(formatTien(tinhThanhToanSauKMVaVAT()) + " đ");
        capNhatTienThua();
    }

    private void capNhatTienThua() {
        try {
            BigDecimal khachDua = new BigDecimal(txtTienKhachDua.getText());
            BigDecimal canTra = tinhThanhToanSauKMVaVAT();
            lblTienThua.setText(formatTien(khachDua.subtract(canTra).max(BigDecimal.ZERO)) + " đ");
        } catch (Exception e) {
            lblTienThua.setText("0 đ");
        }
    }

    private void xuLyKiemTraKhuyenMai() {
        kmHienTai = khuyenMaiDAO.timKhuyenMaiTheoMaHoacTen(txtKhuyenMai.getText());
        lblChietKhau.setText(kmHienTai == null ? "0%" :
                kmHienTai.getTyLe() != null ? kmHienTai.getTyLe() + "%" : formatTien(kmHienTai.getSoTien()) + " đ");
        capNhatThanhToan();
    }

    private void xuLyXacNhanThanhToan() {

        try {
            BigDecimal thanhToan = tinhThanhToanSauKMVaVAT();
            BigDecimal tienKhachDua = new BigDecimal(txtTienKhachDua.getText());
            BigDecimal tienThua = tienKhachDua.subtract(thanhToan);

            if (tienThua.compareTo(BigDecimal.ZERO) < 0) {
                thongBao("Khách đưa chưa đủ tiền!", Alert.AlertType.WARNING);
                return;
            }

            HoaDon hd = new HoaDon();
            hd.setMaHD(lblMaHD.getText());
            hd.setNgayLap(LocalDateTime.now());

            TaiKhoan tk = Session.getCurrentUser();
            if (tk == null) {
                thongBao("Phiên đăng nhập không hợp lệ!", Alert.AlertType.ERROR);
                return;
            }
            NhanVienDAO nvDAO = new NhanVienDAO();
            NhanVien nv = nvDAO.layNhanVienTheoMaTK(tk.getMaTK());
            if (nv == null) {
                thongBao("Không tìm thấy nhân viên ứng với tài khoản đăng nhập!",
                        Alert.AlertType.ERROR);
                return;
            }
            hd.setNhanVien(nv);

            hd.setKhachHang(phieuDatBan.getKhachHang());
            hd.setPhieuDatBan(phieuDatBan);
            hd.setKhuyenMai(kmHienTai);
            hd.setTienKhachDua(tienKhachDua);
            hd.setTienThua(tienThua);
            hd.setPhuongThucThanhToan(cboPTTT.getValue());
            hd.setDeleted(false);

            // ===== LƯU HÓA ĐƠN =====
            if (!hoaDonDAO.themHoaDon(hd)) {
                thongBao("Lưu hóa đơn thất bại!", Alert.AlertType.ERROR);
                return;
            }

            // ===== LƯU CHI TIẾT =====
            for (ChiTietPDB ct : tblMon.getItems()) {
                chiTietHoaDonDAO.themChiTietHD(
                        hd.getMaHD(),
                        ct.getMonAn().getMaMonAn(),
                        ct.getSoLuong(),
                        ct.getMonAn().getDonGia()
                );
            }

            // ===== CẬP NHẬT TRẠNG THÁI PDB =====
            PhieuDatBanDAO pdbDAO = new PhieuDatBanDAO();
            boolean okPDB = pdbDAO.capNhatTrangThai(
                    phieuDatBan.getMaPDB(),
                    "Đã thanh toán"
            );

            if (!okPDB) {
                thongBao("Không thể cập nhật trạng thái phiếu đặt bàn!",
                        Alert.AlertType.WARNING);
            }

            BanDAO banDAO = new BanDAO();
            int soBanCapNhat = banDAO.capNhatTrangThaiDanhSach(
                    phieuDatBan.getDanhSachBan(),
                    "Trống"
            );

            if (soBanCapNhat == 0) {
                thongBao("Không thể cập nhật trạng thái bàn!",
                        Alert.AlertType.WARNING);
            }


            thongBao("Thanh toán thành công!", Alert.AlertType.INFORMATION);
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            thongBao("Lỗi khi thanh toán: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    /* ================== UTIL ================== */
    private String formatTien(BigDecimal t) {
        return new DecimalFormat("#,###").format(t);
    }

    private void thongBao(String msg, Alert.AlertType type) {
        new Alert(type, msg).showAndWait();
    }
}