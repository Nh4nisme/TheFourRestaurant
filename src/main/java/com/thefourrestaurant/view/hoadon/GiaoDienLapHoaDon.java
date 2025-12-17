package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.controller.*;
import com.thefourrestaurant.model.*;
import com.thefourrestaurant.util.Session;
import com.thefourrestaurant.view.ban.GiaoDienDatBan;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class GiaoDienLapHoaDon extends VBox {

    // ================== CONSTANT ==================

    // VAT mặc định 10%
    private static final BigDecimal VAT_RATE = BigDecimal.valueOf(0.1);

    // ================== STATE ==================

    // Stage hiện tại để đóng giao diện
    private final Stage stage;
    private final StackPane mainContent;

    // Phiếu đặt bàn đang xử lý
    private PhieuDatBan phieuDatBan;

    // Khuyến mãi được áp dụng (có thể null)
    private KhuyenMai khuyenMaiHienTai;

    // ================== CONTROLLERS ==================

    private final HoaDonController hoaDonController = new HoaDonController();
    private final ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
    private final KhuyenMaiController khuyenMaiController = new KhuyenMaiController();
    private final PhuongThucThanhToanController ptttController = new PhuongThucThanhToanController();
    private final NhanVienController nhanVienController = new NhanVienController();
    private final PhieuDatBanController phieuDatBanController = new PhieuDatBanController();
    private final BanController banController = new BanController();

    // ================== UI COMPONENTS ==================

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

    // ================== CONSTRUCTOR ==================

    public GiaoDienLapHoaDon(Stage stage, StackPane mainContent) {
        this.stage = stage;
        this.mainContent = mainContent;
        khoiTaoUI();
    }

    // ================== INIT UI ==================

    private void khoiTaoUI() {
        setPadding(new Insets(15));
        setSpacing(12);

        khoiTaoComboPTTT();

        VBox root = new VBox(15,
                taoHeader(),
                taoThongTinKhach(),
                taoBangMon(),
                taoKhungThanhToan(),
                taoFooter()
        );

        getChildren().add(root);

        stage.setScene(new Scene(this, 950, 800));
        stage.setTitle("Lập hóa đơn");
        stage.show();
    }

    // ================== HEADER ==================

    private Node taoHeader() {
        Label title = new Label("LẬP HÓA ĐƠN THANH TOÁN");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold;");
        return new HBox(title);
    }

    // ================== THÔNG TIN KHÁCH ==================

    private Node taoThongTinKhach() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        grid.addRow(0, new Label("Mã HD:"), lblMaHD, new Label("Mã PĐB:"), lblMaPDB);
        grid.addRow(1, new Label("Tên KH:"), lblTenKH, new Label("SĐT:"), lblSDT);
        grid.addRow(2, new Label("Ngày nhận:"), lblNgayNhan);

        return grid;
    }

    // ================== BẢNG MÓN ==================

    private Node taoBangMon() {
        TableColumn<ChiTietPDB, String> colTenMon = new TableColumn<>("Tên món");
        colTenMon.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMonAn().getTenMon()));

        TableColumn<ChiTietPDB, Integer> colSoLuong = new TableColumn<>("SL");
        colSoLuong.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getSoLuong()).asObject());

        TableColumn<ChiTietPDB, BigDecimal> colThanhTien = new TableColumn<>("Thành tiền");
        colThanhTien.setCellValueFactory(c ->
                new SimpleObjectProperty<>(tinhThanhTienMon(c.getValue()))
        );

        tblMon.getColumns().addAll(colTenMon, colSoLuong, colThanhTien);
        tblMon.setPrefHeight(250);

        return tblMon;
    }

    // ================== THANH TOÁN ==================

    private Node taoKhungThanhToan() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Khi khách nhập tiền → cập nhật tiền thừa realtime
        txtTienKhachDua.textProperty().addListener((o, oldVal, newVal) -> capNhatTienThua());

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

    // ================== FOOTER ==================

    private Node taoFooter() {
        Button btnXacNhan = new Button("Xác nhận");
        btnXacNhan.setOnAction(e -> xuLyXacNhanThanhToan());

        Button btnThoat = new Button("Thoát");
        btnThoat.setOnAction(e -> stage.close());

        HBox box = new HBox(10, btnThoat, btnXacNhan);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    // ================== COMBO PTTT ==================

    private void khoiTaoComboPTTT() {
        cboPTTT.getItems().setAll(ptttController.layPhuongThucThanhToan());

        cboPTTT.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(PhuongThucThanhToan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getLoaiPTTT().getTenHienThi());
            }
        });

        cboPTTT.setButtonCell(cboPTTT.getCellFactory().call(null));
        cboPTTT.getSelectionModel().selectFirst();
    }

    // ================== HIỂN THỊ ==================

    public void hienThiThongTinPhieuDatBan(PhieuDatBan pdb) {
        this.phieuDatBan = pdb;

        lblMaHD.setText(hoaDonController.taoMaHD());
        lblMaPDB.setText(pdb.getMaPDB());
        lblTenKH.setText(pdb.getKhachHang().getHoTen());
        lblSDT.setText(pdb.getKhachHang().getSoDT());
        lblNgayNhan.setText(pdb.getNgayDat().toString());

        tblMon.getItems().setAll(pdb.getChiTietPDB());

        BigDecimal tienCoc = pdb.getTienCoc() == null ? BigDecimal.ZERO : pdb.getTienCoc();
        lblTienCoc.setText(dinhDangTien(tienCoc) + " đ");

        capNhatSoTienThanhToan();
    }

    // ================== TÍNH TOÁN ==================

    private BigDecimal tinhThanhTienMon(ChiTietPDB ct) {
        return ct.getMonAn().getDonGia()
                .multiply(BigDecimal.valueOf(ct.getSoLuong()));
    }

    private BigDecimal tinhTongTienMon() {
        return tblMon.getItems().stream()
                .map(this::tinhThanhTienMon)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal tinhTienSauKhuyenMai(BigDecimal tong) {
        if (khuyenMaiHienTai == null) return tong;

        if (khuyenMaiHienTai.getTyLe() != null) {
            return tong.subtract(
                    tong.multiply(khuyenMaiHienTai.getTyLe())
                            .divide(BigDecimal.valueOf(100))
            );
        }

        if (khuyenMaiHienTai.getSoTien() != null) {
            return tong.subtract(khuyenMaiHienTai.getSoTien());
        }

        return tong;
    }

    private BigDecimal tinhTienThanhToan() {
        BigDecimal tong = tinhTongTienMon();
        tong = tinhTienSauKhuyenMai(tong);

        // Cộng VAT
        tong = tong.add(tong.multiply(VAT_RATE));

        // Trừ tiền cọc nếu có
        if (phieuDatBan.getTienCoc() != null) {
            tong = tong.subtract(phieuDatBan.getTienCoc());
        }

        return tong.max(BigDecimal.ZERO);
    }

    private void capNhatSoTienThanhToan() {
        lblTongTien.setText(dinhDangTien(tinhTongTienMon()) + " đ");
        lblThanhToan.setText(dinhDangTien(tinhTienThanhToan()) + " đ");
        capNhatTienThua();
    }

    private void capNhatTienThua() {
        try {
            BigDecimal khachDua = new BigDecimal(txtTienKhachDua.getText());
            BigDecimal tienThua = khachDua.subtract(tinhTienThanhToan());
            lblTienThua.setText(dinhDangTien(tienThua.max(BigDecimal.ZERO)) + " đ");
        } catch (Exception e) {
            lblTienThua.setText("0 đ");
        }
    }

    // ================== XỬ LÝ ==================

    private void xuLyKiemTraKhuyenMai() {
//        khuyenMaiHienTai =
//                khuyenMaiController.timKhuyenMaiTheoMaHoacTen(txtKhuyenMai.getText());
//
//        if (khuyenMaiHienTai == null) {
//            lblChietKhau.setText("0%");
//        } else if (khuyenMaiHienTai.getTyLe() != null) {
//            lblChietKhau.setText(khuyenMaiHienTai.getTyLe() + "%");
//        } else {
//            lblChietKhau.setText(dinhDangTien(khuyenMaiHienTai.getSoTien()) + " đ");
//        }

        capNhatSoTienThanhToan();
    }

    private void xuLyXacNhanThanhToan() {
        try {
            BigDecimal tienKhachDua = new BigDecimal(txtTienKhachDua.getText());
            BigDecimal canTra = tinhTienThanhToan();

            if (tienKhachDua.compareTo(canTra) < 0) {
                hienThongBao("Khách đưa chưa đủ tiền!", Alert.AlertType.WARNING);
                return;
            }

            TaiKhoan tk = Session.getCurrentUser();
            NhanVien nv = nhanVienController.layNhanVienTheoMaTK(tk.getMaTK());

            HoaDon hd = new HoaDon();
            hd.setMaHD(lblMaHD.getText());
            hd.setNgayLap(LocalDateTime.now());
            hd.setNhanVien(nv);
            hd.setKhachHang(phieuDatBan.getKhachHang());
            hd.setPhieuDatBan(phieuDatBan);
            hd.setKhuyenMai(khuyenMaiHienTai);
            hd.setTienKhachDua(tienKhachDua);
            hd.setTienThua(tienKhachDua.subtract(canTra));
            hd.setPhuongThucThanhToan(cboPTTT.getValue());
            hd.setDeleted(false);

            if (!hoaDonController.themHoaDon(hd)) {
                hienThongBao("Lưu hóa đơn thất bại!", Alert.AlertType.ERROR);
                return;
            }

            for (ChiTietPDB ct : tblMon.getItems()) {
                chiTietHoaDonController.themChiTietHoaDon(
                        hd.getMaHD(),
                        ct.getMonAn().getMaMonAn(),
                        ct.getSoLuong(),
                        ct.getMonAn().getDonGia()
                );
            }

            phieuDatBanController.capNhatTrangThai(
                    phieuDatBan.getMaPDB(), "Đã thanh toán");

            // log kiểm tra danh sách bàn
            System.out.println("=== DEBUG DANH SÁCH BÀN ===");

            if (phieuDatBan == null) {
                System.out.println("phieuDatBan = null");
            } else if (phieuDatBan.getDanhSachBan() == null) {
                System.out.println("Danh sách bàn = null");
            } else {
                System.out.println("Số bàn: " + phieuDatBan.getDanhSachBan().size());
                phieuDatBan.getDanhSachBan().forEach(ban ->
                        System.out.println(" - maBan = " + ban.getMaBan())
                );
            }

            BanDAO banDAO = new BanDAO();
            int soBanCapNhat = banDAO.capNhatTrangThaiDanhSach(
                    phieuDatBan.getDanhSachBan(),
                    "Trống"
            );
            if (soBanCapNhat == 0) {
                hienThongBao("Không thể cập nhật trạng thái bàn!",
                        Alert.AlertType.WARNING);
            }

            hienThongBao("Thanh toán thành công!", Alert.AlertType.INFORMATION);
            stage.close();
            mainContent.getChildren().setAll(new GiaoDienDatBan(mainContent));

        } catch (Exception e) {
            e.printStackTrace();
            hienThongBao("Lỗi khi thanh toán!", Alert.AlertType.ERROR);
        }
    }

    // ================== TIỆN ÍCH ==================

    private String dinhDangTien(BigDecimal t) {
        return new DecimalFormat("#,###").format(t);
    }

    private void hienThongBao(String msg, Alert.AlertType type) {
        new Alert(type, msg).showAndWait();
    }
}
