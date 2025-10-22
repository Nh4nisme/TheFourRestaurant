package com.thefourrestaurant.view.taikhoan;

import com.thefourrestaurant.DAO.TaiKhoanDAO;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class GiaoDienTaiKhoan extends GiaoDienThucThe {

    public GiaoDienTaiKhoan() {
        super("Tài khoản", new GiaoDienChiTietTaiKhoan());
    }

    @Override
    protected TableView<TaiKhoan> taoBangChinh() {
        TableView<TaiKhoan> table = new TableView<>();

        // Cột Mã TK
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

        // Cột Vai trò (lấy từ đối tượng VaiTro)
        TableColumn<TaiKhoan, String> colVaiTro = new TableColumn<>("Vai trò");
        colVaiTro.setCellValueFactory(cell -> {
            VaiTro vt = cell.getValue().getVaiTro();
            return new SimpleStringProperty(vt != null ? vt.getTenVaiTro() : "");
        });

        // Cột Hành động (để trống hoặc thêm nút sửa/xóa sau)
        TableColumn<TaiKhoan, String> colHanhDong = new TableColumn<>("Hành động");

        table.getColumns().addAll(colMaTK, colTenDN, colMatKhau, colVaiTro, colHanhDong);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load dữ liệu từ database
        List<TaiKhoan> dsTaiKhoan = TaiKhoanDAO.layDanhSachTaiKhoan();

        //Lọc ra các tài khoản chưa bị xóa (isDeleted == false)
        dsTaiKhoan.removeIf(TaiKhoan::isDeleted);

        //hêm dữ liệu vào bảng
        table.getItems().setAll(dsTaiKhoan);

        return table;
    }
}
