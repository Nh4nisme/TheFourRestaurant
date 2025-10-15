package com.thefourrestaurant.view.components.sidebar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.chart.*;

public class ThongKeContent extends VBox {
    private final ToolBar navBar;
    private final GridPane grid;

    public ThongKeContent(String tenMucCon) {
        setSpacing(10);
        getStyleClass().add("thongke-root");

        navBar = new ToolBar(
                new Label("📊 Thống kê " + tenMucCon)
        );
        navBar.getStyleClass().add("thongke-navbar");

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(true);
        grid.getStyleClass().add("thongke-grid");

        // 2x2 layout
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1, col2);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        grid.getRowConstraints().addAll(row1, row2);

        VBox.setVgrow(grid, Priority.ALWAYS);

        // === Gọi hàm tạo biểu đồ ===
        taoBieuDoDoanhThu();

        getChildren().addAll(navBar, grid);
    }

    private void taoBieuDoDoanhThu() {
        // 1️⃣ Biểu đồ đường - Doanh thu theo ngày
        LineChart<String, Number> lineChart = taoLineChart();

        // 2️⃣ Biểu đồ cột - Doanh thu theo tháng
        BarChart<String, Number> barChart = taoBarChart();

        // 3️⃣ Biểu đồ tròn - Tỷ lệ món theo doanh thu
        PieChart pieChart = taoPieChart();

        // 4️⃣ Biểu đồ vùng - Tổng doanh thu theo năm
        AreaChart<String, Number> areaChart = taoAreaChart();

        grid.add(lineChart, 0, 0);
        grid.add(barChart, 1, 0);
        grid.add(pieChart, 0, 1);
        grid.add(areaChart, 1, 1);

        // Cho biểu đồ fit vừa ô
        GridPane.setHgrow(lineChart, Priority.ALWAYS);
        GridPane.setVgrow(lineChart, Priority.ALWAYS);
        GridPane.setHgrow(barChart, Priority.ALWAYS);
        GridPane.setVgrow(barChart, Priority.ALWAYS);
        GridPane.setHgrow(pieChart, Priority.ALWAYS);
        GridPane.setVgrow(pieChart, Priority.ALWAYS);
        GridPane.setHgrow(areaChart, Priority.ALWAYS);
        GridPane.setVgrow(areaChart, Priority.ALWAYS);
    }

    private LineChart<String, Number> taoLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Ngày");
        yAxis.setLabel("Doanh thu (triệu)");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Doanh thu theo ngày");

        XYChart.Series<String, Number> data = new XYChart.Series<>();
        data.setName("Tháng 10");
        data.getData().add(new XYChart.Data<>("01", 5));
        data.getData().add(new XYChart.Data<>("02", 8));
        data.getData().add(new XYChart.Data<>("03", 6));
        data.getData().add(new XYChart.Data<>("04", 10));
        chart.getData().add(data);
        return chart;
    }

    private BarChart<String, Number> taoBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Tháng");
        yAxis.setLabel("Doanh thu (triệu)");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Doanh thu theo tháng");

        XYChart.Series<String, Number> data = new XYChart.Series<>();
        data.getData().add(new XYChart.Data<>("1", 50));
        data.getData().add(new XYChart.Data<>("2", 65));
        data.getData().add(new XYChart.Data<>("3", 80));
        chart.getData().add(data);
        return chart;
    }

    private PieChart taoPieChart() {
        PieChart chart = new PieChart();
        chart.setTitle("Tỷ lệ doanh thu theo món");
        chart.getData().addAll(
                new PieChart.Data("Cơm", 40),
                new PieChart.Data("Phở", 30),
                new PieChart.Data("Bún", 20),
                new PieChart.Data("Khác", 10)
        );
        return chart;
    }

    private AreaChart<String, Number> taoAreaChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Năm");
        yAxis.setLabel("Doanh thu (triệu)");

        AreaChart<String, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setTitle("Tổng doanh thu theo năm");

        XYChart.Series<String, Number> data = new XYChart.Series<>();
        data.getData().add(new XYChart.Data<>("2022", 600));
        data.getData().add(new XYChart.Data<>("2023", 800));
        data.getData().add(new XYChart.Data<>("2024", 1000));
        chart.getData().add(data);
        return chart;
    }
}
