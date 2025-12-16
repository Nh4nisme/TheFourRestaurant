package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {
    private LoaiMonDAO loaiMonDAO = new LoaiMonDAO();

    private MonAn mapResultSetToMonAn(ResultSet rs) throws SQLException {
        MonAn mon = new MonAn();
        mon.setMaMonAn(rs.getString("maMonAn"));
        mon.setTenMon(rs.getString("tenMon"));
        mon.setDonGia(rs.getBigDecimal("donGia"));
        mon.setTrangThai(rs.getString("trangThai"));
        mon.setHinhAnh(rs.getString("hinhAnh"));
        LoaiMon loai = new LoaiMon(rs.getString("maLoaiMon"), rs.getString("tenLoaiMon"), null);
        mon.setLoaiMon(loai);
        mon.setDeleted(rs.getBoolean("isDeleted"));
        try {
            mon.setVisible(rs.getBoolean("isVisible"));
        } catch (SQLException e) {
            mon.setVisible(true);
        }
        try {
            mon.setSoLuong(rs.getInt("soLuong"));
        } catch (SQLException e) {
            mon.setSoLuong(0);
        }
        try {
            mon.setDaBan(rs.getInt("daBan"));
        } catch (SQLException e) {
            mon.setDaBan(0);
        }
        return mon;
    }

    private String baseQuery() {
        return "SELECT ma.*, lm.tenLoaiMon FROM MonAn ma LEFT JOIN LoaiMonAn lm ON ma.maLoaiMon = lm.maLoaiMon WHERE ma.isDeleted = 0 ";
    }

    public List<MonAn> layTatCaMonAnHienThi() {
        List<MonAn> ds = new ArrayList<>();
        String sql = "SELECT ma.*, lm.tenLoaiMon FROM MonAn ma LEFT JOIN LoaiMonAn lm ON ma.maLoaiMon = lm.maLoaiMon WHERE ma.isDeleted = 0 AND ma.isVisible = 1";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(mapResultSetToMonAn(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public List<MonAn> layTatCaMonAn() {
        List<MonAn> ds = new ArrayList<>();
        String sql = baseQuery(); // Đã bao gồm WHERE isDeleted = 0
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(mapResultSetToMonAn(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public List<MonAn> layMonAnTheoLoai(String maLoaiMon) {
        List<MonAn> ds = new ArrayList<>();
        String sql = baseQuery() + " AND ma.maLoaiMon = ?"; // Đã bao gồm WHERE isDeleted = 0
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLoaiMon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapResultSetToMonAn(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public MonAn layMonAnTheoMa(String maMonAn) {
        MonAn monAn = null;
        String sql = baseQuery() + " AND ma.maMonAn = ?"; // Sử dụng baseQuery và thêm điều kiện mã món ăn

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maMonAn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    monAn = mapResultSetToMonAn(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return monAn;
    }

    public boolean themMonAn(MonAn mon) {
        String sql = "INSERT INTO MonAn (maMonAn, tenMon, donGia, trangThai, maLoaiMon, hinhAnh, soLuong, daBan, isDeleted, isVisible) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getMaMonAn());
            ps.setString(2, mon.getTenMon());
            ps.setBigDecimal(3, mon.getRawDonGia());
            ps.setString(4, mon.getTrangThai());
            ps.setString(5, mon.getLoaiMon().getMaLoaiMon());
            ps.setString(6, mon.getHinhAnh());
            ps.setInt(7, mon.getSoLuong());
            ps.setInt(8, mon.getDaBan());
            ps.setBoolean(9, Boolean.TRUE.equals(mon.getDeleted()));
            ps.setBoolean(10, mon.getVisible() == null ? true : Boolean.TRUE.equals(mon.getVisible()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean capNhatMonAn(MonAn mon) {
        String sql = "UPDATE MonAn SET tenMon=?, donGia=?, trangThai=?, maLoaiMon=?, hinhAnh=?, soLuong=?, isDeleted=?, isVisible=? WHERE maMonAn=?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mon.getTenMon());
            ps.setBigDecimal(2, mon.getRawDonGia());
            ps.setString(3, mon.getTrangThai());
            ps.setString(4, mon.getLoaiMon().getMaLoaiMon());
            ps.setString(5, mon.getHinhAnh());
            ps.setInt(6, mon.getSoLuong());
            ps.setBoolean(7, Boolean.TRUE.equals(mon.getDeleted()));
            ps.setBoolean(8, mon.getVisible() == null ? true : Boolean.TRUE.equals(mon.getVisible()));
            ps.setString(9, mon.getMaMonAn());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean giamSoLuong(String maMonAn, int soLuongGiam) {
        String sql = "UPDATE MonAn SET soLuong = CASE WHEN soLuong - ? >= 0 THEN soLuong - ? ELSE 0 END, "
                + "daBan = COALESCE(daBan,0) + CASE WHEN soLuong - ? >= 0 THEN ? ELSE soLuong END WHERE maMonAn = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongGiam);
            ps.setInt(2, soLuongGiam);
            ps.setInt(3, soLuongGiam);
            ps.setInt(4, soLuongGiam);
            ps.setString(5, maMonAn);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean capNhatSoLuong(String maMonAn, int soLuongMoi) {
        String sql = "UPDATE MonAn SET soLuong = ? WHERE maMonAn = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongMoi);
            ps.setString(2, maMonAn);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaMonAn(String maMonAn) {
        String sql = "UPDATE MonAn SET isDeleted = 1 WHERE maMonAn = ?"; // Xóa mềm
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMonAn);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public String taoMaMonAnMoi() {
        String sql = "SELECT MAX(maMonAn) FROM MonAn";
        String maxMaMonAn = null;
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                maxMaMonAn = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (maxMaMonAn == null) {
            return "MA000001";
        } else {
            int number = Integer.parseInt(maxMaMonAn.substring(2)); // bỏ "MA"
            return "MA" + String.format("%06d", number + 1);
        }
    }

    public List<MonAn> layMonAnDaXoa() {
        List<MonAn> ds = new ArrayList<>();
        String sql = "SELECT ma.*, lm.tenLoaiMon FROM MonAn ma LEFT JOIN LoaiMonAn lm ON ma.maLoaiMon = lm.maLoaiMon WHERE ma.isDeleted = 1";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(mapResultSetToMonAn(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public boolean khoiPhucMonAn(String maMonAn) {
        String sql = "UPDATE MonAn SET isDeleted = 0 WHERE maMonAn = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMonAn);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}