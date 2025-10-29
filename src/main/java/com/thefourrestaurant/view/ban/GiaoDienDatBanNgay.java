package com.thefourrestaurant.view.ban;
import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;
import com.thefourrestaurant.DAO.KhachHangDAO;
import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.DAO.PhieuDatBan_BanDAO;
import com.thefourrestaurant.DAO.NhanVienDAO;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.util.Session;
import com.thefourrestaurant.DAO.LoaiBanDAO;
import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.monan.GiaoDienGoiMon;
import com.thefourrestaurant.view.khachhang.GiaoDienThemKhachHang;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.time.LocalDateTime;

public class GiaoDienDatBanNgay extends VBox {

    private Label lblTrangThaiStatus;
    private Label lblLoaiBanValue;
    private TextField txtSoNguoi;
    private Label lblGiaTienValue;
    private TextField txtSDTKhachDat;
    private Label lblTenKhachDat;
    private Button btnKiemTra;
    private Button btnDatBan;
    private Button btnQuayLai;

    private final PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private KhachHang selectedKhachHang;
    private Ban ban;
    private final StackPane parentPane;
    private QuanLiBan quanLiBan;
    private List<Ban> dsBan = new ArrayList<>();
    
    public GiaoDienDatBanNgay(List<Ban> dsBan, StackPane parentPane, QuanLiBan quanLiBan) {
        this(dsBan != null && !dsBan.isEmpty() ? dsBan.get(0) : null, parentPane, quanLiBan);
        this.dsBan = dsBan != null ? dsBan : new ArrayList<>();

        // Nếu chọn nhiều bàn -> hiển thị danh sách bàn
        if (dsBan != null && dsBan.size() > 1) {
            String danhSach = dsBan.stream().map(Ban::getTenBan).collect(java.util.stream.Collectors.joining(", "));
            Label lblDsBan = new Label("Các bàn được chọn: " + danhSach);
            lblDsBan.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-font-weight: bold;");
            ((VBox) ((VBox) getChildren().get(1)).getChildren().get(0)).getChildren().add(1, lblDsBan);
        }
    }

    public GiaoDienDatBanNgay(Ban ban, StackPane parentPane, QuanLiBan quanLiBan) {
        this.ban = ban;
        this.parentPane = parentPane;
        this.quanLiBan = quanLiBan;
        
        setStyle("-fx-background-color: #F5F5F5;");
        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);

