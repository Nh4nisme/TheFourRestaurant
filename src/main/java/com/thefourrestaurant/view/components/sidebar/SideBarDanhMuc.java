package com.thefourrestaurant.view.components.sidebar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class SideBarDanhMuc extends BaseSideBar {

    public SideBarDanhMuc() {
        super("Quản Lý");
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
        System.out.println("Bạn đã chọn: " + tenMuc);
        // TODO: load giao diện tương ứng
    }
}
