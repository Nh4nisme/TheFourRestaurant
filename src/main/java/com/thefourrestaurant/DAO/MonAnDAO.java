package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {

    // Helper method to map a ResultSet row to a complete MonAn object
    private MonAn mapResultSetToMonAn(ResultSet rs) throws SQLException {
        MonAn monAn = new MonAn();
        monAn.setMaMonAn(rs.getString("maMonAn"));
        monAn.setTenMon(rs.getString("tenMon"));
        monAn.setDonGia(rs.getBigDecimal("donGia"));
        monAn.setTrangThai(rs.getString("trangThai"));
        monAn.setHinhAnh(rs.getString("hinhAnh"));

        // Create and set LoaiMon object
        if (rs.getString("maLoaiMon") != null) {
            LoaiMon loaiMon = new LoaiMon();
            loaiMon.setMaLoaiMon(rs.getString("maLoaiMon"));
            loaiMon.setTenLoaiMon(rs.getString("tenLoaiMon"));
            // hinhAnh for LoaiMon is not joined here for simplicity, can be added if needed
            monAn.setLoaiMon(loaiMon);
        }

        // Create and set KhuyenMai object
        if (rs.getString("maKM") != null) {
            KhuyenMai khuyenMai = new KhuyenMai();
            khuyenMai.setMaKM(rs.getString("maKM"));
            khuyenMai.setMoTa(rs.getString("km_moTa")); // Aliased column
            // Other KhuyenMai fields can be joined and set here if needed
            monAn.setKhuyenMai(khuyenMai);
        }

        return monAn;
    }

    private String getBaseSelectSQL() {
        return "SELECT ma.*, lm.tenLoaiMon, km.moTa AS km_moTa " +
               "FROM MonAn ma " +
               "LEFT JOIN LoaiMonAn lm ON ma.maLoaiMon = lm.maLoaiMon " +
               "LEFT JOIN KhuyenMai km ON ma.maKM = km.maKM ";
    }

    public List<MonAn> getMonAnByLoai(String maLoaiMon) {
        List<MonAn> danhSachMonAn = new ArrayList<>();
        String sql = getBaseSelectSQL() + "WHERE ma.maLoaiMon = ? ORDER BY ma.maMonAn DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiMon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachMonAn.add(mapResultSetToMonAn(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachMonAn;
    }

    public List<MonAn> getAllMonAn() {
        List<MonAn> danhSachMonAn = new ArrayList<>();
        String sql = getBaseSelectSQL() + "ORDER BY ma.tenMon ASC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSachMonAn.add(mapResultSetToMonAn(rs));
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
        String sql = "INSERT INTO MonAn (maMonAn, tenMon, donGia, trangThai, maLoaiMon, hinhAnh, maKM) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, monAn.getMaMonAn());
            ps.setString(2, monAn.getTenMon());
            ps.setBigDecimal(3, monAn.getDonGia());
            ps.setString(4, monAn.getTrangThai());
            ps.setString(5, monAn.getLoaiMon() != null ? monAn.getLoaiMon().getMaLoaiMon() : null);
            ps.setString(6, monAn.getHinhAnh());
            ps.setString(7, monAn.getKhuyenMai() != null ? monAn.getKhuyenMai().getMaKM() : null);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMonAn(MonAn monAn) {
        String sql = "UPDATE MonAn SET tenMon = ?, donGia = ?, trangThai = ?, maLoaiMon = ?, hinhAnh = ?, maKM = ? WHERE maMonAn = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, monAn.getTenMon());
            ps.setBigDecimal(2, monAn.getDonGia());
            ps.setString(3, monAn.getTrangThai());
            ps.setString(4, monAn.getLoaiMon() != null ? monAn.getLoaiMon().getMaLoaiMon() : null);
            ps.setString(5, monAn.getHinhAnh());
            ps.setString(6, monAn.getKhuyenMai() != null ? monAn.getKhuyenMai().getMaKM() : null);
            ps.setString(7, monAn.getMaMonAn());

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
