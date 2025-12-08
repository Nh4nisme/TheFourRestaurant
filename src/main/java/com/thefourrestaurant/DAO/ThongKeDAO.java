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
     */
    public Map<String, Double> getDoanhThuTheoNgay(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> data = new LinkedHashMap<>();
        // Sửa lại truy vấn để tính tổng tiền từ ChiTietHD
        String sql = "SELECT CONVERT(date, HD.ngayLap) as Ngay, SUM(CT.soLuong * CT.donGia) as DoanhThu " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHD CT ON HD.maHD = CT.maHD " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? " +
                     "GROUP BY CONVERT(date, HD.ngayLap) " +
                     "ORDER BY Ngay";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            // Sử dụng < endDate.plusDays(1) để bao gồm cả ngày kết thúc
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("Ngay"), rs.getDouble("DoanhThu"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Lấy dữ liệu thống kê số lượng các món ăn đã bán.
     */
    public Map<String, Integer> getThongKeMonAn(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> data = new LinkedHashMap<>();
        // Sửa lại tên bảng ChiTietHoaDon -> ChiTietHD
        String sql = "SELECT M.tenMon, SUM(CT.soLuong) as TongSoLuong " +
                     "FROM ChiTietHD CT " +
                     "JOIN MonAn M ON CT.maMonAn = M.maMonAn " +
                     "JOIN HoaDon HD ON CT.maHD = HD.maHD " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? " +
                     "GROUP BY M.tenMon " +
                     "ORDER BY TongSoLuong DESC";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("tenMon"), rs.getInt("TongSoLuong"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Lấy dữ liệu thống kê doanh thu theo từng bàn.
     */
    public Map<String, Double> getThongKeBan(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> data = new LinkedHashMap<>();
        // Sửa lại truy vấn phức tạp để join qua nhiều bảng
        String sql = "SELECT B.tenBan, SUM(CTHD.soLuong * CTHD.donGia) as TongDoanhThu " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHD CTHD ON HD.maHD = CTHD.maHD " +
                     "JOIN PhieuDatBan PDB ON HD.maPDB = PDB.maPDB " +
                     "JOIN PhieuDatBan_Ban PDBB ON PDB.maPDB = PDBB.maPDB " +
                     "JOIN Ban B ON PDBB.maBan = B.maBan " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? " +
                     "GROUP BY B.tenBan " +
                     "ORDER BY TongDoanhThu DESC";
        
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("tenBan"), rs.getDouble("TongDoanhThu"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
