package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.ThongKeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

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
        String theo = view.getComboBoxTheo().getValue();
        LocalDate ngayBatDau = null;
        LocalDate ngayKetThuc = null;

        switch (theo) {
            case "Ngày":
                ngayBatDau = view.getDatePickerBatDau().getValue();
                ngayKetThuc = view.getDatePickerKetThuc().getValue();
                if (ngayBatDau == null || ngayKetThuc == null || ngayBatDau.isAfter(ngayKetThuc)) {
                    showAlert("Khoảng thời gian không hợp lệ.");
                    return;
                }
                break;
            case "Tháng":
                int month = view.getCboThang().getValue();
                int yearForMonth = view.getCboNam().getValue();
                YearMonth yearMonth = YearMonth.of(yearForMonth, month);
                ngayBatDau = yearMonth.atDay(1);
                ngayKetThuc = yearMonth.atEndOfMonth();
                break;
            case "Năm":
                int year = view.getCboNam().getValue();
                ngayBatDau = LocalDate.of(year, 1, 1);
                ngayKetThuc = LocalDate.of(year, 12, 31);
                break;
            case "Quý":
                int quarter = Integer.parseInt(view.getCboQuy().getValue().substring(4));
                int yearForQuarter = view.getCboNam().getValue();
                ngayBatDau = LocalDate.of(yearForQuarter, (quarter - 1) * 3 + 1, 1);
                ngayKetThuc = ngayBatDau.plusMonths(2).withDayOfMonth(ngayBatDau.plusMonths(2).lengthOfMonth());
                break;
        }

        String loaiThongKe = view.getComboBoxLoaiThongKe().getValue();
        String loaiBieuDo = view.getComboBoxLoaiBieuDo().getValue();
        String tuyChon = view.getComboBoxTuyChon().getValue();

        if (view.getChkSoSanh().isSelected()) {
            themMoiSeriesVaoBieuDo(ngayBatDau, ngayKetThuc, loaiThongKe, loaiBieuDo, tuyChon);
        } else {
            taoBieuDoMoi(ngayBatDau, ngayKetThuc, loaiThongKe, loaiBieuDo, tuyChon);
        }
    }

    private void taoBieuDoMoi(LocalDate ngayBatDau, LocalDate ngayKetThuc, String loaiThongKe, String loaiBieuDo, String tuyChon) {
        view.getKhuVucBieuDo().getChildren().clear();
        currentChart = null;

        Map<String, ? extends Number> data = layDuLieu(ngayBatDau, ngayKetThuc, loaiThongKe, tuyChon);
        if (data == null || data.isEmpty()) {
            view.getKhuVucBieuDo().getChildren().add(new Label("Không có dữ liệu cho khoảng thời gian này."));
            view.getSummaryGrid().setVisible(false);
            return;
        }

        String title = taoTieuDeBieuDo(loaiThongKe, tuyChon, ngayBatDau, ngayKetThuc);
        String seriesName = taoTenSeries(loaiThongKe, tuyChon, ngayBatDau, ngayKetThuc);

        Chart chart = createChart(data, loaiBieuDo, title, loaiThongKe, seriesName);
        if (chart != null) {
            currentChart = chart;
            currentChartType = loaiBieuDo;
            currentStatsType = loaiThongKe;
            view.getKhuVucBieuDo().getChildren().add(chart);
            updateSummary(ngayBatDau, ngayKetThuc, loaiThongKe, data);
        } else {
            view.getKhuVucBieuDo().getChildren().add(new Label("Loại biểu đồ không được hỗ trợ."));
            view.getSummaryGrid().setVisible(false);
        }
    }

    private void themMoiSeriesVaoBieuDo(LocalDate ngayBatDau, LocalDate ngayKetThuc, String loaiThongKe, String loaiBieuDo, String tuyChon) {
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

        XYChart<String, Number> xyChart = (XYChart<String, Number>) currentChart;
        xyChart.setAnimated(false);

        Map<String, ? extends Number> data = layDuLieu(ngayBatDau, ngayKetThuc, loaiThongKe, tuyChon);
        if (data == null || data.isEmpty()) {
            showAlert("Không có dữ liệu để so sánh trong khoảng thời gian đã chọn.");
            xyChart.setAnimated(true);
            return;
        }

        XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
        String seriesName = taoTenSeries(loaiThongKe, tuyChon, ngayBatDau, ngayKetThuc);
        newSeries.setName(seriesName);

        for (Map.Entry<String, ? extends Number> entry : data.entrySet()) {
            newSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        xyChart.getData().add(newSeries);

        if ("Món ăn".equals(loaiThongKe)) {
            view.getKhuVucBieuDo().getChildren().clear();
            view.getKhuVucBieuDo().getChildren().add(currentChart);
        }
        
        currentChart.setTitle(String.format("So sánh %s", loaiThongKe.toLowerCase()));
        if (!xyChart.isLegendVisible()) {
            xyChart.setLegendVisible(true);
        }
        
        xyChart.setAnimated(true);
    }

    private Map<String, ? extends Number> layDuLieu(LocalDate startDate, LocalDate endDate, String loaiThongKe, String tuyChon) {
        return switch (loaiThongKe) {
            case "Doanh thu" -> thongKeDAO.getDoanhThuTheoNgay(startDate, endDate, tuyChon);
            case "Món ăn" -> thongKeDAO.getThongKeMonAn(startDate, endDate, tuyChon);
            case "Bàn" -> thongKeDAO.getThongKeBan(startDate, endDate, tuyChon);
            default -> null;
        };
    }

    private Chart createChart(Map<String, ? extends Number> data, String loaiBieuDo, String title, String xAxisLabelBase, String seriesName) {
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
            case "Biểu đồ cột" -> createBarChart(data, title, xAxisLabel, yAxisLabel, seriesName);
            case "Biểu đồ đường" -> createLineChart(data, title, xAxisLabel, yAxisLabel, seriesName);
            case "Biểu đồ tròn" -> createPieChart(data, title);
            default -> null;
        };
    }

    private <T extends Number> BarChart<String, Number> createBarChart(Map<String, T> data, String title, String xAxisLabel, String yAxisLabel, String seriesName) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        barChart.setTitle(title);
        barChart.setLegendVisible(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);
        
        ObservableList<String> categories = FXCollections.observableArrayList(data.keySet());
        FXCollections.sort(categories);
        xAxis.setCategories(categories);

        for (String category : categories) {
            series.getData().add(new XYChart.Data<>(category, data.get(category)));
        }
        barChart.getData().add(series);
        return barChart;
    }

    private <T extends Number> LineChart<String, Number> createLineChart(Map<String, T> data, String title, String xAxisLabel, String yAxisLabel, String seriesName) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        lineChart.setTitle(title);
        lineChart.setLegendVisible(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        ObservableList<String> categories = FXCollections.observableArrayList(data.keySet());
        FXCollections.sort(categories);
        xAxis.setCategories(categories);

        for (String category : categories) {
            series.getData().add(new XYChart.Data<>(category, data.get(category)));
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

    private String taoTenSeries(String loaiThongKe, String tuyChon, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        String dateRange;
        if (ngayBatDau.equals(ngayKetThuc)) {
            dateRange = ngayBatDau.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            dateRange = String.format("%s - %s",
                    ngayBatDau.format(DateTimeFormatter.ofPattern("dd/MM")),
                    ngayKetThuc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (("Món ăn".equals(loaiThongKe) || "Bàn".equals(loaiThongKe) || "Doanh thu".equals(loaiThongKe)) && tuyChon != null && !tuyChon.startsWith("Tất cả")) {
            return String.format("%s (%s)", tuyChon, dateRange);
        }
        return dateRange;
    }
    
    private String taoTieuDeBieuDo(String loaiThongKe, String tuyChon, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        String thoiGian;
        if (ngayBatDau.equals(ngayKetThuc)) {
            thoiGian = "ngày " + ngayBatDau.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            thoiGian = String.format("từ %s đến %s",
                    ngayBatDau.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    ngayKetThuc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (("Món ăn".equals(loaiThongKe) || "Bàn".equals(loaiThongKe) || "Doanh thu".equals(loaiThongKe)) && tuyChon != null && !tuyChon.startsWith("Tất cả")) {
            return String.format("Thống kê %s loại '%s' %s", loaiThongKe.toLowerCase(), tuyChon, thoiGian);
        } else {
            return String.format("Thống kê tất cả %s %s", loaiThongKe.toLowerCase(), thoiGian);
        }
    }

    private void updateSummary(LocalDate startDate, LocalDate endDate, String loaiThongKe, Map<String, ? extends Number> data) {
        GridPane summaryGrid = view.getSummaryGrid();
        summaryGrid.getChildren().clear();
        summaryGrid.setVisible(false);

        if ("Món ăn".equals(loaiThongKe)) {
            BigDecimal hoaDonCaoNhat = thongKeDAO.getInvoiceStat(startDate, endDate, "MAX");
            BigDecimal hoaDonThapNhat = thongKeDAO.getInvoiceStat(startDate, endDate, "MIN");

            Optional<? extends Map.Entry<String, ? extends Number>> bestSeller = data.entrySet().stream().max(Map.Entry.comparingByValue(Comparator.comparingDouble(Number::doubleValue)));
            Optional<? extends Map.Entry<String, ? extends Number>> worstSeller = data.entrySet().stream().min(Map.Entry.comparingByValue(Comparator.comparingDouble(Number::doubleValue)));

            summaryGrid.add(new Label("Hóa đơn cao nhất:"), 0, 0);
            summaryGrid.add(new Label(NumberFormat.getCurrencyInstance(Locale.of("vi", "VN")).format(hoaDonCaoNhat)), 1, 0);
            summaryGrid.add(new Label("Hóa đơn thấp nhất:"), 0, 1);
            summaryGrid.add(new Label(NumberFormat.getCurrencyInstance(Locale.of("vi", "VN")).format(hoaDonThapNhat)), 1, 1);

            bestSeller.ifPresent(entry -> {
                summaryGrid.add(new Label("Món bán chạy nhất:"), 2, 0);
                summaryGrid.add(new Label(String.format("%s (%d)", entry.getKey(), entry.getValue().intValue())), 3, 0);
            });
            worstSeller.ifPresent(entry -> {
                summaryGrid.add(new Label("Món bán chậm nhất:"), 2, 1);
                summaryGrid.add(new Label(String.format("%s (%d)", entry.getKey(), entry.getValue().intValue())), 3, 1);
            });

            summaryGrid.setVisible(true);
        }
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
