package com.thefourrestaurant.view.thongke;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.net.URL;

public class ThongKeMonAnView extends VBox {

    public ThongKeMonAnView(String loaiThongKe) {
        this.setAlignment(Pos.TOP_CENTER);

        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            this.getStylesheets().add(urlCSS.toExternalForm());
        }

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        Label duongDan = new Label("Thống kê > Món ăn phổ biến > " + getTitle(loaiThongKe));
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
                titleLabel.setText("Món được đặt nhiều nhất trong ngày");
                setupPieChart(khungDuoi, "Top 5 món bán chạy trong ngày");
                break;
            case "most_popular_month":
                titleLabel.setText("Món được đặt nhiều nhất trong tháng");
                setupPieChart(khungDuoi, "Top 5 món bán chạy trong tháng");
                break;
            case "most_popular_year":
                titleLabel.setText("Món được đặt nhiều nhất trong năm");
                setupPieChart(khungDuoi, "Top 5 món bán chạy trong năm");
                break;
            case "least_popular_month":
                titleLabel.setText("Món ít đặt nhất trong tháng");
                khungDuoi.getChildren().add(new Label("Nội dung thống kê sẽ được hiển thị ở đây."));
                break;
        }
    }

    private String getTitle(String loaiThongKe) {
        switch (loaiThongKe) {
            case "most_popular_day": return "Bán chạy trong ngày";
            case "most_popular_month": return "Bán chạy trong tháng";
            case "most_popular_year": return "Bán chạy trong năm";
            case "least_popular_month": return "Ít bán trong tháng";
            default: return "";
        }
    }

    private void setupPieChart(VBox container, String chartTitle) {
        PieChart chart = new PieChart();
        chart.setTitle(chartTitle);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Cơm Gà Xối Mỡ", 120),
            new PieChart.Data("Phở Bò Tái", 95),
            new PieChart.Data("Bún Chả Hà Nội", 80),
            new PieChart.Data("Hủ Tiếu Nam Vang", 75),
            new PieChart.Data("Mì Quảng", 60)
        );

        chart.setData(pieChartData);

        // Tính tổng số lượng
        double total = 0;
        for (PieChart.Data d : pieChartData) {
            total += d.getPieValue();
        }

        // Tạo nhãn để hiển thị tổng
        Label totalLabel = new Label("Tổng cộng\n" + String.format("%.0f", total));
        totalLabel.setTextAlignment(TextAlignment.CENTER);
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Tạo vòng tròn trắng để làm "lỗ"
        Circle hole = new Circle(80, Color.WHITE);

        // Sử dụng StackPane để xếp chồng các thành phần
        StackPane doughnutChart = new StackPane();
        doughnutChart.getChildren().addAll(chart, hole, totalLabel);

        container.getChildren().add(doughnutChart);
    }
}
