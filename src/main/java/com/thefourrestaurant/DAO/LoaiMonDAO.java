package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.view.loaimonan.LoaiMonAn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiMonDAO {

    public List<LoaiMon> layTatCaLoaiMon() {
        List<LoaiMon> ds = new ArrayList<>();
        String sql = "SELECT * FROM LoaiMonAn";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new LoaiMon(
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

    public LoaiMon layLoaiMonTheoMa(String maLoaiMon) {
        LoaiMon loaiMon = null;
        String sql = "SELECT * FROM LoaiMonAn WHERE maLoaiMon = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiMon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    loaiMon = new LoaiMon(
                            rs.getString("maLoaiMon"),
                            rs.getString("tenLoaiMon"),
                            rs.getString("hinhAnh")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loaiMon;
    }

    public boolean themLoaiMon(LoaiMon loai) {
        String sql = "INSERT INTO LoaiMonAn (maLoaiMon, tenLoaiMon, hinhAnh) VALUES (?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loai.getMaLoaiMon());
            ps.setString(2, loai.getTenLoaiMon());
            ps.setString(3, loai.getHinhAnh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean capNhatLoaiMon(LoaiMon loai) {
        String sql = "UPDATE LoaiMonAn SET tenLoaiMon=?, hinhAnh=? WHERE maLoaiMon=?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loai.getTenLoaiMon());
            ps.setString(2, loai.getHinhAnh());
            ps.setString(3, loai.getMaLoaiMon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean xoaLoaiMon(String maLoaiMon) {
        String sql = "DELETE FROM LoaiMonAn WHERE maLoaiMon = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoaiMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public String taoMaLoaiMonMoi() {
        String sql = "SELECT COUNT(*) FROM LoaiMonAn";
        int count = 0;
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "LM" + String.format("%03d", count + 1);
    }
}
