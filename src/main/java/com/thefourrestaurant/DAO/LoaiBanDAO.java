package com.thefourrestaurant.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiBan;

public class LoaiBanDAO {

    // 🔹 Lấy tất cả loại bàn
    public List<LoaiBan> layTatCa() {
        List<LoaiBan> dsLoaiBan = new ArrayList<>();
        String sql = "SELECT * FROM LoaiBan ORDER BY maLoaiBan";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsLoaiBan.add(mapLoaiBan(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsLoaiBan;
    }

    // 🔹 Lấy loại bàn theo mã
    public LoaiBan layTheoMa(String maLoaiBan) {
        String sql = "SELECT * FROM LoaiBan WHERE maLoaiBan = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapLoaiBan(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Lấy tên loại bàn theo mã bàn
    public String layTenLoaiTheoBan(String maBan) {
        String sql = """
            SELECT lb.tenLoaiBan
            FROM Ban b
            JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
            WHERE b.maBan = ?
        """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tenLoaiBan");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Lấy loại bàn theo tên (VIP / Bàn thường)
    public List<LoaiBan> layLoaiBanTheoTen(String loai) {
        List<LoaiBan> ds = new ArrayList<>();

        String sql = "SELECT * FROM LoaiBan";
        String likeValue = null;

        if ("VIP".equalsIgnoreCase(loai)) {
            sql += " WHERE tenLoaiBan LIKE ?";
            likeValue = "%VIP%";
        } else if ("THUONG".equalsIgnoreCase(loai)) {
            sql += " WHERE tenLoaiBan LIKE ?";
            likeValue = "%Bàn thường%";
        }

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (likeValue != null) {
                ps.setString(1, likeValue);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapLoaiBan(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // 🔹 Hàm map ResultSet → LoaiBan (tái sử dụng)
    private LoaiBan mapLoaiBan(ResultSet rs) throws SQLException {
        return new LoaiBan(
                rs.getString("maLoaiBan"),
                rs.getString("tenLoaiBan"),
                rs.getBigDecimal("giaTien"),
                rs.getInt("soChoNgoi"),
                rs.getString("moTa")
        );
    }
}
