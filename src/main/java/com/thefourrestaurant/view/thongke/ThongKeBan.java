package com.thefourrestaurant.view.thongke;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ThongKeBan {

    public static Node taoBieuDo(String loai) {
        // Logic thống kê bàn thực tế sẽ được thêm vào đây sau
        // Hiện tại vẫn dùng dữ liệu mẫu
        BarChart<Number, String> bieuDo = new BarChart<>(new NumberAxis(), new CategoryAxis());
        bieuDo.setTitle("Lượt dùng bàn theo tầng - " + loai);
        bieuDo.setLegendVisible(false);
        XYChart.Series<Number, String> series = new XYChart.Series<>();
        series.getData().addAll(
            new XYChart.Data<>(50, "Tầng 1"),
            new XYChart.Data<>(85, "Tầng 2"),
            new XYChart.Data<>(62, "Tầng 3"),
            new XYChart.Data<>(93, "Tầng 4"),
            new XYChart.Data<>(45, "Tầng 5"),
            new XYChart.Data<>(30, "Tầng 6"),
            new XYChart.Data<>(71, "Tầng 7")
        );
        bieuDo.getData().add(series);
        return bieuDo;
    }
}
