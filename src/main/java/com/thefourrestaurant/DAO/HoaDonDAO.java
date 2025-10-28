package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


    private String truyVanHoaDon() {
        return """
                SELECT
                        hd.maHD,
                        hd.ngayLap,
                        hd.tienKhachDua,
                        hd.tienThua,
                        hd.isDeleted,
                
                        -- Nhân viên
                        nv.maNV,
                        nv.hoTen AS tenNhanVien,
                
                        -- Khách hàng
                        kh.maKH,
                        kh.hoTen AS tenKhachHang,
                        kh.soDT AS soDienThoaiKH,
                
                        -- Phiếu đặt bàn
                        pdb.maPDB,
                        pdb.trangThai AS trangThaiPDB,
                
                        -- Khuyến mãi
                        km.maKM,
                        km.tenKM,
                        km.tyLe,
                        km.soTien,
                        lkm.tenLoaiKM,  -- loại khuyến mãi
                
                        -- Thuế
                        t.maThue,
                        t.tyLe AS thueSuat,
                
                        -- Phương thức thanh toán
                        pttt.maPTTT,
                        pttt.tenPTTT,
                
                        -- Chi tiết hóa đơn
                        cthd.maMonAn,
                        cthd.soLuong,
                        cthd.donGia,
                        m.tenMon
                
                    FROM HoaDon hd
                    -- Nhân viên
                    LEFT JOIN NhanVien nv ON hd.maNV = nv.maNV AND nv.isDeleted = 0
                    -- Khách hàng
                    LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH AND kh.isDeleted = 0
                    -- Phiếu đặt bàn
                    LEFT JOIN PhieuDatBan pdb ON hd.maPDB = pdb.maPDB AND pdb.isDeleted = 0
                    -- Khuyến mãi
                    LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM
                    LEFT JOIN LoaiKhuyenMai lkm ON km.maLoaiKM = lkm.maLoaiKM
                    -- Thuế
                    LEFT JOIN Thue t ON hd.maThue = t.maThue
                    -- Phương thức thanh toán
                    INNER JOIN PhuongThucThanhToan pttt ON hd.maPTTT = pttt.maPTTT
                    -- Chi tiết hóa đơn
                    LEFT JOIN ChiTietHD cthd ON hd.maHD = cthd.maHD
                    LEFT JOIN MonAn m ON cthd.maMonAn = m.maMonAn
                
                    WHERE hd.isDeleted = 0
                    ORDER BY hd.ngayLap DESC;
            """;
    }

