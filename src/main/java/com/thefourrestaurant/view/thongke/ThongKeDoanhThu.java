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

    private static Node taoBieuDoDoanhThuTheoNgay(List<HoaDon> danhSachHoaDon, LocalDate ngay) {
        Map<Integer, BigDecimal> doanhThuTheoGio = danhSachHoaDon.stream()
                .filter(hd -> hd.getNgayLap().toLocalDate().isEqual(ngay))
                .collect(Collectors.groupingBy(
                        hd -> hd.getNgayLap().getHour(),
                        Collectors.mapping(HoaDon::getTongTien, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        LineChart<String, Number> bieuDo = new LineChart<>(new CategoryAxis(), new NumberAxis());
        bieuDo.setTitle("Doanh thu trong ngày " + ngay);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");

        for (int i = 0; i < 24; i++) {
            series.getData().add(new XYChart.Data<>(i + "h", doanhThuTheoGio.getOrDefault(i, BigDecimal.ZERO)));
        }

        bieuDo.getData().add(series);
        return bieuDo;
    }

    private static Node taoBieuDoDoanhThuTheoThang(List<HoaDon> danhSachHoaDon, LocalDate ngay) {
        Map<Integer, BigDecimal> doanhThuTheoNgay = danhSachHoaDon.stream()
                .filter(hd -> hd.getNgayLap().getYear() == ngay.getYear() && hd.getNgayLap().getMonth() == ngay.getMonth())
                .collect(Collectors.groupingBy(
                        hd -> hd.getNgayLap().getDayOfMonth(),
                        Collectors.mapping(HoaDon::getTongTien, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        LineChart<String, Number> bieuDo = new LineChart<>(new CategoryAxis(), new NumberAxis());
        bieuDo.setTitle("Doanh thu tháng " + ngay.getMonthValue() + "/" + ngay.getYear());
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");

        for (int i = 1; i <= ngay.lengthOfMonth(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), doanhThuTheoNgay.getOrDefault(i, BigDecimal.ZERO)));
        }

        bieuDo.getData().add(series);
        return bieuDo;
    }

    private static Node taoBieuDoDoanhThuTheoNam(List<HoaDon> danhSachHoaDon, LocalDate ngay) {
        Map<Month, BigDecimal> doanhThuTheoThang = danhSachHoaDon.stream()
                .filter(hd -> hd.getNgayLap().getYear() == ngay.getYear())
                .collect(Collectors.groupingBy(
                        hd -> hd.getNgayLap().getMonth(),
                        Collectors.mapping(HoaDon::getTongTien, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tháng");
        LineChart<String, Number> bieuDo = new LineChart<>(xAxis, new NumberAxis());
        bieuDo.setTitle("Doanh thu năm " + ngay.getYear());
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");

        for (Month month : Month.values()) {
            String tenThang = month.getDisplayName(TextStyle.FULL, new Locale("vi"));
            series.getData().add(new XYChart.Data<>(tenThang, doanhThuTheoThang.getOrDefault(month, BigDecimal.ZERO)));
        }

        bieuDo.getData().add(series);
        return bieuDo;
    }
}
