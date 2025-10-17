package com.thefourrestaurant.view.components.sidebar;

import com.thefourrestaurant.view.GiaoDienTaoThucDon;
import com.thefourrestaurant.view.loaimonan.LoaiMonAn;
import com.thefourrestaurant.view.QuanLiBan;
import com.thefourrestaurant.view.monan.MonAnBun;
import com.thefourrestaurant.view.monan.MonAnCom;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.List;

public class SideBarDanhMuc extends BaseSideBar {

    private final HBox mainContainer;

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
        themDanhMuc("Tầng và bàn", List.of("Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4", "Tầng 5", "Tầng 6", "Tầng 7"));
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
                hopChua.getChildren().add(taoNhanClick(mucCon, () -> xuLyChonMuc(mucCon), "muc-con"));
            }

            nhanChinh.setOnMouseClicked(e -> moHoacDongMucCon(hopChua));
            getChildren().addAll(nhanChinh, hopChua);
        } else {
            getChildren().add(nhanChinh);
        }
    }

    private void xuLyChonMuc(String tenMuc) {
        if (mainContainer == null) {
            return;
        }

        Node newContent = switch (tenMuc) {
            case "Thực đơn" -> new GiaoDienTaoThucDon();
            case "Loại món ăn" -> new LoaiMonAn();
            case "Cơm" -> new MonAnCom();
            case "Bún" -> new MonAnBun();
            case "Tầng 1" -> {
                QuanLiBan qlBan = new QuanLiBan();
                qlBan.hienThiBanTheoTang("TG000001"); // Gọi tầng 1 mặc định
                yield qlBan;
            }
            default -> null;
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
