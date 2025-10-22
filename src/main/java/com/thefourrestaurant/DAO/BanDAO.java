package com.thefourrestaurant.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.model.LoaiBan;

public class BanDAO {

    private TangDAO tangDAO = new TangDAO();
    private LoaiBanDAO loaiBanDAO = new LoaiBanDAO();

    // 🔹 Lấy tất cả bàn
    public List<Ban> layTatCaBan() {
        List<Ban> dsBan = new ArrayList<>();
        String sql = "SELECT maBan, tenBan, trangThai, toaDoX, toaDoY, maTang, maLoaiBan, anhBan FROM Ban";

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Tang tang = tangDAO.layTangTheoMa(rs.getString("maTang"));
                LoaiBan loaiBan = loaiBanDAO.layTheoMa(rs.getString("maLoaiBan"));

                dsBan.add(new Ban(
                        rs.getString("maBan"),
                        rs.getString("tenBan"),
                        rs.getString("trangThai"),
                        rs.getInt("toaDoX"),
                        rs.getInt("toaDoY"),
                        tang,
                        loaiBan,
                        rs.getString("anhBan")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsBan;
    }

    // 🔹 Lấy bàn theo mã
    public Ban layTheoMa(String maBan) {
        String sql = "SELECT maBan, tenBan, trangThai, toaDoX, toaDoY, maTang, maLoaiBan, anhBan FROM Ban WHERE maBan = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Tang tang = tangDAO.layTangTheoMa(rs.getString("maTang"));
                LoaiBan loaiBan = loaiBanDAO.layTheoMa(rs.getString("maLoaiBan"));

                return new Ban(
                        rs.getString("maBan"),
                        rs.getString("tenBan"),
                        rs.getString("trangThai"),
                        rs.getInt("toaDoX"),
                        rs.getInt("toaDoY"),
                        tang,
                        loaiBan,
                        rs.getString("anhBan")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Cập nhật trạng thái bàn
    public boolean capNhatTrangThai(String maBan, String trangThai) {
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

    // 🔹 Cập nhật tọa độ bàn
    public boolean capNhatToaDo(String maBan, int toaDoX, int toaDoY) {
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

    // 🔹 Lấy danh sách bàn theo tầng
    public List<Ban> layTheoTang(String maTang) {
        List<Ban> dsBan = new ArrayList<>();
        String sql = "SELECT maBan, tenBan, trangThai, toaDoX, toaDoY, maTang, maLoaiBan, anhBan FROM Ban WHERE maTang = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTang);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Tang tang = tangDAO.layTangTheoMa(rs.getString("maTang"));
                LoaiBan loaiBan = loaiBanDAO.layTheoMa(rs.getString("maLoaiBan"));

                dsBan.add(new Ban(
                        rs.getString("maBan"),
                        rs.getString("tenBan"),
                        rs.getString("trangThai"),
                        rs.getInt("toaDoX"),
                        rs.getInt("toaDoY"),
                        tang,
                        loaiBan,
                        rs.getString("anhBan")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsBan;
    }
}
