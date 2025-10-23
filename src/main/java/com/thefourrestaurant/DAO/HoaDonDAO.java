package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.ChiTietHoaDon;
import com.thefourrestaurant.model.HoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // Lấy tất cả hóa đơn
//    public List<HoaDon> getAll() {
//        List<HoaDon> dsHoaDon = new ArrayList<>();
//        String sql = "SELECT * FROM HoaDon WHERE isDeleted = false";
//
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                HoaDon hd = new HoaDon(
//                        rs.getString("maHD"),
//                        rs.getTimestamp("ngayLap").toLocalDateTime(),
//                        // Lấy nhân viên theo maNV
//                        new NhanVienDAO().layNhanVienTheoMa(rs.getString("maNV")),
//                        // Lấy khách hàng theo maKH
//                        new KhachHangDAO().getById(rs.getString("maKH")),
//                        // Lấy phiếu đặt bàn theo maPDB
//                        new PhieuDatBanDAO().(rs.getString("maPDB")),
//                        new KhuyenMaiDAO().getById(rs.getString("maKM")),
//                        new ThueDAO().getById(rs.getString("maThue")),
//                        rs.getBigDecimal("tienKhachDua"),
//                        rs.getBigDecimal("tienThua"),
//                        PhuongThucThanhToan.valueOf(rs.getString("phuongThucThanhToan")),
//                        rs.getBoolean("isDeleted")
//                );
//                dsHoaDon.add(hd);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dsHoaDon;
//    }
//
//    // Lấy 1 hóa đơn theo ID
//    public HoaDon getById(String maHD) {
//        String sql = "SELECT * FROM HoaDon WHERE maHD = ? AND isDeleted = false";
//        try (Connection conn = Database.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, maHD);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    return new HoaDon(
//                            rs.getString("maHD"),
//                            rs.getTimestamp("ngayLap").toLocalDateTime(),
//                            new NhanVienDAO().getById(rs.getString("maNV")),
//                            new KhachHangDAO().getById(rs.getString("maKH")),
//                            new PhieuDatBanDAO().getById(rs.getString("maPDB")),
//                            new KhuyenMaiDAO().getById(rs.getString("maKM")),
//                            new ThueDAO().getById(rs.getString("maThue")),
//                            rs.getBigDecimal("tienKhachDua"),
//                            rs.getBigDecimal("tienThua"),
//                            PhuongThucThanhToan.valueOf(rs.getString("phuongThucThanhToan")),
//                            rs.getBoolean("isDeleted")
//                    );
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
