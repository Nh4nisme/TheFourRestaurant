package com.thefourrestaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.math.BigDecimal;

public class GiaoDienKetCa {
    private Stage stage;
    private BigDecimal loiNhuan;

    public GiaoDienKetCa() {}

    public void show(Stage stage) {
        this.stage = stage;
        VBox root = new VBox(30);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #fff; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10,0,0,2);");
        root.setMaxWidth(500);

        Label lblTitle = new Label("Kết ca - Thu ngân");
        lblTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Tính lợi nhuận cuối ca (giả lập, cần thay bằng truy vấn thực tế)
        loiNhuan = tinhLoiNhuanCuoiCa();
        Label lblLoiNhuan = new Label("Lợi nhuận cuối ca: " + loiNhuan + " VND");
        lblLoiNhuan.setStyle("-fx-font-size: 18px; -fx-text-fill: #4CAF50;");

        Button btnXacNhan = new Button("Xác nhận kết ca");
        btnXacNhan.setPrefHeight(50);
        btnXacNhan.setPrefWidth(250);
        btnXacNhan.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;");
        btnXacNhan.setOnAction(e -> {
            com.thefourrestaurant.util.Session.setCurrentUser(null);
            new GiaoDienDangNhap().show(stage);
        });

        root.getChildren().addAll(lblTitle, lblLoiNhuan, btnXacNhan);
        Scene scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    private BigDecimal tinhLoiNhuanCuoiCa() {
        com.thefourrestaurant.DAO.HoaDonDAO hoaDonDAO = new com.thefourrestaurant.DAO.HoaDonDAO();
        java.util.List<com.thefourrestaurant.model.HoaDon> ds = hoaDonDAO.layDanhSachHoaDon();
        com.thefourrestaurant.model.TaiKhoan user = com.thefourrestaurant.util.Session.getCurrentUser();
        java.time.LocalDateTime start = com.thefourrestaurant.util.Session.getLoginTime();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        BigDecimal sum = BigDecimal.ZERO;
        for (com.thefourrestaurant.model.HoaDon hd : ds) {
            if (hd.getNgayLap() != null &&
                !hd.isDeleted() &&
                hd.getNhanVien() != null &&
                user != null &&
                hd.getNhanVien().getMaNV() != null &&
                user.getMaTK() != null &&
                hd.getNgayLap().isAfter(start.minusSeconds(1)) && hd.getNgayLap().isBefore(now.plusSeconds(1))) {
                if (hd.getPhuongThucThanhToan() != null &&
                    hd.getPhuongThucThanhToan().getLoaiPTTT() == com.thefourrestaurant.model.PhuongThucThanhToan.LoaiPTTT.TIEN_MAT) {
                    sum = sum.add(hd.getTienKhachDua() != null ? hd.getTienKhachDua() : BigDecimal.ZERO);
                }
            }
        }
        return sum;
    }
}
