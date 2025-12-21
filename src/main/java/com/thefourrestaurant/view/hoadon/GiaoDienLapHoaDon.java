package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.DAO.PhieuDatBan_BanDAO;
import com.thefourrestaurant.controller.*;
import com.thefourrestaurant.model.*;
import com.thefourrestaurant.util.khoiTaoHoaDonPDF;
import com.thefourrestaurant.util.Session;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiaoDienLapHoaDon extends VBox {

    // ================== CONST ==================
    private static final BigDecimal VAT_RATE = BigDecimal.valueOf(0.10);

    // ================== STATE ==================
    private final StackPane mainContent;
    private final StackPane overlay;
    private PhieuDatBan phieuDatBan;
    private KhuyenMai khuyenMaiHienTai;
    private List<ChiTietPDB> originalMonList = new ArrayList<>();

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
    private final CheckBox chkInHoaDon = new CheckBox("In hóa đơn");


    private final TableView<ChiTietPDB> tblMon = new TableView<>();

    // QR overlay
    private final StackPane qrOverlay = new StackPane();

    // Callback
    private Runnable onThanhToanThanhCong;

    // ================== CONSTRUCTOR ==================
    public GiaoDienLapHoaDon(StackPane mainContent, StackPane overlay) {
        this.overlay = overlay;
        this.mainContent = mainContent;

        khoiTaoUI();

        StackPane popupWrapper = new StackPane(this);
        popupWrapper.setMaxWidth(1000);
        popupWrapper.setMaxHeight(780);

        qrOverlay.setVisible(false);
        popupWrapper.getChildren().add(qrOverlay);
        StackPane.setAlignment(qrOverlay, Pos.CENTER);

        this.getProperties().put("popupWrapper", popupWrapper);

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

        setMaxWidth(1000);
        setMaxHeight(780);
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

        grid.addRow(0, new Label("Khuyến mãi: "), cboKhuyenMai);
        grid.addRow(1, new Label("Chiết khấu: "), lblChietKhau);
        grid.addRow(2, new Label("VAT: "), lblVAT);
        grid.addRow(3, new Label("Tổng tiền: "), lblTongTien);
        grid.addRow(4, new Label("Tiền cọc: "), lblTienCoc);
        grid.addRow(5, new Label("PTTT: "), cboPTTT);
        grid.addRow(6, new Label("Tiền khách đưa: "), txtTienKhachDua);
        grid.addRow(7, new Label("Tiền thừa: "), lblTienThua);
        grid.addRow(8, new Label("Phải thanh toán: "), lblThanhToan);

        return grid;
    }

    // ================== FOOTER ==================
    private Node taoFooter() {
        ButtonSample btnBack = new ButtonSample("Quay lại", "", 45, 16, 3);
        btnBack.setOnAction(e -> mainContent.getChildren().remove(overlay));

        ButtonSample btnOK = new ButtonSample("Xác nhận thanh toán", "", 45, 16, 3);
        btnOK.setOnAction(e -> xuLyXacNhanThanhToan());

        chkInHoaDon.setSelected(true);
        chkInHoaDon.setStyle("-fx-font-size: 16; -fx-text-fill: #DDB248; -fx-font-weight: bold;");

        VBox leftBox = new VBox(chkInHoaDon);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        HBox box = new HBox(20, leftBox, btnBack, btnOK);
        box.setAlignment(Pos.CENTER_RIGHT);

        return box;
    }



    // ================== QR ==================
    private void khoiTaoQR() {
        // Overlay nền mờ
        qrOverlay.setVisible(false);
        qrOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        qrOverlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

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
        cboPTTT.setPrefWidth(220);

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
        cboKhuyenMai.setPromptText("Áp dụng khuyến mãi");
        cboKhuyenMai.setPrefWidth(220);

        // Hiển thị tên KM
        cboKhuyenMai.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(KhuyenMai item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? "" : item.getTenKM());
            }
        });

        cboKhuyenMai.setButtonCell(cboKhuyenMai.getCellFactory().call(null));
        cboKhuyenMai.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                khuyenMaiHienTai = null;
            } else {
                if (newVal.getKhuyenMaiDieuKien() == null) {
                    List<KhuyenMai_DieuKien> dsDieuKien =
                            khuyenMaiController.layDieuKienTheoMaKM(newVal.getMaKM());
                    newVal.setKhuyenMaiDieuKien(
                            dsDieuKien.isEmpty() ? null : dsDieuKien.getFirst()
                    );
                }
                khuyenMaiHienTai = newVal;
            }

            applyKhuyenMai();
            capNhatHienThiKhuyenMai();
            capNhatSoTienThanhToan();
        });
    }

    private void loadKhuyenMaiHopLe(BigDecimal tongTien) {
        List<KhuyenMai> dsKM = khuyenMaiController.layKhuyenMaiConHieuLucTheoKieu("MaGiamGia");
        List<KhuyenMai> dsFiltered = new ArrayList<>();

        for (KhuyenMai km : dsKM) {
            List<KhuyenMai_DieuKien> dsDK = khuyenMaiController.layDieuKienTheoMaKM(km.getMaKM());
            if (dsDK.isEmpty()) {
                dsFiltered.add(km);
                continue;
            }

            KhuyenMai_DieuKien dk = dsDK.getFirst();
            km.setKhuyenMaiDieuKien(dk);

            if (dk.getGiaToiThieu() == null || tongTien.compareTo(dk.getGiaToiThieu()) >= 0) {
                dsFiltered.add(km);
            }
        }

        cboKhuyenMai.getItems().clear();
        if (dsFiltered.isEmpty()) {
            cboKhuyenMai.setPromptText("Không có khuyến mãi hợp lệ");
            cboKhuyenMai.setDisable(true);
        } else {
            cboKhuyenMai.setDisable(false);
            cboKhuyenMai.setPromptText("Áp dụng khuyến mãi");
            cboKhuyenMai.getItems().setAll(dsFiltered);
        }
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

        this.originalMonList = new ArrayList<>(danhSachDaGop);
        tblMon.getItems().clear();
        tblMon.getItems().setAll(danhSachDaGop);
        lblTienCoc.setText(dinhDangTien(
                pdb.getTienCoc() == null ? BigDecimal.ZERO : pdb.getTienCoc()) + " đ");

        applyKhuyenMai();
        capNhatSoTienThanhToan();
        loadKhuyenMaiHopLe(tinhTongTienMon());
    }

    private void capNhatHienThiKhuyenMai() {
        if (khuyenMaiHienTai == null) {lblChietKhau.setText("0"); return;}

        KhuyenMai_DieuKien dk = khuyenMaiHienTai.getKhuyenMaiDieuKien();

        if (dk == null) {lblChietKhau.setText("0"); return;}
        if (dk.getTyLeGiam() != null) {lblChietKhau.setText(dk.getTyLeGiam() + "%");}
        else if (dk.getSoTienGiam() != null) {lblChietKhau.setText(dinhDangTien(dk.getSoTienGiam()) + " đ");}
        else {lblChietKhau.setText("0");}
    }

    private void applyKhuyenMai() {
        tblMon.getItems().setAll(originalMonList);
    }


    // ================== LOGIC ==================
    private BigDecimal tinhThanhTienMon(ChiTietPDB ct) {
        return BigDecimal.valueOf(ct.getDonGia()).multiply(BigDecimal.valueOf(ct.getSoLuong()));
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
                        BigDecimal.valueOf(ct.getDonGia())
                );
            }

            // ===== XỬ LÝ VIP SAU THANH TOÁN =====
            hoaDonController.xuLyVIPSauThanhToan(hd);

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
            assert phieuDatBan != null;
            int soBanCapNhat = banDAO.capNhatTrangThaiDanhSach(
                    phieuDatBan.getDanhSachBan(),
                    "Trống"
            );

            if (soBanCapNhat == 0) {
                hienThongBao("Không thể cập nhật trạng thái bàn!", Alert.AlertType.WARNING);
            }

            // ===== HOÀN TẤT =====
            if (chkInHoaDon.isSelected()) {
                inHoaDonPDF();
            }

            hienThongBao("Thanh toán thành công!", Alert.AlertType.INFORMATION);
            mainContent.getChildren().remove(overlay);
            if (onThanhToanThanhCong != null) {
                onThanhToanThanhCong.run();
            }

        } catch (Exception e) {
            hienThongBao("Lỗi khi thanh toán!", Alert.AlertType.ERROR);
        }
    }

    private void hienThongBao(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type, msg);
        alert.initOwner(mainContent.getScene().getWindow());
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
    }

    private void inHoaDonPDF() {
        try {
            String filePath = System.getProperty("user.home")
                    + "/HoaDon_" + lblMaHD.getText() + ".pdf";

            khoiTaoHoaDonPDF.inHoaDon(
                    filePath,
                    lblMaHD.getText(),
                    lblNgayNhan.getText(),
                    nhanVienController.layNhanVienTheoMaTK(Session.getCurrentUser().getMaTK()),
                    phieuDatBan.getKhachHang(),
                    tblMon.getItems(),
                    cboKhuyenMai.getValue(),
                    phieuDatBan.getTienCoc(),
                    new BigDecimal(txtTienKhachDua.getText()),
                    new BigDecimal(lblTienThua.getText().replaceAll("[^\\d]", ""))
            );

            java.awt.Desktop.getDesktop().open(new java.io.File(filePath));

        } catch (Exception e) {
            e.printStackTrace();
            hienThongBao("Không thể in hóa đơn!", Alert.AlertType.ERROR);
        }
    }

    public void setOnThanhToanThanhCong(Runnable r) {
        this.onThanhToanThanhCong = r;
    }
}