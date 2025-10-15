package com.thefourrestaurant.view.components.sidebar;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class SideBarThongKe extends BaseSideBar {

    private HBox mainContainer; // thêm biến này để truy cập container chính

    public SideBarThongKe() {
        super("Thống kê");
    }

    // thêm setter để controller truyền vào
    public void setMainContainer(HBox mainContainer) {
        this.mainContainer = mainContainer;
    }

    @Override
    protected void khoiTaoDanhMuc() {
        themDanhMuc("Doanh Thu", List.of("Cơm"));
        themDanhMuc("Món ăn", List.of("Cơm"));
        themDanhMuc("Tầng và bàn", List.of("Tầng 1"));
    }

    private void themDanhMuc(String tenDanhMuc, List<String> danhSachCon) {
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

            nhanChinh.setOnMouseClicked(e -> moHoacDongMucCon(hopChua));
            getChildren().addAll(nhanChinh, hopChua);
        } else {
            getChildren().add(nhanChinh);
        }
    }

    private void xuLyChonMucCon(String mucCon) {
        if (mainContainer == null) return;

        // Nếu đã có panel nội dung thống kê → xóa nó đi
        if (mainContainer.getChildren().size() > 2)
            mainContainer.getChildren().remove(2);

        // Tạo nội dung mới
        ThongKeContent thongKeContent = new ThongKeContent(mucCon);

        // Cho phép chiếm hết không gian còn lại
        HBox.setHgrow(thongKeContent, Priority.ALWAYS);

        mainContainer.getChildren().add(2, thongKeContent);
    }
}