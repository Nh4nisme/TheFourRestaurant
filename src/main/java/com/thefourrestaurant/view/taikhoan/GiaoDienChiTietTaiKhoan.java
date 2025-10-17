package com.thefourrestaurant.view.taikhoan;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class GiaoDienChiTietTaiKhoan extends VBox {
    public <Item> GiaoDienChiTietTaiKhoan() {
        setPadding(new Insets(20));
        setSpacing(10);

        Label lblSDT = new Label("SDT khách hàng:");
        Label lblGioNhan = new Label("Giờ nhận bàn:");
        Label lblTenKH = new Label("Tên khách hàng:");
        Label lblGioTra = new Label("Giờ trả bàn:");

        // ==== Table ====
        TableView<Item> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        TableColumn<Item, String> sttCol = new TableColumn<>("STT");
        TableColumn<Item, String> tenMonCol = new TableColumn<>("Tên món");
        TableColumn<Item, String> donGiaCol = new TableColumn<>("Đơn giá");
        TableColumn<Item, String> donViCol = new TableColumn<>("Đơn vị");
        TableColumn<Item, String> soLuongCol = new TableColumn<>("Số lượng");
        TableColumn<Item, String> thanhTienCol = new TableColumn<>("Thành tiền");

        table.getColumns().addAll(sttCol, tenMonCol, donGiaCol, donViCol, soLuongCol, thanhTienCol);

        // Thêm dữ liệu mẫu


        // ==== Các dòng tổng kết ====
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(5);
        infoGrid.setPadding(new Insets(10, 0, 0, 0));

        String[] labels = {
                "Mã giảm giá:", "Chiết khấu:", "Tiền nhận:", "Tiền thừa:",
                "Tiền đặt cọc trước:", "Thuế VAT:", "Tiền thanh toán:"
        };

        for (int i = 0; i < labels.length; i++) {
            Label left = new Label(labels[i]);
            Label right = new Label(); // chỗ hiển thị giá trị
            infoGrid.add(left, 0, i);
            infoGrid.add(right, 1, i);
        }

        getChildren().addAll(lblSDT, lblGioNhan, lblTenKH, lblGioTra, table, infoGrid);
    }
}
