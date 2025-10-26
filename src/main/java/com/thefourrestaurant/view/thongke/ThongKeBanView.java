package com.thefourrestaurant.view.thongke;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ThongKeBanView extends VBox {

    public ThongKeBanView(String loaiThongKe) {
        setSpacing(10);
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label();
        titleLabel.setFont(new Font("Arial", 24));

        switch (loaiThongKe) {
            case "most_popular_day":
                titleLabel.setText("Bàn được đặt nhiều nhất trong ngày");
                break;
            case "most_popular_month":
                titleLabel.setText("Bàn được đặt nhiều nhất trong tháng");
                break;
            case "most_popular_year":
                titleLabel.setText("Bàn được đặt nhiều nhất trong năm");
                break;
            case "least_popular_month":
                titleLabel.setText("Bàn ít đặt nhất trong tháng");
                break;
        }

        getChildren().add(titleLabel);
        // Thêm các thành phần biểu đồ hoặc bảng ở đây
    }
}
