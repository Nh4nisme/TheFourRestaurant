package com.thefourrestaurant.view.khachhang;

import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;
import com.thefourrestaurant.DAO.KhachHangDAO;
import com.thefourrestaurant.DAO.LoaiKhachHangDAO;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.LoaiKhachHang;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.DateCell;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.function.UnaryOperator;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

public class GiaoDienThemKhachHang extends VBox {

    private TextField txtTenKhachHang;
    private TextField txtSoDT;
    private DatePicker dpNgaySinh;
    private ComboBox<String> cboGioiTinh;
    private Button btnLamMoi;
    private Button btnThem;
    private Button btnQuayLai;

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private Consumer<KhachHang> onSaved;

    public GiaoDienThemKhachHang() {
        this(null, null);
    }

    public GiaoDienThemKhachHang(String presetPhone, Consumer<KhachHang> onSaved) {
        this.onSaved = onSaved;
        setStyle("-fx-background-color: #F5F5F5;");
        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);

        Label lblTitle = new Label("Thêm khách hàng");
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

        // Row 1: Tên khách hàng
        HBox row1 = new HBox(10);
        row1.setAlignment(Pos.CENTER_LEFT);
        Label lblTenKhachHang = createLabel("Tên khách hàng:");
        lblTenKhachHang.setPrefWidth(120);
        txtTenKhachHang = createTextField();
        HBox.setHgrow(txtTenKhachHang, Priority.ALWAYS);
        row1.getChildren().addAll(lblTenKhachHang, txtTenKhachHang);

        // Row 2: Số ĐT
        HBox row2 = new HBox(10);
        row2.setAlignment(Pos.CENTER_LEFT);
        Label lblSoDT = createLabel("Số ĐT:");
        lblSoDT.setPrefWidth(120);
    txtSoDT = createNumericTextField(Pattern.compile("\\d{0,11}"));
    if (presetPhone != null) txtSoDT.setText(presetPhone);
        HBox.setHgrow(txtSoDT, Priority.ALWAYS);
        row2.getChildren().addAll(lblSoDT, txtSoDT);

        // Row 3: Ngày sinh and Giới tính
        HBox row3 = new HBox(20);
        row3.setAlignment(Pos.CENTER_LEFT);

        Label lblNgaySinh = createLabel("Ngày sinh:");
        lblNgaySinh.setPrefWidth(120);
        dpNgaySinh = new DatePicker();
        dpNgaySinh.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
        dpNgaySinh.setPrefHeight(35);
        dpNgaySinh.setPrefWidth(230);
        dpNgaySinh.setEditable(false);
        // Không cho chọn ngày sinh ở tương lai
        dpNgaySinh.setDayCellFactory(picker -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

    Label lblGioiTinh = createLabel("Giới tính:");
    lblGioiTinh.setPrefWidth(100);
    cboGioiTinh = new ComboBox<>();
    // Hiển thị "Nam" hoặc "Nữ" cho người dùng (lưu DB sẽ map về "Nu")
    cboGioiTinh.getItems().addAll("Nam", "Nữ");
    cboGioiTinh.setPrefWidth(230);
    row3.getChildren().addAll(lblNgaySinh, dpNgaySinh, lblGioiTinh, cboGioiTinh);

        formBox.getChildren().addAll(row1, row2, row3);

        HBox buttonBar = new HBox(20);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        buttonBar.setPadding(new Insets(20, 0, 0, 0));

    btnQuayLai = new ButtonSample2("Quay lại", Variant.YELLOW, 100);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

    btnLamMoi = new ButtonSample2("Làm mới", Variant.YELLOW, 100);
    btnThem = new ButtonSample2("Thêm", Variant.YELLOW, 100);

        buttonBar.getChildren().addAll(btnQuayLai, spacer, btnLamMoi, btnThem);

        contentCard.getChildren().addAll(lblBanHeader, formBox, buttonBar);

        VBox centerWrapper = new VBox(contentCard);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPadding(new Insets(40));
        VBox.setVgrow(centerWrapper, Priority.ALWAYS);

        getChildren().addAll(titleBar, centerWrapper);

        wireHandlers();
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

    private void wireHandlers() {
        btnLamMoi.setOnAction(e -> {
            txtTenKhachHang.clear();
            txtSoDT.clear();
            dpNgaySinh.setValue(null);
            cboGioiTinh.getSelectionModel().clearSelection();
        });

        btnQuayLai.setOnAction(e -> {
            Stage st = (Stage) getScene().getWindow();
            if (st != null) st.close();
        });

        btnThem.setOnAction(e -> {
            String ten = txtTenKhachHang.getText() == null ? "" : txtTenKhachHang.getText().trim();
            String sdt = txtSoDT.getText() == null ? "" : txtSoDT.getText().trim();
            LocalDate ns = dpNgaySinh.getValue();
            String gioiTinh = cboGioiTinh.getSelectionModel().getSelectedItem();

            if (ten.isEmpty()) {
                txtTenKhachHang.requestFocus();
                return;
            }
            if (gioiTinh == null || !("Nam".equals(gioiTinh) || "Nữ".equals(gioiTinh))) {
                cboGioiTinh.requestFocus();
                return;
            }
            if (sdt.length() < 10) {
                txtSoDT.requestFocus();
                return;
            }

            // Nếu đã tồn tại theo SDT -> dùng luôn
            KhachHang existed = khachHangDAO.layKhachHangTheoSDT(sdt);
            if (existed != null) {
                if (onSaved != null) onSaved.accept(existed);
                Stage st = (Stage) getScene().getWindow();
                if (st != null) st.close();
                return;
            }

            KhachHang kh = new KhachHang();
            kh.setHoTen(ten);
            kh.setSoDT(sdt);
            // Map "Nữ" hiển thị sang giá trị DB hợp lệ "Nu"
            String gioiTinhDb = "Nữ".equals(gioiTinh) ? "Nu" : gioiTinh;
            kh.setGioiTinh(gioiTinhDb);
            kh.setNgaySinh(ns != null ? Date.valueOf(ns) : null);
            // Gán loại KH đầu tiên nếu có; nếu không có thì dừng vì maLoaiKH NOT NULL
            List<LoaiKhachHang> ds = new LoaiKhachHangDAO().layDanhSachLoaiKhachHang();
            if (!ds.isEmpty()) {
                kh.setLoaiKH(ds.get(0));
            } else {
                // Không có loại KH trong DB -> không thể lưu theo schema
                return;
            }

            boolean ok = khachHangDAO.themKhachHang(kh);
            if (ok) {
                if (onSaved != null) onSaved.accept(kh);
                Stage st = (Stage) getScene().getWindow();
                if (st != null) st.close();
            }
        });
    }
    

    public TextField getTxtTenKhachHang() { return txtTenKhachHang; }
    public TextField getTxtSoDT() { return txtSoDT; }
    public DatePicker getDpNgaySinh() { return dpNgaySinh; }
    public ComboBox<String> getCboGioiTinh() { return cboGioiTinh; }
    public Button getBtnLamMoi() { return btnLamMoi; }
    public Button getBtnThem() { return btnThem; }
    public Button getBtnQuayLai() { return btnQuayLai; }
}