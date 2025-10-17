package com.thefourrestaurant.view.taikhoan;

import com.thefourrestaurant.view.GiaoDienThucThe;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class GiaoDienTaiKhoan extends GiaoDienThucThe {

    public GiaoDienTaiKhoan() {
        super("Tài khoản", new GiaoDienChiTietTaiKhoan());
    }

    @Override
    protected TableView<?> taoBangChinh() {
        TableView<Object> table = new TableView<>();
        table.getColumns().addAll(
                new TableColumn<>("Mã TK"),
                new TableColumn<>("Tên đăng nhập"),
                new TableColumn<>("Mật khẩu"),
                new TableColumn<>("Vai trò"),
                new TableColumn<>("Hành động")
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }
}
