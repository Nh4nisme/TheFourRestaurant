package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiMon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoaiMonAnDAO {

    public List<LoaiMon> getAllLoaiMonAn() {
        List<LoaiMon> danhSachLoaiMon = new ArrayList<>();
        String sql = "SELECT * FROM LoaiMonAn ORDER BY maLoaiMon DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LoaiMon loaiMon = new LoaiMon();
                loaiMon.setMaLoaiMon(rs.getString("maLoaiMon"));
                loaiMon.setTenLoaiMon(rs.getString("tenLoaiMon"));
                loaiMon.setHinhAnh(rs.getString("hinhAnh"));
                danhSachLoaiMon.add(loaiMon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLoaiMon;
    }

    public String generateNewMaLoaiMon() {
        String newId = "LM000001";
        String sql = "SELECT TOP 1 maLoaiMon FROM LoaiMonAn ORDER BY maLoaiMon DESC";
        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maLoaiMon");
                int num = Integer.parseInt(lastId.substring(2));
                num++;
                newId = String.format("LM%06d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean addLoaiMonAn(LoaiMon loaiMon) {
        String sql = "INSERT INTO LoaiMonAn (maLoaiMon, tenLoaiMon, hinhAnh) VALUES (?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, loaiMon.getMaLoaiMon());
            ps.setString(2, loaiMon.getTenLoaiMon());
            ps.setString(3, loaiMon.getHinhAnh());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateLoaiMonAn(LoaiMon loaiMon) {
        String sql = "UPDATE LoaiMonAn SET tenLoaiMon = ?, hinhAnh = ? WHERE maLoaiMon = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, loaiMon.getTenLoaiMon());
            ps.setString(2, loaiMon.getHinhAnh());
            ps.setString(3, loaiMon.getMaLoaiMon());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLoaiMonAn(String maLoaiMon) {
        String sql = "DELETE FROM LoaiMonAn WHERE maLoaiMon = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiMon);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
