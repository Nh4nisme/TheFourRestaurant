package com.thefourrestaurant.view.components.sidebar;

import java.util.List;
import java.util.stream.Collectors;

import com.thefourrestaurant.DAO.TangDAO;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.view.QuanLiBan;
import com.thefourrestaurant.view.loaimonan.LoaiMonAn;
import com.thefourrestaurant.view.monan.MonAnBun;
import com.thefourrestaurant.view.monan.MonAnCom;
import com.thefourrestaurant.view.thucdon.ThucDon;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SideBarDanhMuc extends BaseSideBar {

    private final HBox mainContainer;
    private Label mucDangChon = null; // ✅ Lưu lại mục đang được chọn

    public SideBarDanhMuc(HBox mainContainer) {
        super("Quản Lý");
        this.mainContainer = mainContainer;
    }

    @Override
    protected void khoiTaoDanhMuc() {
        themDanhMuc("Thực đơn");
        themDanhMuc("Loại món ăn");
        themDanhMuc("Món ăn", List.of("Cơm", "Bún"));
        themDanhMuc("Thời gian sự kiện");

        TangDAO tangDAO = new TangDAO();
        List<Tang> dsTang = tangDAO.getAllTang();
        List<String> danhSachTang = dsTang.stream()
                                          .map(Tang::getTenTang)
                                          .collect(Collectors.toList());

        themDanhMuc("Tầng và bàn", danhSachTang);
    }

    private void themDanhMuc(String tenDanhMuc) {
        themDanhMuc(tenDanhMuc, null);
    }

    private void themDanhMuc(String tenDanhMuc, List<String> danhSachCon) {
        Label nhanChinh = taoNhanClick(tenDanhMuc, () -> xuLyChonMuc(tenDanhMuc), "muc-chinh");

        if (danhSachCon != null && !danhSachCon.isEmpty()) {
            VBox hopChua = new VBox(5);
            hopChua.setPadding(new Insets(5, 0, 5, 20));
            hopChua.setVisible(false);
            hopChua.setManaged(false);
            hopChua.getStyleClass().add("hop-chua-con");

            for (String mucCon : danhSachCon) {
                Label lblCon = taoNhanClick(mucCon, () -> xuLyChonMuc(mucCon), "muc-con");
                hopChua.getChildren().add(lblCon);
            }

            nhanChinh.setOnMouseClicked(e -> moHoacDongMucCon(hopChua));
            getChildren().addAll(nhanChinh, hopChua);
        } else {
            getChildren().add(nhanChinh);
        }
    }

    private void xuLyChonMuc(String tenMuc) {
        if (mainContainer == null) return;

        // ✅ Đổi màu highlight mục được chọn
        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            node.setStyle(""); // reset màu cũ
        }

        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            if (node instanceof Label lbl && lbl.getText().equals(tenMuc)) {
                lbl.setStyle("-fx-text-fill: #2b7cff; -fx-font-weight: bold; -fx-border-width: 0 0 0 4; -fx-border-color: #2b7cff; -fx-padding: 0 0 0 4;");
                mucDangChon = lbl;
            }
        }

        Node newContent = switch (tenMuc) {
            case "Thực đơn" -> new ThucDon();
            case "Loại món ăn" -> new LoaiMonAn();
            case "Cơm" -> new MonAnCom();
            case "Bún" -> new MonAnBun();
            default -> {
                TangDAO tangDAO = new TangDAO();
                Tang tang = tangDAO.getAllTang().stream()
                        .filter(t -> t.getTenTang().equals(tenMuc))
                        .findFirst().orElse(null);

                if (tang != null) {
                    QuanLiBan qlBan = new QuanLiBan();
                    qlBan.hienThiBanTheoTang(tang.getMaTang());
                    yield qlBan;
                } else {
                    yield null;
                }
            }
        };

        if (newContent != null) {
            HBox.setHgrow(newContent, Priority.ALWAYS);
            if (mainContainer.getChildren().size() > 2) {
                mainContainer.getChildren().set(2, newContent);
            } else {
                mainContainer.getChildren().add(newContent);
            }
        }
    }
}
