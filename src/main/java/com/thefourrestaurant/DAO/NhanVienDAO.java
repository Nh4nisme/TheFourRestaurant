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

    public NhanVien layNhanVienTheoMaTK(String maTK) {

        String sql = "SELECT * FROM NhanVien WHERE maTK = ? AND isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTK);

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

                    try {
                        nv.setHinhAnh(rs.getString("hinhAnh"));
                    } catch (Exception ignored) {}

                    return nv;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NhanVien layNhanVienTheoSDT(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) return null;
        String sql = "SELECT * FROM NhanVien WHERE soDienThoai = ? AND isDeleted = 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sdt);
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
        String sql = "UPDATE NhanVien SET hoTen = ?, ngaySinh = ?, gioiTinh = ?, soDienThoai = ?, luong = ?, maTK = ?, hinhAnh = ?, isDeleted = ? WHERE maNV = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoTen());
            ps.setDate(2, nv.getNgaySinh());
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getSoDienThoai());
            ps.setBigDecimal(5, nv.getLuong());
            ps.setString(6, nv.getMaTK() != null ? nv.getMaTK().getMaTK() : null);
            ps.setString(7, nv.getHinhAnh());
            ps.setBoolean(8, nv.isDeleted());
            ps.setString(9, nv.getMaNV());

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String taoMaNhanVienMoi() {
        String sql = "SELECT TOP 1 maNV FROM NhanVien ORDER BY maNV DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String last = rs.getString(1);
                int num = Integer.parseInt(last.substring(2)) + 1;
                return String.format("NV%06d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NV000001";
    }

    public boolean themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (maNV, hoTen, ngaySinh, gioiTinh, soDienThoai, luong, maTK, hinhAnh, isDeleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getHoTen());
            ps.setDate(3, nv.getNgaySinh());
            ps.setString(4, nv.getGioiTinh());
            ps.setString(5, nv.getSoDienThoai());
            ps.setBigDecimal(6, nv.getLuong());
            ps.setString(7, nv.getMaTK() != null ? nv.getMaTK().getMaTK() : null);
            ps.setString(8, nv.getHinhAnh());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}