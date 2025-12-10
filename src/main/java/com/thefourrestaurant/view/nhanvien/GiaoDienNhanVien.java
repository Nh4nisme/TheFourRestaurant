package com.thefourrestaurant.view.nhanvien;

import com.thefourrestaurant.controller.NhanVienController;
import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class GiaoDienNhanVien extends GiaoDienThucThe {

    private final NhanVienController controller = new NhanVienController();
    private final GiaoDienChiTietNhanVien gdChiTiet;
    private TableView<NhanVien> table;
    private ObservableList<NhanVien> danhSachGoc;
    private ObservableList<NhanVien> danhSachHienThi;

    public GiaoDienNhanVien() {
        super("Nhân viên", new GiaoDienChiTietNhanVien());
        gdChiTiet = (GiaoDienChiTietNhanVien) getChiTietNode();
        khoiTaoGiaoDien();
        lamMoiDuLieu();
        khoiTaoSuKien();
    }

    @Override
    protected TableView<?> taoBangChinh() {
        table = new TableView<>();

        TableColumn<NhanVien, String> colMa = new TableColumn<>("Mã NV");
        colMa.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getMaNV()));

        TableColumn<NhanVien, String> colHoTen = new TableColumn<>("Họ tên");
        colHoTen.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getHoTen()));

        TableColumn<NhanVien, String> colSDT = new TableColumn<>("SĐT");
        colSDT.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSoDienThoai()));

        TableColumn<NhanVien, String> colLuong = new TableColumn<>("Lương");
        colLuong.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getLuong() == null ? "" : cd.getValue().getLuong().toPlainString()
        ));

        table.getColumns().addAll(colMa, colHoTen, colSDT, colLuong);
        return table;
    }

    @Override
    protected void lamMoiDuLieu() {
        List<NhanVien> ds = controller.layDanhSachNhanVien();
        danhSachGoc = FXCollections.observableArrayList(ds);
        danhSachHienThi = FXCollections.observableArrayList(ds);
        table.setItems(danhSachHienThi);
    }

    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            table.setItems(danhSachGoc);
            return;
        }
        String q = tuKhoa.toLowerCase();
        ObservableList<NhanVien> filtered = danhSachGoc.filtered(nv ->
                (nv.getMaNV() != null && nv.getMaNV().toLowerCase().contains(q)) ||
                (nv.getHoTen() != null && nv.getHoTen().toLowerCase().contains(q)) ||
                (nv.getSoDienThoai() != null && nv.getSoDienThoai().toLowerCase().contains(q))
        );
        table.setItems(filtered);
    }

    private void khoiTaoSuKien() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            gdChiTiet.hienThi(newV);
        });
    }
}
