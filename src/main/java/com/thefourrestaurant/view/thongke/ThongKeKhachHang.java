package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.ThongKeDAO;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThongKeKhachHang extends VBox {

    private final ThongKeDAO thongKeDAO;
    private final NumberFormat currencyFormat;
    private final HBox chartsRow1;
    private final HBox chartsRow2;
    private final ComboBox<Integer> cboThang;
    private final ComboBox<Integer> cboNam;

    public ThongKeKhachHang() {
        this.thongKeDAO = new ThongKeDAO();
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #F5F5F5;");

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        Label lblThang = new Label("Thang:");
        lblThang.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
        cboThang = new ComboBox<>();
        cboThang.getItems().addAll(IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList()));
        cboThang.setValue(currentMonth);

        Label lblNam = new Label("Nam:");
        lblNam.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
        cboNam = new ComboBox<>();
        cboNam.getItems().addAll(IntStream.rangeClosed(currentYear - 5, currentYear).boxed().sorted((a, b) -> b - a).toList());
        cboNam.setValue(currentYear);

        ButtonSample btnCapNhat = new ButtonSample("Cap Nhat", 50, 25, 14);
        btnCapNhat.getStyleClass().add("button_sampleGamboge");
        btnCapNhat.setOnAction(e -> loadData());

        filterBox.getChildren().addAll(lblThang, cboThang, lblNam, cboNam, btnCapNhat);

        chartsRow1 = new HBox(20);
        chartsRow1.setAlignment(Pos.CENTER);
        chartsRow2 = new HBox(20);
        chartsRow2.setAlignment(Pos.CENTER);

        this.getChildren().addAll(filterBox, chartsRow1, chartsRow2);
        loadData();
    }

    private void loadData() {
        int month = cboThang.getValue();
        int year = cboNam.getValue();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        chartsRow1.getChildren().clear();
        chartsRow2.getChildren().clear();

        Map<String, Integer> loaiKH = thongKeDAO.getThongKeKhachHangTheoLoai(startDate, endDate);
        if (!loaiKH.isEmpty()) {
            PieChart pieChart = new PieChart();
            pieChart.setTitle("Khach hang theo loai");
            pieChart.setPrefSize(700, 500);
            for (Map.Entry<String, Integer> entry : loaiKH.entrySet()) {
                pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
            chartsRow1.getChildren().add(pieChart);
        }

        Map<String, Double> doanhThuKH = thongKeDAO.getDoanhThuTheoKhachHang(startDate, endDate);
        if (!doanhThuKH.isEmpty()) {
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Khach hang");
            yAxis.setLabel("Doanh thu (VND)");
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Top 10 khach hang theo doanh thu");
            barChart.setLegendVisible(false);
            barChart.setPrefSize(800, 500);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<String, Double> entry : doanhThuKH.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barChart.getData().add(series);
            chartsRow1.getChildren().add(barChart);
        }

        Map<String, Integer> tanSuat = thongKeDAO.getTanSuatKhachHang(startDate, endDate);
        if (!tanSuat.isEmpty()) {
            CategoryAxis xAxis2 = new CategoryAxis();
            NumberAxis yAxis2 = new NumberAxis();
            xAxis2.setLabel("Khach hang");
            yAxis2.setLabel("So lan den");
            BarChart<String, Number> barChart2 = new BarChart<>(xAxis2, yAxis2);
            barChart2.setTitle("Top 10 khach hang thuong xuyen");
            barChart2.setLegendVisible(false);
            barChart2.setPrefSize(800, 500);

            XYChart.Series<String, Number> series2 = new XYChart.Series<>();
            for (Map.Entry<String, Integer> entry : tanSuat.entrySet()) {
                series2.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barChart2.getData().add(series2);
            chartsRow2.getChildren().add(barChart2);
        }

        int tongKH = thongKeDAO.getTongKhachHang(startDate, endDate);
        int khMoi = thongKeDAO.getSoKhachHangMoi(startDate, endDate);
        VBox summaryBox = new VBox(10);
        summaryBox.setPadding(new Insets(20));
        summaryBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        summaryBox.getChildren().addAll(
            createStatLabel("Tong khach hang:", String.valueOf(tongKH)),
            createStatLabel("Khach hang moi:", String.valueOf(khMoi))
        );
        chartsRow2.getChildren().add(summaryBox);
    }

    private HBox createStatLabel(String label, String value) {
        HBox hbox = new HBox(10);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 16px; -fx-text-fill: #27AE60;");
        hbox.getChildren().addAll(lbl, val);
        return hbox;
    }
}