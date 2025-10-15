package com.thefourrestaurant.view.components.chart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

public class AreaChartExample extends Application {

    @Override
    public void start(Stage stage) {
        // Tiêu đề
        Text title = new Text("Doanh Thu Ngày/ Tháng/ Năm");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #d4a017;");

        // Trục X (theo ngày trong tuần)
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Ngày trong tuần");

        // Trục Y (doanh thu)
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu");

        // Biểu đồ vùng
        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle(""); // bỏ tiêu đề mặc định
        areaChart.setLegendVisible(true);
        areaChart.setStyle("-fx-background-color: white;");

        // === Ví dụ dữ liệu mẫu ===
        XYChart.Series<String, Number> daily = new XYChart.Series<>();
        daily.setName("Doanh thu ngày");

        XYChart.Series<String, Number> monthly = new XYChart.Series<>();
        monthly.setName("Doanh thu tháng");

        XYChart.Series<String, Number> yearly = new XYChart.Series<>();
        yearly.setName("Doanh thu năm");

        // Tạo trục X mẫu
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        // Dữ liệu mẫu (có thể bỏ qua để tạo biểu đồ trống)
        int[] dayData = {25, 34, 20, 23, 15, 25, 28};
        int[] monthData = {35, 42, 30, 34, 28, 33, 37};
        int[] yearData = {45, 52, 46, 41, 39, 44, 50};

        for (int i = 0; i < days.length; i++) {
            daily.getData().add(new XYChart.Data<>(days[i], dayData[i]));
            monthly.getData().add(new XYChart.Data<>(days[i], monthData[i]));
            yearly.getData().add(new XYChart.Data<>(days[i], yearData[i]));
        }

        // Thêm series vào biểu đồ
        areaChart.getData().addAll(daily, monthly, yearly);

        // Giao diện
        VBox root = new VBox(10, title, areaChart);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #fdfdfd; -fx-padding: 20;");

        // Cảnh
        Scene scene = new Scene(root, 700, 400);
        stage.setTitle("Biểu đồ Doanh Thu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
