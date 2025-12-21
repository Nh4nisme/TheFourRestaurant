package com.thefourrestaurant.view.khachhang;

import com.thefourrestaurant.controller.KhachHangController;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.view.components.GiaoDienTraCuu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;

import java.util.List;

public class GiaoDienTraCuuKhachHang extends GiaoDienTraCuu {

    private TableView<KhachHang> table;
    private KhachHangController controller;
    private ObservableList<KhachHang> danhSachGoc;
    private FilteredList<KhachHang> danhSachHienThi;
    private String chuCaiLoc = "";


    public GiaoDienTraCuuKhachHang() {
        controller = new KhachHangController();
        khoiTaoGiaoDien();
        themThanhTimKiem("nhập số điện thoại");
        themButtonLamMoi();
        themBoLocChuCai();
        lamMoiDuLieu();
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

        table.getColumns().addAll(colMaKH, colHoTen, colNgaySinh, colGioiTinh, colSoDT, colLoaiKH);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<KhachHang> dsKhachHang = controller.layDanhSachKhachHang();
        table.getItems().setAll(dsKhachHang);

        return table;
    }

    @Override
    protected void lamMoiDuLieu() {
        danhSachGoc = FXCollections.observableArrayList(controller.layDanhSachKhachHang());
        danhSachHienThi = new FilteredList<>(danhSachGoc, kh -> true);
        table.setItems(danhSachHienThi);
    }


    private void apDungBoLoc() {
        String tuKhoa = txtTimKiem.getText();
        String key = tuKhoa == null ? "" : tuKhoa.trim();

        danhSachHienThi.setPredicate(kh -> {

            // Lọc theo chữ cái
            if (chuCaiLoc != null && !chuCaiLoc.isEmpty()) {
                if (kh.getHoTen() == null || !kh.getHoTen().toUpperCase().startsWith(chuCaiLoc))
                    return false;
            }

            // Lọc theo số điện thoại
            if (!key.isEmpty()) {
                return kh.getSoDT() != null && kh.getSoDT().contains(key);
            }

            return true; // Nếu pass tất cả điều kiện
        });
    }


    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        apDungBoLoc();
    }

    @Override
    protected void thucHienLocTheoChuCai(String chuCai) {
        this.chuCaiLoc = chuCai.toUpperCase();
        apDungBoLoc();
    }
}
