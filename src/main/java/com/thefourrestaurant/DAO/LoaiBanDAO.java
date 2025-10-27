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
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                LoaiBan lb = new LoaiBan(
                        rs.getString("maLoaiBan"),
                        rs.getString("tenLoaiBan"),
                        rs.getBigDecimal("giaTien")
                );
                dsLoaiBan.add(lb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsLoaiBan;
    }

    // Lấy loại bàn theo mã
    public LoaiBan layTheoMa(String maLoaiBan) {
    	String sql = "SELECT maLoaiBan, tenLoaiBan, giaTien FROM LoaiBan WHERE maLoaiBan = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiBan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new LoaiBan(
                        rs.getString("maLoaiBan"),
                        rs.getString("tenLoaiBan"),
                        rs.getBigDecimal("giaTien")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String layTenLoaiTheoBan(String maBan) {
        String sql = "SELECT lb.tenLoaiBan FROM Ban b JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan WHERE b.maBan = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
