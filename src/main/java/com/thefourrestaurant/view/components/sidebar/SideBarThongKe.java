package com.thefourrestaurant.view.components.sidebar;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class SideBarThongKe extends BaseSideBar {

    private final Pane mainContent;
    private Label mucDangChon = null;

    public SideBarThongKe(Pane mainContent) {
        super("Thống kê");
        this.mainContent = mainContent;
        VBox.setVgrow(mainContent, Priority.ALWAYS);
    }

    @Override
    protected void khoiTaoDanhMuc() {
        themDanhMuc("Doanh Thu", List.of("Theo ngày", "Theo tháng", "Theo năm"));
        themDanhMuc("Món ăn", List.of("Cơm"));
        themDanhMuc("Tầng và bàn", List.of("Tầng 1"));
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
        if (mainContent == null) return;

        // Reset highlight
        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            node.setStyle("");
        }

        // Highlight mục hiện tại
        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            if (node instanceof Label lbl && lbl.getText().equals(tenMuc)) {
                lbl.setStyle("""
                    -fx-text-fill: #2b7cff;
                    -fx-font-weight: bold;
                    -fx-border-width: 0 0 0 4;
                    -fx-border-color: #2b7cff;
                    -fx-padding: 0 0 0 4;
                """);
                mucDangChon = lbl;
            }
        }

        // Load giao diện thống kê
        ThongKeContent thongKeContent = new ThongKeContent();

        // Đảm bảo nó chiếm full diện tích của mainContent
        if (thongKeContent instanceof Region region) {
            region.prefWidthProperty().bind(mainContent.widthProperty());
            region.prefHeightProperty().bind(mainContent.heightProperty());
        }

        // Thay thế nội dung cũ
        mainContent.getChildren().setAll(thongKeContent);
    }
}