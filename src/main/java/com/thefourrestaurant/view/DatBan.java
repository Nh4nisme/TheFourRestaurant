package com.thefourrestaurant.view;

import com.thefourrestaurant.view.components.ButtonSample2;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DatBan extends BorderPane {

    public DatBan() {
        // ====== THANH BÊN ======
        VBox thanhBen = taoThanhBen();
        this.setLeft(thanhBen);

        // ====== NỘI DUNG CHÍNH ======
        VBox noiDungChinh = new VBox();
        noiDungChinh.setStyle("-fx-background-color: #f0f0f0;");

        VBox thanhDieuHuong = taoThanhDieuHuong();

        Pane khuVucBan = new Pane();
        khuVucBan.setStyle("-fx-background-color: white;");
        VBox.setVgrow(khuVucBan, Priority.ALWAYS);

        noiDungChinh.getChildren().addAll(thanhDieuHuong, khuVucBan);
        this.setCenter(noiDungChinh);
    }

    private VBox taoThanhBen() {
        VBox thanhBen = new VBox();
        thanhBen.setPrefWidth(200);
        thanhBen.setStyle("-fx-background-color: #1E424D;");

        // Header
        VBox header = new VBox(5);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #1E424D;");

        Label lblDanhSachBan = new Label("Danh sách bàn");
        lblDanhSachBan.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label lblNgay = new Label("Thứ hai - 03/06/2025");
        lblNgay.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 12px;");

        Label lblGio = new Label("12:36:36");
        lblGio.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 12px;");

        header.getChildren().addAll(lblDanhSachBan, lblNgay, lblGio);

        // Nút chức năng
        VBox cacNut = new VBox(20);
        cacNut.setPadding(new Insets(20, 10, 10, 10));

        ButtonSample2 btnDatBanNgay = new ButtonSample2("Đặt bàn ngay", ButtonSample2.Variant.YELLOW, 180, 40);
        ButtonSample2 btnDatBanTruoc = new ButtonSample2("Đặt bàn trước", ButtonSample2.Variant.YELLOW, 180, 40);
        ButtonSample2 btnNhanBan = new ButtonSample2("Nhận bàn", ButtonSample2.Variant.YELLOW, 180, 40);
        ButtonSample2 btnHuyDatBan = new ButtonSample2("Hủy bàn đặt trước", ButtonSample2.Variant.YELLOW, 180, 40);
        ButtonSample2 btnDatMon = new ButtonSample2("Đặt món", ButtonSample2.Variant.YELLOW, 180, 40);
        ButtonSample2 btnTinhTien = new ButtonSample2("Tính tiền", ButtonSample2.Variant.YELLOW, 180, 40);

        cacNut.getChildren().addAll(btnDatBanNgay, btnDatBanTruoc, btnNhanBan,
                btnHuyDatBan, btnDatMon, btnTinhTien);

        // Chú thích
        VBox chuThich = new VBox(10);
        chuThich.setPadding(new Insets(30, 10, 10, 10));

        HBox banTrong = taoChuThich("Bàn trống", Color.WHITE);
        HBox banDangSuDung = taoChuThich("Bàn đang sử dụng", Color.web("#FFB347"));
        HBox banDaDatTruoc = taoChuThich("Bàn đã được đặt trước", Color.web("#87CEEB"));

        chuThich.getChildren().addAll(banTrong, banDaDatTruoc, banDangSuDung);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        thanhBen.getChildren().addAll(header, cacNut, spacer, chuThich);

        return thanhBen;
    }

    private HBox taoChuThich(String text, Color color) {
        HBox legend = new HBox(10);
        legend.setAlignment(Pos.CENTER_LEFT);

        Circle circle = new Circle(8);
        circle.setFill(color);

        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 11px;");

        legend.getChildren().addAll(circle, lbl);
        return legend;
    }

    private VBox taoThanhDieuHuong() {
    VBox thanhDieuHuong = new VBox(10);
    thanhDieuHuong.setPadding(new Insets(10));
    thanhDieuHuong.setStyle("-fx-background-color: #1E424D; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

    // Hàng 1
    HBox hang1 = new HBox(10);
    hang1.setAlignment(Pos.CENTER_LEFT);

    Label lblBanDatTruoc = new Label("Bàn đặt trước:");
    lblBanDatTruoc.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
    lblBanDatTruoc.setPrefWidth(100);

    ComboBox<String> cboBanDatTruoc = new ComboBox<>();
    cboBanDatTruoc.getItems().add("Bàn đặt trước");
    cboBanDatTruoc.setValue("Bàn đặt trước");
    cboBanDatTruoc.setStyle("-fx-background-color: #DDB248; -fx-text-fill: #1E424D; -fx-font-weight: bold;");
    cboBanDatTruoc.setPrefWidth(150);
    HBox.setHgrow(cboBanDatTruoc, Priority.ALWAYS);

    Label lblSoTang = new Label("Số tầng:");
    lblSoTang.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
    lblSoTang.setPrefWidth(70);

    ComboBox<String> cboSoTang = new ComboBox<>();
    cboSoTang.getItems().addAll("4", "6", "8", "10");
    cboSoTang.setPromptText("Chọn");
    cboSoTang.setPrefWidth(100);
    HBox.setHgrow(cboSoTang, Priority.ALWAYS);

    Label lblMaBan = new Label("Mã bàn:");
    lblMaBan.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
    lblMaBan.setPrefWidth(60);

    TextField txtMaBan = new TextField();
    txtMaBan.setPrefWidth(200);

    ButtonSample2 btnTim = new ButtonSample2("Tìm", ButtonSample2.Variant.YELLOW, 100, 30);

    hang1.getChildren().addAll(lblBanDatTruoc, cboBanDatTruoc, lblSoTang, cboSoTang, lblMaBan, txtMaBan, btnTim);

    // Hàng 2
    HBox hang2 = new HBox(10);
    hang2.setAlignment(Pos.CENTER_LEFT);

    Label lblTang = new Label("Tầng:");
    lblTang.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
    lblTang.setPrefWidth(100);

    ComboBox<String> cboTang = new ComboBox<>();
    cboTang.getItems().addAll("Tầng 1", "Tầng 2", "Tầng 3");
    cboTang.setValue("Tầng");
    cboTang.setStyle("-fx-background-color: #DDB248; -fx-text-fill: #1E424D;");
    cboTang.setPrefWidth(150);
    HBox.setHgrow(cboTang, Priority.ALWAYS);

    Label lblLoaiBan = new Label("Loại bàn:");
    lblLoaiBan.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
    lblLoaiBan.setPrefWidth(70);

    ComboBox<String> cboLoaiBan = new ComboBox<>();
    cboLoaiBan.getItems().addAll("Tất cả", "Bàn tròn", "Bàn vuông");
    cboLoaiBan.setPromptText("Chọn");
    cboLoaiBan.setPrefWidth(100);
    HBox.setHgrow(cboLoaiBan, Priority.ALWAYS);

    Label lblSoGhe = new Label("Số ghế:");
    lblSoGhe.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
    lblSoGhe.setPrefWidth(60);

    ComboBox<String> cboSoGhe = new ComboBox<>();
    cboSoGhe.getItems().addAll("Tất cả", "Có ghi chú", "Không ghi chú");
    cboSoGhe.setPromptText("Chọn");
    cboSoGhe.setPrefWidth(200);
    HBox.setHgrow(cboSoGhe, Priority.ALWAYS);

    ButtonSample2 btnLamMoi = new ButtonSample2("Làm mới", ButtonSample2.Variant.YELLOW, 100, 30);

    hang2.getChildren().addAll(lblTang, cboTang, lblLoaiBan, cboLoaiBan, lblSoGhe, cboSoGhe, btnLamMoi);

    thanhDieuHuong.getChildren().addAll(hang1, hang2);
    return thanhDieuHuong;
}

}
