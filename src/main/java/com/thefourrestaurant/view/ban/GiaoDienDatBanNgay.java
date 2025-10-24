package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;
import com.thefourrestaurant.DAO.KhachHangDAO;
import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.DAO.NhanVienDAO;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.util.Session;
import com.thefourrestaurant.DAO.LoaiBanDAO;
import com.thefourrestaurant.model.LoaiBan;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.GiaoDienThemKhachHang;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.time.LocalDate;

public class GiaoDienDatBanNgay extends VBox {

    private Label lblTrangThaiStatus;
    private ComboBox<String> cbLoaiBan;
    private TextField txtSoNguoi;
    private TextField txtGiaTien;
    private TextField txtSDTKhachDat;
    private Label lblTenKhachDat;
    private Button btnKiemTra;
    private Button btnDatBan;
    private Button btnQuayLai;

    private final PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private KhachHang selectedKhachHang;

    public GiaoDienDatBanNgay() {
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

        Label lblBanHeader = new Label("Bàn B101");
        lblBanHeader.setStyle("-fx-font-size: 22px; -fx-text-fill: #DDB248; -fx-font-weight: bold;");

        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER_LEFT);

        // Row 1: Trạng thái and Loại bàn
        HBox row1 = new HBox(20);
        row1.setAlignment(Pos.CENTER_LEFT);

    Label lblTrangThai = createLabel("Trạng Thái:");
    lblTrangThai.setPrefWidth(120);
    lblTrangThaiStatus = new Label("Bàn đang sử dụng.");
    lblTrangThaiStatus.setStyle("-fx-font-size:14px; -fx-text-fill: black;");
    lblTrangThaiStatus.setPrefWidth(230);

        Label lblLoaiBan = createLabel("Loại bàn:");
        lblLoaiBan.setPrefWidth(100);
        cbLoaiBan = createComboBox();
        cbLoaiBan.setPrefWidth(230);

    row1.getChildren().addAll(lblTrangThai, lblTrangThaiStatus, lblLoaiBan, cbLoaiBan);

        // Row 2: Số người and Giá tiền
        HBox row2 = new HBox(20);
        row2.setAlignment(Pos.CENTER_LEFT);

        Label lblSoNguoi = createLabel("Số người:");
        lblSoNguoi.setPrefWidth(120);
        txtSoNguoi = createNumericTextField(Pattern.compile("\\d{0,3}"));
        txtSoNguoi.setPromptText("Chỉ nhập số");
        txtSoNguoi.setPrefWidth(230);

    Label lblGiaTien = createLabel("Giá tiền:");
    lblGiaTien.setPrefWidth(100);
    txtGiaTien = createNumericTextField(Pattern.compile("\\d{0,12}"));
    txtGiaTien.setPromptText("Chỉ nhập số");
    txtGiaTien.setPrefWidth(230);

    row2.getChildren().addAll(lblSoNguoi, txtSoNguoi, lblGiaTien, txtGiaTien);

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
        LoaiBanDAO dao = new LoaiBanDAO();
        for (LoaiBan lb : dao.layTatCa()) {
            cbLoaiBan.getItems().add(lb.getTenLoaiBan());
        }
        if (!cbLoaiBan.getItems().isEmpty()) cbLoaiBan.getSelectionModel().selectFirst();
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
                lblTenKhachDat.setText(kh.getHoTen() + " (" + kh.getSoDT() + ")");
            } else {
                // Không tìm thấy -> mở popup thêm khách hàng, preset SDT
                Stage st = new Stage();
                GiaoDienThemKhachHang view = new GiaoDienThemKhachHang(sdt, khMoi -> {
                    selectedKhachHang = khMoi;
                    txtSDTKhachDat.setText(khMoi.getSoDT());
                    lblTenKhachDat.setText(khMoi.getHoTen() + " (" + khMoi.getSoDT() + ")");
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

                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setNgayDat(LocalDate.now());
                pdb.setSoNguoi(soNguoi);
                pdb.setKhachHang(kh);
                // try to map current session user to a NhanVien
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
                pdb.setNhanVien(assigned);

                boolean ok = phieuDatBanDAO.themPhieu(pdb);
                lblTenKhachDat.setText(ok ? "Đã lưu phiếu đặt bàn" : "Lưu phiếu thất bại");
                if (ok) {
                    btnDatBan.setDisable(true);
                }
            } catch (Exception ex) {
                lblTenKhachDat.setText("Có lỗi khi lưu");
            }
        });

        btnQuayLai.setOnAction(e -> {
            Stage st = (Stage) getScene().getWindow();
            if (st != null) st.close();
        });
    }

    public Label getLblTrangThai() { return lblTrangThaiStatus; }
    public ComboBox<String> getCbLoaiBan() { return cbLoaiBan; }
    public TextField getTxtSoNguoi() { return txtSoNguoi; }
    public TextField getTxtGiaTien() { return txtGiaTien; }
    public TextField getTxtSDTKhachDat() { return txtSDTKhachDat; }
    public Label getLblTenKhachDat() { return lblTenKhachDat; }
    public Button getBtnKiemTra() { return btnKiemTra; }
    public Button getBtnDatBan() { return btnDatBan; }
    public Button getBtnQuayLai() { return btnQuayLai; }
}