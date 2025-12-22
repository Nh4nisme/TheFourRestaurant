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

public class ThongKeNhanVien extends VBox {

    private final ThongKeDAO thongKeDAO;
    private final NumberFormat currencyFormat;
    private final HBox chartsContainer;
    private final ComboBox<Integer> cboThang;
    private final ComboBox<Integer> cboNam;

    public ThongKeNhanVien() {
        this.thongKeDAO = new ThongKeDAO();
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #F5F5F5;");

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        Label lblThang = new Label("Tháng:");
        lblThang.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
        cboThang = new ComboBox<>();
        cboThang.getItems().addAll(IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList()));
        cboThang.setValue(currentMonth);

        Label lblNam = new Label("Năm:");
        lblNam.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
        cboNam = new ComboBox<>();
        cboNam.getItems().addAll(IntStream.rangeClosed(currentYear - 5, currentYear).boxed().sorted((a, b) -> b - a).toList());
        cboNam.setValue(currentYear);

        ButtonSample btnCapNhat = new ButtonSample("Cập nhật", 50, 25, 14);
        btnCapNhat.getStyleClass().add("button_sampleGamboge");
        btnCapNhat.setOnAction(e -> loadData());

        filterBox.getChildren().addAll(lblThang, cboThang, lblNam, cboNam, btnCapNhat);

        chartsContainer = new HBox(20);
        chartsContainer.setAlignment(Pos.CENTER);

        this.getChildren().addAll(filterBox, chartsContainer);
        loadData();
    }

    private void loadData() {
        int month = cboThang.getValue();
        int year = cboNam.getValue();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        chartsContainer.getChildren().clear();

        Map<String, Integer> soHDNV = thongKeDAO.getThongKeNhanVienTheoSoHD(startDate, endDate);
        if (!soHDNV.isEmpty()) {
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Nhân viên");
            yAxis.setLabel("Số hóa đơn");
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Số hóa đơn theo nhân viên");
            barChart.setLegendVisible(false);
            barChart.setPrefSize(800, 500);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<String, Integer> entry : soHDNV.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barChart.getData().add(series);
            chartsContainer.getChildren().add(barChart);
        }

        Map<String, Double> doanhThuNV = thongKeDAO.getDoanhThuTheoNhanVien(startDate, endDate);
        if (!doanhThuNV.isEmpty()) {
            CategoryAxis xAxis2 = new CategoryAxis();
            NumberAxis yAxis2 = new NumberAxis();
            xAxis2.setLabel("Nhân viên");
            yAxis2.setLabel("Doanh thu (VND)");
            BarChart<String, Number> barChart2 = new BarChart<>(xAxis2, yAxis2);
            barChart2.setTitle("Doanh thu theo nhân viên");
            barChart2.setLegendVisible(false);
            barChart2.setPrefSize(800, 500);

            XYChart.Series<String, Number> series2 = new XYChart.Series<>();
            for (Map.Entry<String, Double> entry : doanhThuNV.entrySet()) {
                series2.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barChart2.getData().add(series2);
            chartsContainer.getChildren().add(barChart2);
        }
    }
}