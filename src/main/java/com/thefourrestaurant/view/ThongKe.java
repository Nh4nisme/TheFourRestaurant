package com.thefourrestaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class ThongKe extends BorderPane {

    private final GridPane grid;
    private final List<VBox> chartSlots = new ArrayList<>();

    public ThongKe() {
        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        // Tạo 4 ô trống
        for (int i = 0; i < 4; i++) {
            VBox slot = createEmptySlot("Empty slot " + (i + 1));
            chartSlots.add(slot);
        }

        // Bố trí 2x2
        grid.add(chartSlots.get(0), 0, 0);
        grid.add(chartSlots.get(1), 1, 0);
        grid.add(chartSlots.get(2), 0, 1);
        grid.add(chartSlots.get(3), 1, 1);

        // Chia đều không gian
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col, col);

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(50);
        grid.getRowConstraints().addAll(row, row);

        setCenter(grid);
        setStyle("-fx-background-color: #F5F6FA;");
    }

    // ======= Hàm tạo ô trống =======
    private VBox createEmptySlot(String label) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        box.setPadding(new Insets(15));
        box.setStyle(
                "-fx-border-color: #CCCCCC; " +
                        "-fx-border-radius: 12; " +
                        "-fx-background-color: white; " +
                        "-fx-background-radius: 12;"
        );

        Text text = new Text(label);
        text.setFill(Color.GRAY);
        text.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
        box.getChildren().add(text);
        return box;
    }

    // ======= Thêm biểu đồ vào ô trống kế tiếp =======
    public void addChart(Node chart) {
        for (VBox slot : chartSlots) {
            if (isSlotEmpty(slot)) {
                slot.getChildren().setAll(chart);
                return;
            }
        }
        System.out.println("⚠ All slots are already filled!");
    }

    // ======= Kiểm tra slot rỗng =======
    private boolean isSlotEmpty(VBox slot) {
        return slot.getChildren().size() == 1 && slot.getChildren().get(0) instanceof Text;
    }

    // ======= Xóa toàn bộ biểu đồ =======
    public void clearAll() {
        for (int i = 0; i < chartSlots.size(); i++) {
            VBox slot = chartSlots.get(i);
            slot.getChildren().setAll(new Text("Empty slot " + (i + 1)));
        }
    }

    // ======= Ví dụ tạo biểu đồ (demo) =======
    public BarChart<String, Number> createDailyRevenueChart() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setTitle("Daily Revenue");
        chart.setLegendVisible(false);
        chart.setPrefSize(400, 250);
        return chart;
    }

    public LineChart<String, Number> createCustomerGrowthChart() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        LineChart<String, Number> chart = new LineChart<>(x, y);
        chart.setTitle("Customer Growth");
        chart.setLegendVisible(false);
        chart.setPrefSize(400, 250);
        return chart;
    }
}