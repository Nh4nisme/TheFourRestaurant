package com.thefourrestaurant.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.model.PhieuDatBan;

public class PhieuDatBanDAO {

    // 🔹 Lấy toàn bộ phiếu đặt bàn (chưa xóa)
    public List<PhieuDatBan> layTatCaPhieu() {
        List<PhieuDatBan> danhSach = new ArrayList<>();
        String sql = "SELECT maPDB, ngayDat, soNguoi, maKH, maNV, isDeleted FROM PhieuDatBan WHERE isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayDat(rs.getDate("ngayDat").toLocalDate());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(new KhachHang(rs.getString("maKH")));
                pdb.setNhanVien(new NhanVien(rs.getString("maNV")));
                pdb.setDeleted(rs.getBoolean("isDeleted"));
                danhSach.add(pdb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // 🔹 Lấy phiếu đặt bàn theo mã
    public PhieuDatBan layPhieuTheoMa(String maPDB) {
        String sql = "SELECT maPDB, ngayDat, soNguoi, maKH, maNV, isDeleted FROM PhieuDatBan WHERE maPDB = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPDB);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayDat(rs.getDate("ngayDat").toLocalDate());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(new KhachHang(rs.getString("maKH")));
                pdb.setNhanVien(new NhanVien(rs.getString("maNV")));
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
        String sql = "INSERT INTO PhieuDatBan (maPDB, ngayDat, soNguoi, maKH, maNV) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String maMoi = taoMaPhieuMoi();
            pdb.setMaPDB(maMoi);

            ps.setString(1, maMoi);
            ps.setDate(2, java.sql.Date.valueOf(pdb.getNgayDat() != null ? pdb.getNgayDat() : LocalDate.now()));
            ps.setInt(3, pdb.getSoNguoi());
            ps.setString(4, pdb.getKhachHang().getMaKH());
            ps.setString(5, pdb.getNhanVien().getMaNV());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.err.println("Lỗi khi thêm phiếu đặt bàn: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật thông tin phiếu đặt bàn
    public boolean capNhatPhieu(PhieuDatBan pdb) {
        String sql = "UPDATE PhieuDatBan SET ngayDat = ?, soNguoi = ?, maKH = ?, maNV = ? WHERE maPDB = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(pdb.getNgayDat()));
            ps.setInt(2, pdb.getSoNguoi());
            ps.setString(3, pdb.getKhachHang().getMaKH());
            ps.setString(4, pdb.getNhanVien().getMaNV());
            ps.setString(5, pdb.getMaPDB());

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
            System.err.println("Lỗi khi tạo mã phiếu mới: " + e.getMessage());
            e.printStackTrace();
        }

        return "PD000001";
    }
}
