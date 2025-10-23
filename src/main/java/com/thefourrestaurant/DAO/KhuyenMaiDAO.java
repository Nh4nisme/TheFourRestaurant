package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
// import com.thefourrestaurant.model.MonAn; // Không cần thiết nếu không có cột maMonTang/maMonApDung trực tiếp

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

        if (rs.getString("maLoaiKM") != null) {
            LoaiKhuyenMai lkm = new LoaiKhuyenMai();
            lkm.setMaLoaiKM(rs.getString("maLoaiKM"));
            lkm.setTenLoaiKM(rs.getString("tenLoaiKM")); // Đảm bảo cột tenLoaiKM có trong SELECT
            km.setLoaiKhuyenMai(lkm);
        }

        // Loại bỏ logic liên quan đến maMonTang và maMonApDung vì chúng không có trong bảng KhuyenMai
        // if (rs.getString("maMonTang") != null) {
        //     MonAn monAnTang = new MonAn();
        //     monAnTang.setMaMonAn(rs.getString("maMonTang"));
        //     monAnTang.setTenMon(rs.getString("tenMonTang"));
        //     km.setMonAnTang(monAnTang);
        // }

        // if (rs.getString("maMonApDung") != null) {
        //     MonAn monAnApDung = new MonAn();
        //     monAnApDung.setMaMonAn(rs.getString("maMonApDung"));
        //     monAnApDung.setTenMon(rs.getString("tenMonApDung"));
        //     km.setMonAnApDung(monAnApDung);
        // }

        return km;
    }

    private String getBaseSelectSQL() {
        // Loại bỏ các JOIN và SELECT liên quan đến MonAn vì chúng không có trong bảng KhuyenMai
        return "SELECT km.*, lkm.tenLoaiKM " +
               "FROM KhuyenMai km " +
               "LEFT JOIN LoaiKhuyenMai lkm ON km.maLoaiKM = lkm.maLoaiKM ";
    }

    public List<KhuyenMai> layTatCaKhuyenMai() {
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

    public String taoMaKhuyenMaiMoi() {
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

    public boolean themKhuyenMai(KhuyenMai km) {
        // Loại bỏ maMonTang, maMonApDung khỏi câu lệnh INSERT
        String sql = "INSERT INTO KhuyenMai (maKM, maLoaiKM, tyLe, soTien, ngayBatDau, ngayKetThuc, moTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setBigDecimal(3, km.getTyLe());
            ps.setBigDecimal(4, km.getSoTien());
            ps.setObject(5, km.getNgayBatDau());
            ps.setObject(6, km.getNgayKetThuc());
            ps.setString(7, km.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        // Loại bỏ maMonTang, maMonApDung khỏi câu lệnh UPDATE
        // Sửa lỗi chính tả: ngayBatDuy -> ngayBatDau
        String sql = "UPDATE KhuyenMai SET maLoaiKM = ?, tyLe = ?, soTien = ?, " +
                     "ngayBatDau = ?, ngayKetThuc = ?, moTa = ? WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setBigDecimal(2, km.getTyLe());
            ps.setBigDecimal(3, km.getSoTien());
            ps.setObject(4, km.getNgayBatDau());
            ps.setObject(5, km.getNgayKetThuc());
            ps.setString(6, km.getMoTa());
            ps.setString(7, km.getMaKM());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhuyenMai(String maKM) {
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
