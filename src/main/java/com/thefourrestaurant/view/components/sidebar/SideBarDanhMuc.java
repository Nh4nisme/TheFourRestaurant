package com.thefourrestaurant.view.components.sidebar;

import com.thefourrestaurant.view.GiaoDienTaoThucDon;
import com.thefourrestaurant.view.LoaiMonAn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.List;

public class SideBarDanhMuc extends BaseSideBar {
    private final HBox mainContainer;

    public SideBarDanhMuc(HBox mainContainer) {
        super("Danh mục");
        this.mainContainer = mainContainer;
    }

    @Override
    protected void khoiTaoDanhMuc() {
        themDanhMuc("Thực đơn", null, this::showThucDon);
        themDanhMuc("Loại món ăn", null, this::showLoaiMonAn);
        themDanhMuc("Món ăn", null, () -> {});
        themDanhMuc("Tầng và bàn", List.of("Tầng 1", "Tầng 2", "Tầng 3"), null);
    }

    private void themDanhMuc(String tenDanhMuc, List<String> danhSachCon, Runnable hanhDong) {
        var nhanChinh = taoNhanClick(tenDanhMuc, hanhDong, "muc-chinh");

        if (danhSachCon != null && !danhSachCon.isEmpty()) {
            VBox hopChua = new VBox();
            hopChua.setSpacing(5);
            hopChua.setVisible(false);
            hopChua.setManaged(false);

            nhanChinh.setOnMouseClicked(e -> moHoacDongMucCon(hopChua));
            getChildren().addAll(nhanChinh, hopChua);
        } else {
            getChildren().add(nhanChinh);
        }
    }

    private void showThucDon() {
        if (mainContainer.getChildren().size() > 2) {
            mainContainer.getChildren().remove(2);
        }

        GiaoDienTaoThucDon giaoDienTaoThucDon = new GiaoDienTaoThucDon();
        HBox.setHgrow(giaoDienTaoThucDon, Priority.ALWAYS);
        mainContainer.getChildren().add(giaoDienTaoThucDon);
    }

    private void showLoaiMonAn() {
        if (mainContainer.getChildren().size() > 2) {
            mainContainer.getChildren().remove(2);
        }

        LoaiMonAn loaiMonAn = new LoaiMonAn();
        HBox.setHgrow(loaiMonAn, Priority.ALWAYS);
        mainContainer.getChildren().add(loaiMonAn);
    }
}
