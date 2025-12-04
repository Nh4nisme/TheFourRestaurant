package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThongKeDAO {

    /**
     * Lấy dữ liệu doanh thu theo từng ngày trong một khoảng thời gian.
     * @return Map với Key là ngày (String) và Value là tổng doanh thu (Double).
     */
    public Map<String, Double> getDoanhThuTheoNgay(LocalDate startDate, LocalDate endDate) {
        // Dữ liệu mẫu
        Map<String, Double> data = new LinkedHashMap<>();
        data.put("2023-10-26", 1200000.0);
        data.put("2023-10-27", 1550000.0);
        data.put("2023-10-28", 2100000.0);
        data.put("2023-10-29", 1800000.0);
        return data;
    }
    public Map<String, Integer> getThongKeMonAn(LocalDate startDate, LocalDate endDate) {
        // Dữ liệu mẫu
        Map<String, Integer> data = new LinkedHashMap<>();
        data.put("Phở Bò", 50);
        data.put("Bún Chả", 45);
        data.put("Cơm Rang", 65);
        data.put("Nem Rán", 80);
        data.put("Gà Nướng", 30);
        return data;
    }
    public Map<String, Double> getThongKeBan(LocalDate startDate, LocalDate endDate) {
        // Dữ liệu mẫu
        Map<String, Double> data = new LinkedHashMap<>();
        data.put("Bàn 1", 2500000.0);
        data.put("Bàn 2", 3100000.0);
        data.put("Bàn 5", 1800000.0);
        data.put("Bàn 8", 4200000.0);
        return data;
    }
}
