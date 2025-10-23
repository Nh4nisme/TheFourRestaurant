package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.MonAn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietKhuyenMaiDAO {

    private ChiTietKhuyenMai mapResultSetToChiTiet(ResultSet rs) throws SQLException {
        ChiTietKhuyenMai ct = new ChiTietKhuyenMai();
        ct.setMaCTKM(rs.getString("maCTKM"));

        // KhuyenMai
        KhuyenMai km = new KhuyenMai();
        km.setMaKM(rs.getString("maKM"));
        km.setMoTa(rs.getString("moTaKM")); // Alias for KhuyenMai.moTa
        // Có thể set thêm các thuộc tính khác của KhuyenMai nếu cần
        ct.setKhuyenMai(km);

        // MonApDung
        MonAn monApDung = new MonAn();
        monApDung.setMaMonAn(rs.getString("maMonApDung"));
        monApDung.setTenMon(rs.getString("tenMonApDung")); // Alias for MonAn.tenMon
        ct.setMonApDung(monApDung);

        // MonTang (có thể null)
        String maMonTang = rs.getString("maMonTang");
        if (maMonTang != null) {
            MonAn monTang = new MonAn();
            monTang.setMaMonAn(maMonTang);
            monTang.setTenMon(rs.getString("tenMonTang")); // Alias for MonAn.tenMon
            ct.setMonTang(monTang);
        }

        ct.setTyLeGiam(rs.getBigDecimal("tyLeGiam"));
        ct.setSoTienGiam(rs.getBigDecimal("soTienGiam"));
        ct.setSoLuongTang(rs.getObject("soLuongTang", Integer.class)); // Handle nullable Integer

        return ct;
    }

    private String getBaseSelectSQL() {
        return "SELECT ctkm.*, " +
               "km.moTa AS moTaKM, " + // Alias for KhuyenMai.moTa
               "ma_ad.tenMon AS tenMonApDung, " + // Alias for MonAn.tenMon (applied)
               "ma_tang.tenMon AS tenMonTang " + // Alias for MonAn.tenMon (gifted)
               "FROM ChiTietKhuyenMai ctkm " +
               "JOIN KhuyenMai km ON ctkm.maKM = km.maKM " +
               "JOIN MonAn ma_ad ON ctkm.maMonApDung = ma_ad.maMonAn " +
               "LEFT JOIN MonAn ma_tang ON ctkm.maMonTang = ma_tang.maMonAn ";
    }

    public List<ChiTietKhuyenMai> layTheoMaKM(String maKM) {
        List<ChiTietKhuyenMai> ds = new ArrayList<>();
        String sql = getBaseSelectSQL() + " WHERE ctkm.maKM = ? ORDER BY ctkm.maCTKM";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapResultSetToChiTiet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean themChiTiet(ChiTietKhuyenMai ct) {
        String sql = "INSERT INTO ChiTietKhuyenMai (maCTKM, maKM, maMonApDung, maMonTang, tyLeGiam, soTienGiam, soLuongTang) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaCTKM());
            ps.setString(2, ct.getKhuyenMai().getMaKM());
            ps.setString(3, ct.getMonApDung().getMaMonAn());
            ps.setString(4, ct.getMonTang() != null ? ct.getMonTang().getMaMonAn() : null);
            ps.setBigDecimal(5, ct.getTyLeGiam());
            ps.setBigDecimal(6, ct.getSoTienGiam());
            ps.setObject(7, ct.getSoLuongTang()); // Use setObject for nullable Integer
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatChiTiet(ChiTietKhuyenMai ct) {
        String sql = "UPDATE ChiTietKhuyenMai SET maKM=?, maMonApDung=?, maMonTang=?, tyLeGiam=?, soTienGiam=?, soLuongTang=? WHERE maCTKM=?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getKhuyenMai().getMaKM());
            ps.setString(2, ct.getMonApDung().getMaMonAn());
            ps.setString(3, ct.getMonTang() != null ? ct.getMonTang().getMaMonAn() : null);
            ps.setBigDecimal(4, ct.getTyLeGiam());
            ps.setBigDecimal(5, ct.getSoTienGiam());
            ps.setObject(6, ct.getSoLuongTang()); // Use setObject for nullable Integer
            ps.setString(7, ct.getMaCTKM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaChiTiet(String maCTKM) {
        String sql = "DELETE FROM ChiTietKhuyenMai WHERE maCTKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCTKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaTheoMaKM(String maKM) {
        String sql = "DELETE FROM ChiTietKhuyenMai WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String taoMaChiTietKhuyenMaiMoi() {
        String newId = "CTKM0001";
        String sql = "SELECT TOP 1 maCTKM FROM ChiTietKhuyenMai ORDER BY maCTKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maCTKM");
                int num = Integer.parseInt(lastId.substring(4));
                num++;
                newId = String.format("CTKM%04d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }
}
