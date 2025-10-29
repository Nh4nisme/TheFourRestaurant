package com.thefourrestaurant.DAO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

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
                pdb.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(khachHangDAO.layKhachHangTheoMa(rs.getString("maKH")));
                pdb.setNhanVien(nhanVienDAO.layNhanVienTheoMa(rs.getString("maNV")));
                pdb.setBan(banDAO.layTheoMa(rs.getString("maBan")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setTienCoc(rs.getBigDecimal("tienCoc"));
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
                pdb.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(khachHangDAO.layKhachHangTheoMa(rs.getString("maKH")));
                pdb.setNhanVien(nhanVienDAO.layNhanVienTheoMa(rs.getString("maNV")));
                pdb.setBan(banDAO.layTheoMa(rs.getString("maBan")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setTienCoc(rs.getBigDecimal("tienCoc"));
                pdb.setDeleted(rs.getBoolean("isDeleted"));

                return pdb;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm phiếu mới (tự động lưu tiền cọc nếu là "Đặt trước")
    public boolean themPhieu(PhieuDatBan pdb, String context) {
        String sql = "INSERT INTO PhieuDatBan (maPDB, ngayDat, soNguoi, maKH, maNV, maBan, trangThai, tienCoc) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String maMoi = taoMaPhieuMoi();
            pdb.setMaPDB(maMoi);

            String trangThaiMacDinh = "Đang phục vụ";
            if ("DAT_TRUOC".equals(context)) {
                trangThaiMacDinh = "Đặt trước";
            }
            String trangThaiThucTe = (pdb.getTrangThai() != null && !pdb.getTrangThai().isBlank())
                    ? pdb.getTrangThai()
                    : trangThaiMacDinh;

            BigDecimal tienCoc = BigDecimal.ZERO;
            try {
                if ("Đặt trước".equals(trangThaiThucTe)) {
                    Ban ban = banDAO.layTheoMa(pdb.getBan().getMaBan());
                    if (ban != null && ban.getLoaiBan() != null && ban.getLoaiBan().getGiaTien() != null) {
                        tienCoc = ban.getLoaiBan().getGiaTien();
                    }
                }
            } catch (Exception ignore) {
                // Giữ tienCoc = 0 nếu có lỗi khi tra cứu giá
            }

            ps.setString(1, maMoi);
            ps.setTimestamp(2, Timestamp.valueOf(pdb.getNgayDat() != null ? pdb.getNgayDat() : LocalDateTime.now()));
            ps.setInt(3, pdb.getSoNguoi());
            ps.setString(4, pdb.getKhachHang().getMaKH());
            ps.setString(5, pdb.getNhanVien().getMaNV());
            ps.setString(6, pdb.getBan().getMaBan());
            ps.setString(7, trangThaiThucTe);
            ps.setBigDecimal(8, tienCoc);
            
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
	            pdb.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
	            pdb.setSoNguoi(rs.getInt("soNguoi"));
	            pdb.setKhachHang(new KhachHangDAO().layKhachHangTheoMa(rs.getString("maKH")));
	            pdb.setNhanVien(new NhanVienDAO().layNhanVienTheoMa(rs.getString("maNV")));
	            pdb.setBan(new BanDAO().layTheoMa(rs.getString("maBan")));
	            pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setTienCoc(rs.getBigDecimal("tienCoc"));
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

    public List<PhieuDatBan> layDanhSachPhieuDatTruocTheoBan(String maBan) {
        List<PhieuDatBan> danhSach = new ArrayList<>();
        String sql = """
	            SELECT * FROM PhieuDatBan
	            WHERE maBan = ? AND trangThai = N'Đặt trước' AND isDeleted = 0
	            """;

        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                pdb.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(new KhachHangDAO().layKhachHangTheoMa(rs.getString("maKH")));
                pdb.setNhanVien(new NhanVienDAO().layNhanVienTheoMa(rs.getString("maNV")));
                pdb.setBan(new BanDAO().layTheoMa(rs.getString("maBan")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setTienCoc(rs.getBigDecimal("tienCoc"));
                pdb.setDeleted(rs.getBoolean("isDeleted"));
                pdb.setChiTietPDB(new ChiTietPDBDAO().layTheoPhieu(pdb.getMaPDB()));

                danhSach.add(pdb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return danhSach;
    }

	public boolean huyPhieuDatBan(String maPDB) {
	    String sql = """
	            UPDATE PhieuDatBan
	            SET trangThai = N'Đã hủy'
	            WHERE maPDB = ? AND isDeleted = 0
	            """;
	    try (Connection con = ConnectSQL.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maPDB);
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

    public boolean capNhatTrangThai(String maPDB, String trangThaiMoi) {
        // Nếu cập nhật sang "Đặt trước" thì tự động gán tiền cọc = giá loại bàn
        String sqlDatTruoc = """
                UPDATE PhieuDatBan
                SET trangThai = ?,
                    tienCoc = (SELECT lb.giaTien
                               FROM PhieuDatBan pdb
                               JOIN Ban b ON pdb.maBan = b.maBan
                               JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
                               WHERE pdb.maPDB = ?)
                WHERE maPDB = ?
                """;
        String sqlKhac = "UPDATE PhieuDatBan SET trangThai = ? WHERE maPDB = ?";

        try (Connection conn = ConnectSQL.getConnection()) {
            if ("Đặt trước".equals(trangThaiMoi)) {
                try (PreparedStatement ps = conn.prepareStatement(sqlDatTruoc)) {
                    ps.setString(1, trangThaiMoi);
                    ps.setString(2, maPDB);
                    ps.setString(3, maPDB);
                    return ps.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(sqlKhac)) {
                    ps.setString(1, trangThaiMoi);
                    ps.setString(2, maPDB);
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // 🔹 Lấy phiếu đặt trước gần nhất theo mã bàn
    public PhieuDatBan layPhieuDatTruocTheoBan(String maBan) {
        String sql = """
            SELECT TOP 1 * FROM PhieuDatBan
            WHERE maBan = ? AND trangThai = N'Đặt trước' AND isDeleted = 0
            ORDER BY ngayDat DESC
            """;
        try (Connection con = ConnectSQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                pdb.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
                pdb.setSoNguoi(rs.getInt("soNguoi"));
                pdb.setKhachHang(new KhachHangDAO().layKhachHangTheoMa(rs.getString("maKH")));
                pdb.setNhanVien(new NhanVienDAO().layNhanVienTheoMa(rs.getString("maNV")));
                pdb.setBan(new BanDAO().layTheoMa(rs.getString("maBan")));
                pdb.setTrangThai(rs.getString("trangThai"));
                pdb.setTienCoc(rs.getBigDecimal("tienCoc"));
                pdb.setDeleted(rs.getBoolean("isDeleted"));
                pdb.setChiTietPDB(new ChiTietPDBDAO().layTheoPhieu(pdb.getMaPDB()));
                return pdb;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
