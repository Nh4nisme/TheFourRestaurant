package com.thefourrestaurant.view.taikhoan;

import com.thefourrestaurant.DAO.TaiKhoanDAO;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class GiaoDienTaiKhoan extends GiaoDienThucThe {

    public GiaoDienTaiKhoan() {
        super("Tài khoản", new GiaoDienChiTietTaiKhoan());
    }

    @Override
    protected TableView<TaiKhoan> taoBangChinh() {
        TableView<TaiKhoan> table = new TableView<>();

        // Cột Mã TK (String để tránh lỗi asObject)
        TableColumn<TaiKhoan, String> colMaTK = new TableColumn<>("Mã TK");
        colMaTK.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getMaTK())
        );

        // Cột Tên đăng nhập
        TableColumn<TaiKhoan, String> colTenDN = new TableColumn<>("Tên đăng nhập");
        colTenDN.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getTenDN())
        );

        // Cột Mật khẩu
        TableColumn<TaiKhoan, String> colMatKhau = new TableColumn<>("Mật khẩu");
        colMatKhau.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getMatKhau())
        );

        // Cột Vai trò (khóa ngoại)
        TableColumn<TaiKhoan, String> colVaiTro = new TableColumn<>("Vai trò");
        colVaiTro.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getVaiTro())
        );

        // Cột Hành động (có thể thêm Button sửa/xóa sau)
        TableColumn<TaiKhoan, String> colHanhDong = new TableColumn<>("Hành động");

        table.getColumns().addAll(colMaTK, colTenDN, colMatKhau, colVaiTro, colHanhDong);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load dữ liệu từ database
        table.getItems().addAll(TaiKhoanDAO.layDanhSachTaiKhoan());

        return table;
    }
}
