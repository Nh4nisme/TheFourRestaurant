package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    public List<KhuyenMai> getAllKhuyenMai() {
        List<KhuyenMai> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai ORDER BY maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("maKM"));
                km.setMaLoaiKM(rs.getString("maLoaiKM"));
                km.setTyLe(rs.getBigDecimal("tyLe"));
                km.setSoTien(rs.getBigDecimal("soTien"));
                km.setMaMonTang(rs.getString("maMonTang"));
                Date ngayBD = rs.getDate("ngayBatDau");
                if (ngayBD != null) {
                    km.setNgayBatDau(ngayBD.toLocalDate());
                }
                Date ngayKT = rs.getDate("ngayKetThuc");
                if (ngayKT != null) {
                    km.setNgayKetThuc(ngayKT.toLocalDate());
                }
                km.setMoTa(rs.getString("moTa"));
                danhSach.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public String generateNewMaKM() {
        String newId = "KM000001";
        String sql = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maKM");
                int num = Integer.parseInt(lastId.substring(2));
                num++;
                newId = String.format("KM%06d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean addKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (maKM, maLoaiKM, tyLe, soTien, maMonTang, ngayBatDau, ngayKetThuc, moTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getMaLoaiKM());
            ps.setBigDecimal(3, km.getTyLe());
            ps.setBigDecimal(4, km.getSoTien());
            ps.setString(5, km.getMaMonTang());
            ps.setObject(6, km.getNgayBatDau());
            ps.setObject(7, km.getNgayKetThuc());
            ps.setString(8, km.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET maLoaiKM = ?, tyLe = ?, soTien = ?, maMonTang = ?, " +
                     "ngayBatDau = ?, ngayKetThuc = ?, moTa = ? WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaLoaiKM());
            ps.setBigDecimal(2, km.getTyLe());
            ps.setBigDecimal(3, km.getSoTien());
            ps.setString(4, km.getMaMonTang());
            ps.setObject(5, km.getNgayBatDau());
            ps.setObject(6, km.getNgayKetThuc());
            ps.setString(7, km.getMoTa());
            ps.setString(8, km.getMaKM());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteKhuyenMai(String maKM) {
        String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
