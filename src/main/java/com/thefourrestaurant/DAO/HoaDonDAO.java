package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.*;

import java.math.BigDecimal;
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
            -- Hóa đơn
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
            pdb.tienCoc,

            -- Khuyến mãi
            km.maKM,
            km.tenKM,
            km.kieuKM,
            km.maCode,
            lkm.tenLoaiKM,

            -- Điều kiện khuyến mãi (1–1)
            dk.loaiApDung,
            dk.tyLeGiam,
            dk.soTienGiam,
            dk.soLuongTang,

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

        LEFT JOIN NhanVien nv
            ON hd.maNV = nv.maNV
           AND nv.isDeleted = 0

        LEFT JOIN KhachHang kh
            ON hd.maKH = kh.maKH
           AND kh.isDeleted = 0

        LEFT JOIN PhieuDatBan pdb
            ON hd.maPDB = pdb.maPDB
           AND pdb.isDeleted = 0

        LEFT JOIN KhuyenMai km
            ON hd.maKM = km.maKM
           AND km.isDeleted = 0

        LEFT JOIN LoaiKhuyenMai lkm
            ON km.maLoaiKM = lkm.maLoaiKM

        LEFT JOIN KhuyenMai_DieuKien dk
            ON km.maKM = dk.maKM

        LEFT JOIN Thue t
            ON hd.maThue = t.maThue

        INNER JOIN PhuongThucThanhToan pttt
            ON hd.maPTTT = pttt.maPTTT

        LEFT JOIN ChiTietHD cthd
            ON hd.maHD = cthd.maHD

        LEFT JOIN MonAn m
            ON cthd.maMonAn = m.maMonAn

        WHERE hd.isDeleted = 0
        ORDER BY hd.ngayLap DESC;
        """;
    }

    public List<HoaDon> layDanhSachHoaDon() {
        Map<String, HoaDon> mapHoaDon = new LinkedHashMap<>();
        String sql = truyVanHoaDon();

        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                HoaDon hd = mapHoaDon.get(maHD);

                if (hd == null) {
                    hd = new HoaDon();
                    hd.setMaHD(maHD);
                    hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                    hd.setTienKhachDua(rs.getBigDecimal("tienKhachDua"));
                    hd.setTienThua(rs.getBigDecimal("tienThua"));
                    hd.setChiTietHoaDon(new ArrayList<>());

                    // Nhân viên
                    if (rs.getString("maNV") != null) {
                        NhanVien nv = new NhanVien();
                        nv.setMaNV(rs.getString("maNV"));
                        nv.setHoTen(rs.getString("tenNhanVien"));
                        hd.setNhanVien(nv);
                    }

                    // Khách hàng
                    if (rs.getString("maKH") != null) {
                        KhachHang kh = new KhachHang();
                        kh.setMaKH(rs.getString("maKH"));
                        kh.setHoTen(rs.getString("tenKhachHang"));
                        kh.setSoDT(rs.getString("soDienThoaiKH"));
                        hd.setKhachHang(kh);
                    }

                    // Phiếu đặt bàn
                    if (rs.getString("maPDB") != null) {
                        PhieuDatBan pdb = new PhieuDatBan();
                        pdb.setMaPDB(rs.getString("maPDB"));
                        pdb.setTrangThai(rs.getString("trangThaiPDB"));
                        pdb.setTienCoc(rs.getBigDecimal("tienCoc"));
                        hd.setPhieuDatBan(pdb);
                    }

                    // Khuyến mãi
                    if (rs.getString("maKM") != null) {
                        KhuyenMai km = new KhuyenMai();
                        km.setMaKM(rs.getString("maKM"));
                        km.setTenKM(rs.getString("tenKM"));
                        km.setKieuKM(rs.getString("kieuKM"));
                        km.setMaCode(rs.getString("maCode"));

                        LoaiKhuyenMai loaiKM = new LoaiKhuyenMai();
                        loaiKM.setTenLoaiKM(rs.getString("tenLoaiKM"));
                        km.setLoaiKhuyenMai(loaiKM);

                        // Lấy danh sách điều kiện
                        List<KhuyenMai_DieuKien> dsDieuKien = new KhuyenMai_DieuKienDAO().layDieuKienTheoMaKM(km.getMaKM());
                        km.setKhuyenMaiDieuKien(dsDieuKien.isEmpty() ? null : dsDieuKien.get(0)); // null-safe

                        hd.setKhuyenMai(km);
                    }

                    // Thuế
                    if (rs.getString("maThue") != null) {
                        Thue thue = new Thue();
                        thue.setMaThue(rs.getString("maThue"));
                        thue.setTyLe(rs.getInt("thueSuat"));
                        hd.setThue(thue);
                    }

                    // Phương thức thanh toán
                    String tenPTTT = rs.getString("tenPTTT");
                    PhuongThucThanhToan.LoaiPTTT loaiPTTT =
                            "Tiền mặt".equalsIgnoreCase(tenPTTT)
                                    ? PhuongThucThanhToan.LoaiPTTT.TIEN_MAT
                                    : PhuongThucThanhToan.LoaiPTTT.CHUYEN_KHOAN;
                    PhuongThucThanhToan pttt = new PhuongThucThanhToan(rs.getString("maPTTT"), loaiPTTT);
                    hd.setPhuongThucThanhToan(pttt);

                    mapHoaDon.put(maHD, hd);
                }

                // Chi tiết hóa đơn
                if (rs.getString("maMonAn") != null) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setDonGia(rs.getBigDecimal("donGia"));

                    MonAn mon = new MonAn();
                    mon.setMaMonAn(rs.getString("maMonAn"));
                    mon.setTenMon(rs.getString("tenMon"));
                    mon.setDonGia(rs.getBigDecimal("donGia"));

                    ct.setMonAn(mon);
                    hd.getChiTietHoaDon().add(ct);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(mapHoaDon.values());
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
