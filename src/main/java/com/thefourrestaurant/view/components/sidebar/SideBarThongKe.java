package com.thefourrestaurant.view.components.sidebar;

import com.thefourrestaurant.view.thongke.ThongKeBanView;
import com.thefourrestaurant.view.thongke.ThongKeDoanhThuView;
import com.thefourrestaurant.view.thongke.ThongKeMonAnView;
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

public class SideBarThongKe extends BaseSideBar {

    private final Pane mainContent;
    private final VBox container;

    public SideBarThongKe(Pane mainContent) {
        super("Thống kê");
        this.mainContent = mainContent;
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
        themDanhMuc("Doanh thu", List.of("Doanh thu theo ngày", "Doanh thu theo tháng", "Doanh thu theo năm"));
        themDanhMuc("Món ăn phổ biến", List.of("Món được đặt nhiều nhất trong ngày", "Món được đặt nhiều nhất trong tháng", "Món được đặt nhiều nhất trong năm", "Món ít đặt nhất trong tháng"));
        themDanhMuc("Bàn phổ biến", List.of("Bàn được đặt nhiều nhất trong ngày", "Bàn được đặt nhiều nhất trong tháng", "Bàn được đặt nhiều nhất trong năm", "Bàn ít đặt nhất trong tháng"));
    }

    private void themDanhMuc(String tenDanhMuc, List<String> danhSachCon) {
        Label nhanChinh = new Label(tenDanhMuc);
        nhanChinh.getStyleClass().add("muc-chinh");

        if (danhSachCon != null && !danhSachCon.isEmpty()) {
            VBox hopChua = new VBox(5);
            hopChua.setPadding(new Insets(5, 0, 5, 20));
            hopChua.getStyleClass().add("hop-chua-con");

            for (String mucCon : danhSachCon) {
                Label lblCon = taoNhanClick(mucCon, () -> xuLyChonMuc(mucCon), "muc-con");
                hopChua.getChildren().add(lblCon);
            }

            ScrollPane scrollMucCon = new ScrollPane(hopChua);
            scrollMucCon.setFitToWidth(true);
            scrollMucCon.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollMucCon.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollMucCon.setPrefHeight(300);
            scrollMucCon.setVisible(false);
            scrollMucCon.setManaged(false);
            scrollMucCon.getStyleClass().add("scroll-muc-con");

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

        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            node.setStyle("");
        }

        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            if (node instanceof Label lbl && lbl.getText().equals(tenMuc)) {
                lbl.setStyle("-fx-text-fill: #2b7cff; -fx-font-weight: bold; -fx-border-width: 0 0 0 4; -fx-border-color: #2b7cff; -fx-padding: 0 0 0 4;");
            }
        }

        Node newContent = null;
        switch (tenMuc) {
            // Doanh thu
            case "Doanh thu theo ngày":
                newContent = new ThongKeDoanhThuView("ngay");
                break;
            case "Doanh thu theo tháng":
                newContent = new ThongKeDoanhThuView("thang");
                break;
            case "Doanh thu theo năm":
                newContent = new ThongKeDoanhThuView("nam");
                break;

            // Món ăn
            case "Món được đặt nhiều nhất trong ngày":
                newContent = new ThongKeMonAnView("most_popular_day");
                break;
            case "Món được đặt nhiều nhất trong tháng":
                newContent = new ThongKeMonAnView("most_popular_month");
                break;
            case "Món được đặt nhiều nhất trong năm":
                newContent = new ThongKeMonAnView("most_popular_year");
                break;
            case "Món ít đặt nhất trong tháng":
                newContent = new ThongKeMonAnView("least_popular_month");
                break;

            // Bàn
            case "Bàn được đặt nhiều nhất trong ngày":
                newContent = new ThongKeBanView("most_popular_day");
                break;
            case "Bàn được đặt nhiều nhất trong tháng":
                newContent = new ThongKeBanView("most_popular_month");
                break;
            case "Bàn được đặt nhiều nhất trong năm":
                newContent = new ThongKeBanView("most_popular_year");
                break;
            case "Bàn ít đặt nhất trong tháng":
                newContent = new ThongKeBanView("least_popular_month");
                break;

            default:
                break;
        }

        if (newContent != null) {
            mainContent.getChildren().setAll(newContent);
            if (mainContent instanceof StackPane) {
                StackPane.setAlignment(newContent, Pos.CENTER);
            }
        }
    }
}
