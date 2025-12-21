package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.model.ChiTietHoaDon;
import com.thefourrestaurant.model.DieuKien_MonTang;
import com.thefourrestaurant.model.HoaDon;
import com.thefourrestaurant.model.KhuyenMai_DieuKien;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GiaoDienChiTietHoaDon extends VBox {

    // ===== Khai báo biến instance =====
    private final Label lblSDT = new Label();
    private final Label lblGioNhan = new Label();
    private final Label lblTenKH = new Label();
    private final Label lblGioTra = new Label();
    private final TableView<ChiTietHoaDon> table = new TableView<>();
    private final Map<String, Label> thongTinPhu = new HashMap<>();

    public GiaoDienChiTietHoaDon() {
        setPadding(new Insets(20));
        setSpacing(10);
        setAlignment(Pos.CENTER);
        Label TieuDe = new Label("Thông Tin Chi Tiết Hóa Đơn");
        TieuDe.setStyle("-fx-font-size: 24; -fx-text-fill: #DDB248; -fx-font-weight: bold;");


        // ==== Thông tin khách hàng / giờ nhận trả ====
        getChildren().addAll(
                TieuDe,
                taoDongThongTin("SĐT khách hàng:", lblSDT),
                taoDongThongTin("Tên khách hàng:", lblTenKH)
        );

        // ==== Table chi tiết món ====
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        TableColumn<ChiTietHoaDon, String> sttCol = new TableColumn<>("STT");
        sttCol.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(table.getItems().indexOf(cd.getValue()) + 1))
        );

        TableColumn<ChiTietHoaDon, String> tenMonCol = new TableColumn<>("Tên món");
        tenMonCol.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getMonAn().getTenMon())
        );

        TableColumn<ChiTietHoaDon, String> donGiaCol = new TableColumn<>("Đơn giá");
        donGiaCol.setCellValueFactory(cd ->
                new SimpleStringProperty(String.format("%,.0f đ", cd.getValue().getDonGia()))
        );


        TableColumn<ChiTietHoaDon, String> soLuongCol = new TableColumn<>("Số lượng");
        soLuongCol.setCellValueFactory(cd ->
                new SimpleStringProperty(String.valueOf(cd.getValue().getSoLuong()))
        );

        TableColumn<ChiTietHoaDon, String> thanhTienCol = new TableColumn<>("Thành tiền");
        thanhTienCol.setCellValueFactory(cd -> {
            BigDecimal tt = cd.getValue().getDonGia()
                    .multiply(BigDecimal.valueOf(cd.getValue().getSoLuong()));
            return new SimpleStringProperty(String.format("%,.0f đ", tt));
        });

        table.getColumns().addAll(sttCol, tenMonCol, donGiaCol, soLuongCol, thanhTienCol);

        getChildren().add(table);

        // ==== Thông tin tổng kết ====
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(5);
        infoGrid.setPadding(new Insets(10, 0, 0, 0));

        String[] labels = {"Mã giảm giá:", "Chiết khấu:", "Thành tiền:","Thuế VAT:","Tiền thanh toán:", "Tiền đặt cọc trước:", "Tiền nhận:", "Tiền thừa:"};

        for (int i = 0; i < labels.length; i++) {
            Label left = new Label(labels[i]);
            Label right = new Label();
            thongTinPhu.put(labels[i], right);
            infoGrid.add(left, 0, i);
            infoGrid.add(right, 1, i);
        }

        getChildren().add(infoGrid);
    }

    // ===== Hàm hiển thị thông tin hóa đơn =====
    public void hienThiThongTin(HoaDon hd) {
        lblSDT.setText(hd.getKhachHang() != null ? hd.getKhachHang().getSoDT() : "");
        lblTenKH.setText(hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : "");
        lblGioNhan.setText(hd.getPhieuDatBan() != null && hd.getPhieuDatBan().getNgayDat() != null
                ? hd.getPhieuDatBan().getNgayDat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "null");
        // Table chi tiết món
        table.getItems().setAll(hd.getChiTietHoaDon());

        // Các dòng tổng kết
        BigDecimal tongTien = hd.getTongTien();
        thongTinPhu.get("Mã giảm giá:").setText(hd.getKhuyenMai() != null ? hd.getKhuyenMai().getTenKM() : "Không áp dụng");
        if (hd.getKhuyenMai() != null && hd.getKhuyenMai().getKhuyenMaiDieuKien() != null) {
            KhuyenMai_DieuKien dk = hd.getKhuyenMai().getKhuyenMaiDieuKien();
            String loai = dk.getLoaiApDung();

            switch (loai.toUpperCase()) {
                case "GIAM_TRUC_TIEP":
                    if (dk.getTyLeGiam() != null) {
                        thongTinPhu.get("Chiết khấu:")
                                .setText(dk.getTyLeGiam() + "%");
                    } else if (dk.getSoTienGiam() != null) {
                        thongTinPhu.get("Chiết khấu:")
                                .setText(String.format("%,.0f đ", dk.getSoTienGiam()));
                    } else {
                        thongTinPhu.get("Chiết khấu:").setText("0 đ");
                    }
                    break;

                default:
                    thongTinPhu.get("Chiết khấu:").setText("Không áp dụng");
            }
        } else {
            thongTinPhu.get("Chiết khấu:").setText("Không áp dụng");
        }
        thongTinPhu.get("Thuế VAT:").setText(hd.getThue() != null ? hd.getThue().getTyLe() + "%" : "0%");
        thongTinPhu.get("Tiền nhận:").setText(String.format("%,.0f đ", hd.getTienKhachDua()));
        thongTinPhu.get("Tiền thừa:").setText(String.format("%,.0f đ", hd.getTienThua()));
        thongTinPhu.get("Tiền đặt cọc trước:").setText(String.format("%,.0f đ", hd.getPhieuDatBan().getTienCoc()));
        thongTinPhu.get("Tiền thanh toán:").setText(String.format("%,.0f đ", tongTien));
        thongTinPhu.get("Thành tiền:").setText(String.format("%,.0f đ", hd.getTongTienGoc()));
    }

    // ===== Hàm tạo dòng nhãn + giá trị =====
    private HBox taoDongThongTin(String tieuDe, Label lblGiaTri) {
        Label lblTieuDe = new Label(tieuDe);
        lblTieuDe.setMinWidth(130);
        HBox box = new HBox(10, lblTieuDe, lblGiaTri);
        return box;
    }
}
