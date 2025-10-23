package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiMonAn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiMonAnDAO {

    public List<LoaiMonAn> layTatCaLoaiMonAn() {
        List<LoaiMonAn> ds = new ArrayList<>();
        String sql = "SELECT * FROM LoaiMonAn";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new LoaiMonAn(
                        rs.getString("maLoaiMon"),
                        rs.getString("tenLoaiMon"),
                        rs.getString("hinhAnh")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean themLoaiMonAn(LoaiMonAn loai) {
        String sql = "INSERT INTO LoaiMonAn (maLoaiMon, tenLoaiMon, hinhAnh) VALUES (?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loai.getMaLoaiMon());
            ps.setString(2, loai.getTenLoaiMon());
            ps.setString(3, loai.getHinhAnh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean capNhatLoaiMonAn(LoaiMonAn loai) {
        String sql = "UPDATE LoaiMonAn SET tenLoaiMon=?, hinhAnh=? WHERE maLoaiMon=?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loai.getTenLoaiMon());
            ps.setString(2, loai.getHinhAnh());
            ps.setString(3, loai.getMaLoaiMon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean xoaLoaiMonAn(String maLoaiMon) {
        String sql = "DELETE FROM LoaiMonAn WHERE maLoaiMon = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoaiMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
