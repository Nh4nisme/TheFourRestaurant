package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.ThongKeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ThongKeController {

    private final ThongKeView view;
    private final ThongKeDAO thongKeDAO;
    private Chart currentChart = null;
    private String currentChartType = "";
    private String currentStatsType = "";

    public ThongKeController(ThongKeView view) {
        this.view = view;
        this.thongKeDAO = new ThongKeDAO();
        addEventHandlers();
    }

    private void addEventHandlers() {
        view.getBtnXemThongKe().setOnAction(e -> xuLyXemThongKe());

        view.getChkSoSanh().selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                view.getBtnXemThongKe().setText("Thêm So Sánh");
            } else {
                view.getBtnXemThongKe().setText("Xem Thống Kê");
            }
        });

        view.getDatePickerBatDau().setOnAction(e -> validateDateRange());
        view.getDatePickerKetThuc().setOnAction(e -> validateDateRange());
    }

    private void validateDateRange() {
        LocalDate ngayBatDau = view.getDatePickerBatDau().getValue();
        LocalDate ngayKetThuc = view.getDatePickerKetThuc().getValue();
        if (ngayBatDau != null && ngayKetThuc != null) {
            if (ngayBatDau.isAfter(ngayKetThuc)) {
                view.getDatePickerKetThuc().setValue(ngayBatDau);
            }
        }
    }

    private void xuLyXemThongKe() {
        LocalDate ngayBatDau = view.getDatePickerBatDau().getValue();
        LocalDate ngayKetThuc = view.getDatePickerKetThuc().getValue();
        String loaiThongKe = view.getComboBoxLoaiThongKe().getValue();
        String loaiBieuDo = view.getComboBoxLoaiBieuDo().getValue();

        if (ngayBatDau == null || ngayKetThuc == null || ngayBatDau.isAfter(ngayKetThuc)) {
            showAlert("Khoảng thời gian không hợp lệ.");
            return;
        }

        if (view.getChkSoSanh().isSelected()) {
            themMoiSeriesVaoBieuDo(ngayBatDau, ngayKetThuc, loaiThongKe, loaiBieuDo);
        } else {
            taoBieuDoMoi(ngayBatDau, ngayKetThuc, loaiThongKe, loaiBieuDo);
        }
    }

    private void taoBieuDoMoi(LocalDate ngayBatDau, LocalDate ngayKetThuc, String loaiThongKe, String loaiBieuDo) {
        view.getKhuVucBieuDo().getChildren().clear();
        currentChart = null;

        Map<String, ? extends Number> data = layDuLieu(ngayBatDau, ngayKetThuc, loaiThongKe);
        if (data == null || data.isEmpty()) {
            view.getKhuVucBieuDo().getChildren().add(new Label("Không có dữ liệu cho khoảng thời gian này."));
            return;
        }

        String title = String.format("Thống kê %s từ %s đến %s", loaiThongKe.toLowerCase(),
                ngayBatDau.format(DateTimeFormatter.ISO_LOCAL_DATE),
                ngayKetThuc.format(DateTimeFormatter.ISO_LOCAL_DATE));

        Chart chart = createChart(data, loaiBieuDo, title, loaiThongKe);
        if (chart != null) {
            currentChart = chart;
            currentChartType = loaiBieuDo;
            currentStatsType = loaiThongKe;
            view.getKhuVucBieuDo().getChildren().add(chart);
        } else {
            view.getKhuVucBieuDo().getChildren().add(new Label("Loại biểu đồ không được hỗ trợ."));
        }
    }

    private void themMoiSeriesVaoBieuDo(LocalDate ngayBatDau, LocalDate ngayKetThuc, String loaiThongKe, String loaiBieuDo) {
        if (currentChart == null) {
            showAlert("Vui lòng tạo một biểu đồ gốc trước khi so sánh.");
            return;
        }
        if (!currentChartType.equals(loaiBieuDo) || !currentStatsType.equals(loaiThongKe)) {
            showAlert("Không thể so sánh hai loại thống kê hoặc biểu đồ khác nhau.");
            return;
        }
        if (currentChart instanceof PieChart) {
            showAlert("Không thể thêm so sánh cho biểu đồ tròn.");
            return;
        }

        Map<String, ? extends Number> data = layDuLieu(ngayBatDau, ngayKetThuc, loaiThongKe);
        if (data == null || data.isEmpty()) {
            showAlert("Không có dữ liệu để so sánh trong khoảng thời gian đã chọn.");
            return;
        }

        XYChart<String, Number> xyChart = (XYChart<String, Number>) currentChart;
        XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
        newSeries.setName(String.format("%s đến %s",
                ngayBatDau.format(DateTimeFormatter.ISO_LOCAL_DATE),
                ngayKetThuc.format(DateTimeFormatter.ISO_LOCAL_DATE)));

        for (Map.Entry<String, ? extends Number> entry : data.entrySet()) {
            newSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        xyChart.getData().add(newSeries);
        currentChart.setTitle(String.format("So sánh %s", loaiThongKe.toLowerCase()));
        if (!xyChart.isLegendVisible()) {
            xyChart.setLegendVisible(true);
            // Đổi tên series đầu tiên
            if (!xyChart.getData().isEmpty()) {
                xyChart.getData().get(0).setName("Dữ liệu gốc");
            }
        }
    }

    private Map<String, ? extends Number> layDuLieu(LocalDate startDate, LocalDate endDate, String loaiThongKe) {
        return switch (loaiThongKe) {
            case "Doanh thu" -> thongKeDAO.getDoanhThuTheoNgay(startDate, endDate);
            case "Món ăn" -> thongKeDAO.getThongKeMonAn(startDate, endDate);
            case "Bàn" -> thongKeDAO.getThongKeBan(startDate, endDate);
            default -> null;
        };
    }

    private Chart createChart(Map<String, ? extends Number> data, String loaiBieuDo, String title, String xAxisLabelBase) {
        String yAxisLabel = switch (xAxisLabelBase) {
            case "Doanh thu", "Bàn" -> "Doanh thu (VND)";
            case "Món ăn" -> "Số lượng bán";
            default -> "Giá trị";
        };
        String xAxisLabel = switch (xAxisLabelBase) {
            case "Doanh thu" -> "Ngày";
            default -> xAxisLabelBase;
        };

        return switch (loaiBieuDo) {
            case "Biểu đồ cột" -> createBarChart(data, title, xAxisLabel, yAxisLabel);
            case "Biểu đồ đường" -> createLineChart(data, title, xAxisLabel, yAxisLabel);
            case "Biểu đồ tròn" -> createPieChart(data, title);
            default -> null;
        };
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Lấy owner (cửa sổ cha) từ một thành phần trong view
        Window owner = view.getScene().getWindow();
        alert.initOwner(owner);

        alert.showAndWait();
    }
}
