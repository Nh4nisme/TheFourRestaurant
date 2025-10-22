package com.thefourrestaurant.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.Tang;

public class TangDAO {

    // 🔹 Lấy tất cả tầng chưa bị xóa
    public List<Tang> layTatCaTang() {
        List<Tang> dsTang = new ArrayList<>();
        String sql = "SELECT maTang, tenTang, moTa, isDeleted FROM Tang WHERE isDeleted = 0 ORDER BY maTang";

        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tang tang = new Tang(
                        rs.getString("maTang"),
                        rs.getString("tenTang"),
                        rs.getString("moTa"),
                        rs.getBoolean("isDeleted")
                );
                dsTang.add(tang);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsTang;
    }

    // 🔹 Tìm tầng theo mã
    public Tang layTangTheoMa(String maTang) {
        String sql = "SELECT maTang, tenTang, moTa, isDeleted FROM Tang WHERE maTang = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTang);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Tang(
                        rs.getString("maTang"),
                        rs.getString("tenTang"),
                        rs.getString("moTa"),
                        rs.getBoolean("isDeleted")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm tầng mới
    public boolean themTang(Tang tang) {
        String sql = "INSERT INTO Tang (maTang, tenTang, moTa, isDeleted) VALUES (?, ?, ?, 0)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tang.getMaTang());
            ps.setString(2, tang.getTenTang());
            ps.setString(3, tang.getMoTa());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật thông tin tầng
    public boolean capNhatTang(Tang tang) {
        String sql = "UPDATE Tang SET tenTang = ?, moTa = ? WHERE maTang = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tang.getTenTang());
            ps.setString(2, tang.getMoTa());
            ps.setString(3, tang.getMaTang());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xóa mềm tầng
    public boolean xoaTang(String maTang) {
        String sql = "UPDATE Tang SET isDeleted = 1 WHERE maTang = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTang);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
