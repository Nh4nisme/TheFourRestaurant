package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.controller.HoaDonController;
import com.thefourrestaurant.model.HoaDon;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.PhuongThucThanhToan;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiaoDienHoaDon extends GiaoDienThucThe {
    private final HoaDonController controller = new HoaDonController();
    private GiaoDienChiTietHoaDon gdChiTietHoaDon;
    private TableView<HoaDon> table;

    public GiaoDienHoaDon() {
        super("Hóa đơn", new GiaoDienChiTietHoaDon());
        gdChiTietHoaDon = (GiaoDienChiTietHoaDon) getChiTietNode();
        khoiTaoGiaoDien();
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
            return new SimpleStringProperty(
                    (pttt != null && pttt.getLoaiPTTT() != null)
                            ? pttt.getLoaiPTTT().getTenHienThi()
                            : ""
            );
        });

        // ===== Cột Tổng tiền =====
        TableColumn<HoaDon, String> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(cd -> {
            BigDecimal tong = cd.getValue().getTongTien();
            String formatted = tong != null ? String.format("%,.0f đ", tong) : "";
            return new SimpleStringProperty(formatted);
        });
        colTongTien.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<HoaDon, Void> colHanhDong = new TableColumn<>("Hành động");
        colHanhDong.setCellFactory(col -> new TableCell<>() {
            private final Button btnXoa = new Button("🗑");

            {
                btnXoa.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 14;");
                btnXoa.setOnAction(event -> {
                    HoaDon hd = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) btnXoa.getScene().getWindow();

                    if (xacNhan(stage, "Bạn có chắc muốn xóa hóa đơn: " + hd.getMaHD() + " ?")) {
                        boolean ok = controller.xoaHoaDon(hd.getMaHD()); // Gọi controller/DAO xóa hóa đơn

                        if (ok) {
                            getTableView().getItems().remove(hd);
                            hienThongBao(stage,"Đã xóa hóa đơn!");
                        } else {
                            hienThongBao(stage,"Không thể xóa hóa đơn này!", Alert.AlertType.ERROR);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnXoa);
            }
        });

        table.getColumns().addAll(colMaHD, colNgayLap, colSoDT, colPTTT, colTongTien, colHanhDong);
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
    protected void thucHienTimKiem(String tuKhoa) {

    }

    @Override
    protected void lamMoiDuLieu() {

    }

    private void hienThiChiTiet(HoaDon hd) {gdChiTietHoaDon.hienThiThongTin(hd);}
}
