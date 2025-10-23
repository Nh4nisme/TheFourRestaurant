package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiKhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiKhuyenMaiDAO {

    public List<LoaiKhuyenMai> layTatCaLoaiKhuyenMai() {
        List<LoaiKhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM LoaiKhuyenMai";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(new LoaiKhuyenMai(
                        rs.getString("maLoaiKM"),
                        rs.getString("tenLoaiKM")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean themLoaiKhuyenMai(LoaiKhuyenMai lkm) {
        String sql = "INSERT INTO LoaiKhuyenMai (maLoaiKM, tenLoaiKM) VALUES (?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lkm.getMaLoaiKM());
            ps.setString(2, lkm.getTenLoaiKM());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatLoaiKhuyenMai(LoaiKhuyenMai lkm) {
        String sql = "UPDATE LoaiKhuyenMai SET tenLoaiKM=? WHERE maLoaiKM=?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lkm.getTenLoaiKM());
            ps.setString(2, lkm.getMaLoaiKM());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaLoaiKhuyenMai(String maLoaiKM) {
        String sql = "DELETE FROM LoaiKhuyenMai WHERE maLoaiKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiKM);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
