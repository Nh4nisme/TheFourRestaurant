//package com.thefourrestaurant.view.hoadon;
//
//import com.thefourrestaurant.controller.HoaDonController;
//import com.thefourrestaurant.model.HoaDon;
//import com.thefourrestaurant.model.KhachHang;
//import com.thefourrestaurant.model.PhuongThucThanhToan;
//import com.thefourrestaurant.view.components.GiaoDienThucThe;
//
//import javafx.beans.property.SimpleStringProperty;
//import javafx.scene.control.Label;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class GiaoDienHoaDon extends GiaoDienThucThe {
//    private final HoaDonController hoaDonController = new HoaDonController();
//
//    public GiaoDienHoaDon() {
//        super("Hóa đơn", new GiaoDienChiTietHoaDon());
//        khoiTaoGiaoDien();
//    }
//
//    @Override
//    protected TableView<?> taoBangChinh() {
//        TableView<HoaDon> table = new TableView<>();
//
//        // ===== Cột Mã hóa đơn =====
//        TableColumn<HoaDon, String> colMaHD = new TableColumn<>("Mã HĐ");
//        colMaHD.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getMaHD()));
//
//        // ===== Cột Ngày lập =====
//        TableColumn<HoaDon, String> colNgayLap = new TableColumn<>("Ngày lập");
//        colNgayLap.setCellValueFactory(cd -> {
//            LocalDateTime date = cd.getValue().getNgayLap();
//            return new SimpleStringProperty(date != null
//                    ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                    : "");
//        });
//
//        // ===== Cột SĐT Khách =====
//        TableColumn<HoaDon, String> colSoDT = new TableColumn<>("SĐT Khách");
//        colSoDT.setCellValueFactory(cd -> {
//            KhachHang kh = cd.getValue().getKhachHang();
//            return new SimpleStringProperty(kh != null ? kh.getSoDT() : "");
//        });
//
//        // ===== Cột Phương thức thanh toán =====
//        TableColumn<HoaDon, String> colPTTT = new TableColumn<>("Phương thức TT");
//        colPTTT.setCellValueFactory(cd -> {
//            PhuongThucThanhToan pttt = cd.getValue().getPhuongThucThanhToan();
//            return new SimpleStringProperty(
//                    (pttt != null && pttt.getLoaiPTTT() != null)
//                            ? pttt.getLoaiPTTT().getTenHienThi()
//                            : ""
//            );
//        });
//
//        // ===== Cột Tổng tiền =====
//        TableColumn<HoaDon, String> colTongTien = new TableColumn<>("Tổng tiền");
//        colTongTien.setCellValueFactory(cd -> {
//            BigDecimal tong = cd.getValue().getTongTien();
//            String formatted = tong != null ? String.format("%,.0f đ", tong) : "";
//            return new SimpleStringProperty(formatted);
//        });
//        colTongTien.setStyle("-fx-alignment: CENTER-RIGHT;");
//
//        table.getColumns().addAll(colMaHD, colNgayLap, colSoDT, colPTTT, colTongTien);
//
//        // ===== Lấy dữ liệu từ Controller =====
//        table.setItems(hoaDonController.layDanhSachHoaDon());
//
//        // ===== Style =====
//        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        table.setPlaceholder(new Label("Chưa có hóa đơn nào."));
//
//        return table;
//    }
//}
