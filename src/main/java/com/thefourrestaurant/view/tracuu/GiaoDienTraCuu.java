package com.thefourrestaurant.view.tracuu;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * ============================
 * CLASS CHA – GIAO DIỆN TRA CỨU
 * ============================
 *
 * - Giữ layout top bar
 * - Khai báo toàn bộ filter
 * - Xử lý logic filter tại đây
 * - Class con chỉ bật filter cần dùng
 */
public abstract class GiaoDienTraCuu extends VBox {

    protected HBox thanhDieuHuong;

    protected TextField filterTimKiem;     // Filter tìm kiếm
    protected ComboBox<String> filterA;    // Filter A (vd: loại)
    protected ComboBox<String> filterB;    // Filter B (vd: sắp xếp)

    private boolean dungTimKiem = false;
    private boolean dungFilterA = false;
    private boolean dungFilterB = false;


    protected GiaoDienTraCuu() {
        khoiTaoGiaoDien();
        khoiTaoFilter();
        khoiTaoLogicFilter();
    }

    // Giao diện

    private void khoiTaoGiaoDien() {
        thanhDieuHuong = new HBox(10);
        thanhDieuHuong.setAlignment(Pos.CENTER_LEFT);
        thanhDieuHuong.setPrefHeight(80);
        thanhDieuHuong.setStyle("-fx-background-color: #1E424D;");

        getChildren().add(thanhDieuHuong);
    }

    // khởi tạo filter

    private void khoiTaoFilter() {

        filterTimKiem = new TextField();
        filterTimKiem.setPromptText("Tìm kiếm...");

        filterA = new ComboBox<>();
        filterA.getItems().addAll("Tất cả");

        filterB = new ComboBox<>();
        filterB.getItems().addAll("Mặc định");
    }

    // logic filter

    /**
     * Gắn listener cho tất cả filter
     * → Khi filter thay đổi sẽ gọi xuLyFilterTong()
     */
    private void khoiTaoLogicFilter() {

        filterTimKiem.textProperty().addListener((obs, o, n) -> {
            if (dungTimKiem) xuLyFilterTong();
        });

        filterA.valueProperty().addListener((obs, o, n) -> {
            if (dungFilterA) xuLyFilterTong();
        });

        filterB.valueProperty().addListener((obs, o, n) -> {
            if (dungFilterB) xuLyFilterTong();
        });
    }

    // dùng cho class con gọi

    /**
     * Bật filter tìm kiếm
     * → thêm vào giao diện + cho phép xử lý
     */
    protected void batFilterTimKiem() {
        dungTimKiem = true;
        thanhDieuHuong.getChildren().add(filterTimKiem);
    }

    /**
     * Bật filter A
     * (ví dụ: lọc theo loại)
     */
    protected void batFilterA() {
        dungFilterA = true;
        thanhDieuHuong.getChildren().add(filterA);
    }

    /**
     * Bật filter B
     * (ví dụ: sắp xếp)
     */
    protected void batFilterB() {
        dungFilterB = true;
        thanhDieuHuong.getChildren().add(filterB);
    }

    // xử lý tổng

    /**
     * HÀM QUAN TRỌNG NHẤT
     *
     * - Được gọi tự động khi filter thay đổi
     * - Class con override để xử lý lọc dữ liệu
     * - Dùng biến dungFilterX để biết filter nào đang bật
     */
    protected void xuLyFilterTong() {
        // TODO:
        // if (dungTimKiem) đọc filterTimKiem.getText()
        // if (dungFilterA) đọc filterA.getValue()
        // if (dungFilterB) đọc filterB.getValue()
        // lọc danh sách
        // cập nhật UI
    }
}
