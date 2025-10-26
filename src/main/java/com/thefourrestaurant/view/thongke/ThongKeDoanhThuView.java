package com.thefourrestaurant.view.thongke;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ThongKeDoanhThuView extends VBox {

    public ThongKeDoanhThuView(String loaiThongKe) {
        setSpacing(10);
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label();
        titleLabel.setFont(new Font("Arial", 24));

        switch (loaiThongKe) {
            case "ngay":
                titleLabel.setText("Thống kê doanh thu theo ngày");
                break;
            case "thang":
                titleLabel.setText("Thống kê doanh thu theo tháng");
                break;
            case "nam":
                titleLabel.setText("Thống kê doanh thu theo năm");
                break;
        }

        getChildren().add(titleLabel);
        // Thêm các thành phần biểu đồ hoặc bảng ở đây
    }
}
