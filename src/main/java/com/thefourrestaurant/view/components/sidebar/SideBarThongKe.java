package com.thefourrestaurant.view.components.sidebar;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class SideBarThongKe extends BaseSideBar {

    public SideBarThongKe() {
        super("Thống kê");
    }

    @Override
    protected void khoiTaoDanhMuc() {
        themDanhMuc("Doanh Thu", List.of("Cơm"));
        themDanhMuc("Món ăn", List.of("Cơm"));
        themDanhMuc("Tầng và bàn", List.of("Tầng 1"));
    }

    private void themDanhMuc(String tenDanhMuc) {
        themDanhMuc(tenDanhMuc, null);
    }

    private void themDanhMuc(String tenDanhMuc, List<String> danhSachCon) {
        Label nhanChinh = taoNhanClick(tenDanhMuc, () -> xuLyChonMuc(tenDanhMuc), "muc-chinh");

        if (danhSachCon != null && !danhSachCon.isEmpty()) {
            VBox hopChua = new VBox();
            hopChua.setSpacing(5);
            hopChua.setPadding(new Insets(5, 0, 5, 20));
            hopChua.setVisible(false);
            hopChua.setManaged(false);
            hopChua.getStyleClass().add("hop-chua-con");

            for (String mucCon : danhSachCon) {
                HBox mucConNhapLieu = taoMucConNhapLieu(mucCon);
                hopChua.getChildren().add(mucConNhapLieu);
            }

            nhanChinh.setOnMouseClicked(e -> moHoacDongMucCon(hopChua));
            getChildren().addAll(nhanChinh, hopChua);
        } else {
            getChildren().add(nhanChinh);
        }
    }

    HBox taoMucConNhapLieu(String tenMuc) {
        HBox box = new HBox(5);
        box.setPadding(new Insets(2, 0, 2, 0));

        Label lbl = new Label(tenMuc + ":");
        lbl.getStyleClass().add("muc-con-label");

        DatePicker datePicker = new DatePicker(); // ô nhập ngày
        TextField tf = new TextField(); // ô nhập giá trị tùy ý
        tf.setPromptText("Nhập giá trị");

        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Ngày", "Tháng", "Năm"); // ví dụ
        combo.setValue("Ngày");

        box.getChildren().addAll(lbl, datePicker, tf, combo);

        return box;
    }

    private void xuLyChonMuc(String tenMuc) {
        System.out.println("Bạn đã chọn: " + tenMuc);
        // TODO: load giao diện tương ứng
    }
}
