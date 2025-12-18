package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.DieuKien_MonTang;
import com.thefourrestaurant.model.MonAn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DieuKien_MonTangDAO {

    public List<DieuKien_MonTang> layMonTangTheoMaDieuKien(String maDieuKien) {
        List<DieuKien_MonTang> dsMonTang = new ArrayList<>();
        String sql = "SELECT dmt.*, m.tenMon FROM DieuKien_MonTang dmt JOIN MonAn m ON dmt.maMonAnTang = m.maMonAn WHERE dmt.maDieuKien = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDieuKien);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DieuKien_MonTang dkmt = new DieuKien_MonTang();
                MonAn mon = new MonAn();
                mon.setMaMonAn(rs.getString("maMonAnTang"));
                mon.setTenMon(rs.getString("tenMon"));
                dkmt.setMonAnTang(mon);
                dsMonTang.add(dkmt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsMonTang;
    }

    public void themMonTang(Connection conn, String maDieuKien, List<DieuKien_MonTang> danhSachMonTang) throws SQLException {
        String sql = "INSERT INTO DieuKien_MonTang (maDieuKien, maMonAnTang) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (DieuKien_MonTang dkmt : danhSachMonTang) {
                ps.setString(1, maDieuKien);
                ps.setString(2, dkmt.getMonAnTang().getMaMonAn());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void xoaMonTangTheoMaDieuKien(Connection conn, String maDieuKien) throws SQLException {
        String sql = "DELETE FROM DieuKien_MonTang WHERE maDieuKien = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDieuKien);
            ps.executeUpdate();
        }
    }
}
