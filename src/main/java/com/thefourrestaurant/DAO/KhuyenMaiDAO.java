package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.model.MonAn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    private KhuyenMai mapResultSetToKhuyenMai(ResultSet rs) throws SQLException {
        KhuyenMai km = new KhuyenMai();
        km.setMaKM(rs.getString("maKM"));
        km.setTyLe(rs.getBigDecimal("tyLe"));
        km.setSoTien(rs.getBigDecimal("soTien"));
        Date ngayBD = rs.getDate("ngayBatDau");
        if (ngayBD != null) km.setNgayBatDau(ngayBD.toLocalDate());
        Date ngayKT = rs.getDate("ngayKetThuc");
        if (ngayKT != null) km.setNgayKetThuc(ngayKT.toLocalDate());
        km.setMoTa(rs.getString("moTa"));

        // Build LoaiKhuyenMai object
        if (rs.getString("maLoaiKM") != null) {
            LoaiKhuyenMai lkm = new LoaiKhuyenMai();
            lkm.setMaLoaiKM(rs.getString("maLoaiKM"));
            lkm.setTenLoaiKM(rs.getString("tenLoaiKM"));
            km.setLoaiKhuyenMai(lkm);
        }

        // Build MonAnTang object
        if (rs.getString("maMonTang") != null) {
            MonAn monAnTang = new MonAn();
            monAnTang.setMaMonAn(rs.getString("maMonTang"));
            monAnTang.setTenMon(rs.getString("tenMonTang"));
            km.setMonAnTang(monAnTang);
        }

        // Build MonAnApDung object
        if (rs.getString("maMonApDung") != null) {
            MonAn monAnApDung = new MonAn();
            monAnApDung.setMaMonAn(rs.getString("maMonApDung"));
            monAnApDung.setTenMon(rs.getString("tenMonApDung"));
            km.setMonAnApDung(monAnApDung);
        }

        return km;
    }

    private String getBaseSelectSQL() {
        return "SELECT km.*, lkm.tenLoaiKM, " +
               "mat.tenMon AS tenMonTang, " +
               "maad.tenMon AS tenMonApDung " +
               "FROM KhuyenMai km " +
               "LEFT JOIN LoaiKhuyenMai lkm ON km.maLoaiKM = lkm.maLoaiKM " +
               "LEFT JOIN MonAn mat ON km.maMonTang = mat.maMonAn " +
               "LEFT JOIN MonAn maad ON km.maMonApDung = maad.maMonAn ";
    }

    public List<KhuyenMai> getAllKhuyenMai() {
        List<KhuyenMai> danhSach = new ArrayList<>();
        String sql = getBaseSelectSQL() + "ORDER BY km.maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapResultSetToKhuyenMai(rs));
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
        String sql = "INSERT INTO KhuyenMai (maKM, maLoaiKM, tyLe, soTien, maMonTang, maMonApDung, ngayBatDau, ngayKetThuc, moTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setBigDecimal(3, km.getTyLe());
            ps.setBigDecimal(4, km.getSoTien());
            ps.setString(5, km.getMonAnTang() != null ? km.getMonAnTang().getMaMonAn() : null);
            ps.setString(6, km.getMonAnApDung() != null ? km.getMonAnApDung().getMaMonAn() : null);
            ps.setObject(7, km.getNgayBatDau());
            ps.setObject(8, km.getNgayKetThuc());
            ps.setString(9, km.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET maLoaiKM = ?, tyLe = ?, soTien = ?, maMonTang = ?, maMonApDung = ?, " +
                     "ngayBatDau = ?, ngayKetThuc = ?, moTa = ? WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setBigDecimal(2, km.getTyLe());
            ps.setBigDecimal(3, km.getSoTien());
            ps.setString(4, km.getMonAnTang() != null ? km.getMonAnTang().getMaMonAn() : null);
            ps.setString(5, km.getMonAnApDung() != null ? km.getMonAnApDung().getMaMonAn() : null);
            ps.setObject(6, km.getNgayBatDau());
            ps.setObject(7, km.getNgayKetThuc());
            ps.setString(8, km.getMoTa());
            ps.setString(9, km.getMaKM());

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
