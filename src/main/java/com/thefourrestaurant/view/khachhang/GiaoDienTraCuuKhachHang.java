package com.thefourrestaurant.view.khachhang;

import com.thefourrestaurant.controller.KhachHangController;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.view.components.GiaoDienTraCuu;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class GiaoDienTraCuuKhachHang extends GiaoDienTraCuu {

    private TableView<KhachHang> table;
    private KhachHangController controller;

    public GiaoDienTraCuuKhachHang() {
        controller = new KhachHangController();
        khoiTaoGiaoDien();
        themBoLocChuCai();
    }

    @Override
    protected TableView<KhachHang> taoBangChinh() {
        table = new TableView<>();

        TableColumn<KhachHang, String> colMaKH = new TableColumn<>("Mã KH");
        colMaKH.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMaKH()));

        TableColumn<KhachHang, String> colHoTen = new TableColumn<>("Họ tên");
        colHoTen.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getHoTen()));

        TableColumn<KhachHang, String> colNgaySinh = new TableColumn<>("Ngày sinh");
        colNgaySinh.setCellValueFactory(cell -> {
            if (cell.getValue().getNgaySinh() != null) {
                return new SimpleStringProperty(
                        new java.text.SimpleDateFormat("dd/MM/yyyy").format(cell.getValue().getNgaySinh())
                );
            }
            return new SimpleStringProperty("");
        });

        TableColumn<KhachHang, String> colGioiTinh = new TableColumn<>("Giới tính");
        colGioiTinh.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGioiTinh()));

        TableColumn<KhachHang, String> colSoDT = new TableColumn<>("Số ĐT");
        colSoDT.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSoDT()));

        TableColumn<KhachHang, String> colLoaiKH = new TableColumn<>("Loại KH");
        colLoaiKH.setCellValueFactory(cell -> {
            if (cell.getValue().getLoaiKH() != null) {
                return new SimpleStringProperty(cell.getValue().getLoaiKH().getTenLoaiKH());
            }
            return new SimpleStringProperty("");
        });

        TableColumn<KhachHang, Void> colHanhDong = new TableColumn<>("Hành động");
        colHanhDong.setCellFactory(col -> new TableCell<>() {
            private final Button btnXoa = new Button("🗑");


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnXoa);
            }
        });

        table.getColumns().addAll(colMaKH, colHoTen, colNgaySinh, colGioiTinh, colSoDT, colLoaiKH, colHanhDong);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<KhachHang> dsKhachHang = controller.layDanhSachKhachHang();
        table.getItems().setAll(dsKhachHang);

        return table;
    }

    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        // logic
    }

    @Override
    protected void lamMoiDuLieu() {
        // logic
    }
}
