package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhungGio;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class KhungGioDAO {

    public List<KhungGio> layTatCaKhungGio() {
        List<KhungGio> danhSach = new ArrayList<>();
        String sql = "SELECT maTG, gioBatDau, gioKetThuc, lapLaiHangNgay FROM KhungGio";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(new KhungGio(
                        rs.getString("maTG"),
                        rs.getTime("gioBatDau").toLocalTime(),
                        rs.getTime("gioKetThuc").toLocalTime(),
                        rs.getBoolean("lapLaiHangNgay")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public KhungGio layKhungGioTheoMa(String maTG) {
        String sql = "SELECT maTG, gioBatDau, gioKetThuc, lapLaiHangNgay FROM KhungGio WHERE maTG = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTG);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhungGio(
                            rs.getString("maTG"),
                            rs.getTime("gioBatDau").toLocalTime(),
                            rs.getTime("gioKetThuc").toLocalTime(),
                            rs.getBoolean("lapLaiHangNgay")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themKhungGio(KhungGio khungGio) {
        String sql = "INSERT INTO KhungGio (maTG, gioBatDau, gioKetThuc, lapLaiHangNgay) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, khungGio.getMaTG());
            ps.setTime(2, Time.valueOf(khungGio.getGioBatDau()));
            ps.setTime(3, Time.valueOf(khungGio.getGioKetThuc()));
            ps.setBoolean(4, khungGio.isLapLaiHangNgay());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhungGio(KhungGio khungGio) {
        String sql = "UPDATE KhungGio SET gioBatDau = ?, gioKetThuc = ?, lapLaiHangNgay = ? WHERE maTG = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTime(1, Time.valueOf(khungGio.getGioBatDau()));
            ps.setTime(2, Time.valueOf(khungGio.getGioKetThuc()));
            ps.setBoolean(3, khungGio.isLapLaiHangNgay());
            ps.setString(4, khungGio.getMaTG());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhungGio(String maTG) {
        String sql = "DELETE FROM KhungGio WHERE maTG = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTG);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String taoMaKhungGioMoi() {
        String sql = "SELECT TOP 1 maTG FROM KhungGio ORDER BY maTG DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("maTG");
                int num = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("TG%06d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "TG000001";
    }
}
