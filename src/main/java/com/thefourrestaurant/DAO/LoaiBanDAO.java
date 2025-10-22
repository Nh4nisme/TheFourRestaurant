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
        String sql = "SELECT maLoaiBan, tenLoaiBan FROM LoaiBan ORDER BY maLoaiBan";

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                LoaiBan lb = new LoaiBan(
                        rs.getString("maLoaiBan"),
                        rs.getString("tenLoaiBan")
                );
                dsLoaiBan.add(lb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsLoaiBan;
    }

    // 🔹 Lấy loại bàn theo mã
    public LoaiBan layTheoMa(String maLoaiBan) {
        String sql = "SELECT maLoaiBan, tenLoaiBan FROM LoaiBan WHERE maLoaiBan = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiBan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new LoaiBan(
                        rs.getString("maLoaiBan"),
                        rs.getString("tenLoaiBan")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
