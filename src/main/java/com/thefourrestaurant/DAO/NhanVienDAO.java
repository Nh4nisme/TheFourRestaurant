package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    public List<NhanVien> layDanhSachNhanVien() {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE isDeleted = 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh"),
                        rs.getString("gioiTinh"),
                        rs.getString("soDienThoai"),
                        rs.getBigDecimal("luong"),
                        TaiKhoanDAO.layTaiKhoanTheoMa(rs.getString("maTK")),
                        rs.getBoolean("isDeleted")
                );
                // read image path if present
                try { nv.setHinhAnh(rs.getString("hinhAnh")); } catch (Exception ignored) {}
                ds.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public NhanVien layNhanVienTheoMa(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ? AND isDeleted = 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien(
                            rs.getString("maNV"),
                            rs.getString("hoTen"),
                            rs.getDate("ngaySinh"),
                            rs.getString("gioiTinh"),
                            rs.getString("soDienThoai"),
                            rs.getBigDecimal("luong"),
                            TaiKhoanDAO.layTaiKhoanTheoMa(rs.getString("maTK")),
                            rs.getBoolean("isDeleted")
                    );
                    try { nv.setHinhAnh(rs.getString("hinhAnh")); } catch (Exception ignored) {}
                    return nv;
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET hoTen = ?, ngaySinh = ?, gioiTinh = ?, soDienThoai = ?, luong = ?, maTK = ?, hinhAnh = ? WHERE maNV = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoTen());
            ps.setDate(2, nv.getNgaySinh());
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getSoDienThoai());
            ps.setBigDecimal(5, nv.getLuong());
            ps.setString(6, nv.getMaTK() != null ? nv.getMaTK().getMaTK() : null);
            ps.setString(7, nv.getHinhAnh());
            ps.setString(8, nv.getMaNV());

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}