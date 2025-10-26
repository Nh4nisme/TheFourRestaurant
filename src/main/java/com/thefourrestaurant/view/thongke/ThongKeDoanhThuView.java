package com.thefourrestaurant.view.thongke;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ThongKeDoanhThuView extends VBox {

    public ThongKeDoanhThuView(String loaiThongKe) {
        this.setAlignment(Pos.TOP_CENTER);

        GridPane contentPane = new GridPane();
        VBox.setVgrow(contentPane, Priority.ALWAYS);
        contentPane.setStyle("-fx-background-color: #F5F5F5;");

        // Top Bar (Breadcrumb)
        Label duongDan = new Label("Thống kê > Doanh thu > " + getTitle(loaiThongKe));
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

        // Main Content Area
        VBox khungDuoi = new VBox();
        khungDuoi.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        khungDuoi.setAlignment(Pos.TOP_CENTER); // Align content to the top
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

        // Create a 2x2 grid for the four sections
        GridPane fourSectionsGrid = new GridPane();
        fourSectionsGrid.setHgap(10);
        fourSectionsGrid.setVgap(10);
        VBox.setVgrow(fourSectionsGrid, Priority.ALWAYS);

        // Add placeholder labels to the other sections
        fourSectionsGrid.add(new Label("Phần trên bên trái"), 0, 0);
        fourSectionsGrid.add(new Label("Phần dưới bên trái"), 0, 1);
        fourSectionsGrid.add(new Label("Phần dưới bên phải"), 1, 1);

        khungDuoi.getChildren().add(fourSectionsGrid);

        switch (loaiThongKe) {
            case "ngay":
                titleLabel.setText("Thống kê doanh thu theo ngày");
                fourSectionsGrid.add(new Label("Nội dung thống kê theo ngày sẽ được hiển thị ở đây."), 0, 0, 2, 2); // Span across all sections
                break;

            case "thang":
                titleLabel.setText("Thống kê doanh thu theo tháng");
                setupMonthlyChart(fourSectionsGrid);
                break;

            case "nam":
                titleLabel.setText("Thống kê doanh thu theo năm");
                fourSectionsGrid.add(new Label("Nội dung thống kê theo năm sẽ được hiển thị ở đây."), 0, 0, 2, 2); // Span across all sections
                break;
        }
    }

    private String getTitle(String loaiThongKe) {
        switch (loaiThongKe) {
            case "ngay": return "Theo ngày";
            case "thang": return "Theo tháng";
            case "nam": return "Theo năm";
            default: return "";
        }
    }

    private void setupMonthlyChart(GridPane grid) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Ngày trong tuần");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu (VND)");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Doanh thu hàng tuần trong tháng");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu tháng này");

        series.getData().add(new XYChart.Data<>("Thứ 2", 1500000));
        series.getData().add(new XYChart.Data<>("Thứ 3", 1750000));
        series.getData().add(new XYChart.Data<>("Thứ 4", 2200000));
        series.getData().add(new XYChart.Data<>("Thứ 5", 2100000));
        series.getData().add(new XYChart.Data<>("Thứ 6", 3500000));
        series.getData().add(new XYChart.Data<>("Thứ 7", 4500000));
        series.getData().add(new XYChart.Data<>("Chủ nhật", 4200000));

        lineChart.getData().add(series);

        // Add the chart to the top-right section of the grid
        grid.add(lineChart, 1, 0);
    }
}
