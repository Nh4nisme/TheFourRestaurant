package com.thefourrestaurant.DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.*;

public class PhieuDatBanDAO {
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private BanDAO banDAO = new BanDAO();

    // 🔹 Lấy tất cả phiếu chưa xóa
    public List<PhieuDatBan> layTatCaPhieu() {
        List<PhieuDatBan> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDatBan WHERE isDeleted = 0";

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
                pdb.setBan(banDAO.layTheoMa(rs.getString("maBan")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setDeleted(rs.getBoolean("isDeleted"));
                pdb.setChiTietPDB(new ChiTietPDBDAO().layTheoPhieu(rs.getString("maPDB")));

                danhSach.add(pdb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // 🔹 Lấy phiếu theo mã
    public PhieuDatBan layPhieuTheoMa(String maPDB) {
        String sql = "SELECT * FROM PhieuDatBan WHERE maPDB = ?";
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
                pdb.setBan(banDAO.layTheoMa(rs.getString("maBan")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setDeleted(rs.getBoolean("isDeleted"));

                return pdb;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm phiếu mới
    public boolean themPhieu(PhieuDatBan pdb) {
        String sql = "INSERT INTO PhieuDatBan (maPDB, ngayDat, soNguoi, maKH, maNV, maBan, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String maMoi = taoMaPhieuMoi();
            pdb.setMaPDB(maMoi);

            ps.setString(1, maMoi);
            ps.setDate(2, java.sql.Date.valueOf(pdb.getNgayDat() != null ? pdb.getNgayDat() : LocalDate.now()));
            ps.setInt(3, pdb.getSoNguoi());
            ps.setString(4, pdb.getKhachHang().getMaKH());
            ps.setString(5, pdb.getNhanVien().getMaNV());
            ps.setString(6, pdb.getBan().getMaBan());
            ps.setString(7, pdb.getTrangThai() != null ? pdb.getTrangThai() : "Đang phục vụ");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
 // Xóa phiếu (xóa logic, set isDeleted = 1)
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

    // 🔹 Tạo mã phiếu mới
    public String taoMaPhieuMoi() {
        String sql = "SELECT TOP 1 maPDB FROM PhieuDatBan ORDER BY maPDB DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("maPDB");
                int number = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("PD%06d", number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "PD000001";
    }
    
 // Trong PhieuDatBanDAO
	public PhieuDatBan layPhieuDangHoatDongTheoBan(String maBan) {
	    String sql = "SELECT * FROM PhieuDatBan WHERE maBan = ? AND trangThai = N'Đang phục vụ' AND isDeleted = 0";
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
	            pdb.setKhachHang(new KhachHangDAO().layKhachHangTheoMa(rs.getString("maKH")));
	            pdb.setNhanVien(new NhanVienDAO().layNhanVienTheoMa(rs.getString("maNV")));
	            pdb.setBan(new BanDAO().layTheoMa(rs.getString("maBan")));
	            pdb.setTrangThai(rs.getString("trangThai"));
	            pdb.setDeleted(rs.getBoolean("isDeleted"));
	
	            ChiTietPDBDAO chiTietPDBDAO = new ChiTietPDBDAO();
	            pdb.setChiTietPDB(chiTietPDBDAO.layTheoPhieu(pdb.getMaPDB()));
	
	            return pdb;
	        }
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

    public boolean capNhatTrangThai(String maPDB, String trangThaiMoi) {
        String sql = "UPDATE PhieuDatBan SET trangThai = ? WHERE maPDB = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThaiMoi);
            ps.setString(2, maPDB);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
