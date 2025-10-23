package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.model.LoaiMonAn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {

    private MonAn mapResultSetToMonAn(ResultSet rs) throws SQLException {
        MonAn mon = new MonAn();
        mon.setMaMonAn(rs.getString("maMonAn"));
        mon.setTenMon(rs.getString("tenMon"));
        mon.setDonGia(rs.getBigDecimal("donGia"));
        mon.setTrangThai(rs.getString("trangThai"));
        mon.setHinhAnh(rs.getString("hinhAnh"));
        LoaiMonAn loai = new LoaiMonAn(rs.getString("maLoaiMon"), rs.getString("tenLoaiMon"), null);
        mon.setLoaiMonAn(loai);
        return mon;
    }

    private String baseQuery() {
        return "SELECT ma.*, lm.tenLoaiMon FROM MonAn ma LEFT JOIN LoaiMonAn lm ON ma.maLoaiMon = lm.maLoaiMon ";
    }

    public List<MonAn> layTatCaMonAn() {
        List<MonAn> ds = new ArrayList<>();
        String sql = baseQuery();
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(mapResultSetToMonAn(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public boolean themMonAn(MonAn mon) {
        String sql = "INSERT INTO MonAn (maMonAn, tenMon, donGia, trangThai, maLoaiMon, hinhAnh) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getMaMonAn());
            ps.setString(2, mon.getTenMon());
            ps.setBigDecimal(3, mon.getDonGia());
            ps.setString(4, mon.getTrangThai());
            ps.setString(5, mon.getLoaiMonAn().getMaLoaiMon());
            ps.setString(6, mon.getHinhAnh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean capNhatMonAn(MonAn mon) {
        String sql = "UPDATE MonAn SET tenMon=?, donGia=?, trangThai=?, maLoaiMon=?, hinhAnh=? WHERE maMonAn=?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getTenMon());
            ps.setBigDecimal(2, mon.getDonGia());
            ps.setString(3, mon.getTrangThai());
            ps.setString(4, mon.getLoaiMonAn().getMaLoaiMon());
            ps.setString(5, mon.getHinhAnh());
            ps.setString(6, mon.getMaMonAn());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean xoaMonAn(String maMonAn) {
        String sql = "DELETE FROM MonAn WHERE maMonAn = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMonAn);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
