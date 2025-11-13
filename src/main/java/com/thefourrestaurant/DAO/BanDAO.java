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
        String sql = """
            SELECT b.maBan, b.tenBan, b.trangThai, b.toaDoX, b.toaDoY, b.anhBan,
                   t.maTang, t.tenTang,
                   lb.maLoaiBan, lb.tenLoaiBan, lb.giaTien, lb.soChoNgoi, lb.moTa
            FROM Ban b
            JOIN Tang t ON b.maTang = t.maTang
            JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
        """;

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Tang tang = new Tang(
                    rs.getString("maTang"),
                    rs.getString("tenTang")
                );

                LoaiBan loaiBan = new LoaiBan(
                    rs.getString("maLoaiBan"),
                    rs.getString("tenLoaiBan"),
                    rs.getBigDecimal("giaTien"),
                    rs.getInt("soChoNgoi"),
                    rs.getString("moTa")
                );

                Ban ban = new Ban(
                    rs.getString("maBan"),
                    rs.getString("tenBan"),
                    rs.getString("trangThai"),
                    rs.getInt("toaDoX"),
                    rs.getInt("toaDoY"),
                    tang,
                    loaiBan,
                    rs.getString("anhBan")
                );

                dsBan.add(ban);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsBan;
    }

    // 🔹 Lấy bàn theo mã
    public Ban layTheoMa(String maBan) {
        String sql = """
            SELECT b.maBan, b.tenBan, b.trangThai, b.toaDoX, b.toaDoY, b.anhBan,
                   t.maTang, t.tenTang,
                   lb.maLoaiBan, lb.tenLoaiBan, lb.giaTien, lb.soChoNgoi, lb.moTa
            FROM Ban b
            JOIN Tang t ON b.maTang = t.maTang
            JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
            WHERE b.maBan = ?
        """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Tang tang = new Tang(rs.getString("maTang"), rs.getString("tenTang"));
                LoaiBan loaiBan = new LoaiBan(
                    rs.getString("maLoaiBan"),
                    rs.getString("tenLoaiBan"),
                    rs.getBigDecimal("giaTien"),
                    rs.getInt("soChoNgoi"),
                    rs.getString("moTa")
                );

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
    String sql = """
        SELECT b.maBan, b.tenBan, b.trangThai, b.toaDoX, b.toaDoY, b.anhBan,
               t.maTang, t.tenTang,
               lb.maLoaiBan, lb.tenLoaiBan, lb.giaTien, lb.soChoNgoi, lb.moTa
        FROM Ban b
        JOIN Tang t ON b.maTang = t.maTang
        JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
        WHERE b.maTang = ?
        """;

    try (Connection conn = ConnectSQL.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maTang);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Tang tang = new Tang(
                rs.getString("maTang"),
                rs.getString("tenTang")
            );

            LoaiBan loaiBan = new LoaiBan(
                rs.getString("maLoaiBan"),
                rs.getString("tenLoaiBan"),
                rs.getBigDecimal("giaTien"),
                rs.getInt("soChoNgoi"),
                rs.getString("moTa")
            );

            Ban ban = new Ban(
                rs.getString("maBan"),
                rs.getString("tenBan"),
                rs.getString("trangThai"),
                rs.getInt("toaDoX"),
                rs.getInt("toaDoY"),
                tang,
                loaiBan,
                rs.getString("anhBan")
            );

            dsBan.add(ban);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return dsBan;
}
    
    public List<String> layDanhSachTrangThaiTuCSDL() {
        List<String> dsTrangThai = new ArrayList<>();
        String sql = "SELECT DISTINCT trangThai FROM Ban";

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                dsTrangThai.add(rs.getString("trangThai"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsTrangThai;
    }
}
