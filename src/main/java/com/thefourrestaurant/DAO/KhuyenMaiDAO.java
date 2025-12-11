package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    private KhuyenMai anhXaResultSetVaoKhuyenMai(ResultSet rs) throws SQLException {
        KhuyenMai km = new KhuyenMai();
        km.setMaKM(rs.getString("maKM"));
        km.setTenKM(rs.getString("tenKM"));
        km.setKieuKM(rs.getString("kieuKM"));
        km.setMaCode(rs.getString("maCode"));
        int soLuot = rs.getInt("soLuotSuDung");
        km.setSoLuotSuDung(rs.wasNull() ? null : soLuot);
        km.setTyLe(rs.getBigDecimal("tyLe"));
        km.setSoTien(rs.getBigDecimal("soTien"));
        Timestamp ngayBDTimestamp = rs.getTimestamp("ngayBatDau");
        if (ngayBDTimestamp != null) km.setNgayBatDau(ngayBDTimestamp.toLocalDateTime());
        Timestamp ngayKTTimestamp = rs.getTimestamp("ngayKetThuc");
        if (ngayKTTimestamp != null) km.setNgayKetThuc(ngayKTTimestamp.toLocalDateTime());
        km.setMoTa(rs.getString("moTa"));

        if (rs.getString("maLoaiKM") != null) {
            LoaiKhuyenMai lkm = new LoaiKhuyenMai();
            lkm.setMaLoaiKM(rs.getString("maLoaiKM"));
            lkm.setTenLoaiKM(rs.getString("tenLoaiKM"));
            km.setLoaiKhuyenMai(lkm);
        }

        return km;
    }

    private String layCauTruyVanCoBan() {
        return "SELECT km.*, lkm.tenLoaiKM " +
               "FROM KhuyenMai km " +
               "LEFT JOIN LoaiKhuyenMai lkm ON km.maLoaiKM = lkm.maLoaiKM ";
    }

    public List<KhuyenMai> layDanhSachKhuyenMai() {
        List<KhuyenMai> danhSach = new ArrayList<>();
        String sql = layCauTruyVanCoBan() + "ORDER BY km.maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(anhXaResultSetVaoKhuyenMai(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<KhuyenMai> layDanhSachKhuyenMaiTheoKieu(String kieuKM) {
        List<KhuyenMai> danhSach = new ArrayList<>();
        String sql = layCauTruyVanCoBan() + "WHERE km.kieuKM = ? ORDER BY km.maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kieuKM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(anhXaResultSetVaoKhuyenMai(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
        String sql = layCauTruyVanCoBan() + " WHERE km.maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return anhXaResultSetVaoKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String taoMaKhuyenMaiMoi() {
        String newId = "KM000001";
        String sql = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maKM");
                int num = Integer.parseInt(lastId.substring(2));
                num++;
                newId = String.format("KM%06d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (maKM, tenKM, maLoaiKM, kieuKM, maCode, soLuotSuDung, tyLe, soTien, ngayBatDau, ngayKetThuc, moTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setString(3, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setString(4, km.getKieuKM());
            ps.setString(5, km.getMaCode());
            if (km.getSoLuotSuDung() != null) {
                ps.setInt(6, km.getSoLuotSuDung());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setBigDecimal(7, km.getTyLe());
            ps.setBigDecimal(8, km.getSoTien());
            ps.setObject(9, km.getNgayBatDau());
            ps.setObject(10, km.getNgayKetThuc());
            ps.setString(11, km.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKM = ?, maLoaiKM = ?, kieuKM = ?, maCode = ?, soLuotSuDung = ?, tyLe = ?, soTien = ?, " +
                     "ngayBatDau = ?, ngayKetThuc = ?, moTa = ? WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getTenKM());
            ps.setString(2, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setString(3, km.getKieuKM());
            ps.setString(4, km.getMaCode());
            if (km.getSoLuotSuDung() != null) {
                ps.setInt(5, km.getSoLuotSuDung());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setBigDecimal(6, km.getTyLe());
            ps.setBigDecimal(7, km.getSoTien());
            ps.setObject(8, km.getNgayBatDau());
            ps.setObject(9, km.getNgayKetThuc());
            ps.setString(10, km.getMoTa());
            ps.setString(11, km.getMaKM());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhuyenMai(String maKM) {
        String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhuyenMai timKhuyenMaiTheoMaHoacTen(String input) {
        String sql = layCauTruyVanCoBan() +
                " WHERE (km.maKM = ? OR km.tenKM = ?) " +
                " AND GETDATE() BETWEEN km.ngayBatDau AND km.ngayKetThuc";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, input);
            ps.setString(2, input);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return anhXaResultSetVaoKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KhuyenMai timKhuyenMaiTheoMaCode(String maCode) {
        String sql = layCauTruyVanCoBan() +
                " WHERE km.maCode = ? AND km.kieuKM = ? " +
                " AND GETDATE() BETWEEN km.ngayBatDau AND km.ngayKetThuc" +
                " AND (km.soLuotSuDung IS NULL OR km.soLuotSuDung > 0)";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maCode);
            ps.setString(2, KhuyenMai.KIEU_MA_GIAM_GIA);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return anhXaResultSetVaoKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<KhuyenMai> layDanhSachKhuyenMaiSuKienHieuLuc() {
        List<KhuyenMai> danhSach = new ArrayList<>();
        String sql = layCauTruyVanCoBan() +
                "WHERE km.kieuKM = ? " +
                "AND GETDATE() BETWEEN km.ngayBatDau AND km.ngayKetThuc " +
                "ORDER BY km.maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, KhuyenMai.KIEU_SU_KIEN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(anhXaResultSetVaoKhuyenMai(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public boolean giamSoLuotSuDung(String maKM) {
        String sql = "UPDATE KhuyenMai SET soLuotSuDung = soLuotSuDung - 1 " +
                     "WHERE maKM = ? AND soLuotSuDung IS NOT NULL AND soLuotSuDung > 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}