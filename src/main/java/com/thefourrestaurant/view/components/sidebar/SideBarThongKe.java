package com.thefourrestaurant.view.components.sidebar;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SideBarThongKe extends BaseSideBar {
    private HBox mainContainer;

    public SideBarThongKe(HBox mainContainer) {
        super("Thống kê");
        this.mainContainer = mainContainer;
    }

    @Override
    protected void khoiTaoDanhMuc() {
        themDanhMuc("Doanh Thu", List.of("Theo ngày","Theo tháng","Theo năm"),this::showThongKeContent);
        themDanhMuc("Món ăn", List.of("Cơm"),this::showThongKeContent);
        themDanhMuc("Tầng và bàn", List.of("Tầng 1"),this::showThongKeContent);
    }

    private void themDanhMuc(String tenDanhMuc, List<String> danhSachCon, Runnable hanhDong) {
        Label nhanChinh = taoNhanClick(tenDanhMuc, null, "muc-chinh");

        if (danhSachCon != null && !danhSachCon.isEmpty()) {
            VBox hopChua = new VBox();
            hopChua.setSpacing(5);
            hopChua.setPadding(new Insets(5, 0, 5, 20));
            hopChua.setVisible(false);
            hopChua.setManaged(false);

            for (String mucCon : danhSachCon) {
                Label mucConLabel = taoNhanClick(mucCon, () -> xuLyChonMucCon(mucCon), "muc-con");
                hopChua.getChildren().add(mucConLabel);
            }

            nhanChinh.setOnMouseClicked(e -> {
                moHoacDongMucCon(hopChua);
                showThongKeContent();
            });
            getChildren().addAll(nhanChinh, hopChua);
        } else {
            getChildren().add(nhanChinh);
        }
    }

    private void showThongKeContent() {
        if (mainContainer.getChildren().size() > 2) {
            mainContainer.getChildren().remove(2);
        }

        ThongKeContent thongKeContent = new ThongKeContent();
        HBox.setHgrow(thongKeContent, Priority.ALWAYS);
        mainContainer.getChildren().add(thongKeContent);
    }

    private void xuLyChonMucCon(String mucCon) {
//        if (mainContainer.getChildren().size() > 2) {
//            mainContainer.getChildren().remove(2);
//        }
//
//        ThongKeContent thongKeContent = new ThongKeContent();
//        HBox.setHgrow(thongKeContent, Priority.ALWAYS);
//
//        mainContainer.getChildren().add(thongKeContent);
    }
}