package com.thefourrestaurant.view.taikhoan;

import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GiaoDienChiTietTaiKhoan extends VBox {

    private TextField txtMaTK, txtTenDangNhap, txtMatKhau, txtVaiTro;
    private ButtonSample btnLuu, btnClear, btnTaoMoi;
    private ComboBox<VaiTro> cboVaiTro;

    public GiaoDienChiTietTaiKhoan() {
        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(Pos.TOP_LEFT);
        getStyleClass().add("giao-dien-chi-tiet-tai-khoan");

        // ==== Tiêu đề ====
        Label lblTieuDe = new Label("Thông tin tài khoản");
        lblTieuDe.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ==== Các trường nhập ====
        txtMaTK = taoTextField("Mã tài khoản");
        txtTenDangNhap = taoTextField("Tên đăng nhập");
        txtMatKhau = taoTextField("Mật khẩu");
        cboVaiTro = new ComboBox<>();
        cboVaiTro.setPromptText("Chọn vai trò");
        cboVaiTro.setMaxWidth(Double.MAX_VALUE);
        cboVaiTro.setPrefHeight(40);
        cboVaiTro.setCellFactory(cbo -> new ListCell<>() {
            @Override
            protected void updateItem(VaiTro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenVaiTro());
            }
        });
        cboVaiTro.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(VaiTro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenVaiTro());
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
        getChildren().addAll(lblTieuDe, txtMaTK, txtTenDangNhap, txtMatKhau, cboVaiTro, nutBox);

        btnClear.setOnAction(e -> Clear());
    }

    /** Tạo TextField có cùng style và placeholder */
    private TextField taoTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(40);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle("-fx-font-size: 15; -fx-background-radius: 6;");
        return tf;
    }

    public void hienThiThongTin(TaiKhoan tk) {
        if (tk == null) return;

        txtMaTK.setText(tk.getMaTK());
        txtMaTK.setDisable(true);
        txtTenDangNhap.setText(tk.getTenDN());
        txtMatKhau.setText(tk.getMatKhau());
        if (tk.getVaiTro() != null) {
            VaiTro vt = cboVaiTro.getItems().stream()
                    .filter(v -> v.getMaVT().equals(tk.getVaiTro().getMaVT()))
                    .findFirst()
                    .orElse(null); // Lấy chính object trong ComboBox
            cboVaiTro.setValue(vt);
        } else {
            cboVaiTro.getSelectionModel().clearSelection();
        }
    }

    public void Clear() {
        txtMaTK.clear();
        txtMaTK.setDisable(false);
        txtTenDangNhap.clear();
        txtMatKhau.clear();
    }

    // Getter để sau này controller có thể thao tác
    public TextField getTxtMaTK() { return txtMaTK; }
    public TextField getTxtTenDangNhap() { return txtTenDangNhap; }
    public TextField getTxtMatKhau() { return txtMatKhau; }
    public ComboBox<VaiTro> getCboVaiTro() {return cboVaiTro;}

    public ButtonSample getBtnLuu() { return btnLuu; }
    public ButtonSample getBtnClear() { return btnClear; }
    public ButtonSample getBtnTaoMoi() { return btnTaoMoi; }

}
