package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.DieuKien_Mon;
import com.thefourrestaurant.model.MonAn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DieuKien_MonDAO {

    public List<DieuKien_Mon> layMonTheoMaDieuKien(String maDieuKien) {
        List<DieuKien_Mon> dsMon = new ArrayList<>();
        String sql = "SELECT dm.*, m.tenMon FROM DieuKien_Mon dm JOIN MonAn m ON dm.maMonAn = m.maMonAn WHERE dm.maDieuKien = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDieuKien);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DieuKien_Mon dkm = new DieuKien_Mon();
                MonAn mon = new MonAn();
                mon.setMaMonAn(rs.getString("maMonAn"));
                mon.setTenMon(rs.getString("tenMon"));
                dkm.setMonAn(mon);
                dkm.setSoLuong(rs.getInt("soLuong"));
                dkm.setVaiTro(rs.getString("vaiTro"));
                dsMon.add(dkm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsMon;
    }

    public void themMonDieuKien(Connection conn, String maDieuKien, List<DieuKien_Mon> danhSachMon) throws SQLException {
        String sql = "INSERT INTO DieuKien_Mon (maDieuKien, maMonAn, soLuong, vaiTro) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (DieuKien_Mon dkm : danhSachMon) {
                ps.setString(1, maDieuKien);
                ps.setString(2, dkm.getMonAn().getMaMonAn());
                ps.setInt(3, dkm.getSoLuong());
                ps.setString(4, dkm.getVaiTro());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void xoaMonTheoMaDieuKien(Connection conn, String maDieuKien) throws SQLException {
        String sql = "DELETE FROM DieuKien_Mon WHERE maDieuKien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDieuKien);
            ps.executeUpdate();
        }
    }
}
