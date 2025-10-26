package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.model.Tang;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ThongKeBan {

    private static final PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();

    public static Node taoBieuDo(String loai) {
        List<PhieuDatBan> tatCaPhieu = phieuDatBanDAO.layTatCaPhieu();
        LocalDate homNay = LocalDate.now();

        Predicate<PhieuDatBan> boLoc;
        String giaiDoanTieuDe;

        switch (loai) {
            case "ngày":
                boLoc = pdb -> pdb.getNgayDat() != null && pdb.getNgayDat().isEqual(homNay);
                giaiDoanTieuDe = "trong ngày";
                break;
            case "tháng":
                boLoc = pdb -> pdb.getNgayDat().getYear() == homNay.getYear() && pdb.getNgayDat().getMonth() == homNay.getMonth();
                giaiDoanTieuDe = "trong tháng";
                break;
            case "năm":
                boLoc = pdb -> pdb.getNgayDat().getYear() == homNay.getYear();
                giaiDoanTieuDe = "trong năm";
                break;
            default:
                return new Label("Loại thống kê không hợp lệ");
        }

        // Thống kê số lần bàn được đặt ở mỗi tầng
        Map<String, Long> luotSuDungTheoTang = tatCaPhieu.stream()
                .filter(boLoc) // Lọc phiếu theo ngày/tháng/năm
                .map(PhieuDatBan::getBan) // Lấy ra đối tượng Bàn từ PhieuDatBan
                .filter(Objects::nonNull)
                .map(Ban::getTang) // Lấy ra đối tượng Tầng từ Bàn
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Tang::getTenTang, // Nhóm theo tên tầng
                        Collectors.counting() // Đếm số lần xuất hiện
                ));

        if (luotSuDungTheoTang.isEmpty()) {
            return new Label("Không có dữ liệu đặt bàn " + giaiDoanTieuDe);
        }

        // Tạo biểu đồ thanh ngang
        CategoryAxis yAxis = new CategoryAxis(); // Trục Y là tên tầng
        yAxis.setLabel("Tầng");
        NumberAxis xAxis = new NumberAxis(); // Trục X là số lần
        xAxis.setLabel("Số lần được đặt");

        BarChart<Number, String> bieuDo = new BarChart<>(xAxis, yAxis);
        bieuDo.setTitle("Mức độ phổ biến của các tầng " + giaiDoanTieuDe);
        bieuDo.setLegendVisible(false);

        XYChart.Series<Number, String> series = new XYChart.Series<>();

        // Thêm dữ liệu vào biểu đồ
        for (Map.Entry<String, Long> entry : luotSuDungTheoTang.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getValue(), entry.getKey()));
        }

        bieuDo.getData().add(series);
        return bieuDo;
    }
}