        Label lblTitle = new Label("Đặt bàn");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
        HBox titleBar = new HBox(lblTitle);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(10, 20, 10, 20));
        titleBar.setStyle("-fx-background-color: #1E424D;");
        titleBar.setPrefHeight(50);

        VBox contentCard = new VBox(20);
        contentCard.setStyle("-fx-background-color: transparent;");
        contentCard.setPadding(new Insets(30));
        contentCard.setMaxWidth(650);
        contentCard.setAlignment(Pos.TOP_CENTER);

        Label lblBanHeader = new Label(ban.getTenBan());
        lblBanHeader.setStyle("-fx-font-size: 22px; -fx-text-fill: #DDB248; -fx-font-weight: bold;");

        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER_LEFT);

        // Row 1: Trạng thái and Loại bàn
        HBox row1 = new HBox(20);
        row1.setAlignment(Pos.CENTER_LEFT);

        Label lblTrangThai = createLabel("Trạng Thái:");
        lblTrangThai.setPrefWidth(120);
        lblTrangThaiStatus = new Label(ban.getTrangThai());
        lblTrangThaiStatus.setStyle("-fx-font-size:14px; -fx-text-fill: black;");
        lblTrangThaiStatus.setPrefWidth(230);

        Label lblLoaiBan = createLabel("Loại bàn:");
        lblLoaiBan.setPrefWidth(100);
        // Hiển thị Loại bàn như Trạng thái (chỉ đọc)
        String tenLoaiBan = null;
        try {
            if (ban != null && ban.getMaBan() != null) {
                tenLoaiBan = new LoaiBanDAO().layTenLoaiTheoBan(ban.getMaBan());
                if (tenLoaiBan == null && ban.getLoaiBan() != null) {
                    tenLoaiBan = ban.getLoaiBan().getTenLoaiBan();
                }
            }
        } catch (Exception ignore) {}
        lblLoaiBanValue = new Label(tenLoaiBan != null ? tenLoaiBan : "");
        lblLoaiBanValue.setStyle("-fx-font-size:14px; -fx-text-fill: black;");
        lblLoaiBanValue.setPrefWidth(230);

        row1.getChildren().addAll(lblTrangThai, lblTrangThaiStatus, lblLoaiBan, lblLoaiBanValue);

        // Row 2: Số người and Giá tiền
        HBox row2 = new HBox(20);
        row2.setAlignment(Pos.CENTER_LEFT);

        Label lblSoNguoi = createLabel("Số người:");
        lblSoNguoi.setPrefWidth(120);
        txtSoNguoi = createNumericTextField(Pattern.compile("[0-9]{0,2}"));
        txtSoNguoi.setPromptText("Chỉ nhập số");
        txtSoNguoi.setPrefWidth(230);

    Label lblGiaTien = createLabel("Giá tiền:");
    lblGiaTien.setPrefWidth(100);
    // Giá tiền hiển thị tự động theo loại bàn (chỉ đọc)
    lblGiaTienValue = new Label("");
    lblGiaTienValue.setStyle("-fx-font-size:14px; -fx-text-fill: black;");
    lblGiaTienValue.setPrefWidth(230);

    row2.getChildren().addAll(lblSoNguoi, txtSoNguoi, lblGiaTien, lblGiaTienValue);

        // Row 3: SDT khách đặt
        HBox row3 = new HBox(10);
        row3.setAlignment(Pos.CENTER_LEFT);
        Label lblSDT = createLabel("SDT khách đặt:");
        lblSDT.setPrefWidth(120);
        txtSDTKhachDat = createNumericTextField(Pattern.compile("\\d{0,11}"));
        txtSDTKhachDat.setPromptText("Chỉ nhập số (10-11 chữ số)");
        HBox.setHgrow(txtSDTKhachDat, Priority.ALWAYS);
        btnKiemTra = new ButtonSample2("Kiểm tra", Variant.YELLOW, 100);
        row3.getChildren().addAll(lblSDT, txtSDTKhachDat, btnKiemTra);

        // Row 4: Tên khách đặt
        HBox row4 = new HBox(10);
        row4.setAlignment(Pos.CENTER_LEFT);
        Label lblTenKhach = createLabel("Tên khách đặt:");
        lblTenKhach.setPrefWidth(120);
        lblTenKhachDat = new Label("");
        lblTenKhachDat.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        row4.getChildren().addAll(lblTenKhach, lblTenKhachDat);

        formBox.getChildren().addAll(row1, row2, row3, row4);

        HBox buttonBar = new HBox(20);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        buttonBar.setPadding(new Insets(20, 0, 0, 0));

        btnQuayLai = new ButtonSample2("Quay lại", Variant.YELLOW, 100);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnDatBan = new ButtonSample2("Đặt bàn", Variant.YELLOW, 100);

        buttonBar.getChildren().addAll(btnQuayLai, spacer, btnDatBan);

        contentCard.getChildren().addAll(lblBanHeader, formBox, buttonBar);

        VBox centerWrapper = new VBox(contentCard);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPadding(new Insets(40));
        VBox.setVgrow(centerWrapper, Priority.ALWAYS);

        getChildren().addAll(titleBar, centerWrapper);

        wireHandlers();
        loadLoaiBan();
        loadGiaTienTheoLoaiBan();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
        label.setMinWidth(Region.USE_PREF_SIZE);
        return label;
    }

    private TextField createTextField() {
        TextField textField = new TextField();
        textField.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
        textField.setPrefHeight(35);
        return textField;
    }

    private TextField createNumericTextField(Pattern pattern) {
        TextField tf = createTextField();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return pattern.matcher(newText).matches() ? change : null;
        };
        tf.setTextFormatter(new TextFormatter<>(filter));
        return tf;
    }

    private ComboBox<String> createComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
        comboBox.setPrefHeight(35);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        return comboBox;
    }

    private void loadLoaiBan() {
        try {
            String tenLoai = null;
            if (ban != null && ban.getMaBan() != null) {
                tenLoai = new LoaiBanDAO().layTenLoaiTheoBan(ban.getMaBan());
                if (tenLoai == null && ban.getLoaiBan() != null) {
                    tenLoai = ban.getLoaiBan().getTenLoaiBan();
                }
            }
            lblLoaiBanValue.setText(tenLoai != null ? tenLoai : "");
        } catch (Exception ignore) {
            lblLoaiBanValue.setText("");
        }
    }
 
    private void loadGiaTienTheoLoaiBan() {
        try {
            BigDecimal gia = null;
            if (ban != null) {
                if (ban.getLoaiBan() != null && ban.getLoaiBan().getGiaTien() != null) {
                    gia = ban.getLoaiBan().getGiaTien();
                } else if (ban.getMaBan() != null) {
                    Ban refreshed = new BanDAO().layTheoMa(ban.getMaBan());
                    if (refreshed != null && refreshed.getLoaiBan() != null) {
                        gia = refreshed.getLoaiBan().getGiaTien();
                    }
                }
            }
            if (gia == null || gia.compareTo(BigDecimal.ZERO) <= 0) {
                String tenLoai = lblLoaiBanValue != null ? lblLoaiBanValue.getText() : null;
                if (tenLoai != null) {
                    if (tenLoai.contains("8")) {
                        gia = new BigDecimal(500000);
                    } else if (tenLoai.contains("6")) {
                        gia = new BigDecimal(400000);
                    } else if (tenLoai.contains("4")) {
                        gia = new BigDecimal(300000);
                    } else if (tenLoai.contains("2")) {
                        gia = new BigDecimal(200000);
                    }
                }
            }
            lblGiaTienValue.setText(gia != null ? dinhDangTienVND(gia) + " VNĐ" : "");
        } catch (Exception e) {
            lblGiaTienValue.setText("");
        }
    }

    private String dinhDangTienVND(BigDecimal amount) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        nf.setMaximumFractionDigits(0);
        return nf.format(amount);
    }
 
    
    private void wireHandlers() {
        btnKiemTra.setOnAction(e -> {
            String sdt = txtSDTKhachDat.getText() == null ? "" : txtSDTKhachDat.getText().trim();
            if (sdt.length() < 10) {
                lblTenKhachDat.setText("SDT không hợp lệ");
                return;
            }
            KhachHang kh = khachHangDAO.layKhachHangTheoSDT(sdt);
            if (kh != null) {
                selectedKhachHang = kh;
                lblTenKhachDat.setText(kh.getHoTen());
            } else {
                Stage st = new Stage();
                GiaoDienThemKhachHang view = new GiaoDienThemKhachHang(sdt, khMoi -> {
                    selectedKhachHang = khMoi;
                    txtSDTKhachDat.setText(khMoi.getSoDT());
                    lblTenKhachDat.setText(khMoi.getHoTen());
                });
                st.setScene(new Scene(view));
                st.initOwner(getScene() != null ? getScene().getWindow() : null);
                st.initModality(Modality.APPLICATION_MODAL);
                st.setTitle("Thêm khách hàng");
                st.showAndWait();
            }
        });

        btnDatBan.setOnAction(e -> {
            try {
                String soNguoiStr = txtSoNguoi.getText();
                String sdt = txtSDTKhachDat.getText();

                if (soNguoiStr == null || soNguoiStr.isBlank()) {
                    lblTenKhachDat.setText("Vui lòng nhập số người");
                    return;
                }
                int soNguoi = Integer.parseInt(soNguoiStr);
                if (soNguoi <= 0) {
                    lblTenKhachDat.setText("Số người không hợp lệ");
                    return;
                }
                if (sdt == null || sdt.length() < 10) {
                    lblTenKhachDat.setText("SDT không hợp lệ");
                    return;
                }

                KhachHang kh = selectedKhachHang != null ? selectedKhachHang : khachHangDAO.layKhachHangTheoSDT(sdt);
                if (kh == null) {
                    lblTenKhachDat.setText("Khách hàng chưa tồn tại");
                    return;
                }

                NhanVien assigned = null;
                TaiKhoan current = Session.getCurrentUser();
                if (current != null) {
                    NhanVienDAO nvDao = new NhanVienDAO();
                    for (NhanVien nv : nvDao.layDanhSachNhanVien()) {
                        if (nv.getMaTK() != null && current.getMaTK() != null && current.getMaTK().equals(nv.getMaTK().getMaTK())) {
                            assigned = nv;
                            break;
                        }
                    }
                }
                if (assigned == null) assigned = new NhanVien("NV000001");

                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setNgayDat(LocalDateTime.now());
                pdb.setSoNguoi(soNguoi);
                pdb.setKhachHang(kh);
                pdb.setNhanVien(assigned);

                boolean ok = false;
                BanDAO banDAO = new BanDAO();
                PhieuDatBan_BanDAO lienKetDAO = new PhieuDatBan_BanDAO();

                if (dsBan != null && dsBan.size() > 1) {
                    // Nhiều bàn: lấy bàn đầu làm tiêu biểu để lưu phiếu
                    pdb.setBan(dsBan.get(0));
                    ok = phieuDatBanDAO.themPhieu(pdb, "DAT_NGAY");
                    if (ok) {
                        lienKetDAO.themLienKet(pdb.getMaPDB(), dsBan);
                        // Cập nhật trạng thái tất cả bàn
                        for (Ban b : dsBan) {
                            banDAO.capNhatTrangThai(b.getMaBan(), "Đang sử dụng");
                        }
                    }
                } else {
                    // Bàn đơn
                    pdb.setBan(this.ban);
                    ok = phieuDatBanDAO.themPhieu(pdb, "DAT_NGAY");
                    if (ok) {
                        banDAO.capNhatTrangThai(this.ban.getMaBan(), "Đang sử dụng");
                    }
                }

                if (ok) {
                    if (quanLiBan != null && pdb.getBan() != null && pdb.getBan().getTang() != null) {
                        quanLiBan.hienThiBanTheoTang(pdb.getBan().getTang().getMaTang());
                    }

                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.CONFIRMATION,
                        "Đặt bàn thành công!\nBạn có muốn gọi món ngay không?",
                        javafx.scene.control.ButtonType.YES,
                        javafx.scene.control.ButtonType.NO
                    );
                    alert.setTitle("Xác nhận");
                    alert.setHeaderText(null);
                    alert.initOwner(getScene().getWindow());
                    alert.showAndWait().ifPresent(buttonType -> {
                        Stage st = (Stage) getScene().getWindow();
                        if (buttonType == ButtonType.YES) {
                            if (parentPane != null) {
                                Ban banChinh = dsBan != null && !dsBan.isEmpty() ? dsBan.get(0) : this.ban;
                                parentPane.getChildren().setAll(new GiaoDienGoiMon(parentPane, banChinh, pdb));
                            }
                        }
                        if (st != null) st.close();
                    });
                } else {
                    javafx.scene.control.Alert failAlert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR,
                        "Lưu phiếu thất bại! Vui lòng thử lại."
                    );
                    failAlert.setTitle("Lỗi");
                    failAlert.setHeaderText(null);
                    failAlert.initOwner(getScene().getWindow());
                    failAlert.showAndWait();
                }
            } catch (Exception ex) {
                lblTenKhachDat.setText("Có lỗi khi lưu");
                ex.printStackTrace();
            }
        });

        btnQuayLai.setOnAction(e -> {
            Stage st = (Stage) getScene().getWindow();
            if (st != null) st.close();
        });
    }

    public Label getLblTrangThai() { return lblTrangThaiStatus; }
    public ComboBox<String> getCbLoaiBan() { return null; }
    public TextField getTxtSoNguoi() { return txtSoNguoi; }
    // Deprecated: Giá tiền hiển thị tự động, không còn TextField.
    public TextField getTxtGiaTien() { return null; }
    public TextField getTxtSDTKhachDat() { return txtSDTKhachDat; }
    public Label getLblTenKhachDat() { return lblTenKhachDat; }
    public Button getBtnKiemTra() { return btnKiemTra; }
    public Button getBtnDatBan() { return btnDatBan; }
    public Button getBtnQuayLai() { return btnQuayLai; }
}