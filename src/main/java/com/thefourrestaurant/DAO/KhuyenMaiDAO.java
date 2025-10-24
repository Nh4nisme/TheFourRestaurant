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
        String sql = "INSERT INTO KhuyenMai (maKM, maLoaiKM, tyLe, soTien, ngayBatDau, ngayKetThuc, moTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setBigDecimal(3, km.getTyLe());
            ps.setBigDecimal(4, km.getSoTien());
            ps.setObject(5, km.getNgayBatDau());
            ps.setObject(6, km.getNgayKetThuc());
            ps.setString(7, km.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET maLoaiKM = ?, tyLe = ?, soTien = ?, " +
                     "ngayBatDau = ?, ngayKetThuc = ?, moTa = ? WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setBigDecimal(2, km.getTyLe());
            ps.setBigDecimal(3, km.getSoTien());
            ps.setObject(4, km.getNgayBatDau());
            ps.setObject(5, km.getNgayKetThuc());
            ps.setString(6, km.getMoTa());
            ps.setString(7, km.getMaKM());

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
}
