package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMai_DieuKienDAO {

    public List<KhuyenMai_DieuKien> layDieuKienTheoMaKM(String maKM) {
        List<KhuyenMai_DieuKien> dsDieuKien = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai_DieuKien WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhuyenMai_DieuKien dk = new KhuyenMai_DieuKien();
                dk.setMaDieuKien(rs.getString("maDieuKien"));
                dk.setLoaiApDung(rs.getString("loaiApDung"));
                dk.setTyLeGiam(rs.getBigDecimal("tyLeGiam"));
                dk.setSoTienGiam(rs.getBigDecimal("soTienGiam"));
                dk.setSoLuongTang(rs.getObject("soLuongTang", Integer.class));
                dk.setMoTaDieuKien(rs.getString("moTaDieuKien"));
                dsDieuKien.add(dk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDieuKien;
    }

    public String taoMaDieuKienMoi() {
        String newId = "DK000001";
        String sql = "SELECT TOP 1 maDieuKien FROM KhuyenMai_DieuKien ORDER BY maDieuKien DESC";
        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("maDieuKien");
                int num = Integer.parseInt(lastId.substring(2));
                num++;
                newId = String.format("DK%06d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean themDieuKien(KhuyenMai_DieuKien dieuKien) throws SQLException {
        String sqlDieuKien = "INSERT INTO KhuyenMai_DieuKien (maDieuKien, maKM, loaiApDung, tyLeGiam, soTienGiam, soLuongTang, moTaDieuKien) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectSQL.getConnection();
            conn.setAutoCommit(false);

            // Insert into KhuyenMai_DieuKien
            try (PreparedStatement ps = conn.prepareStatement(sqlDieuKien)) {
                ps.setString(1, dieuKien.getMaDieuKien());
                ps.setString(2, dieuKien.getKhuyenMai().getMaKM());
                ps.setString(3, dieuKien.getLoaiApDung());
                ps.setBigDecimal(4, dieuKien.getTyLeGiam());
                ps.setBigDecimal(5, dieuKien.getSoTienGiam());
                ps.setObject(6, dieuKien.getSoLuongTang());
                ps.setString(7, dieuKien.getMoTaDieuKien());
                ps.executeUpdate();
            }

            // Insert into DieuKien_Mon
            new DieuKien_MonDAO().themMonDieuKien(conn, dieuKien.getMaDieuKien(), dieuKien.getDanhSachMonDieuKien());

            // Insert into DieuKien_MonTang
            new DieuKien_MonTangDAO().themMonTang(conn, dieuKien.getMaDieuKien(), dieuKien.getDanhSachMonTang());

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public boolean xoaDieuKien(String maDieuKien) throws SQLException {
        String sql = "DELETE FROM KhuyenMai_DieuKien WHERE maDieuKien = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDieuKien);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean capNhatDieuKien(KhuyenMai_DieuKien dieuKien) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectSQL.getConnection();
            conn.setAutoCommit(false);

            // 1. Delete old related data
            new DieuKien_MonDAO().xoaMonTheoMaDieuKien(conn, dieuKien.getMaDieuKien());
            new DieuKien_MonTangDAO().xoaMonTangTheoMaDieuKien(conn, dieuKien.getMaDieuKien());

            // 2. Update main condition table
            String sqlDieuKien = "UPDATE KhuyenMai_DieuKien SET loaiApDung=?, tyLeGiam=?, soTienGiam=?, soLuongTang=?, moTaDieuKien=? WHERE maDieuKien=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDieuKien)) {
                ps.setString(1, dieuKien.getLoaiApDung());
                ps.setBigDecimal(2, dieuKien.getTyLeGiam());
                ps.setBigDecimal(3, dieuKien.getSoTienGiam());
                ps.setObject(4, dieuKien.getSoLuongTang());
                ps.setString(5, dieuKien.getMoTaDieuKien());
                ps.setString(6, dieuKien.getMaDieuKien());
                ps.executeUpdate();
            }

            // 3. Insert new related data
            new DieuKien_MonDAO().themMonDieuKien(conn, dieuKien.getMaDieuKien(), dieuKien.getDanhSachMonDieuKien());
            new DieuKien_MonTangDAO().themMonTang(conn, dieuKien.getMaDieuKien(), dieuKien.getDanhSachMonTang());

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
