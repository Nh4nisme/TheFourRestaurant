package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.controller.HoaDonController;
import com.thefourrestaurant.model.HoaDon;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.PhuongThucThanhToan;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiaoDienHoaDon extends GiaoDienThucThe {
    private final HoaDonController controller = new HoaDonController();
    private GiaoDienChiTietHoaDon gdChiTietHoaDon;
    private TableView<HoaDon> table;
    private ObservableList<HoaDon> danhSachGoc;
    private FilteredList<HoaDon> danhSachHienThi;

    public GiaoDienHoaDon() {
        super("Hóa đơn", new GiaoDienChiTietHoaDon());
        gdChiTietHoaDon = (GiaoDienChiTietHoaDon) getChiTietNode();
        khoiTaoGiaoDien();
        khoiTaoBoLocNgayCuThe();
        khoiTaoBoLocTimKiem("nhập số điện thoại khách hàng...");
        lamMoiDuLieu();
    }

    @Override
    protected TableView<?> taoBangChinh() {
        table = new TableView<>();

        // ===== Cột Mã hóa đơn =====
        TableColumn<HoaDon, String> colMaHD = new TableColumn<>("Mã HĐ");
        colMaHD.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getMaHD()));

        // ===== Cột Ngày lập =====
        TableColumn<HoaDon, String> colNgayLap = new TableColumn<>("Ngày lập");
        colNgayLap.setCellValueFactory(cd -> {
            LocalDateTime date = cd.getValue().getNgayLap();
            return new SimpleStringProperty(date != null
                    ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "");
        });

        // ===== Cột SĐT Khách =====
        TableColumn<HoaDon, String> colSoDT = new TableColumn<>("SĐT Khách");
        colSoDT.setCellValueFactory(cd -> {
            KhachHang kh = cd.getValue().getKhachHang();
            return new SimpleStringProperty(kh != null ? kh.getSoDT() : "");
        });

        // ===== Cột Phương thức thanh toán =====
        TableColumn<HoaDon, String> colPTTT = new TableColumn<>("Phương thức TT");
        colPTTT.setCellValueFactory(cd -> {
            PhuongThucThanhToan pttt = cd.getValue().getPhuongThucThanhToan();
            String value = "null";
            if (pttt != null && pttt.getLoaiPTTT() != null) {
                value = pttt.getLoaiPTTT().toString();
            }
            return new SimpleStringProperty(value);
        });

        // ===== Cột Tổng tiền =====
        TableColumn<HoaDon, String> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(cd -> {
            BigDecimal tong = cd.getValue().getTongTien();
            String formatted = tong != null ? String.format("%,.0f đ", tong) : "";
            return new SimpleStringProperty(formatted);
        });
        colTongTien.setStyle("-fx-alignment: CENTER-RIGHT;");

        table.getColumns().addAll(colMaHD, colNgayLap, colSoDT, colPTTT, colTongTien);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ===== Lấy dữ liệu từ Controller =====
        List<HoaDon> dsHoaDon = controller.layDanhSachHoaDon();
        table.getItems().setAll(dsHoaDon);

        table.setRowFactory(t ->{
            TableRow<HoaDon> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (!row.isEmpty()) {
                    HoaDon hd = row.getItem();
                    hienThiChiTiet(hd);
                }
            });
            return row;
        });

        return table;
    }

    @Override
    protected void lamMoiDuLieu() {
        danhSachGoc = FXCollections.observableArrayList(controller.layDanhSachHoaDon());
        danhSachHienThi = new FilteredList<>(danhSachGoc, hd -> true);
        table.setItems(danhSachHienThi);
    }

    private void apDungBoLoc() {
        String tuKhoa = txtTimKiem.getText();
        LocalDate ngay = dpNgayCuThe.getValue();

        String key = tuKhoa == null ? "" : tuKhoa.trim();

        danhSachHienThi.setPredicate(hd -> {

            // lọc theo sdt
            if (!key.isEmpty()) {
                if (hd.getKhachHang() == null) return false;
                String soDT = hd.getKhachHang().getSoDT();
                if (soDT == null || !soDT.contains(key))
                    return false;
            }

            // lọc theo ngayf
            if (ngay != null) {
                LocalDateTime ngayLap = hd.getNgayLap();
                if (ngayLap == null) return false;
                if (!ngayLap.toLocalDate().equals(ngay))
                    return false;
            }

            return true;
        });
    }


    @Override
    protected void thucHienTimKiem(String tuKhoa) {
        apDungBoLoc();
    }

    @Override
    protected void locTheoNgay(LocalDate tuNgay, LocalDate denNgay) {
        apDungBoLoc();
    }

    private void hienThiChiTiet(HoaDon hd) {gdChiTietHoaDon.hienThiThongTin(hd);}
}
