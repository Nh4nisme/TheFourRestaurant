package com.thefourrestaurant.view.components.sidebar;

import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.DAO.TangDAO;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.view.QuanLyThucDon;
import com.thefourrestaurant.view.ban.GiaoDienPhieuDatBan;
import com.thefourrestaurant.view.ban.QuanLiBan;
import com.thefourrestaurant.view.hoadon.GiaoDienHoaDon;
import com.thefourrestaurant.view.khachhang.GiaoDienKhachHang;
import com.thefourrestaurant.view.khuyenmai.GiaoDienKhuyenMai;
import com.thefourrestaurant.view.loaimonan.LoaiMonAn;
import com.thefourrestaurant.view.monan.GiaoDienMonAn;
import com.thefourrestaurant.view.taikhoan.GiaoDienTaiKhoan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SideBarDanhMuc extends BaseSideBar {

    private final Pane mainContent;
    private final LoaiMonDAO loaiMonDAO;
    private final VBox container;

    public SideBarDanhMuc(Pane mainContent) {
        super("Quản Lý");
        this.mainContent = mainContent;
        this.loaiMonDAO = new LoaiMonDAO();
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // Container chính cho các danh mục
        container = new VBox(10);
        container.setPadding(new Insets(10));
        container.getStyleClass().add("base-sidebar");

        getChildren().add(container);

        khoiTaoDanhMuc();
    }

    @Override
    protected void khoiTaoDanhMuc() {
        themDanhMuc("Thực đơn");
        themDanhMuc("Loại món ăn");
        themDanhMuc("Món ăn", List.of(
                "Cơm", "Đồ nước", "Tráng miệng", "Món đặc biệt"
        ));

        themDanhMuc("Khuyến mãi");
        themDanhMuc("Hóa đơn");
        themDanhMuc("Khách hàng");
        themDanhMuc("Tài khoản");

        // Tầng và bàn
        TangDAO tangDAO = new TangDAO();
        List<Tang> dsTang = tangDAO.layTatCaTang();
        List<String> danhSachTang = dsTang.stream()
                .map(Tang::getTenTang)
                .collect(Collectors.toList());
        themDanhMuc("Tầng và bàn", danhSachTang);
    }

    private void themDanhMuc(String tenDanhMuc) {
        themDanhMuc(tenDanhMuc, null);
    }

    private void themDanhMuc(String tenDanhMuc, List<String> danhSachCon) {
        Label nhanChinh = new Label(tenDanhMuc);
        nhanChinh.getStyleClass().add("muc-chinh");

        if (danhSachCon != null && !danhSachCon.isEmpty()) {
            // Tạo VBox chứa các mục con
            VBox hopChua = new VBox(5);
            hopChua.setPadding(new Insets(5, 0, 5, 20));
            hopChua.getStyleClass().add("hop-chua-con");

            for (String mucCon : danhSachCon) {
                Label lblCon = taoNhanClick(mucCon, () -> xuLyChonMuc(mucCon), "muc-con");
                hopChua.getChildren().add(lblCon);
            }

            //ScrollPane chỉ bọc phần con
            ScrollPane scrollMucCon = new ScrollPane(hopChua);
            scrollMucCon.setFitToWidth(true);
            scrollMucCon.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollMucCon.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            //scrollMucCon.setPrefHeight(300); // Chiều cao tối đa vùng con
            scrollMucCon.setVisible(false);
            scrollMucCon.setManaged(false);
            scrollMucCon.getStyleClass().add("scroll-muc-con");

            // Xử lý mở/đóng vùng cuộn con
            nhanChinh.setOnMouseClicked(e -> {
                boolean hienThi = scrollMucCon.isVisible();
                scrollMucCon.setVisible(!hienThi);
                scrollMucCon.setManaged(!hienThi);
            });

            container.getChildren().addAll(nhanChinh, scrollMucCon);
        } else {
            nhanChinh.setOnMouseClicked(e -> xuLyChonMuc(tenDanhMuc));
            container.getChildren().add(nhanChinh);
        }
    }

    private void xuLyChonMuc(String tenMuc) {
        if (mainContent == null) return;

        // Reset highlight
        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            node.setStyle("");
        }

        // Highlight mục đang chọn
        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            if (node instanceof Label lbl && lbl.getText().equals(tenMuc)) {
                lbl.setStyle("-fx-text-fill: #2b7cff;"
                    + "-fx-font-weight: bold;"
                    + "-fx-border-width: 0 0 0 4;"
                    + "-fx-border-color: #2b7cff;"
                    + "-fx-padding: 0 0 0 4;");
            }
        }

        Node newContent = switch (tenMuc) {
            case "Thực đơn" -> new QuanLyThucDon();
            case "Loại món ăn" -> new LoaiMonAn();
            case "Khuyến mãi" -> new GiaoDienKhuyenMai();
            case "Phiếu đặt bàn" -> new GiaoDienPhieuDatBan();
            case "Khách hàng" -> new GiaoDienKhachHang();
            case "Hóa đơn" -> new GiaoDienHoaDon();
            case "Tài khoản" -> new GiaoDienTaiKhoan();
            default -> {
                // Kiểm tra nếu là loại món
                Optional<LoaiMon> loaiMonOpt = loaiMonDAO.layTatCaLoaiMon().stream()
                        .filter(lm -> lm.getTenLoaiMon().equals(tenMuc))
                        .findFirst();

                if (loaiMonOpt.isPresent()) {
                    LoaiMon lm = loaiMonOpt.get();
                    yield new GiaoDienMonAn(lm.getMaLoaiMon(), lm.getTenLoaiMon());
                }

                // Kiểm tra nếu là tầng
                TangDAO tangDAO = new TangDAO();
                Optional<Tang> tangOpt = tangDAO.layTatCaTang().stream()
                        .filter(t -> t.getTenTang().equals(tenMuc))
                        .findFirst();

                if (tangOpt.isPresent()) {
                    Tang tang = tangOpt.get();
                    QuanLiBan qlBan = new QuanLiBan((StackPane) mainContent);
                    qlBan.hienThiBanTheoTang(tang.getMaTang());
                    yield qlBan;
                }

                yield null; // Không tìm thấy gì
            }
        };

        if (newContent != null) {
            mainContent.getChildren().setAll(newContent);
            StackPane.setAlignment(newContent, Pos.CENTER);
        }
    }
}