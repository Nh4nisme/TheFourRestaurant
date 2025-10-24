package com.thefourrestaurant.view.khachhang;

import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.LoaiKhachHang;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;

public class GiaoDienChiTietKhachHang extends VBox {

    private TextField txtMaKH, txtHoTen, txtGioiTinh, txtSoDT;
    private DatePicker dpNgaySinh;
    private ComboBox<LoaiKhachHang> cboLoaiKH;
    private ButtonSample btnLuu, btnClear, btnTaoMoi;

    public GiaoDienChiTietKhachHang() {
        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("giao-dien-chi-tiet-khach-hang");

        // ==== Tiêu đề ====
        Label lblTieuDe = new Label("Thông tin khách hàng");
        lblTieuDe.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #DDB248;");

        // ==== Các trường nhập ====
        txtMaKH = taoTextField("Mã khách hàng");
        txtHoTen = taoTextField("Họ tên");
        dpNgaySinh = new DatePicker();
        dpNgaySinh.setPromptText("Ngày sinh");
        dpNgaySinh.setMaxWidth(Double.MAX_VALUE);
        txtGioiTinh = taoTextField("Giới tính (Nam/Nu)");
        txtSoDT = taoTextField("Số điện thoại");

        cboLoaiKH = new ComboBox<>();
        cboLoaiKH.setPromptText("Chọn loại khách hàng");
        cboLoaiKH.setMaxWidth(Double.MAX_VALUE);
        cboLoaiKH.setCellFactory(cbo -> new ListCell<>() {
            @Override
            protected void updateItem(LoaiKhachHang item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenLoaiKH());
            }
        });
        cboLoaiKH.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LoaiKhachHang item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenLoaiKH());
            }
        });

        // ==== Nút chức năng ====
        btnLuu = new ButtonSample("Lưu", 40, 16, 3);
        btnClear = new ButtonSample("Xóa trắng", 40, 16, 3);
        btnTaoMoi = new ButtonSample("Tạo mới", 40, 16, 3);

        HBox nutBox = new HBox(10);
        nutBox.setAlignment(Pos.CENTER_LEFT);
        nutBox.getChildren().addAll(btnLuu, btnClear, btnTaoMoi);

        // ==== Thêm tất cả vào giao diện ====
        getChildren().addAll(lblTieuDe, txtMaKH, txtHoTen, dpNgaySinh, txtGioiTinh, txtSoDT, cboLoaiKH, nutBox);

        btnClear.setOnAction(e -> Clear());
    }

    /** Tạo TextField có cùng style và placeholder */
    private TextField taoTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(40);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle("-fx-font-size: 15px; -fx-background-radius: 6;");
        return tf;
    }

    public void hienThiThongTin(KhachHang kh) {
        if (kh == null) return;

        txtMaKH.setText(kh.getMaKH());
        txtMaKH.setDisable(true);
        txtHoTen.setText(kh.getHoTen());
        if (kh.getNgaySinh() != null) {
            dpNgaySinh.setValue(kh.getNgaySinh().toLocalDate());
        } else {
            dpNgaySinh.setValue(null);
        }
        txtGioiTinh.setText(kh.getGioiTinh());
        txtSoDT.setText(kh.getSoDT());

        if (kh.getLoaiKH() != null) {
            LoaiKhachHang lkh = cboLoaiKH.getItems().stream()
                    .filter(l -> l.getMaLoaiKH().equals(kh.getLoaiKH().getMaLoaiKH()))
                    .findFirst()
                    .orElse(null);
            cboLoaiKH.setValue(lkh);
        } else {
            cboLoaiKH.getSelectionModel().clearSelection();
        }
    }

    public void Clear() {
        txtMaKH.clear();
        txtMaKH.setDisable(false);
        txtHoTen.clear();
        dpNgaySinh.setValue(null);
        txtGioiTinh.clear();
        txtSoDT.clear();
        cboLoaiKH.getSelectionModel().clearSelection();
    }

    // Getter để controller thao tác
    public TextField getTxtMaKH() { return txtMaKH; }
    public TextField getTxtHoTen() { return txtHoTen; }
    public DatePicker getDpNgaySinh() { return dpNgaySinh; }
    public TextField getTxtGioiTinh() { return txtGioiTinh; }
    public TextField getTxtSoDT() { return txtSoDT; }
    public ComboBox<LoaiKhachHang> getCboLoaiKH() { return cboLoaiKH; }
    public ButtonSample getBtnLuu() { return btnLuu; }
    public ButtonSample getBtnClear() { return btnClear; }
    public ButtonSample getBtnTaoMoi() { return btnTaoMoi; }

}