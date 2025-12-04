package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.ThongKeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.Map;

public class ThongKeController {

    private final ThongKeView view;
    private final ThongKeDAO thongKeDAO;

    public ThongKeController(ThongKeView view) {
        this.view = view;
        this.thongKeDAO = new ThongKeDAO();
        addEventHandlers();
    }

    private void addEventHandlers() {
        // Sự kiện cho nút xem thống kê
        view.getBtnXemThongKe().setOnAction(e -> xuLyXemThongKe());

        // Sự kiện cho DatePicker để đảm bảo ngày hợp lệ
        view.getDatePickerBatDau().setOnAction(e -> {
            LocalDate ngayBatDau = view.getDatePickerBatDau().getValue();
            LocalDate ngayKetThuc = view.getDatePickerKetThuc().getValue();
            if (ngayBatDau != null && ngayKetThuc != null && ngayBatDau.isAfter(ngayKetThuc)) {
                view.getDatePickerKetThuc().setValue(ngayBatDau);
            }
        });

        view.getDatePickerKetThuc().setOnAction(e -> {
            LocalDate ngayBatDau = view.getDatePickerBatDau().getValue();
            LocalDate ngayKetThuc = view.getDatePickerKetThuc().getValue();
            if (ngayBatDau != null && ngayKetThuc != null && ngayKetThuc.isBefore(ngayBatDau)) {
                view.getDatePickerBatDau().setValue(ngayKetThuc);
            }
        });
    }

    private void xuLyXemThongKe() {
        LocalDate ngayBatDau = view.getDatePickerBatDau().getValue();
        LocalDate ngayKetThuc = view.getDatePickerKetThuc().getValue();
        String loaiThongKe = view.getComboBoxLoaiThongKe().getValue();
        String loaiBieuDo = view.getComboBoxLoaiBieuDo().getValue();

        if (ngayBatDau == null || ngayKetThuc == null) {
            showAlert("Vui lòng chọn ngày bắt đầu và ngày kết thúc.");
            return;
        }

        // Kiểm tra lại một lần nữa trước khi xử lý
        if (ngayBatDau.isAfter(ngayKetThuc)) {
            showAlert("Ngày bắt đầu không thể sau ngày kết thúc.");
            return;
        }

        view.getKhuVucBieuDo().getChildren().clear();
        Chart chart = null;

        try {
            switch (loaiThongKe) {
                case "Doanh thu":
                    Map<String, Double> doanhThuData = thongKeDAO.getDoanhThuTheoNgay(ngayBatDau, ngayKetThuc);
                    chart = createChart(doanhThuData, loaiBieuDo, "Thống kê Doanh thu", "Ngày", "Doanh thu (VND)");
                    break;
                case "Món ăn":
                    Map<String, Integer> monAnData = thongKeDAO.getThongKeMonAn(ngayBatDau, ngayKetThuc);
                    chart = createChart(monAnData, loaiBieuDo, "Thống kê Món ăn", "Món ăn", "Số lượng bán");
                    break;
                case "Bàn":
                    Map<String, Double> banData = thongKeDAO.getThongKeBan(ngayBatDau, ngayKetThuc);
                    chart = createChart(banData, loaiBieuDo, "Thống kê Bàn", "Bàn", "Doanh thu (VND)");
                    break;
            }

            if (chart != null) {
                view.getKhuVucBieuDo().getChildren().add(chart);
            } else {
                view.getKhuVucBieuDo().getChildren().add(new Label("Không có dữ liệu hoặc loại biểu đồ không được hỗ trợ."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Đã xảy ra lỗi khi tạo biểu đồ.");
        }
    }

    private <T extends Number> Chart createChart(Map<String, T> data, String loaiBieuDo, String title, String xAxisLabel, String yAxisLabel) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        switch (loaiBieuDo) {
            case "Biểu đồ cột":
                return createBarChart(data, title, xAxisLabel, yAxisLabel);
            case "Biểu đồ đường":
                return createLineChart(data, title, xAxisLabel, yAxisLabel);
            case "Biểu đồ tròn":
                return createPieChart(data, title);
            default:
                return null;
        }
    }

    private <T extends Number> BarChart<String, Number> createBarChart(Map<String, T> data, String title, String xAxisLabel, String yAxisLabel) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        barChart.setTitle(title);
        barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, T> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
        return barChart;
    }

    private <T extends Number> LineChart<String, Number> createLineChart(Map<String, T> data, String title, String xAxisLabel, String yAxisLabel) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        lineChart.setTitle(title);
        lineChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, T> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().add(series);
        return lineChart;
    }

    private <T extends Number> PieChart createPieChart(Map<String, T> data, String title) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, T> entry : data.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue().doubleValue()));
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(title);
        return pieChart;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