//     Lấy tất cả hóa đơn
//    public List<HoaDon> layDanhSachHoaDon() {
//        List<HoaDon> dsHoaDon = new ArrayList<>();
//        String sql = "select * from HoaDon where isDeleted = 0;";
//
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                HoaDon hd = new HoaDon(
//                        rs.getString("maHD"),
//                        rs.getTimestamp("ngayLap").toLocalDateTime(),
//                        nhanVienDAO.layNhanVienTheoMa(rs.getString("maNV")),
//                        khachHangDAO.layKhachHangTheoMa(rs.getString("maKH")),
//                        phieuDatBanDAO.layPhieuTheoMa(rs.getString("maPDB")),
//                        khuyenMaiDAO.layKhuyenMaiTheoMa(rs.getString("maKM")),
//                        thueDAO.layThueTheoMa(rs.getString("maThue")),
//                        rs.getBigDecimal("tienKhachDua"),
//                        rs.getBigDecimal("tienThua"),
//                        phuongThucThanhToanDAO.layPTTTTheoMa(rs.getString("maPTTT")),
//                        rs.getBoolean("isDeleted")
//                );
//                hd.setChiTietHoaDon(chiTietHoaDonDAO.layCTHDTheoMa(hd.getMaHD()));
//                dsHoaDon.add(hd);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dsHoaDon;
//    }


    public List<HoaDon> layDanhSachHoaDon() {
        List<HoaDon> danhSach = new ArrayList<>();
        Map<String, HoaDon> mapHoaDon = new LinkedHashMap<>();

        String sql = truyVanHoaDon();

        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                HoaDon hd = mapHoaDon.get(maHD);

                if (hd == null) {
                    // ========== HÓA ĐƠN ==========
                    hd = new HoaDon();
                    hd.setMaHD(maHD);
                    hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                    hd.setTienKhachDua(rs.getBigDecimal("tienKhachDua"));
                    hd.setTienThua(rs.getBigDecimal("tienThua"));

                    // ========== NHÂN VIÊN ==========
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("maNV"));
                    nv.setHoTen(rs.getString("tenNhanVien"));
                    hd.setNhanVien(nv);

                    // ========== KHÁCH HÀNG ==========
                    if (rs.getString("maKH") != null) {
                        KhachHang kh = new KhachHang();
                        kh.setMaKH(rs.getString("maKH"));
                        kh.setHoTen(rs.getString("tenKhachHang"));
                        kh.setSoDT(rs.getString("soDienThoaiKH"));
                        hd.setKhachHang(kh);
                    }

                    // ========== KHUYẾN MÃI ==========
                    if (rs.getString("maKM") != null) {
                        KhuyenMai km = new KhuyenMai();
                        km.setMaKM(rs.getString("maKM"));
                        km.setTenKM(rs.getString("tenKM"));
                        km.setTyLe(rs.getBigDecimal("tyLe"));
                        km.setSoTien(rs.getBigDecimal("soTien"));

                        LoaiKhuyenMai loaiKM = new LoaiKhuyenMai();
                        loaiKM.setTenLoaiKM(rs.getString("tenLoaiKM"));
                        km.setLoaiKhuyenMai(loaiKM);
                        hd.setKhuyenMai(km);
                    }

                    // ========== THUẾ ==========
                    if (rs.getString("maThue") != null) {
                        Thue thue = new Thue();
                        thue.setMaThue(rs.getString("maThue"));
                        thue.setTyLe(rs.getInt("thueSuat"));
                        hd.setThue(thue);
                    }

                    // ========== PTTT ==========
                    String tenPTTT = rs.getString("tenPTTT");
                    PhuongThucThanhToan.LoaiPTTT loai = null;
                    if ("Tiền mặt".equalsIgnoreCase(tenPTTT)) {
                        loai = PhuongThucThanhToan.LoaiPTTT.TIEN_MAT;
                    } else if ("Chuyển khoản".equalsIgnoreCase(tenPTTT)) {
                        loai = PhuongThucThanhToan.LoaiPTTT.CHUYEN_KHOAN;
                    }
                    PhuongThucThanhToan pttt = new PhuongThucThanhToan(rs.getString("maPTTT"), loai);
                    hd.setPhuongThucThanhToan(pttt);

                    // ========== CHI TIẾT ==========
                    hd.setChiTietHoaDon(new ArrayList<>());

                    mapHoaDon.put(maHD, hd);
                }

                // ========== CHI TIẾT HÓA ĐƠN ==========
                String maMonAn = rs.getString("maMonAn");
                if (maMonAn != null) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setDonGia(rs.getBigDecimal("donGia"));

                    MonAn mon = new MonAn();
                    mon.setMaMonAn(maMonAn);
                    mon.setTenMon(rs.getString("tenMon"));
                    mon.setDonGia(rs.getBigDecimal("donGia"));
                    ct.setMonAn(mon);

                    hd.getChiTietHoaDon().add(ct);
                }
            }
            danhSach.addAll(mapHoaDon.values());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    public String taoMaHDMoi() {
        String newId = "HD000001";
        String sql = "SELECT TOP 1 maHD FROM HoaDon ORDER BY maHD DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("maHD");
                int num = Integer.parseInt(lastId.substring(2)); // lấy số sau HD
                num++;
                newId = String.format("HD%06d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean themHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (maHD, ngayLap, maNV, maKH, maPDB, maKM, maThue, tienKhachDua, tienThua, maPTTT, isDeleted) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            ps.setString(10, hd.getPhuongThucThanhToan() != null ? hd.getPhuongThucThanhToan().getMaPTTT() : null);
            ps.setBoolean(11, hd.isDeleted());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
