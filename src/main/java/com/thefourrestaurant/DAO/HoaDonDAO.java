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

    // Lấy tất cả hóa đơn
    public List<HoaDon> layDanhSachHoaDon() {
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
                hd.setChiTietHoaDon(chiTietHoaDonDAO.layCTHDTheoMa(hd.getMaHD()));
                dsHoaDon.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }
    public String taoMaHoaDonMoi() {
        String sql = "SELECT maHD FROM HoaDon ORDER BY maHD DESC LIMIT 1";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String maCu = rs.getString("maHD");
                int so = Integer.parseInt(maCu.replaceAll("\\D+", "")) + 1;
                return String.format("HD%03d", so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "HD001";
    }

    public boolean themHoaDon(HoaDon hd) {
        String sql = """
                INSERT INTO HoaDon(maHD, ngayLap, maNV, maKH, maPDB, maKM, maThue,
                                   tienKhachDua, tienThua, tongTien, phuongThucThanhToan, isDeleted)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)
                """;
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHD());
            ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap()));
            ps.setString(3, hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : null);
            ps.setString(4, hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : null);
            ps.setString(5, hd.getPhieuDatBan() != null ? hd.getPhieuDatBan().getMaPDB() : null);
            ps.setString(6, hd.getKhuyenMai() != null ? hd.getKhuyenMai().getMaKM() : null);
            ps.setString(7, hd.getThue() != null ? hd.getThue().getMaThue() : null);
            ps.setBigDecimal(8, hd.getTienKhachDua());
            ps.setBigDecimal(9, hd.getTienThua());
            ps.setBigDecimal(10, hd.getTongTien()); // dùng getTongTien() từ model
            ps.setString(11, hd.getPhuongThucThanhToan().getMaPTTT());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaHoaDon(String maHD) {
        String sql = "UPDATE HoaDon SET isDeleted = 1 WHERE maHD = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maHD);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
