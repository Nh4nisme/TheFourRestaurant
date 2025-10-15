package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.Ban;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BanDAO {

    // 🔹 Lấy tất cả bàn
    public List<Ban> getAll() {
        List<Ban> ds = new ArrayList<>();
        String sql = "SELECT maBan, tenBan, trangThai, toaDoX, toaDoY, maTang, maLoaiBan FROM Ban";

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ds.add(new Ban(
                    rs.getString("maBan"),
                    rs.getString("tenBan"),
                    rs.getString("trangThai"),
                    rs.getInt("toaDoX"),
                    rs.getInt("toaDoY"),
                    rs.getString("maTang"),
                    rs.getString("maLoaiBan")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // 🔹 Lấy bàn theo mã
    public Ban getById(String maBan) {
        String sql = "SELECT maBan, tenBan, trangThai, toaDoX, toaDoY, maTang, maLoaiBan FROM Ban WHERE maBan = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Ban(
                    rs.getString("maBan"),
                    rs.getString("tenBan"),
                    rs.getString("trangThai"),
                    rs.getInt("toaDoX"),
                    rs.getInt("toaDoY"),
                    rs.getString("maTang"),
                    rs.getString("maLoaiBan")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Cập nhật trạng thái bàn
    public boolean updateTrangThai(String maBan, String trangThai) {
        String sql = "UPDATE Ban SET trangThai = ? WHERE maBan = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            ps.setString(2, maBan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật vị trí bàn (nếu bạn cho phép kéo thả bàn trong giao diện)
    public boolean updateToaDo(String maBan, int toaDoX, int toaDoY) {
        String sql = "UPDATE Ban SET toaDoX = ?, toaDoY = ? WHERE maBan = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, toaDoX);
            ps.setInt(2, toaDoY);
            ps.setString(3, maBan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
