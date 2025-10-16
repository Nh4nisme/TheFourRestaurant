package com.thefourrestaurant.view.components.sidebar;

import com.thefourrestaurant.view.LoaiMonAn;
import com.thefourrestaurant.view.MonAnBun;
import com.thefourrestaurant.view.MonAnCom;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;


public class SideBarDanhMuc extends BaseSideBar {

    private final VBox rightBox;

    public SideBarDanhMuc(VBox rightBox) {
        super("Quản Lý");
        this.rightBox = rightBox;
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
        if (rightBox == null) {
            return;
        }

        GridPane newContent = null;

        switch (tenMuc) {
            case "Loại món ăn":
                newContent = new LoaiMonAn();
                break;
            case "Cơm":
                newContent = new MonAnCom();
                break;
            case "Bún":
                newContent = new MonAnBun();
                break;
        }

        if (newContent != null) {
            VBox.setVgrow(newContent, Priority.ALWAYS);
            if (rightBox.getChildren().size() > 1) {
                rightBox.getChildren().set(1, newContent);
            } else {
                rightBox.getChildren().add(newContent);
            }
        }
    }
}
