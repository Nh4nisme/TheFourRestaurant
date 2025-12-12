package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.KhuyenMaiDAO;
import com.thefourrestaurant.DAO.LoaiBanDAO;
import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiBan;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThongKeGiaoDien extends BorderPane {

    private final DatePicker boChonNgayBatDau;
    private final DatePicker boChonNgayKetThuc;
    private final ComboBox<String> hopChonLoaiThongKe;
    private final ComboBox<String> hopChonLoaiBieuDo;
    private final ButtonSample nutXemThongKe;
    private final VBox khuVucBieuDo;
    private final CheckBox hopKiemSoSanh;
    private final ComboBox<String> hopChonTheo;
    private final ComboBox<String> hopChonTuyChon;
    private final GridPane luoiTomTat;

    private final HBox hopChonNgay;
    private final ComboBox<Integer> hopChonThang;
    private final ComboBox<Integer> hopChonNam;
    private final ComboBox<String> hopChonQuy;

    public ThongKeGiaoDien() {
        this.setStyle("-fx-background-color: white;");

//        Label nhanTieuDe = new Label("Báo Cáo & Thống Kê");
//        nhanTieuDe.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #D4A017;");
//        HBox tieuDe = new HBox(nhanTieuDe);
//        tieuDe.setPadding(new Insets(15));
//        tieuDe.setAlignment(Pos.CENTER_LEFT);
//        tieuDe.setStyle("-fx-background-color: #1E424D;");
//        this.setTop(tieuDe);

        HBox hopDieuKhien = new HBox(20);
        hopDieuKhien.setPadding(new Insets(20));
        hopDieuKhien.setAlignment(Pos.CENTER_LEFT);

        VBox hopTuyChon = new VBox(15);

        hopChonTheo = new ComboBox<>();
        hopChonLoaiThongKe = new ComboBox<>();
        hopChonTuyChon = new ComboBox<>();
        hopChonLoaiBieuDo = new ComboBox<>();
        
        boChonNgayBatDau = new DatePicker(LocalDate.now().withDayOfMonth(1));
        boChonNgayKetThuc = new DatePicker(LocalDate.now());
        hopChonThang = new ComboBox<>();
        hopChonNam = new ComboBox<>();
        hopChonQuy = new ComboBox<>();

        hopChonTheo.getStyleClass().add("thongke-input");
        boChonNgayBatDau.getStyleClass().add("thongke-input");
        boChonNgayKetThuc.getStyleClass().add("thongke-input");
        hopChonLoaiThongKe.getStyleClass().add("thongke-input");
        hopChonTuyChon.getStyleClass().add("thongke-input");
        hopChonLoaiBieuDo.getStyleClass().add("thongke-input");
        hopChonThang.getStyleClass().add("thongke-input");
        hopChonNam.getStyleClass().add("thongke-input");
        hopChonQuy.getStyleClass().add("thongke-input");

        hopChonTheo.getItems().addAll("Ngày", "Tháng", "Quý", "Năm");
        hopChonTheo.setValue("Ngày");
        
        hopChonLoaiThongKe.getItems().addAll("Doanh thu", "Món ăn", "Bàn");
        hopChonLoaiThongKe.setValue("Doanh thu");
        
        hopChonLoaiBieuDo.getItems().addAll("Biểu đồ cột", "Biểu đồ đường", "Biểu đồ tròn");
        hopChonLoaiBieuDo.setValue("Biểu đồ cột");

        hopChonThang.getItems().addAll(IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList()));
        hopChonThang.setValue(LocalDate.now().getMonthValue());

        int namHienTai = LocalDate.now().getYear();
        hopChonNam.getItems().addAll(IntStream.rangeClosed(namHienTai - 10, namHienTai).boxed().sorted((a, b) -> b - a).toList());
        hopChonNam.setValue(namHienTai);

        hopChonQuy.getItems().addAll("Quý 1", "Quý 2", "Quý 3", "Quý 4");
        hopChonQuy.setValue("Quý 1");

        HBox hangChonThoiGian = new HBox(10);
        hangChonThoiGian.setAlignment(Pos.CENTER_LEFT);
        Label nhanTheo = new Label("Theo:");
        nhanTheo.getStyleClass().add("thongke-label");
        
        hopChonNgay = new HBox(10);
        hopChonNgay.setAlignment(Pos.CENTER_LEFT);
        
        hangChonThoiGian.getChildren().addAll(nhanTheo, hopChonTheo, hopChonNgay);
        
        HBox hangChonThongKe = new HBox(10);
        hangChonThongKe.setAlignment(Pos.CENTER_LEFT);
        Label nhanLoaiThongKe = new Label("Thống kê theo:");
        Label nhanTuyChon = new Label("Tùy chọn:");
        Label nhanLoaiBieuDo = new Label("Loại biểu đồ:");
        
        nhanLoaiThongKe.getStyleClass().add("thongke-label");
        nhanTuyChon.getStyleClass().add("thongke-label");
        nhanLoaiBieuDo.getStyleClass().add("thongke-label");
        
        hangChonThongKe.getChildren().addAll(nhanLoaiThongKe, hopChonLoaiThongKe, nhanTuyChon, hopChonTuyChon, nhanLoaiBieuDo, hopChonLoaiBieuDo);

        hopTuyChon.getChildren().addAll(hangChonThoiGian, hangChonThongKe);

        hopChonTheo.valueProperty().addListener((obs, giaTriCu, giaTriMoi) -> capNhatDieuKhienChonNgay(giaTriMoi));
        capNhatDieuKhienChonNgay(hopChonTheo.getValue());

        hopChonLoaiThongKe.valueProperty().addListener((obs, giaTriCu, giaTriMoi) -> capNhatHopChonTuyChon(giaTriMoi));
        capNhatHopChonTuyChon(hopChonLoaiThongKe.getValue());

        nutXemThongKe = new ButtonSample("Xem Thống Kê", 50, 25, 14);
        nutXemThongKe.getStyleClass().add("button_sampleGamboge");
        
        hopKiemSoSanh = new CheckBox("So sánh");
        hopKiemSoSanh.getStyleClass().add("thongke-label");

        VBox hopDieuKhienPhai = new VBox(10, hopKiemSoSanh, nutXemThongKe);
        hopDieuKhienPhai.setAlignment(Pos.CENTER_LEFT);

        hopDieuKhien.getChildren().addAll(hopTuyChon, hopDieuKhienPhai);
        HBox.setHgrow(hopTuyChon, Priority.ALWAYS);

        khuVucBieuDo = new VBox();
        khuVucBieuDo.setAlignment(Pos.CENTER);
        khuVucBieuDo.setPadding(new Insets(20));
        khuVucBieuDo.setPrefHeight(600);
        
        Label nhanGiuCho = new Label("Chọn các tùy chọn và nhấn 'Xem Thống Kê' để tạo báo cáo.");
        nhanGiuCho.setStyle("-fx-font-size: 16px; -fx-text-fill: #888;");
        khuVucBieuDo.getChildren().add(nhanGiuCho);

        luoiTomTat = new GridPane();
        luoiTomTat.setHgap(20);
        luoiTomTat.setVgap(10);
        luoiTomTat.setPadding(new Insets(10, 20, 20, 20));
        luoiTomTat.setVisible(false);

        VBox boCucGiua = new VBox(20, hopDieuKhien, khuVucBieuDo, luoiTomTat);
        this.setCenter(boCucGiua);

        new ThongKeDieuKhien(this);
    }

    private void capNhatDieuKhienChonNgay(String luaChon) {
        hopChonNgay.getChildren().clear();
        if (luaChon == null) return;

        switch (luaChon) {
            case "Ngày":
                hopChonNgay.getChildren().addAll(
                        new Label("Từ ngày:"), boChonNgayBatDau,
                        new Label("Đến ngày:"), boChonNgayKetThuc
                );
                break;
            case "Tháng":
                hopChonNgay.getChildren().addAll(
                        new Label("Tháng:"), hopChonThang,
                        new Label("Năm:"), hopChonNam
                );
                break;
            case "Năm":
                hopChonNgay.getChildren().addAll(
                        new Label("Năm:"), hopChonNam
                );
                break;
            case "Quý":
                hopChonNgay.getChildren().addAll(
                        new Label("Quý:"), hopChonQuy,
                        new Label("Năm:"), hopChonNam
                );
                break;
        }
        for (Node nut : hopChonNgay.getChildren()) {
            if (nut instanceof Label) {
                nut.getStyleClass().add("thongke-label");
            }
        }
    }

    private void capNhatHopChonTuyChon(String loaiThongKe) {
        if (loaiThongKe == null) {
            hopChonTuyChon.getItems().clear();
            return;
        }

        switch (loaiThongKe) {
            case "Món ăn":
                LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
                List<String> tenLoaiMon = loaiMonDAO.layTatCaLoaiMon().stream()
                        .map(LoaiMon::getTenLoaiMon)
                        .collect(Collectors.toList());
                hopChonTuyChon.getItems().setAll(tenLoaiMon);
                hopChonTuyChon.getItems().add(0, "Tất cả Món Ăn");
                hopChonTuyChon.setValue("Tất cả Món Ăn");
                break;
            case "Bàn":
                LoaiBanDAO loaiBanDAO = new LoaiBanDAO();
                List<String> tenLoaiBan = loaiBanDAO.layTatCa().stream()
                        .map(LoaiBan::getTenLoaiBan)
                        .collect(Collectors.toList());
                hopChonTuyChon.getItems().setAll(tenLoaiBan);
                hopChonTuyChon.getItems().add(0, "Tất cả Bàn");
                hopChonTuyChon.setValue("Tất cả Bàn");
                break;
            case "Doanh thu":
                KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
                List<String> tenKhuyenMai = khuyenMaiDAO.layDanhSachKhuyenMai().stream()
                        .map(KhuyenMai::getTenKM)
                        .collect(Collectors.toList());
                hopChonTuyChon.getItems().setAll(tenKhuyenMai);
                hopChonTuyChon.getItems().add(0, "Tất cả Khuyến Mãi");
                hopChonTuyChon.setValue("Tất cả Khuyến Mãi");
                break;
            default:
                hopChonTuyChon.getItems().clear();
                break;
        }
    }

    public DatePicker layBoChonNgayBatDau() { return boChonNgayBatDau; }
    public DatePicker layBoChonNgayKetThuc() { return boChonNgayKetThuc; }
    public ComboBox<String> layHopChonLoaiThongKe() { return hopChonLoaiThongKe; }
    public ComboBox<String> layHopChonLoaiBieuDo() { return hopChonLoaiBieuDo; }
    public ButtonSample layNutXemThongKe() { return nutXemThongKe; }
    public VBox layKhuVucBieuDo() { return khuVucBieuDo; }
    public CheckBox layHopKiemSoSanh() { return hopKiemSoSanh; }
    public ComboBox<String> layHopChonTheo() { return hopChonTheo; }
    public ComboBox<String> layHopChonTuyChon() { return hopChonTuyChon; }
    public ComboBox<Integer> layHopChonThang() { return hopChonThang; }
    public ComboBox<Integer> layHopChonNam() { return hopChonNam; }
    public ComboBox<String> layHopChonQuy() { return hopChonQuy; }
    public GridPane layLuoiTomTat() { return luoiTomTat; }
}