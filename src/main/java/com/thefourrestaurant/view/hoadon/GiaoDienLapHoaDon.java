package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GiaoDienLapHoaDon {

    private Stage stage;

    // ==== Thông tin khách hàng ====
    private Label lblMaPDB, lblSDT, lblTenKH, lblGioNhan, lblGioTra;

    // ==== Bảng món ====
    private TableView<MonAn> bangMon;

    // ==== Thanh toán ====
    private TextField txtMaGiamGia;
    private Label lblChietKhau, lblVAT, lblTongTien, lblTienThua;
    private TextField txtTienNhan;
    private ComboBox<String> cboPTThanhToan;
    private CheckBox chkXuatHoaDon;
    private VBox khungQR;
    private ImageView qrImage;
    private ButtonSample btnKiemTra;

    // ==== Nút chức năng ====
    private ButtonSample btnQuayLai, btnXacNhan, btnXuatHoaDon;

    public GiaoDienLapHoaDon(Stage stage) {
        this.stage = stage;
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        VBox khungChinh = new VBox(15);
        khungChinh.setPadding(new Insets(0));
        khungChinh.getStyleClass().add("root-pane");

        // ===== Tiêu đề =====
        Label lblTieuDe = new Label("THANH TOÁN HÓA ĐƠN");
        lblTieuDe.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        lblTieuDe.setMaxWidth(Double.MAX_VALUE);
        lblTieuDe.setPadding(new Insets(15, 0, 10, 0));
        lblTieuDe.setStyle("-fx-text-fill: #f8d231; -fx-alignment: center;");

        // ===== Nội dung chính =====
        VBox khungNoiDung = new VBox(20);
        khungNoiDung.setPadding(new Insets(25));
        khungNoiDung.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
        khungNoiDung.setAlignment(Pos.TOP_CENTER);

        // ===== Thông tin khách hàng =====
        GridPane khungThongTin = new GridPane();
        khungThongTin.setHgap(20);
        khungThongTin.setVgap(10);
        khungThongTin.setPadding(new Insets(10));

        Label lMaPDB = new Label("Mã PĐB:");
        Label lSDT = new Label("SĐT khách:");
        Label lTenKH = new Label("Tên khách:");
        Label lGioNhan = new Label("Giờ nhận:");
        Label lGioTra = new Label("Giờ trả:");

        lblMaPDB = new Label();
        lblSDT = new Label();
        lblTenKH = new Label();
        lblGioNhan = new Label();
        lblGioTra = new Label();

        khungThongTin.addRow(0, lMaPDB, lblMaPDB, lSDT, lblSDT);
        khungThongTin.addRow(1, lTenKH, lblTenKH, lGioNhan, lblGioNhan);
        khungThongTin.addRow(2, lGioTra, lblGioTra);

        // ===== Bảng món =====
        bangMon = new TableView<>();
        bangMon.setPrefHeight(230);

        TableColumn<MonAn, Integer> cotSTT = new TableColumn<>("STT");
        TableColumn<MonAn, String> cotTenMon = new TableColumn<>("Tên món");
        TableColumn<MonAn, String> cotDonGia = new TableColumn<>("Đơn giá");
        TableColumn<MonAn, String> cotSoLuong = new TableColumn<>("Số lượng");
        TableColumn<MonAn, String> cotThanhTien = new TableColumn<>("Thành tiền");

        cotSTT.setCellValueFactory(new PropertyValueFactory<>("stt"));
        cotTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
        cotDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        cotSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        cotThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        cotSTT.setPrefWidth(50);
        cotTenMon.setPrefWidth(200);
        cotDonGia.setPrefWidth(120);
        cotSoLuong.setPrefWidth(100);
        cotThanhTien.setPrefWidth(130);

        bangMon.getColumns().addAll(cotSTT, cotTenMon, cotDonGia, cotSoLuong, cotThanhTien);

        // ===== Thanh toán =====
        GridPane khungThanhToan = new GridPane();
        khungThanhToan.setHgap(20);
        khungThanhToan.setVgap(12);
        khungThanhToan.setPadding(new Insets(10));

        Label lMaGiamGia = new Label("Mã khuyến mãi:");
        Label lChietKhau = new Label("Chiết khấu (%):");
        Label lVAT = new Label("Thuế VAT (%):");
        Label lTongTien = new Label("Tổng tiền:");
        Label lTienNhan = new Label("Tiền khách đưa:");
        Label lTienThua = new Label("Tiền thừa:");
        Label lPTThanhToan = new Label("Phương thức thanh toán:");
        Label lXuatHD = new Label("Xuất hóa đơn:");

        txtMaGiamGia = new TextField();
        btnKiemTra = new ButtonSample("Kiểm tra", 30, 16, 3);

        lblChietKhau = new Label();
        lblVAT = new Label();
        lblTongTien = new Label();
        txtTienNhan = new TextField();
        lblTienThua = new Label();

        HBox hangMaKM = new HBox(10, txtMaGiamGia, btnKiemTra);
        hangMaKM.setAlignment(Pos.CENTER_LEFT);

        cboPTThanhToan = new ComboBox<>(FXCollections.observableArrayList(
                "Tiền mặt", "Chuyển khoản", "Thanh toán QR"));
        cboPTThanhToan.setPrefWidth(200);
        cboPTThanhToan.setPromptText("Chọn phương thức");

        chkXuatHoaDon = new CheckBox("In / Xuất hóa đơn PDF");

        khungThanhToan.addRow(0, lMaGiamGia, hangMaKM, lChietKhau, lblChietKhau);
        khungThanhToan.addRow(1, lVAT, lblVAT, lTongTien, lblTongTien);
        khungThanhToan.addRow(2, lTienNhan, txtTienNhan, lTienThua, lblTienThua);
        khungThanhToan.addRow(3, lPTThanhToan, cboPTThanhToan, lXuatHD, chkXuatHoaDon);

        // ===== QR =====
        khungQR = new VBox(10);
        khungQR.setAlignment(Pos.CENTER);
        khungQR.setPadding(new Insets(10));
        khungQR.setStyle("-fx-border-color: #ccc; -fx-border-radius: 10; -fx-background-radius: 10;");
        Label lblQRTitle = new Label("Mã QR thanh toán");
        lblQRTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        qrImage = new ImageView();
        qrImage.setFitWidth(200);
        qrImage.setFitHeight(200);
        qrImage.setPreserveRatio(true);
        khungQR.getChildren().addAll(lblQRTitle, qrImage);
        khungQR.setVisible(false);

        cboPTThanhToan.setOnAction(e -> {
            String pttt = cboPTThanhToan.getValue();
            if ("Thanh toán QR".equals(pttt)) {
                qrImage.setImage(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/QRCode.png")));
                khungQR.setVisible(true);
            } else {
                khungQR.setVisible(false);
            }
        });

        // ===== Nút chức năng =====
        HBox khungNut = new HBox(20);
        khungNut.setAlignment(Pos.CENTER);
        khungNut.setPadding(new Insets(15, 0, 0, 0));

        btnQuayLai = new ButtonSample("Quay lại", 45, 18, 1);
        btnXuatHoaDon = new ButtonSample("Xuất hóa đơn", 45, 18, 2);
        btnXacNhan = new ButtonSample("Xác nhận thanh toán", 45, 18, 2);

        khungNut.getChildren().addAll(btnQuayLai, btnXuatHoaDon, btnXacNhan);

        // ===== Ghép tất cả =====
        HBox hangCuoi = new HBox(40, khungThanhToan, khungQR);
        hangCuoi.setAlignment(Pos.TOP_CENTER);

        khungNoiDung.getChildren().addAll(khungThongTin, bangMon, hangCuoi, khungNut);
        khungChinh.getChildren().addAll(lblTieuDe, khungNoiDung);

        Scene canh = new Scene(khungChinh, 1200, 850);
        canh.getStylesheets().add(getClass().getResource("/com/thefourrestaurant/css/Application.css").toExternalForm());

        stage.setTitle("Thanh Toán Hóa Đơn");
        stage.setScene(canh);
        stage.show();
    }
}