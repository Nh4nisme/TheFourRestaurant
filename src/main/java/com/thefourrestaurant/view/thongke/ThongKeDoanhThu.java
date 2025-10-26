package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.HoaDonDAO;
import com.thefourrestaurant.model.HoaDon;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class ThongKeDoanhThu {

    private static final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    public static Node taoBieuDo(String loai) {
        List<HoaDon> tatCaHoaDon = hoaDonDAO.getAll();
        LocalDate homNay = LocalDate.now();

        switch (loai) {
            case "ngày":
                return taoBieuDoDoanhThuTheoNgay(tatCaHoaDon, homNay);
            case "tháng":
                return taoBieuDoDoanhThuTheoThang(tatCaHoaDon, homNay);
            case "năm":
                return taoBieuDoDoanhThuTheoNam(tatCaHoaDon, homNay);
            default:
                return new Label("Loại thống kê không hợp lệ");
        }
    }

    private static Node taoBieuDoDoanhThuTheoNgay(List<HoaDon> danhSachHoaDon, LocalDate homNay) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Giờ");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu");
        LineChart<String, Number> bieuDo = new LineChart<>(xAxis, yAxis);
        bieuDo.setTitle("Doanh thu 3 ngày gần nhất");
        bieuDo.setLegendVisible(true);

        String[] colors = {"#2196F3", "#4CAF50", "#FFC107"}; // Blue, Green, Amber

        // Data for today
        Map<Integer, BigDecimal> doanhThuHomNay = tinhDoanhThuTheoGioChoNgay(danhSachHoaDon, homNay);
        XYChart.Series<String, Number> seriesHomNay = new XYChart.Series<>();
        seriesHomNay.setName("Hôm nay (" + homNay.format(DateTimeFormatter.ofPattern("dd/MM")) + ")");
        for (int i = 0; i < 24; i++) {
            seriesHomNay.getData().add(new XYChart.Data<>(i + "h", doanhThuHomNay.getOrDefault(i, BigDecimal.ZERO)));
        }
        bieuDo.getData().add(seriesHomNay);

        // Data for yesterday
        LocalDate homQua = homNay.minusDays(1);
        Map<Integer, BigDecimal> doanhThuHomQua = tinhDoanhThuTheoGioChoNgay(danhSachHoaDon, homQua);
        XYChart.Series<String, Number> seriesHomQua = new XYChart.Series<>();
        seriesHomQua.setName("Hôm qua (" + homQua.format(DateTimeFormatter.ofPattern("dd/MM")) + ")");
        for (int i = 0; i < 24; i++) {
            seriesHomQua.getData().add(new XYChart.Data<>(i + "h", doanhThuHomQua.getOrDefault(i, BigDecimal.ZERO)));
        }
        bieuDo.getData().add(seriesHomQua);

        // Data for two days ago
        LocalDate homKia = homNay.minusDays(2);
        Map<Integer, BigDecimal> doanhThuHomKia = tinhDoanhThuTheoGioChoNgay(danhSachHoaDon, homKia);
        XYChart.Series<String, Number> seriesHomKia = new XYChart.Series<>();
        seriesHomKia.setName("Hôm kia (" + homKia.format(DateTimeFormatter.ofPattern("dd/MM")) + ")");
        for (int i = 0; i < 24; i++) {
            seriesHomKia.getData().add(new XYChart.Data<>(i + "h", doanhThuHomKia.getOrDefault(i, BigDecimal.ZERO)));
        }
        bieuDo.getData().add(seriesHomKia);

        apDungMauChoNhieuBieuDo(bieuDo, colors);
        return bieuDo;
    }

    private static Map<Integer, BigDecimal> tinhDoanhThuTheoGioChoNgay(List<HoaDon> danhSachHoaDon, LocalDate ngay) {
        return danhSachHoaDon.stream()
                .filter(hd -> hd.getNgayLap().toLocalDate().isEqual(ngay))
                .collect(Collectors.groupingBy(
                        hd -> hd.getNgayLap().getHour(),
                        Collectors.mapping(HoaDon::getTongTien, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    private static Node taoBieuDoDoanhThuTheoThang(List<HoaDon> danhSachHoaDon, LocalDate homNay) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Ngày");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu");
        LineChart<String, Number> bieuDo = new LineChart<>(xAxis, yAxis);
        bieuDo.setTitle("Doanh thu 3 tháng gần nhất");
        bieuDo.setLegendVisible(true);

        String[] colors = {"#2196F3", "#4CAF50", "#FFC107"}; // Blue, Green, Amber

        // Data for current month
        Map<Integer, BigDecimal> doanhThuThangNay = tinhDoanhThuTheoNgayChoThang(danhSachHoaDon, homNay);
        XYChart.Series<String, Number> seriesThangNay = new XYChart.Series<>();
        seriesThangNay.setName("Tháng này (" + homNay.format(DateTimeFormatter.ofPattern("MM/yyyy")) + ")");
        for (int i = 1; i <= homNay.lengthOfMonth(); i++) {
            seriesThangNay.getData().add(new XYChart.Data<>(String.valueOf(i), doanhThuThangNay.getOrDefault(i, BigDecimal.ZERO)));
        }
        bieuDo.getData().add(seriesThangNay);

        // Data for previous month
        LocalDate thangTruoc = homNay.minusMonths(1);
        Map<Integer, BigDecimal> doanhThuThangTruoc = tinhDoanhThuTheoNgayChoThang(danhSachHoaDon, thangTruoc);
        XYChart.Series<String, Number> seriesThangTruoc = new XYChart.Series<>();
        seriesThangTruoc.setName("Tháng trước (" + thangTruoc.format(DateTimeFormatter.ofPattern("MM/yyyy")) + ")");
        for (int i = 1; i <= thangTruoc.lengthOfMonth(); i++) {
            seriesThangTruoc.getData().add(new XYChart.Data<>(String.valueOf(i), doanhThuThangTruoc.getOrDefault(i, BigDecimal.ZERO)));
        }
        bieuDo.getData().add(seriesThangTruoc);

        // Data for two months ago
        LocalDate haiThangTruoc = homNay.minusMonths(2);
        Map<Integer, BigDecimal> doanhThuHaiThangTruoc = tinhDoanhThuTheoNgayChoThang(danhSachHoaDon, haiThangTruoc);
        XYChart.Series<String, Number> seriesHaiThangTruoc = new XYChart.Series<>();
        seriesHaiThangTruoc.setName("2 tháng trước (" + haiThangTruoc.format(DateTimeFormatter.ofPattern("MM/yyyy")) + ")");
        for (int i = 1; i <= haiThangTruoc.lengthOfMonth(); i++) {
            seriesHaiThangTruoc.getData().add(new XYChart.Data<>(String.valueOf(i), doanhThuHaiThangTruoc.getOrDefault(i, BigDecimal.ZERO)));
        }
        bieuDo.getData().add(seriesHaiThangTruoc);

        apDungMauChoNhieuBieuDo(bieuDo, colors);
        return bieuDo;
    }

    private static Map<Integer, BigDecimal> tinhDoanhThuTheoNgayChoThang(List<HoaDon> danhSachHoaDon, LocalDate ngayTrongThang) {
        return danhSachHoaDon.stream()
                .filter(hd -> hd.getNgayLap().getYear() == ngayTrongThang.getYear() && hd.getNgayLap().getMonth() == ngayTrongThang.getMonth())
                .collect(Collectors.groupingBy(
                        hd -> hd.getNgayLap().getDayOfMonth(),
                        Collectors.mapping(HoaDon::getTongTien, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    private static Node taoBieuDoDoanhThuTheoNam(List<HoaDon> danhSachHoaDon, LocalDate homNay) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tháng");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu");
        LineChart<String, Number> bieuDo = new LineChart<>(xAxis, yAxis);
        bieuDo.setTitle("Doanh thu 3 năm gần nhất");
        bieuDo.setLegendVisible(true);

        String[] colors = {"#2196F3", "#4CAF50", "#FFC107"}; // Blue, Green, Amber

        // Data for current year
        Map<Month, BigDecimal> doanhThuNamNay = tinhDoanhThuTheoThangChoNam(danhSachHoaDon, homNay.getYear());
        XYChart.Series<String, Number> seriesNamNay = new XYChart.Series<>();
        seriesNamNay.setName("Năm nay (" + homNay.getYear() + ")");
        for (Month month : Month.values()) {
            String tenThang = month.getDisplayName(TextStyle.SHORT, new Locale("vi"));
            seriesNamNay.getData().add(new XYChart.Data<>(tenThang, doanhThuNamNay.getOrDefault(month, BigDecimal.ZERO)));
        }
        bieuDo.getData().add(seriesNamNay);

        // Data for previous year
        int namTruoc = homNay.getYear() - 1;
        Map<Month, BigDecimal> doanhThuNamTruoc = tinhDoanhThuTheoThangChoNam(danhSachHoaDon, namTruoc);
        XYChart.Series<String, Number> seriesNamTruoc = new XYChart.Series<>();
        seriesNamTruoc.setName("Năm trước (" + namTruoc + ")");
        for (Month month : Month.values()) {
            String tenThang = month.getDisplayName(TextStyle.SHORT, new Locale("vi"));
            seriesNamTruoc.getData().add(new XYChart.Data<>(tenThang, doanhThuNamTruoc.getOrDefault(month, BigDecimal.ZERO)));
        }
        bieuDo.getData().add(seriesNamTruoc);

        // Data for two years ago
        int haiNamTruoc = homNay.getYear() - 2;
        Map<Month, BigDecimal> doanhThuHaiNamTruoc = tinhDoanhThuTheoThangChoNam(danhSachHoaDon, haiNamTruoc);
        XYChart.Series<String, Number> seriesHaiNamTruoc = new XYChart.Series<>();
        seriesHaiNamTruoc.setName("2 năm trước (" + haiNamTruoc + ")");
        for (Month month : Month.values()) {
            String tenThang = month.getDisplayName(TextStyle.SHORT, new Locale("vi"));
            seriesHaiNamTruoc.getData().add(new XYChart.Data<>(tenThang, doanhThuHaiNamTruoc.getOrDefault(month, BigDecimal.ZERO)));
        }
        bieuDo.getData().add(seriesHaiNamTruoc);

        apDungMauChoNhieuBieuDo(bieuDo, colors);
        return bieuDo;
    }

    private static Map<Month, BigDecimal> tinhDoanhThuTheoThangChoNam(List<HoaDon> danhSachHoaDon, int nam) {
        return danhSachHoaDon.stream()
                .filter(hd -> hd.getNgayLap().getYear() == nam)
                .collect(Collectors.groupingBy(
                        hd -> hd.getNgayLap().getMonth(),
                        Collectors.mapping(HoaDon::getTongTien, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    private static void apDungMauChoNhieuBieuDo(LineChart<String, Number> bieuDo, String[] mauSac) {
        bieuDo.setLegendVisible(true);
        bieuDo.setCreateSymbols(true);
        bieuDo.setAlternativeRowFillVisible(false);
        bieuDo.setAlternativeColumnFillVisible(false);
        bieuDo.setHorizontalGridLinesVisible(true);
        bieuDo.setVerticalGridLinesVisible(false);

        // Chờ biểu đồ render xong trước khi áp màu
        bieuDo.applyCss();
        bieuDo.layout();

        for (int i = 0; i < bieuDo.getData().size(); i++) {
            XYChart.Series<String, Number> series = bieuDo.getData().get(i);
            final String mau = mauSac[i % mauSac.length];

            // Gán màu cho đường (sau khi node series đã được render)
            series.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    Node line = newNode.lookup(".chart-series-line");
                    if (line != null) {
                        line.setStyle("-fx-stroke: " + mau + "; -fx-stroke-width: 2.5px;");
                    }
                }
            });

            // Gán màu cho từng điểm (chấm tròn)
            for (XYChart.Data<String, Number> data : series.getData()) {
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle("-fx-background-color: " + mau + ", white; -fx-border-width: 1px;");
                    }
                });
            }
        }
    }
}
