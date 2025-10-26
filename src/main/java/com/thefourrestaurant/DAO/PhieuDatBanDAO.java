package com.thefourrestaurant.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.*;

public class PhieuDatBanDAO {
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private ChiTietPDBDAO chiTietPDBDAO = new ChiTietPDBDAO();
    private BanDAO banDAO = new BanDAO();

    // Lấy toàn bộ phiếu đặt bàn (chưa xóa)
    public List<PhieuDatBan> layTatCaPhieu() {
        List<PhieuDatBan> danhSach = new ArrayList<>();
        String sql = """
                SELECT maPDB, ngayTao, ngayDat, soNguoi, maKH, maNV, trangThai, isDeleted
                FROM PhieuDatBan
                WHERE isDeleted = 0
                """;

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                pdb.setNgayDat(rs.getDate("ngayDat").toLocalDate());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(khachHangDAO.layKhachHangTheoMa(rs.getString("maKH")));
                pdb.setNhanVien(nhanVienDAO.layNhanVienTheoMa(rs.getString("maNV")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setDeleted(rs.getBoolean("isDeleted"));

                List<ChiTietPDB> dsChiTiet = chiTietPDBDAO.layTheoPhieu(pdb.getMaPDB());
                pdb.setChiTietPDB(dsChiTiet);

                List<Ban> dsBan = dsChiTiet.stream()
                        .map(ChiTietPDB::getBan)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                pdb.setDanhSachBan(dsBan);

                danhSach.add(pdb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // 🔹 Lấy phiếu đặt bàn theo mã
    public PhieuDatBan layPhieuTheoMa(String maPDB) {
        String sql = """
                SELECT maPDB, ngayTao, ngayDat, soNguoi, maKH, maNV, trangThai, isDeleted
                FROM PhieuDatBan
                WHERE maPDB = ?
                """;
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPDB);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                pdb.setNgayDat(rs.getDate("ngayDat").toLocalDate());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(khachHangDAO.layKhachHangTheoMa(rs.getString("maKH")));
                pdb.setNhanVien(nhanVienDAO.layNhanVienTheoMa(rs.getString("maNV")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setDeleted(rs.getBoolean("isDeleted"));
                return pdb;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm mới phiếu đặt bàn (tự sinh mã tự động)
    public boolean themPhieu(PhieuDatBan pdb) {
        String sql = """
                INSERT INTO PhieuDatBan (maPDB, ngayDat, soNguoi, maKH, maNV, trangThai)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String maMoi = taoMaPhieuMoi();
            pdb.setMaPDB(maMoi);

            ps.setString(1, maMoi);
            ps.setDate(2, java.sql.Date.valueOf(
                    pdb.getNgayDat() != null ? pdb.getNgayDat() : LocalDate.now()));
            ps.setInt(3, pdb.getSoNguoi());
            ps.setString(4, pdb.getKhachHang().getMaKH());
            ps.setString(5, pdb.getNhanVien().getMaNV());
            ps.setString(6, pdb.getTrangThai() != null ? pdb.getTrangThai() : "Đang phục vụ");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi thêm phiếu đặt bàn: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật thông tin phiếu đặt bàn
    public boolean capNhatPhieu(PhieuDatBan pdb) {
        String sql = """
                UPDATE PhieuDatBan
                SET ngayDat = ?, soNguoi = ?, maKH = ?, maNV = ?, trangThai = ?
                WHERE maPDB = ?
                """;
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(pdb.getNgayDat()));
            ps.setInt(2, pdb.getSoNguoi());
            ps.setString(3, pdb.getKhachHang().getMaKH());
            ps.setString(4, pdb.getNhanVien().getMaNV());
            ps.setString(5, pdb.getTrangThai());
            ps.setString(6, pdb.getMaPDB());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xóa mềm phiếu đặt bàn
    public boolean xoaPhieu(String maPDB) {
        String sql = "UPDATE PhieuDatBan SET isDeleted = 1 WHERE maPDB = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPDB);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Tạo mã phiếu đặt bàn tự động
    public String taoMaPhieuMoi() {
        String sql = "SELECT TOP 1 maPDB FROM PhieuDatBan ORDER BY maPDB DESC";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("maPDB");
                if (lastId != null && lastId.startsWith("PD")) {
                    int number = Integer.parseInt(lastId.substring(2)) + 1;
                    return String.format("PD%06d", number);
                }
            }

        } catch (Exception e) {
            System.err.println("⚠️ Lỗi khi tạo mã phiếu mới: " + e.getMessage());
            e.printStackTrace();
        }

        return "PD000001";
    }

    // 🔹 Lấy danh sách phiếu theo trạng thái (VD: “Đặt trước”, “Đang phục vụ”)
    public List<PhieuDatBan> layTheoTrangThai(String trangThai) {
        List<PhieuDatBan> ds = new ArrayList<>();
        String sql = """
                SELECT maPDB, ngayTao, ngayDat, soNguoi, maKH, maNV, trangThai, isDeleted
                FROM PhieuDatBan
                WHERE trangThai = ? AND isDeleted = 0
                """;
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                pdb.setNgayDat(rs.getDate("ngayDat").toLocalDate());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(new KhachHang(rs.getString("maKH")));
                pdb.setNhanVien(new NhanVien(rs.getString("maNV")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setDeleted(rs.getBoolean("isDeleted"));
                ds.add(pdb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
    
 // 🔹 Lấy phiếu hiện tại (Đang phục vụ hoặc Đặt trước) theo mã bàn
    public PhieuDatBan layPhieuDangHoatDongTheoBan(String maBan) {
        String sql = """
                SELECT TOP 1 p.maPDB, p.ngayTao, p.ngayDat, p.soNguoi, p.maKH, p.maNV, p.trangThai, p.isDeleted
				FROM PhieuDatBan p
				JOIN ChiTietPDB c ON p.maPDB = c.maPDB
				WHERE c.maBan = ? 
				  AND p.trangThai IN (N'Đang phục vụ', N'Đặt trước')
				  AND p.isDeleted = 0
				ORDER BY p.ngayTao DESC
                """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                pdb.setNgayDat(rs.getDate("ngayDat").toLocalDate());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(new KhachHang(rs.getString("maKH")));
                pdb.setNhanVien(new NhanVien(rs.getString("maNV")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setDeleted(rs.getBoolean("isDeleted"));
                return pdb;
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy phiếu đang hoạt động theo bàn: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
