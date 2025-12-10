package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThongKeDAO {

    public Map<String, Double> getDoanhThuTheoNgay(LocalDate startDate, LocalDate endDate, String tenKhuyenMai) {
        Map<String, Double> data = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder(
            "SELECT CONVERT(date, HD.ngayLap) as Ngay, SUM(CT.soLuong * CT.donGia) as DoanhThu " +
            "FROM HoaDon HD " +
            "JOIN ChiTietHD CT ON HD.maHD = CT.maHD "
        );

        if (tenKhuyenMai != null && !tenKhuyenMai.equals("Tất cả Khuyến Mãi")) {
            sql.append("JOIN KhuyenMai KM ON HD.maKM = KM.maKM ");
        }

        sql.append("WHERE HD.ngayLap >= ? AND HD.ngayLap < ? ");

        if (tenKhuyenMai != null && !tenKhuyenMai.equals("Tất cả Khuyến Mãi")) {
            sql.append("AND KM.tenKM = ? ");
        }

        sql.append("GROUP BY CONVERT(date, HD.ngayLap) ORDER BY Ngay");

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            if (tenKhuyenMai != null && !tenKhuyenMai.equals("Tất cả Khuyến Mãi")) {
                ps.setString(3, tenKhuyenMai);
            }
            
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

    public Map<String, Integer> getThongKeMonAn(LocalDate startDate, LocalDate endDate, String tenLoaiMon) {
        Map<String, Integer> data = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder(
            "SELECT M.tenMon, SUM(CT.soLuong) as TongSoLuong " +
            "FROM ChiTietHD CT " +
            "JOIN MonAn M ON CT.maMonAn = M.maMonAn " +
            "JOIN HoaDon HD ON CT.maHD = HD.maHD " +
            "JOIN LoaiMonAn LM ON M.maLoaiMon = LM.maLoaiMon " +
            "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? "
        );

        if (tenLoaiMon != null && !tenLoaiMon.equals("Tất cả Món Ăn")) {
            sql.append("AND LM.tenLoaiMon = ? ");
        }

        sql.append("GROUP BY M.tenMon ORDER BY TongSoLuong DESC");

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            if (tenLoaiMon != null && !tenLoaiMon.equals("Tất cả Món Ăn")) {
                ps.setString(3, tenLoaiMon);
            }

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

    public Map<String, Double> getThongKeBan(LocalDate startDate, LocalDate endDate, String tenLoaiBan) {
        Map<String, Double> data = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder(
            "SELECT B.tenBan, SUM(CTHD.soLuong * CTHD.donGia) as TongDoanhThu " +
            "FROM HoaDon HD " +
            "JOIN ChiTietHD CTHD ON HD.maHD = CTHD.maHD " +
            "JOIN PhieuDatBan PDB ON HD.maPDB = PDB.maPDB " +
            "JOIN PhieuDatBan_Ban PDBB ON PDB.maPDB = PDBB.maPDB " +
            "JOIN Ban B ON PDBB.maBan = B.maBan " +
            "JOIN LoaiBan LB ON B.maLoaiBan = LB.maLoaiBan " +
            "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? "
        );

        if (tenLoaiBan != null && !tenLoaiBan.equals("Tất cả Bàn")) {
            sql.append("AND LB.tenLoaiBan = ? ");
        }
        
        sql.append("GROUP BY B.tenBan ORDER BY TongDoanhThu DESC");
        
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            if (tenLoaiBan != null && !tenLoaiBan.equals("Tất cả Bàn")) {
                ps.setString(3, tenLoaiBan);
            }

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

    public BigDecimal getInvoiceStat(LocalDate startDate, LocalDate endDate, String statType) {
        String orderBy = "ASC";
        if ("MAX".equalsIgnoreCase(statType)) {
            orderBy = "DESC";
        }

        String sql = String.format(
            "SELECT TOP 1 SUM(CT.soLuong * CT.donGia) AS DoanhThu " +
            "FROM HoaDon HD " +
            "JOIN ChiTietHD CT ON HD.maHD = CT.maHD " +
            "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? " +
            "GROUP BY HD.maHD " +
            "ORDER BY DoanhThu %s", orderBy
        );

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("DoanhThu");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
}
