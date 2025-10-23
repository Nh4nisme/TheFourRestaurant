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
        String sql = "SELECT * FROM NhanVien WHERE isDeleted = false";
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
                ds.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public NhanVien layNhanVienTheoMa(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ? AND isDeleted = false";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getString("maNV"),
                            rs.getString("hoTen"),
                            rs.getDate("ngaySinh"),
                            rs.getString("gioiTinh"),
                            rs.getString("soDienThoai"),
                            rs.getBigDecimal("luong"),
                            TaiKhoanDAO.layTaiKhoanTheoMa(rs.getString("maTK")),
                            rs.getBoolean("isDeleted")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}