package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.LoaiKhachHang;
import com.thefourrestaurant.DAO.LoaiKhachHangDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

//    public List<KhachHang> getAll() {
//        List<KhachHang> ds = new ArrayList<>();
//        String sql = "SELECT * FROM KhachHang WHERE isDeleted = false";
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                KhachHang kh = new KhachHang(
//                        rs.getString("maKH"),
//                        rs.getString("hoTen"),
//                        rs.getDate("ngaySinh"),
//                        rs.getString("gioiTinh"),
//                        rs.getString("soDT"),
//                        new LoaiKhachHang()
//                        rs.getBoolean("isDeleted")
//                );
//                ds.add(kh);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ds;
//    }

    public KhachHang layKhachHangTheoMa(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ? AND isDeleted = 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("hoTen"),
                            rs.getDate("ngaySinh"),
                            rs.getString("gioiTinh"),
                            rs.getString("soDT"),
                            LoaiKhachHangDAO.layLoaiKhachHangTheoMa(rs.getString("maLoaiKH")),
                            rs.getBoolean("isDeleted")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KhachHang layKhachHangTheoSDT(String soDT) {
        String sql = "SELECT * FROM KhachHang WHERE soDT = ? AND isDeleted = 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, soDT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("hoTen"),
                            rs.getDate("ngaySinh"),
                            rs.getString("gioiTinh"),
                            rs.getString("soDT"),
                            LoaiKhachHangDAO.layLoaiKhachHangTheoMa(rs.getString("maLoaiKH")),
                            rs.getBoolean("isDeleted")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm khách hàng mới, tự sinh mã KH
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, hoTen, ngaySinh, gioiTinh, soDT, maLoaiKH, isDeleted) VALUES (?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String maMoi = taoMaKHMoi();
            kh.setMaKH(maMoi);

            // Nếu chưa chọn loại KH thì chọn loại đầu tiên còn hoạt động
            if (kh.getLoaiKH() == null) {
                List<LoaiKhachHang> ds = new LoaiKhachHangDAO().layDanhSachLoaiKhachHang();
                if (!ds.isEmpty()) {
                    kh.setLoaiKH(ds.get(0));
                }
            }

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getHoTen());
            ps.setDate(3, kh.getNgaySinh());
            ps.setString(4, kh.getGioiTinh());
            ps.setString(5, kh.getSoDT());
            ps.setString(6, kh.getLoaiKH() != null ? kh.getLoaiKH().getMaLoaiKH() : null);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Sinh mã KH tự động dạng KH000001
    public String taoMaKHMoi() {
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY maKH DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String last = rs.getString(1); 
                try {
                    int num = Integer.parseInt(last.substring(2));
                    return String.format("KH%06d", num + 1);
                } catch (Exception ignore) {
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "KH000001";
    }
}
