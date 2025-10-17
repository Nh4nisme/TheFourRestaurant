package com.thefourrestaurant.view;

import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ThanhToan {

    public void show(Stage cuaSoChinh) {
        VBox khungChinh = new VBox(15);
        khungChinh.setPadding(new Insets(0));
        khungChinh.setStyle("-fx-background-color: #1a4d5c;");

        // ==== TIÊU ĐỀ ====
        Label lblTieuDe = new Label("THANH TOÁN");
        lblTieuDe.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lblTieuDe.setTextFill(Color.web("#f4c430"));
        lblTieuDe.setAlignment(Pos.CENTER);
        lblTieuDe.setMaxWidth(Double.MAX_VALUE);

        // ==== NỘI DUNG CHÍNH ====
        VBox khungNoiDung = new VBox(10);
        khungNoiDung.setPadding(new Insets(20));
        khungNoiDung.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // ==== THÔNG TIN KHÁCH HÀNG ====
        Label lblSDT = new Label("SĐT khách hàng:");
        Label lblTenKH = new Label("Tên khách hàng:");
        Label lblGioNhan = new Label("Giờ nhận bàn:");
        Label lblGioTra = new Label("Giờ trả bàn:");

        double doRongNhan = 100;

        lblSDT.setPrefWidth(doRongNhan);
        lblTenKH.setPrefWidth(doRongNhan);
        lblGioNhan.setPrefWidth(doRongNhan);
        lblGioTra.setPrefWidth(doRongNhan);

        TextField txtSDT = new TextField();
        TextField txtTenKH = new TextField();
        TextField txtGioNhan = new TextField();
        TextField txtGioTra = new TextField();

        txtSDT.setPrefWidth(200);
        txtTenKH.setPrefWidth(200);
        txtGioNhan.setPrefWidth(200);
        txtGioTra.setPrefWidth(200);

        HBox hangKhachHang = new HBox(30, lblSDT, txtSDT, lblTenKH, txtTenKH);
        hangKhachHang.setAlignment(Pos.CENTER_LEFT);

        HBox hangGio = new HBox(30, lblGioNhan, txtGioNhan, lblGioTra, txtGioTra);
        hangGio.setAlignment(Pos.CENTER_LEFT);

        VBox khungThongTin = new VBox(10, hangKhachHang, hangGio);
        khungThongTin.setPadding(new Insets(10));

        // ==== BẢNG MÓN ĂN ====
        TableView<MonDat> bangMon = new TableView<>();
        bangMon.setPrefHeight(200);

        TableColumn<MonDat, Integer> cotSTT = new TableColumn<>("STT");
        cotSTT.setCellValueFactory(new PropertyValueFactory<>("stt"));
        cotSTT.setPrefWidth(50);

        TableColumn<MonDat, String> cotTenMon = new TableColumn<>("Tên món");
        cotTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
        cotTenMon.setPrefWidth(150);

        TableColumn<MonDat, String> cotDonGia = new TableColumn<>("Đơn giá");
        cotDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        cotDonGia.setPrefWidth(100);

        TableColumn<MonDat, String> cotDonViTinh = new TableColumn<>("ĐVT");
        cotDonViTinh.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
        cotDonViTinh.setPrefWidth(80);

        TableColumn<MonDat, Integer> cotSoLuong = new TableColumn<>("Số lượng");
        cotSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        cotSoLuong.setPrefWidth(80);

        TableColumn<MonDat, String> cotThanhTien = new TableColumn<>("Thành tiền");
        cotThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        cotThanhTien.setPrefWidth(120);

        bangMon.getColumns().addAll(cotSTT, cotTenMon, cotDonGia, cotDonViTinh, cotSoLuong, cotThanhTien);

        // ==== KHUNG DƯỚI ====
        Label lblMaGiamGia = new Label("Mã giảm giá:");
        Label lblChietKhau = new Label("Chiết khấu:");
        Label lblTienDatCoc = new Label("Tiền đặt cọc:");
        Label lblTienThanhToan = new Label("Tiền thanh toán:");
        Label lblTienNhan = new Label("Tiền nhận:");
        Label lblTienThua = new Label("Tiền thừa:");
        Label lblVAT = new Label("Thuế VAT:");
        Label lblPTThanhToan = new Label("Phương thức thanh toán:");

        double doRong = 175;

        lblMaGiamGia.setPrefWidth(doRong);
        lblChietKhau.setPrefWidth(doRong);
        lblTienDatCoc.setPrefWidth(doRong);
        lblTienThanhToan.setPrefWidth(doRong);
        lblTienNhan.setPrefWidth(doRong);
        lblTienThua.setPrefWidth(doRong);
        lblVAT.setPrefWidth(doRong);
        lblPTThanhToan.setPrefWidth(doRong);

        TextField txtMaGiamGia = new TextField();
        TextField txtChietKhau = new TextField();
        TextField txtTienDatCoc = new TextField();
        TextField txtTienThanhToan = new TextField();
        TextField txtTienNhan = new TextField();
        TextField txtTienThua = new TextField();
        TextField txtVAT = new TextField();
        TextField txtPTThanhToan = new TextField();

        txtMaGiamGia.setPrefWidth(200);
        txtChietKhau.setPrefWidth(200);
        txtTienDatCoc.setPrefWidth(200);
        txtTienThanhToan.setPrefWidth(200);
        txtTienNhan.setPrefWidth(200);
        txtTienThua.setPrefWidth(200);
        txtVAT.setPrefWidth(200);
        txtPTThanhToan.setPrefWidth(200);

        Button btnKiemTra = new Button("Kiểm tra");

        HBox hangMaGiamGia = new HBox(10, lblMaGiamGia, txtMaGiamGia, btnKiemTra);
        hangMaGiamGia.setAlignment(Pos.CENTER_LEFT);

        HBox hangChietKhau = new HBox(30, lblChietKhau, txtChietKhau, lblTienDatCoc, txtTienDatCoc);
        hangChietKhau.setAlignment(Pos.CENTER_LEFT);

        HBox hangThanhToan = new HBox(30, lblTienThanhToan, txtTienThanhToan, lblTienNhan, txtTienNhan);
        hangThanhToan.setAlignment(Pos.CENTER_LEFT);

        HBox hangPhuThu = new HBox(30, lblTienThua, txtTienThua, lblVAT, txtVAT);
        hangPhuThu.setAlignment(Pos.CENTER_LEFT);

        HBox hangPTThanhToan = new HBox(30, lblPTThanhToan, txtPTThanhToan);
        hangPTThanhToan.setAlignment(Pos.CENTER_LEFT);

        HBox khungDuoi = new HBox(20);
        VBox cotTrai = new VBox(15, hangMaGiamGia, hangChietKhau, hangThanhToan, hangPhuThu, hangPTThanhToan);
        cotTrai.setPrefWidth(500);

        ImageView qrCode = new ImageView(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/QRCode.png")));
        qrCode.setPreserveRatio(true);
        qrCode.setFitWidth(200);
        VBox cotPhai = new VBox(qrCode);
        cotPhai.setPrefWidth(khungDuoi.getPrefWidth() * 0.3);
        cotPhai.setAlignment(Pos.CENTER);

        khungDuoi.getChildren().addAll(cotTrai, cotPhai);

        // ==== NÚT CHỨC NĂNG ====
        HBox khungNut = new HBox(20);
        khungNut.setAlignment(Pos.CENTER);
        khungNut.setPadding(new Insets(10, 0, 0, 0));

        ButtonSample btnQuayLai = new ButtonSample("Quay lại", 45, 18, 1);
        ButtonSample btnXuatHoaDon = new ButtonSample("Xuất hóa đơn", 45, 18, 2);
        ButtonSample btnXacNhan = new ButtonSample("Xác nhận thanh toán", 45, 18, 2);

        khungNut.getChildren().addAll(btnQuayLai, btnXuatHoaDon, btnXacNhan);

        // ==== GHÉP TẤT CẢ ====
        khungNoiDung.getChildren().addAll(khungThongTin, bangMon, khungDuoi, khungNut);
        khungChinh.getChildren().addAll(lblTieuDe, khungNoiDung);

        Scene canh = new Scene(khungChinh, 750, 780);
        cuaSoChinh.setTitle("Thanh Toán");
        cuaSoChinh.setScene(canh);
        cuaSoChinh.show();
    }

    // ==== LỚP MÔ HÌNH ====
    public static class MonDat {
        private int stt;
        private String tenMon;
        private String donGia;
        private String donViTinh;
        private int soLuong;
        private String thanhTien;

        public MonDat(int stt, String tenMon, String donGia, String donViTinh, int soLuong, String thanhTien) {
            this.stt = stt;
            this.tenMon = tenMon;
            this.donGia = donGia;
            this.donViTinh = donViTinh;
            this.soLuong = soLuong;
            this.thanhTien = thanhTien;
        }

        public int getStt() { return stt; }
        public String getTenMon() { return tenMon; }
        public String getDonGia() { return donGia; }
        public String getDonViTinh() { return donViTinh; }
        public int getSoLuong() { return soLuong; }
        public String getThanhTien() { return thanhTien; }
    }
}
