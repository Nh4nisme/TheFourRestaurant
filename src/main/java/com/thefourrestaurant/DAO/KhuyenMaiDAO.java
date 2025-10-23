package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    // ===== CREATE =====
//    public boolean themKhuyenMai(KhuyenMai km) {
//        String sql = "INSERT INTO KhuyenMai (maKM, maLoaiKM, tyLe, soTien, ngayBatDau, ngayKetThuc, moTa) VALUES (?, ?, ?, ?, ?, ?, ?)";
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, km.getMaKM());
//            ps.setString(2, km.getLoaiKhuyenMai().getMaLoaiKM());
//            ps.setBigDecimal(3, km.getTyLe());
//            ps.setBigDecimal(4, km.getSoTien());
//            ps.setDate(5, km.getNgayBatDau() != null ? Date.valueOf(km.getNgayBatDau()) : null);
//            ps.setDate(6, km.getNgayKetThuc() != null ? Date.valueOf(km.getNgayKetThuc()) : null);
//            ps.setString(7, km.getMoTa());
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    // ===== READ ALL =====
//    public List<KhuyenMai> layTatCaKhuyenMai() {
//        List<KhuyenMai> ds = new ArrayList<>();
//        String sql = "SELECT * FROM KhuyenMai";
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                KhuyenMai km = new KhuyenMai();
//                km.setMaKM(rs.getString("maKM"));
//                LoaiKhuyenMai lkm = new LoaiKhuyenMai(rs.getString("maLoaiKM"), null);
//                km.setLoaiKhuyenMai(lkm);
//                km.setTyLe(rs.getBigDecimal("tyLe"));
//                km.setSoTien(rs.getBigDecimal("soTien"));
//                km.setNgayBatDau(rs.getDate("ngayBatDau").toLocalDate();
//                km.setNgayKetThuc(rs.getDate("ngayKetThuc").toLocalDate();
//                km.setMoTa(rs.getString("moTa"));
//                ds.add(km);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ds;
//    }


    public String taoMaKhuyenMaiMoi() {
        String newId = "KM000001";
        String sql = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maKM"); // ví dụ "KM000123"
                if (lastId != null && lastId.length() > 2) {
                    // Lấy phần số, tăng lên 1 — xử lý từng chữ số cẩn thận
                    String numPart = lastId.substring(2); // "000123"
                    int num = 0;
                    try {
                        // tính số bằng cách parse digit-by-digit để tránh lỗi (an toàn)
                        num = Integer.parseInt(numPart);
                    } catch (NumberFormatException ex) {
                        num = 0; // nếu không parse được thì bắt đầu từ 0
                    }
                    num += 1;
                    // đảm bảo format với 6 chữ số: KM + 6 chữ số
                    newId = String.format("KM%06d", num);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // nếu lỗi DB thì trả về default (KM000001) để không gây NPE; bạn có thể xử lý khác
        }
        return newId;
    }
    // ===== READ BY ID =====
//    public KhuyenMai timKhuyenMaiTheoMa(String maKM) {
//        String sql = "SELECT * FROM KhuyenMai WHERE maKM = ?";
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, maKM);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    KhuyenMai km = new KhuyenMai();
//                    km.setMaKM(rs.getString("maKM"));
//                    LoaiKhuyenMai lkm = new LoaiKhuyenMai(rs.getString("maLoaiKM"), null);
//                    km.setLoaiKhuyenMai(lkm);
//                    km.setTyLe(rs.getBigDecimal("tyLe"));
//                    km.setSoTien(rs.getBigDecimal("soTien"));
//                    km.setNgayBatDau(rs.getDate("ngayBatDau") != null ? rs.getDate("ngayBatDau").toLocalDate() : null);
//                    km.setNgayKetThuc(rs.getDate("ngayKetThuc") != null ? rs.getDate("ngayKetThuc").toLocalDate() : null);
//                    km.setMoTa(rs.getString("moTa"));
//                    return km;
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
        String sql = "select * from KhuyenMai where maKM=?";

        try(Connection conn = ConnectSQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,maKM);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new KhuyenMai(
                            rs.getString("maKM"),
                            new LoaiKhuyenMaiDAO().layLoaiKhuyenMaiTheoMa(rs.getString("maLoaiKM")),
                            rs.getBigDecimal("tyLe"),
                            rs.getBigDecimal("soTien"),
                            rs.getTimestamp("ngayBatDau").toLocalDateTime(),
                            rs.getTimestamp("ngayKetThuc").toLocalDateTime(),
                            rs.getString("moTa")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===== UPDATE =====
//    public boolean capNhatKhuyenMai(KhuyenMai km) {
//        String sql = "UPDATE KhuyenMai SET maLoaiKM=?, tyLe=?, soTien=?, ngayBatDau=?, ngayKetThuc=?, moTa=? WHERE maKM=?";
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, km.getLoaiKhuyenMai().getMaLoaiKM());
//            ps.setBigDecimal(2, km.getTyLe());
//            ps.setBigDecimal(3, km.getSoTien());
//            ps.setDate(4, km.getNgayBatDau() != null ? Date.valueOf(km.getNgayBatDau()) : null);
//            ps.setDate(5, km.getNgayKetThuc() != null ? Date.valueOf(km.getNgayKetThuc()) : null);
//            ps.setString(6, km.getMoTa());
//            ps.setString(7, km.getMaKM());
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    // ===== DELETE =====
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
