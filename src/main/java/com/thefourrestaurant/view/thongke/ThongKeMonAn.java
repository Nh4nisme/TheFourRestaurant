package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.HoaDonDAO;
import com.thefourrestaurant.model.ChiTietHoaDon;
import com.thefourrestaurant.model.HoaDon;
import com.thefourrestaurant.model.MonAn;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ThongKeMonAn {

    private static final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    public static Node taoBieuDo(String loai) {
        List<HoaDon> tatCaHoaDon = hoaDonDAO.getAll();
        LocalDate homNay = LocalDate.now();

        Predicate<HoaDon> boLoc;
        String giaiDoanTieuDe;

        switch (loai) {
            case "ngày":
                boLoc = hd -> hd.getNgayLap().toLocalDate().isEqual(homNay);
                giaiDoanTieuDe = "trong ngày";
                break;
            case "tháng":
                boLoc = hd -> hd.getNgayLap().getYear() == homNay.getYear() && hd.getNgayLap().getMonth() == homNay.getMonth();
                giaiDoanTieuDe = "trong tháng";
                break;
            case "năm":
                boLoc = hd -> hd.getNgayLap().getYear() == homNay.getYear();
                giaiDoanTieuDe = "trong năm";
                break;
            default:
                return new Label("Loại thống kê không hợp lệ");
        }

        Map<MonAn, Integer> soLuongMonAn = tatCaHoaDon.stream()
                .filter(boLoc)
                .flatMap(hd -> hd.getChiTietHoaDon().stream())
                .collect(Collectors.groupingBy(
                        ChiTietHoaDon::getMonAn,
                        Collectors.summingInt(ChiTietHoaDon::getSoLuong)
                ));

        List<PieChart.Data> top5MonAn = soLuongMonAn.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> new PieChart.Data(entry.getKey().getTenMon(), entry.getValue()))
                .collect(Collectors.toList());

        PieChart bieuDo = new PieChart(FXCollections.observableArrayList(top5MonAn));
        bieuDo.setTitle("Top 5 món bán chạy " + giaiDoanTieuDe);

        if (top5MonAn.isEmpty()) {
            return new Label("Không có dữ liệu món ăn " + giaiDoanTieuDe);
        }

        double tong = top5MonAn.stream().mapToDouble(PieChart.Data::getPieValue).sum();
        Label nhanTong = new Label("Tổng\n" + String.format("%.0f", tong));
        nhanTong.setTextAlignment(TextAlignment.CENTER);
        nhanTong.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Circle loDonut = new Circle(80, Color.WHITE);
        return new StackPane(bieuDo, loDonut, nhanTong);
    }
}
