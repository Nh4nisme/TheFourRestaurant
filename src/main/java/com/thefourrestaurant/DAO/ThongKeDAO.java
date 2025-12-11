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

    public BigDecimal getTongDoanhThu(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COALESCE(SUM(CT.soLuong * CT.donGia), 0) as TongDoanhThu " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHD CT ON HD.maHD = CT.maHD " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("TongDoanhThu");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public int getSoHoaDon(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) as SoHD FROM HoaDon " +
                     "WHERE ngayLap >= ? AND ngayLap < ? AND isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SoHD");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSoKhachHangMoi(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(DISTINCT HD.maKH) as SoKH " +
                     "FROM HoaDon HD " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0 " +
                     "AND HD.maKH IS NOT NULL " +
                     "AND NOT EXISTS (SELECT 1 FROM HoaDon HD2 WHERE HD2.maKH = HD.maKH AND HD2.ngayLap < ? AND HD2.isDeleted = 0)";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            ps.setDate(3, java.sql.Date.valueOf(startDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SoKH");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSoMonAnBanRa(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COALESCE(SUM(CT.soLuong), 0) as SoMon " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHD CT ON HD.maHD = CT.maHD " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SoMon");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public BigDecimal getDoanhThuTrungBinhHD(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COALESCE(AVG(TongHD), 0) as TrungBinh FROM (" +
                     "SELECT SUM(CT.soLuong * CT.donGia) as TongHD " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHD CT ON HD.maHD = CT.maHD " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0 " +
                     "GROUP BY HD.maHD) as T";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("TrungBinh");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public Map<String, Integer> getThongKeKhachHangTheoLoai(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT COALESCE(LKH.tenLoaiKH, N'Khách vãng lai') as LoaiKH, COUNT(DISTINCT HD.maKH) as SoKH " +
                     "FROM HoaDon HD " +
                     "LEFT JOIN KhachHang KH ON HD.maKH = KH.maKH " +
                     "LEFT JOIN LoaiKhachHang LKH ON KH.maLoaiKH = LKH.maLoaiKH " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0 " +
                     "GROUP BY LKH.tenLoaiKH ORDER BY SoKH DESC";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("LoaiKH"), rs.getInt("SoKH"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<String, Double> getDoanhThuTheoKhachHang(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT TOP 10 COALESCE(KH.hoTen, N'Khách vãng lai') as TenKH, " +
                     "SUM(CT.soLuong * CT.donGia) as DoanhThu " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHD CT ON HD.maHD = CT.maHD " +
                     "LEFT JOIN KhachHang KH ON HD.maKH = KH.maKH " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0 " +
                     "GROUP BY HD.maKH, KH.hoTen " +
                     "ORDER BY DoanhThu DESC";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("TenKH"), rs.getDouble("DoanhThu"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<String, Integer> getTanSuatKhachHang(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT TOP 10 KH.hoTen, COUNT(HD.maHD) as SoLanDen " +
                     "FROM HoaDon HD " +
                     "JOIN KhachHang KH ON HD.maKH = KH.maKH " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0 " +
                     "GROUP BY HD.maKH, KH.hoTen " +
                     "ORDER BY SoLanDen DESC";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("hoTen"), rs.getInt("SoLanDen"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<String, Integer> getThongKeNhanVienTheoSoHD(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT NV.hoTen, COUNT(HD.maHD) as SoHD " +
                     "FROM HoaDon HD " +
                     "JOIN NhanVien NV ON HD.maNV = NV.maNV " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0 " +
                     "GROUP BY HD.maNV, NV.hoTen " +
                     "ORDER BY SoHD DESC";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("hoTen"), rs.getInt("SoHD"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<String, Double> getDoanhThuTheoNhanVien(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT NV.hoTen, SUM(CT.soLuong * CT.donGia) as DoanhThu " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHD CT ON HD.maHD = CT.maHD " +
                     "JOIN NhanVien NV ON HD.maNV = NV.maNV " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0 " +
                     "GROUP BY HD.maNV, NV.hoTen " +
                     "ORDER BY DoanhThu DESC";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("hoTen"), rs.getDouble("DoanhThu"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<String, Double> getDoanhThuTheoThang(int year) {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT MONTH(HD.ngayLap) as Thang, SUM(CT.soLuong * CT.donGia) as DoanhThu " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHD CT ON HD.maHD = CT.maHD " +
                     "WHERE YEAR(HD.ngayLap) = ? AND HD.isDeleted = 0 " +
                     "GROUP BY MONTH(HD.ngayLap) ORDER BY Thang";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put("Tháng " + rs.getInt("Thang"), rs.getDouble("DoanhThu"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<String, Integer> getThongKeGioPhucVu(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT DATEPART(HOUR, ngayLap) as Gio, COUNT(*) as SoHD " +
                     "FROM HoaDon " +
                     "WHERE ngayLap >= ? AND ngayLap < ? AND isDeleted = 0 " +
                     "GROUP BY DATEPART(HOUR, ngayLap) ORDER BY Gio";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getInt("Gio") + "h", rs.getInt("SoHD"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getTongKhachHang(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(DISTINCT HD.maKH) as SoKH " +
                     "FROM HoaDon HD " +
                     "WHERE HD.ngayLap >= ? AND HD.ngayLap < ? AND HD.isDeleted = 0 AND HD.maKH IS NOT NULL";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate.plusDays(1)));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SoKH");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}