package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.MonAn;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {

    public List<MonAn> getMonAnByLoai(String maLoaiMon) {
        List<MonAn> danhSachMonAn = new ArrayList<>();
        String sql = "SELECT * FROM MonAn WHERE maLoaiMon = ? ORDER BY maMonAn DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiMon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MonAn monAn = new MonAn();
                    monAn.setMaMonAn(rs.getString("maMonAn"));
                    monAn.setTenMon(rs.getString("tenMon"));
                    monAn.setDonGia(rs.getBigDecimal("donGia"));
                    monAn.setTrangThai(rs.getString("trangThai"));
                    monAn.setMaLoaiMon(rs.getString("maLoaiMon"));
                    monAn.setHinhAnh(rs.getString("hinhAnh"));
                    danhSachMonAn.add(monAn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachMonAn;
    }

    public String generateNewMaMonAn() {
        String newId = "MA000001";
        String sql = "SELECT TOP 1 maMonAn FROM MonAn ORDER BY maMonAn DESC";
        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maMonAn");
                int num = Integer.parseInt(lastId.substring(2));
                num++;
                newId = String.format("MA%06d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean addMonAn(MonAn monAn) {
        String sql = "INSERT INTO MonAn (maMonAn, tenMon, donGia, trangThai, maLoaiMon, hinhAnh) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, monAn.getMaMonAn());
            ps.setString(2, monAn.getTenMon());
            ps.setBigDecimal(3, monAn.getDonGia());
            ps.setString(4, monAn.getTrangThai());
            ps.setString(5, monAn.getMaLoaiMon());
            ps.setString(6, monAn.getHinhAnh());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMonAn(MonAn monAn) {
        String sql = "UPDATE MonAn SET tenMon = ?, donGia = ?, trangThai = ?, maLoaiMon = ?, hinhAnh = ? WHERE maMonAn = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, monAn.getTenMon());
            ps.setBigDecimal(2, monAn.getDonGia());
            ps.setString(3, monAn.getTrangThai());
            ps.setString(4, monAn.getMaLoaiMon());
            ps.setString(5, monAn.getHinhAnh());
            ps.setString(6, monAn.getMaMonAn());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMonAn(String maMonAn) {
        String sql = "DELETE FROM MonAn WHERE maMonAn = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maMonAn);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
