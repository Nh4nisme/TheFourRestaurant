package com.thefourrestaurant.view.thongke;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;

public class ThongKeBanView extends VBox {

    public ThongKeBanView(String loaiThongKe) {
        this.setAlignment(Pos.TOP_CENTER);

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        }

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        Label duongDan = new Label("Thống kê > Bàn phổ biến > " + getTitle(loaiThongKe));
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungTren = new VBox(duongDan);
        khungTren.setStyle("-fx-background-color: #673E1F;");
        khungTren.setAlignment(Pos.CENTER_LEFT);
        khungTren.setPadding(new Insets(0, 20, 0, 20));
        khungTren.setPrefHeight(30);
        khungTren.setMinHeight(30);
        khungTren.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(khungTren, Priority.ALWAYS);
        contentPane.add(khungTren, 0, 0);

        VBox khungDuoi = new VBox();
        khungDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khungDuoi.setAlignment(Pos.CENTER);
        khungDuoi.setPadding(new Insets(20));
        GridPane.setMargin(khungDuoi, new Insets(10, 10, 10, 10));
        contentPane.add(khungDuoi, 0, 1);
        GridPane.setHgrow(khungDuoi, Priority.ALWAYS);
        GridPane.setVgrow(khungDuoi, Priority.ALWAYS);

        this.getChildren().add(contentPane);

        Label titleLabel = new Label();
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");
        khungDuoi.getChildren().add(titleLabel);

        switch (loaiThongKe) {
            case "most_popular_day":
            case "most_popular_month":
            case "most_popular_year":
                titleLabel.setText(getTitle(loaiThongKe));
                setupHorizontalBarChart(khungDuoi, "Số lần bàn được sử dụng theo tầng");
                break;
            case "least_popular_month":
                titleLabel.setText("Bàn ít đặt nhất trong tháng");
                khungDuoi.getChildren().add(new Label("Nội dung thống kê sẽ được hiển thị ở đây."));
                break;
        }
    }

    private String getTitle(String loaiThongKe) {
        switch (loaiThongKe) {
            case "most_popular_day": return "Bàn được đặt nhiều nhất trong ngày";
            case "most_popular_month": return "Bàn được đặt nhiều nhất trong tháng";
            case "most_popular_year": return "Bàn được đặt nhiều nhất trong năm";
            case "least_popular_month": return "Bàn ít đặt nhất trong tháng";
            default: return "";
        }
    }

    private void setupHorizontalBarChart(VBox container, String chartTitle) {
        // Trục Y là các tầng (CategoryAxis)
        CategoryAxis yAxis = new CategoryAxis();
        yAxis.setLabel("Tầng");

        // Trục X là số lần sử dụng (NumberAxis)
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Số lần sử dụng");

        // Tạo biểu đồ thanh. Lưu ý thứ tự: BarChart<X, Y>
        BarChart<Number, String> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(chartTitle);
        barChart.setLegendVisible(false); // Ẩn chú thích vì chỉ có một chuỗi dữ liệu

        // Tạo chuỗi dữ liệu
        XYChart.Series<Number, String> series = new XYChart.Series<>();

        // Thêm dữ liệu mẫu (bạn sẽ thay thế bằng dữ liệu thực tế)
        series.getData().add(new XYChart.Data<>(50, "Tầng 1"));
        series.getData().add(new XYChart.Data<>(85, "Tầng 2"));
        series.getData().add(new XYChart.Data<>(62, "Tầng 3"));
        series.getData().add(new XYChart.Data<>(93, "Tầng 4"));
        series.getData().add(new XYChart.Data<>(45, "Tầng 5"));
        series.getData().add(new XYChart.Data<>(30, "Tầng 6"));
        series.getData().add(new XYChart.Data<>(71, "Tầng 7"));

        // Thêm chuỗi dữ liệu vào biểu đồ
        barChart.getData().add(series);

        container.getChildren().add(barChart);
    }
}
