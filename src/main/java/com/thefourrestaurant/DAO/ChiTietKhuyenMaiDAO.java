package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietKhuyenMaiDAO {

    public boolean themChiTiet(ChiTietKhuyenMai ct) {
        String sql = "INSERT INTO ChiTietKhuyenMai (maCTKM, maKM, maMonApDung, maMonTang, tyLeGiam, soTienGiam, soLuongTang) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaCTKM());
            ps.setString(2, ct.getKhuyenMai().getMaKM());
            ps.setString(3, ct.getMonApDung().getMaMonAn());
            ps.setString(4, ct.getMonTang() != null ? ct.getMonTang().getMaMonAn() : null);
            ps.setBigDecimal(5, ct.getTyLeGiam());
            ps.setBigDecimal(6, ct.getSoTienGiam());
            ps.setInt(7, ct.getSoLuongTang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<ChiTietKhuyenMai> layTheoMaKM(String maKM) {
        List<ChiTietKhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietKhuyenMai WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietKhuyenMai ct = new ChiTietKhuyenMai();
                    ct.setMaCTKM(rs.getString("maCTKM"));
                    ct.setTyLeGiam(rs.getBigDecimal("tyLeGiam"));
                    ct.setSoTienGiam(rs.getBigDecimal("soTienGiam"));
                    ct.setSoLuongTang(rs.getInt("soLuongTang"));
                    // Có thể set thêm MonAn sau này nếu cần
                    ds.add(ct);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public boolean capNhatChiTiet(ChiTietKhuyenMai ct) {
        String sql = "UPDATE ChiTietKhuyenMai SET maMonApDung=?, maMonTang=?, tyLeGiam=?, soTienGiam=?, soLuongTang=? WHERE maCTKM=?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMonApDung().getMaMonAn());
            ps.setString(2, ct.getMonTang() != null ? ct.getMonTang().getMaMonAn() : null);
            ps.setBigDecimal(3, ct.getTyLeGiam());
            ps.setBigDecimal(4, ct.getSoTienGiam());
            ps.setInt(5, ct.getSoLuongTang());
            ps.setString(6, ct.getMaCTKM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean xoaChiTiet(String maCTKM) {
        String sql = "DELETE FROM ChiTietKhuyenMai WHERE maCTKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCTKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean xoaTheoMaKM(String maKM) {
        String sql = "DELETE FROM ChiTietKhuyenMai WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
