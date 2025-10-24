package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    private NhanVienDAO nhanVienDAO;
    private KhachHangDAO khachHangDAO;
    private PhieuDatBanDAO phieuDatBanDAO;
    private KhuyenMaiDAO  khuyenMaiDAO;
    private ThueDAO thueDAO;
    private PhuongThucThanhToanDAO phuongThucThanhToanDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;

    public HoaDonDAO() {
        nhanVienDAO = new NhanVienDAO();
        khachHangDAO = new KhachHangDAO();
        phieuDatBanDAO = new PhieuDatBanDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
        thueDAO = new ThueDAO();
        phuongThucThanhToanDAO = new PhuongThucThanhToanDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    }

    public List<HoaDon> layTatCaHoaDon() {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon(
                        rs.getString("maHD"),
                        rs.getTimestamp("ngayLap").toLocalDateTime(),
                        nhanVienDAO.layNhanVienTheoMa(rs.getString("maNV")),
                        khachHangDAO.layKhachHangTheoMa(rs.getString("maKH")),
                        phieuDatBanDAO.layPhieuTheoMa(rs.getString("maPDB")),
                        khuyenMaiDAO.layKhuyenMaiTheoMa(rs.getString("maKM")),
                        thueDAO.layThueTheoMa(rs.getString("maThue")),
                        rs.getBigDecimal("tienKhachDua"),
                        rs.getBigDecimal("tienThua"),
                        phuongThucThanhToanDAO.layPTTTTheoMa(rs.getString("maPTTT")),
                        rs.getBoolean("isDeleted")
                );
                hd.setchiTietHoaDon(chiTietHoaDonDAO.layCTHDTheoMa(hd.getMaHD()));
                dsHoaDon.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }
    // Lấy 1 hóa đơn theo ID
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
