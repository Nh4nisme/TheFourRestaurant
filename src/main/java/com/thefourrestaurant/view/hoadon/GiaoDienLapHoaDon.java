package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.DAO.KhuyenMai_DieuKienDAO;
import com.thefourrestaurant.DAO.PhieuDatBan_BanDAO;
import com.thefourrestaurant.controller.*;
import com.thefourrestaurant.model.*;
import com.thefourrestaurant.util.Session;
import com.thefourrestaurant.view.ban.GiaoDienDatBan;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GiaoDienLapHoaDon extends VBox {

    // ================== CONST ==================
    private static final BigDecimal VAT_RATE = BigDecimal.valueOf(0.10);

    // ================== STATE ==================
    private final Stage stage;
    private final StackPane mainContent;
    private PhieuDatBan phieuDatBan;
    private KhuyenMai khuyenMaiHienTai;

    // ================== CONTROLLERS ==================
    private final HoaDonController hoaDonController = new HoaDonController();
    private final ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
    private final KhuyenMaiController khuyenMaiController = new KhuyenMaiController();
    private final PhuongThucThanhToanController ptttController = new PhuongThucThanhToanController();
    private final NhanVienController nhanVienController = new NhanVienController();
    private final PhieuDatBanController phieuDatBanController = new PhieuDatBanController();

    // ================== UI ==================
    private final Label lblMaHD = new Label();
    private final Label lblMaPDB = new Label();
    private final Label lblTenKH = new Label();
    private final Label lblSDT = new Label();
    private final Label lblNgayNhan = new Label();
    private final Label lblNhanVien = new Label();

    private final Label lblTongTien = new Label("0 đ");
    private final Label lblVAT = new Label("10%");
    private final Label lblChietKhau = new Label("0%");
    private final Label lblThanhToan = new Label("0 đ");
    private final Label lblTienThua = new Label("0 đ");
    private final Label lblTienCoc = new Label("0 đ");

    private final TextField txtTienKhachDua = new TextField();
    private final ComboBox<PhuongThucThanhToan> cboPTTT = new ComboBox<>();
    private final ComboBox<KhuyenMai> cboKhuyenMai = new ComboBox<>();

    private final TableView<ChiTietPDB> tblMon = new TableView<>();

    // QR overlay
    private final StackPane qrOverlay = new StackPane();

    // ================== CONSTRUCTOR ==================
    public GiaoDienLapHoaDon(Stage stage, StackPane mainContent) {
        this.stage = stage;
        this.mainContent = mainContent;
        khoiTaoUI();
        getStyleClass().add("hoa-don-root");
    }

    // ================== INIT ==================
    private void khoiTaoUI() {
        setPadding(new Insets(20));
        setSpacing(15);

        khoiTaoComboPTTT();
        khoiTaoComboKhuyenMai();
        khoiTaoQR();

        getChildren().addAll(
                taoTitle(),
                taoHeader(),
                taoBangMon(),
                taoThanhToan(),
                taoFooter()
        );

        Scene scene = new Scene(new StackPane(this, qrOverlay), 1000, 780);
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/com/thefourrestaurant/css/Application.css")
                ).toExternalForm()
        );

        stage.setScene(scene);
        stage.setTitle("Lập hóa đơn");
        stage.show();
    }

    private Node taoTitle() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);

        Label title = new Label("LẬP HÓA ĐƠN THANH TOÁN");
        title.getStyleClass().add("hoa-don-title");
        hBox.getChildren().add(title);

        return hBox;
    }

    // ================== HEADER ==================
    private Node taoHeader() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("hoa-don-header");
        grid.setHgap(5);

        grid.addRow(1, new Label("Mã HD:"), lblMaHD, new Label("Nhân viên:"), lblNhanVien);
        grid.addRow(2, new Label("Mã PĐB:"), lblMaPDB, new Label("Ngày:"), lblNgayNhan);
        grid.addRow(3, new Label("Khách hàng:"), lblTenKH, new Label("SĐT:"), lblSDT);

        return grid;
    }

    // ================== TABLE ==================
    private Node taoBangMon() {
        TableColumn<ChiTietPDB, String> colTen = new TableColumn<>("Món ăn");
        colTen.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMonAn().getTenMon()));

        TableColumn<ChiTietPDB, Integer> colSL = new TableColumn<>("SL");
        colSL.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getSoLuong()).asObject());

        TableColumn<ChiTietPDB, BigDecimal> colTien = new TableColumn<>("Thành tiền");
        colTien.setCellValueFactory(c ->
                new SimpleObjectProperty<>(tinhThanhTienMon(c.getValue())));
        colTien.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VND", item.doubleValue()));
                }
            }
        });

        tblMon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblMon.getColumns().setAll(colTen, colSL, colTien);
        tblMon.setPrefHeight(260);
        tblMon.getStyleClass().add("hoa-don-table");

        return tblMon;
    }


    // ================== PAYMENT ==================
    private Node taoThanhToan() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("hoa-don-payment");

        txtTienKhachDua.textProperty().addListener((o, a, b) -> capNhatTienThua());

        grid.addRow(0, new Label("Khuyến mãi:"), cboKhuyenMai);
        grid.addRow(1, new Label("Chiết khấu:"), lblChietKhau);
        grid.addRow(2, new Label("VAT:"), lblVAT);
        grid.addRow(3, new Label("Tổng tiền:"), lblTongTien);
        grid.addRow(4, new Label("Tiền cọc:"), lblTienCoc);
        grid.addRow(5, new Label("PTTT:"), cboPTTT);
        grid.addRow(6, new Label("Tiền khách đưa:"), txtTienKhachDua);
        grid.addRow(7, new Label("Tiền thừa:"), lblTienThua);
        grid.addRow(8, new Label("Phải thanh toán:"), lblThanhToan);

        return grid;
    }

    // ================== FOOTER ==================
    private Node taoFooter() {
        ButtonSample btnBack = new ButtonSample("Quay lại", "", 45, 16, 3);
        btnBack.setOnAction(e -> stage.close());

        ButtonSample btnOK = new  ButtonSample("Xác nhận thanh toán", "", 45, 16, 3);
        btnOK.setOnAction(e -> xuLyXacNhanThanhToan());

        HBox box = new HBox(15, btnBack, btnOK);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    // ================== QR ==================
    private void khoiTaoQR() {
        // Overlay nền mờ
        qrOverlay.setVisible(false);
        qrOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        // QR image
        ImageView qr = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/thefourrestaurant/images/QR.png")
                ))
        );
        qr.setFitWidth(260);
        qr.setPreserveRatio(true);

        Label lbl = new Label("Quét mã để thanh toán");
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        // ===== BUTTONS =====
        ButtonSample btnXacNhan = new ButtonSample(
                "Xác nhận", "", 40, 14, 3
        );
        btnXacNhan.setOnAction(e -> xuLyXacNhanThanhToan());

        ButtonSample btnQuayLai = new ButtonSample(
                "Quay lại", "", 40, 14, 3
        );
        btnQuayLai.setOnAction(e -> qrOverlay.setVisible(false));

        HBox boxBtn = new HBox(12, btnQuayLai, btnXacNhan);
        boxBtn.setAlignment(Pos.CENTER);

        // ===== CONTENT =====
        VBox content = new VBox(15, qr, lbl, boxBtn);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(25));
        content.setStyle("""
        -fx-background-color: rgba(255,255,255,0.15);
        -fx-background-radius: 12;
        -fx-border-color: rgba(255,255,255,0.4);
        -fx-border-radius: 12;
    """);

        qrOverlay.getChildren().add(content);
    }


    private void khoiTaoComboPTTT() {
        cboPTTT.getItems().setAll(ptttController.layPhuongThucThanhToan());
        cboPTTT.getSelectionModel().selectFirst();

        cboPTTT.valueProperty().addListener((obs, o, n) -> {
            if (n.getLoaiPTTT().getTenHienThi().equalsIgnoreCase("Chuyển khoản")) {
                qrOverlay.setVisible(true);
                txtTienKhachDua.setText(tinhTienThanhToan().toString());
                txtTienKhachDua.setDisable(true);
            } else {
                qrOverlay.setVisible(false);
                txtTienKhachDua.clear();
                txtTienKhachDua.setDisable(false);
            }
        });
    }

    private void khoiTaoComboKhuyenMai() {
        // Lấy danh sách khuyến mãi
        cboKhuyenMai.getItems().setAll(khuyenMaiController.layDanhSachKhuyenMai());

        // Hiển thị tên khuyến mãi trong ComboBox
        cboKhuyenMai.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(KhuyenMai item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.getTenKM());
                }
            }
        });

        cboKhuyenMai.setButtonCell(cboKhuyenMai.getCellFactory().call(null));
        cboKhuyenMai.setPromptText("Áp dụng khuyến mãi");
        cboKhuyenMai.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                khuyenMaiHienTai = null;
            } else {
                // Lấy danh sách điều kiện từ DB
                List<KhuyenMai_DieuKien> dsDieuKien = new KhuyenMai_DieuKienDAO()
                        .layDieuKienTheoMaKM(newVal.getMaKM());

                // Lấy điều kiện đầu tiên (hoặc xử lý theo nhu cầu)
                newVal.setKhuyenMaiDieuKien(dsDieuKien.isEmpty() ? null : dsDieuKien.get(0));

                khuyenMaiHienTai = newVal;
            }

            capNhatHienThiKhuyenMai();
            capNhatSoTienThanhToan();
        });

    }


    // ================== DISPLAY ==================
    public void hienThiThongTinPhieuDatBan(PhieuDatBan pdb) {
        this.phieuDatBan = pdb;
        
        PhieuDatBan_BanDAO pdbBanDAO = new PhieuDatBan_BanDAO();
        pdb.setDanhSachBan(pdbBanDAO.layDanhSachBanTheoPhieu(pdb.getMaPDB()));

        lblMaHD.setText(hoaDonController.taoMaHD());
        lblMaPDB.setText(pdb.getMaPDB());
        lblTenKH.setText(pdb.getKhachHang().getHoTen());
        lblSDT.setText(pdb.getKhachHang().getSoDT());
        lblNgayNhan.setText(pdb.getNgayDat().toString());

        TaiKhoan tk = Session.getCurrentUser();
        NhanVien nv = nhanVienController.layNhanVienTheoMaTK(tk.getMaTK());
        lblNhanVien.setText(nv.getHoTen());

        List<ChiTietPDB> danhSachDaGop =
                hoaDonController.layChiTietHienThi(pdb.getMaPDB());

        tblMon.getItems().clear();
        tblMon.getItems().setAll(danhSachDaGop);
        lblTienCoc.setText(dinhDangTien(
                pdb.getTienCoc() == null ? BigDecimal.ZERO : pdb.getTienCoc()) + " đ");

        capNhatSoTienThanhToan();
    }

    private void capNhatHienThiKhuyenMai() {
        if (khuyenMaiHienTai == null) {
            lblChietKhau.setText("0");
            return;
        }

        KhuyenMai_DieuKien dk = khuyenMaiHienTai.getKhuyenMaiDieuKien();

        if (dk == null) {
            lblChietKhau.setText("0");
            return;
        }

        if (dk.getTyLeGiam() != null) {
            lblChietKhau.setText(dk.getTyLeGiam() + "%");
        }
        else if (dk.getSoTienGiam() != null) {
            lblChietKhau.setText(dinhDangTien(dk.getSoTienGiam()) + " đ");
        }
        else {
            lblChietKhau.setText("0");
        }
    }


    // ================== LOGIC (GIỮ NGUYÊN) ==================
    private BigDecimal tinhThanhTienMon(ChiTietPDB ct) {
        return ct.getMonAn().getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong()));
    }

    private BigDecimal tinhTongTienMon() {
        return tblMon.getItems().stream()
                .map(this::tinhThanhTienMon)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal tinhTienSauKhuyenMai(BigDecimal tong) {
        if (khuyenMaiHienTai == null || khuyenMaiHienTai.getKhuyenMaiDieuKien() == null) {
            return tong;
        }

        KhuyenMai_DieuKien dk = khuyenMaiHienTai.getKhuyenMaiDieuKien();
        String loaiApDung = dk.getLoaiApDung();

        if ("GIAM_TRUC_TIEP".equalsIgnoreCase(loaiApDung)) {
            // Giảm theo tỷ lệ %
            if (dk.getTyLeGiam() != null) {
                BigDecimal tyLeKM = dk.getTyLeGiam().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                return tong.subtract(tong.multiply(tyLeKM));
            }
            // Giảm theo số tiền
            if (dk.getSoTienGiam() != null) {
                return tong.subtract(dk.getSoTienGiam());
            }
        }
        return tong.max(BigDecimal.ZERO);
    }

    private BigDecimal tinhTienThanhToan() {
        BigDecimal tong = tinhTienSauKhuyenMai(tinhTongTienMon());
        tong = tong.add(tong.multiply(VAT_RATE));
        if (phieuDatBan.getTienCoc() != null)
            tong = tong.subtract(phieuDatBan.getTienCoc());
        return tong.max(BigDecimal.ZERO);
    }

    private void capNhatSoTienThanhToan() {
        lblTongTien.setText(dinhDangTien(tinhTongTienMon()) + " đ");
        lblThanhToan.setText(dinhDangTien(tinhTienThanhToan()) + " đ");
        capNhatTienThua();
    }

    private void capNhatTienThua() {
        try {
            BigDecimal khach = new BigDecimal(txtTienKhachDua.getText());
            lblTienThua.setText(dinhDangTien(khach.subtract(tinhTienThanhToan())) + " đ");
        } catch (Exception e) {
            lblTienThua.setText("0 đ");
        }
    }

    private String dinhDangTien(BigDecimal t) {
        return new DecimalFormat("#,###").format(t);
    }

    private void xuLyXacNhanThanhToan() {
        try {
            // ===== TIỀN KHÁCH ĐƯA =====
            BigDecimal tienKhachDua = new BigDecimal(txtTienKhachDua.getText());
            BigDecimal canTra = tinhTienThanhToan();

            if (tienKhachDua.compareTo(canTra) < 0) {
                hienThongBao("Khách đưa chưa đủ tiền!", Alert.AlertType.WARNING);
                return;
            }

            // ===== LẤY NHÂN VIÊN HIỆN TẠI =====
            TaiKhoan tk = Session.getCurrentUser();
            NhanVien nv = nhanVienController.layNhanVienTheoMaTK(tk.getMaTK());

            // ===== THUẾ MẶC ĐỊNH =====
            Thue thueMacDinh = new Thue();
            thueMacDinh.setMaThue("TH000001");

            // ===== TẠO HÓA ĐƠN =====
            HoaDon hd = new HoaDon();
            hd.setMaHD(lblMaHD.getText());
            hd.setNgayLap(LocalDateTime.now());
            hd.setNhanVien(nv);
            hd.setKhachHang(phieuDatBan.getKhachHang());
            hd.setPhieuDatBan(phieuDatBan);
            hd.setKhuyenMai(khuyenMaiHienTai);
            hd.setThue(thueMacDinh);
            hd.setTienKhachDua(tienKhachDua);
            hd.setTienThua(tienKhachDua.subtract(canTra));
            hd.setPhuongThucThanhToan(cboPTTT.getValue());
            hd.setDeleted(false);

            if (!hoaDonController.themHoaDon(hd)) {
                hienThongBao("Lưu hóa đơn thất bại!", Alert.AlertType.ERROR);
                return;
            }

            // ===== LƯU CHI TIẾT HÓA ĐƠN =====
            for (ChiTietPDB ct : tblMon.getItems()) {
                chiTietHoaDonController.themChiTietHoaDon(
                        hd.getMaHD(),
                        ct.getMonAn().getMaMonAn(),
                        ct.getSoLuong(),
                        ct.getMonAn().getDonGia()
                );
            }

            // ===== CẬP NHẬT PHIẾU ĐẶT BÀN =====
            phieuDatBanController.capNhatTrangThai(
                    phieuDatBan.getMaPDB(),
                    "Đã thanh toán"
            );

            // ===== DEBUG DANH SÁCH BÀN =====
            System.out.println("=== DEBUG DANH SÁCH BÀN ===");
            if (phieuDatBan == null) {
                System.out.println("phieuDatBan = null");
            } else if (phieuDatBan.getDanhSachBan() == null) {
                System.out.println("Danh sách bàn = null");
            } else {
                System.out.println("Số bàn: " + phieuDatBan.getDanhSachBan().size());
                phieuDatBan.getDanhSachBan()
                        .forEach(ban -> System.out.println(" - maBan = " + ban.getMaBan()));
            }

            // ===== CẬP NHẬT TRẠNG THÁI BÀN =====
            BanDAO banDAO = new BanDAO();
            int soBanCapNhat = banDAO.capNhatTrangThaiDanhSach(
                    phieuDatBan.getDanhSachBan(),
                    "Trống"
            );

            if (soBanCapNhat == 0) {
                hienThongBao("Không thể cập nhật trạng thái bàn!", Alert.AlertType.WARNING);
            }

            // ===== HOÀN TẤT =====
            hienThongBao("Thanh toán thành công!", Alert.AlertType.INFORMATION);
            stage.close();
            mainContent.getChildren().setAll(new GiaoDienDatBan(mainContent));

        } catch (Exception e) {
            e.printStackTrace();
            hienThongBao("Lỗi khi thanh toán!", Alert.AlertType.ERROR);
        }
    }

    private void hienThongBao(String msg, Alert.AlertType type) { new Alert(type, msg).showAndWait(); }
}