package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import jfxtras.scene.control.LocalTimeTextField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class GiaoDienDatBanTruoc extends VBox {

    private TextField txtTrangThai;
    private ComboBox<String> cbLoaiBan;
    private TextField txtSoNguoi;
    private TextField txtGiaTien;
    private DatePicker dtpNgayNhanBan;
    private LocalTimeTextField timeNhanBan;
    private TextField txtSDTKhachDat;
    private Label lblTenKhachDat;
    private Button btnKiemTra;
    private Button btnDatBan;
    private Button btnQuayLai;

    public GiaoDienDatBanTruoc() {
        setStyle("-fx-background-color: #F5F5F5;");
        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);

        Label lblTitle = new Label("Đặt bàn trước");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
        HBox titleBar = new HBox(lblTitle);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(10, 20, 10, 20));
        titleBar.setStyle("-fx-background-color: #1E424D;");
        titleBar.setPrefHeight(50);

        VBox contentCard = new VBox(20);
        contentCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #CCCCCC; -fx-border-radius: 15;");
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
        txtTrangThai = createTextField();
        txtTrangThai.setPrefWidth(230);

        Label lblLoaiBan = createLabel("Loại bàn:");
        lblLoaiBan.setPrefWidth(100);
        cbLoaiBan = createComboBox();
        cbLoaiBan.setPrefWidth(230);

        row1.getChildren().addAll(lblTrangThai, txtTrangThai, lblLoaiBan, cbLoaiBan);

        // Row 2: Số người and Giá tiền
        HBox row2 = new HBox(20);
        row2.setAlignment(Pos.CENTER_LEFT);

        Label lblSoNguoi = createLabel("Số người:");
        lblSoNguoi.setPrefWidth(120);
        txtSoNguoi = createTextField();
        txtSoNguoi.setPrefWidth(230);

    Label lblGiaTien = createLabel("Giá tiền:");
    lblGiaTien.setPrefWidth(100);
    txtGiaTien = createTextField();
    txtGiaTien.setPrefWidth(230);

    row2.getChildren().addAll(lblSoNguoi, txtSoNguoi, lblGiaTien, txtGiaTien);

        // Row 3: Ngày nhận bàn and Giờ nhận bàn
        HBox row3 = new HBox(20);
        row3.setAlignment(Pos.CENTER_LEFT);

        Label lblNgayNhanBan = createLabel("Ngày nhận bàn:");
        lblNgayNhanBan.setPrefWidth(120);
        dtpNgayNhanBan = new DatePicker();
        dtpNgayNhanBan.setPrefWidth(230);

    Label lblGioNhanBan = createLabel("Giờ nhận bàn:");
    lblGioNhanBan.setPrefWidth(100);
    timeNhanBan = new LocalTimeTextField();
    timeNhanBan.setPrefHeight(35);
    timeNhanBan.setPrefWidth(230);
    timeNhanBan.setPromptText("HH:mm:ss");

    row3.getChildren().addAll(lblNgayNhanBan, dtpNgayNhanBan, lblGioNhanBan, timeNhanBan);

        // Row 4: SDT khách đặt
        HBox row4 = new HBox(10);
        row4.setAlignment(Pos.CENTER_LEFT);
        Label lblSDT = createLabel("SDT khách đặt:");
        lblSDT.setPrefWidth(120);
        txtSDTKhachDat = createTextField();
        HBox.setHgrow(txtSDTKhachDat, Priority.ALWAYS);
    btnKiemTra = new ButtonSample2("Kiểm tra", Variant.YELLOW, 100);
        row4.getChildren().addAll(lblSDT, txtSDTKhachDat, btnKiemTra);

        // Row 5: Tên khách đặt
        HBox row5 = new HBox(10);
        row5.setAlignment(Pos.CENTER_LEFT);
        Label lblTenKhach = createLabel("Tên khách đặt:");
        lblTenKhach.setPrefWidth(120);
        lblTenKhachDat = new Label("");
        lblTenKhachDat.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        row5.getChildren().addAll(lblTenKhach, lblTenKhachDat);

        formBox.getChildren().addAll(row1, row2, row3, row4, row5);

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

    private ComboBox<String> createComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
        comboBox.setPrefHeight(35);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        return comboBox;
    }
    
    public TextField getTxtTrangThai() { return txtTrangThai; }
    public ComboBox<String> getCbLoaiBan() { return cbLoaiBan; }
    public TextField getTxtSoNguoi() { return txtSoNguoi; }
    public TextField getTxtGiaTien() { return txtGiaTien; }
    public DatePicker getDtpNgayNhanBan() { return dtpNgayNhanBan; }
    public LocalTimeTextField getTimeNhanBan() { return timeNhanBan; }
    public LocalDateTime getGioNhanBan() {
        LocalDate date = dtpNgayNhanBan.getValue();
        if (date == null) return null;
        LocalTime t = timeNhanBan.getLocalTime();
        if (t == null) t = LocalTime.MIDNIGHT;
        return LocalDateTime.of(date, t);
    }
    public TextField getTxtSDTKhachDat() { return txtSDTKhachDat; }
    public Label getLblTenKhachDat() { return lblTenKhachDat; }
    public Button getBtnKiemTra() { return btnKiemTra; }
    public Button getBtnDatBan() { return btnDatBan; }
    public Button getBtnQuayLai() { return btnQuayLai; }
}