package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.view.components.GiaoDienThucThe;
import javafx.scene.control.*;

public class GiaoDienHoaDon extends GiaoDienThucThe {
    public GiaoDienHoaDon() {
        super("Hóa đơn", new GiaoDienChiTietHoaDon());
    }

    @Override
    protected TableView<?> taoBangChinh() {
        TableView<Object> table = new TableView<>();
        table.getColumns().addAll(
                new TableColumn<>("Mã HD"),
                new TableColumn<>("Trạng thái"),
                new TableColumn<>("Phương thức thanh toán"),
                new TableColumn<>("Ngày lập hóa đơn"),
                new TableColumn<>("Tổng tiền"),
                new TableColumn<>("Hành động")
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }
}
