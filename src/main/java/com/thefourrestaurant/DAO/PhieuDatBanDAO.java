package com.thefourrestaurant.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.*;

public class PhieuDatBanDAO {
	private BanDAO banDAO = new BanDAO();

	private void mapResultSetToPhieuDatBan(ResultSet rs, PhieuDatBan pdb) throws SQLException {

		// ===== BÀN =====
		String maBan = rs.getString("maBan");
		if (maBan != null) {
			boolean daCo = pdb.getDanhSachBan().stream().anyMatch(b -> b.getMaBan().equals(maBan));

			if (!daCo) {
				Ban ban = new Ban();
				ban.setMaBan(maBan);
				ban.setTenBan(rs.getString("tenBan"));
				ban.setToaDoX(rs.getInt("toaDoX"));
				ban.setToaDoY(rs.getInt("toaDoY"));
				ban.setTrangThai(rs.getString("trangThaiBan"));
				ban.setBanChinh(rs.getBoolean("isBanChinh"));

				String maLoaiBan = rs.getString("maLoaiBan");
				if (maLoaiBan != null) {
					LoaiBan lb = new LoaiBan();
					lb.setMaLoaiBan(maLoaiBan);
					lb.setTenLoaiBan(rs.getString("tenLoai"));
					lb.setSoChoNgoi(rs.getInt("soChoNgoi"));
					lb.setGiaTien(rs.getBigDecimal("giaTien"));
					ban.setLoaiBan(lb);
				}

				pdb.getDanhSachBan().add(ban);
			}
		}

		// ===== CHI TIẾT PHIẾU =====
		String maCT = rs.getString("maCT");
		if (maCT != null) {
			boolean daCoCT = pdb.getChiTietPDB().stream().anyMatch(ct -> ct.getMaCT().equals(maCT));

			if (!daCoCT) {
				ChiTietPDB ct = new ChiTietPDB();
				ct.setMaCT(maCT);

				MonAn mon = new MonAn();
				mon.setMaMonAn(rs.getString("maMonAn"));
				mon.setTenMon(rs.getString("tenMon"));
				mon.setDonGia(rs.getBigDecimal("donGia"));

				ct.setMonAn(mon);
				ct.setSoLuong(rs.getInt("soLuong"));
				ct.setDonGia(rs.getDouble("donGia"));
				ct.setGhiChu(rs.getString("ghiChu"));

				pdb.getChiTietPDB().add(ct);
			}
		}
	}

	// 🔹 Lấy tất cả phiếu chưa xóa
	private List<PhieuDatBan> layPhieuTheoQuery(String sql, Object... params) {
		Map<String, PhieuDatBan> mapPhieu = new HashMap<>();

		try (Connection conn = ConnectSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String maPDB = rs.getString("maPDB");
				PhieuDatBan pdb = mapPhieu.get(maPDB);

				if (pdb == null) {
					pdb = new PhieuDatBan();
					pdb.setMaPDB(maPDB);

					Timestamp tsNgayTao = rs.getTimestamp("ngayTao");
					if (tsNgayTao != null)
						pdb.setNgayTao(tsNgayTao.toLocalDateTime());

					Timestamp tsNgayDat = rs.getTimestamp("ngayDat");
					if (tsNgayDat != null)
						pdb.setNgayDat(tsNgayDat.toLocalDateTime());

					pdb.setSoNguoi(rs.getInt("soNguoi"));
					pdb.setTrangThai(rs.getString("trangThai"));
					pdb.setTienCoc(rs.getBigDecimal("tienCoc"));
					pdb.setDeleted(rs.getBoolean("isDeleted"));
					pdb.setDanhSachBan(new ArrayList<>());
					pdb.setChiTietPDB(new ArrayList<>());

					// Khách hàng
					String maKH = rs.getString("maKH");
					if (maKH != null) {
						KhachHang kh = new KhachHang();
						kh.setMaKH(maKH);
						kh.setHoTen(rs.getString("tenKH"));
						kh.setSoDT(rs.getString("soDT"));
						pdb.setKhachHang(kh);
					}

					// Nhân viên
					String maNV = rs.getString("maNV");
					if (maNV != null) {
						NhanVien nv = new NhanVien();
						nv.setMaNV(maNV);
						nv.setHoTen(rs.getString("tenNV"));
						pdb.setNhanVien(nv);
					}

					mapPhieu.put(maPDB, pdb);
				}

				// map bàn & chi tiết cho pdb hiện tại
				mapResultSetToPhieuDatBan(rs, pdb);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>(mapPhieu.values());
	}

	public List<PhieuDatBan> layTatCaPhieu() {
		String sql = """
				    SELECT pdb.*,
				           kh.maKH, kh.hoTen AS tenKH, kh.soDT,
				           nv.maNV, nv.hoTen AS tenNV,
				           pdbb.isBanChinh,
				           b.maBan, b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
				           lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien,
				           ctpdb.maCT, ctpdb.donGia, ctpdb.ghiChu, m.tenMon, m.maMonAn, ctpdb.soLuong
				    FROM PhieuDatBan pdb
				    LEFT JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
				    LEFT JOIN Ban b ON pdbb.maBan = b.maBan
				    LEFT JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				    LEFT JOIN KhachHang kh ON pdb.maKH = kh.maKH
				    LEFT JOIN NhanVien nv ON pdb.maNV = nv.maNV
				    LEFT JOIN ChiTietPDB ctpdb ON pdb.maPDB = ctpdb.maPDB
				    LEFT JOIN MonAn m ON ctpdb.maMonAn = m.maMonAn
				    WHERE pdb.isDeleted = 0
				""";
		return layPhieuTheoQuery(sql);
	}

	// 🔹 Lấy phiếu theo mã
	public PhieuDatBan layPhieuTheoMa(String maPDB) {
		String sql = """
				    SELECT pdb.*,
				           kh.maKH, kh.hoTen AS tenKH, kh.soDT,
				           nv.maNV, nv.hoTen AS tenNV,
				           pdbb.isBanChinh,
				           b.maBan, b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
				           lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien,
				           ctpdb.maCT, ctpdb.donGia, ctpdb.ghiChu, m.tenMon, m.maMonAn, ctpdb.soLuong
				    FROM PhieuDatBan pdb
				    LEFT JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
				    LEFT JOIN Ban b ON pdbb.maBan = b.maBan
				    LEFT JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				    LEFT JOIN KhachHang kh ON pdb.maKH = kh.maKH
				    LEFT JOIN NhanVien nv ON pdb.maNV = nv.maNV
				    LEFT JOIN ChiTietPDB ctpdb ON pdb.maPDB = ctpdb.maPDB
				    LEFT JOIN MonAn m ON ctpdb.maMonAn = m.maMonAn
				    WHERE pdb.maPDB = ? AND pdb.isDeleted = 0
				""";
		List<PhieuDatBan> list = layPhieuTheoQuery(sql, maPDB);
		return list.isEmpty() ? null : list.get(0);
	}

	public List<PhieuDatBan> layPhieuTheoTrangThai(String trangThai) {
		String sql = """
				    SELECT pdb.*, pdbb.maBan, kh.maKH, kh.hoTen AS tenKH, kh.soDT,
				           nv.maNV, nv.hoTen AS tenNV,
				           pdbb.isBanChinh,
				           b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
				           lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien,
				           ctpdb.maCT, ctpdb.donGia, ctpdb.ghiChu, m.tenMon, m.maMonAn, ctpdb.soLuong
				    FROM PhieuDatBan pdb
				    JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
				    LEFT JOIN Ban b ON pdbb.maBan = b.maBan
				    LEFT JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				    LEFT JOIN KhachHang kh ON pdb.maKH = kh.maKH
				    LEFT JOIN NhanVien nv ON pdb.maNV = nv.maNV
				    LEFT JOIN ChiTietPDB ctpdb ON pdb.maPDB = ctpdb.maPDB
				    LEFT JOIN MonAn m ON ctpdb.maMonAn = m.maMonAn
				    WHERE pdb.trangThai = ? AND pdb.isDeleted = 0
				""";
		return layPhieuTheoQuery(sql, trangThai);
	}

	public PhieuDatBan layPhieuDatTruocTheoBan(String maBan) {
		List<PhieuDatBan> ds = layDanhSachPhieuDatTruocTheoBan(maBan);
		return ds.isEmpty() ? null : ds.get(0);
	}

	// 🔹 Thêm phiếu mới (tự động lưu tiền cọc nếu là "Đặt trước")
	public boolean themPhieu(PhieuDatBan pdb, String context, List<Ban> danhSachBan) {

		String sql = """
				    INSERT INTO PhieuDatBan
				    (maPDB, ngayDat, soNguoi, maKH, maNV, trangThai, tienCoc)
				    VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		Connection conn = null;

		try {
			conn = ConnectSQL.getConnection();
			conn.setAutoCommit(false); // transaction duy nhất

			String maMoi = taoMaPhieuMoi();
			pdb.setMaPDB(maMoi);

			String trangThaiPhieu = "DAT_TRUOC".equals(context) ? "Đặt trước" : "Đang phục vụ";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, maMoi);
			ps.setTimestamp(2, Timestamp.valueOf(pdb.getNgayDat()));
			ps.setInt(3, pdb.getSoNguoi());
			ps.setString(4, pdb.getKhachHang().getMaKH());
			ps.setString(5, pdb.getNhanVien().getMaNV());
			ps.setString(6, trangThaiPhieu);
			ps.setBigDecimal(7, BigDecimal.ZERO);
			ps.executeUpdate();
			
			if (!danhSachBan.isEmpty()) {
			    danhSachBan.get(0).setBanChinh(true);
			    for (int i = 1; i < danhSachBan.size(); i++) {
			        danhSachBan.get(i).setBanChinh(false);
			    }
			}

			// INSERT bảng liên kết CÙNG CONNECTION
			new PhieuDatBan_BanDAO().themBanVaoPhieu(conn, maMoi, danhSachBan);

			// chỉ DAT_NGAY mới đổi trạng thái bàn
			if ("DAT_NGAY".equals(context)) {
				banDAO.capNhatTrangThaiDanhSach(danhSachBan, trangThaiPhieu);
			}

			conn.commit();
			return true;

		} catch (Exception e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}

	// Xóa phiếu (xóa logic, set isDeleted = 1)
	public boolean xoaPhieu(String maPDB) {
		String sql = "UPDATE PhieuDatBan SET isDeleted = 1 WHERE maPDB = ?";
		try (Connection conn = ConnectSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

	// PhieuDatBanDAO
	public PhieuDatBan layPhieuDangHoatDongTheoBan(String maBan) {
		String sql = """
				    SELECT pdb.*,
				           kh.maKH, kh.hoTen AS tenKH, kh.soDT,
				           nv.maNV, nv.hoTen AS tenNV,
				           pdbb.isBanChinh,
				           b.maBan, b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
				           lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien,
				           ctpdb.maCT, ctpdb.donGia, ctpdb.ghiChu, m.maMonAn, m.tenMon, ctpdb.soLuong
				    FROM PhieuDatBan pdb
				    JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
				    LEFT JOIN Ban b ON pdbb.maBan = b.maBan
				    LEFT JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				    LEFT JOIN KhachHang kh ON pdb.maKH = kh.maKH
				    LEFT JOIN NhanVien nv ON pdb.maNV = nv.maNV
				    LEFT JOIN ChiTietPDB ctpdb ON pdb.maPDB = ctpdb.maPDB
				    LEFT JOIN MonAn m ON ctpdb.maMonAn = m.maMonAn
				    WHERE pdbb.maBan = ? AND pdb.trangThai = N'Đang phục vụ' AND pdb.isDeleted = 0
				""";

		try (Connection conn = ConnectSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, maBan);
			ResultSet rs = ps.executeQuery();

			PhieuDatBan pdb = null;
			Map<String, Ban> mapBan = new HashMap<>();
			Map<String, ChiTietPDB> mapChiTiet = new HashMap<>();

			while (rs.next()) {
				if (pdb == null) {
					pdb = new PhieuDatBan();
					pdb.setMaPDB(rs.getString("maPDB"));
					pdb.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
					pdb.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
					pdb.setSoNguoi(rs.getInt("soNguoi"));
					pdb.setTrangThai(rs.getString("trangThai"));
					pdb.setTienCoc(rs.getBigDecimal("tienCoc"));
					pdb.setDeleted(rs.getBoolean("isDeleted"));
					pdb.setDanhSachBan(new ArrayList<>());
					pdb.setChiTietPDB(new ArrayList<>());

					// Khách hàng
					String maKH = rs.getString("maKH");
					if (maKH != null) {
						KhachHang kh = new KhachHang();
						kh.setMaKH(maKH);
						kh.setHoTen(rs.getString("tenKH"));
						kh.setSoDT(rs.getString("soDT"));
						pdb.setKhachHang(kh);
					}

					// Nhân viên
					String maNV = rs.getString("maNV");
					if (maNV != null) {
						NhanVien nv = new NhanVien();
						nv.setMaNV(maNV);
						nv.setHoTen(rs.getString("tenNV"));
						pdb.setNhanVien(nv);
					}
				}

				// Bàn
				String maBanRs = rs.getString("maBan");
				if (maBanRs != null && !mapBan.containsKey(maBanRs)) {
					Ban ban = new Ban();
					ban.setMaBan(maBanRs);
					ban.setTenBan(rs.getString("tenBan"));
					ban.setToaDoX(rs.getInt("toaDoX"));
					ban.setToaDoY(rs.getInt("toaDoY"));
					ban.setTrangThai(rs.getString("trangThaiBan"));
					ban.setBanChinh(rs.getBoolean("isBanChinh"));

					// Loại bàn
					String maLoaiBan = rs.getString("maLoaiBan");
					if (maLoaiBan != null) {
						LoaiBan lb = new LoaiBan();
						lb.setMaLoaiBan(maLoaiBan);
						lb.setTenLoaiBan(rs.getString("tenLoai"));
						lb.setSoChoNgoi(rs.getInt("soChoNgoi"));
						lb.setGiaTien(rs.getBigDecimal("giaTien"));
						ban.setLoaiBan(lb);
					}

					pdb.getDanhSachBan().add(ban);
					mapBan.put(maBanRs, ban);
				}

				// Chi tiết phiếu
				String maChiTiet = rs.getString("maCT");
				if (maChiTiet != null && !mapChiTiet.containsKey(maChiTiet)) {
					ChiTietPDB ct = new ChiTietPDB();
					ct.setMaCT(maChiTiet);

					MonAn mon = new MonAn();
					mon.setMaMonAn(rs.getString("maMonAn"));
					mon.setTenMon(rs.getString("tenMon"));
					mon.setDonGia(rs.getBigDecimal("donGia"));
					ct.setMonAn(mon);

					ct.setSoLuong(rs.getInt("soLuong"));
					ct.setDonGia(rs.getDouble("donGia"));
					ct.setGhiChu(rs.getString("ghiChu"));

					pdb.getChiTietPDB().add(ct);
					mapChiTiet.put(maChiTiet, ct);
				}

			}

			return pdb;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<PhieuDatBan> layDanhSachPhieuDatTruocTheoBan(String maBan) {
		String sql = """
				    SELECT pdb.*, kh.maKH, kh.hoTen AS tenKH, kh.soDT,
				           nv.maNV, nv.hoTen AS tenNV,
				           pdbb.isBanChinh,
				           b.maBan, b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
				           lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien,
				           ctpdb.maCT, ctpdb.donGia, ctpdb.ghiChu, m.tenMon, m.maMonAn, ctpdb.soLuong
				    FROM PhieuDatBan pdb
				    JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
				    LEFT JOIN Ban b ON pdbb.maBan = b.maBan
				    LEFT JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				    LEFT JOIN KhachHang kh ON pdb.maKH = kh.maKH
				    LEFT JOIN NhanVien nv ON pdb.maNV = nv.maNV
				    LEFT JOIN ChiTietPDB ctpdb ON pdb.maPDB = ctpdb.maPDB
				    LEFT JOIN MonAn m ON ctpdb.maMonAn = m.maMonAn
				    WHERE pdbb.maBan = ? AND pdb.trangThai = N'Đặt trước' AND pdb.isDeleted = 0
				    ORDER BY pdb.ngayDat DESC
				""";
		return layPhieuTheoQuery(sql, maBan);
	}

	public boolean huyPhieuDatBan(String maPDB) {
		String sql = """
				UPDATE PhieuDatBan
				SET trangThai = N'Đã hủy'
				WHERE maPDB = ? AND isDeleted = 0
				""";
		try (Connection con = ConnectSQL.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
				JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
				JOIN Ban b ON pdbb.maBan = b.maBan
				JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				WHERE pdb.maPDB = ?)
				         WHERE maPDB = ? AND pdbb.isBanChinh = 1
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

	public boolean kiemTraTrungGioDatTruoc(String maBan, LocalDateTime gioBatDauMoi) {
		LocalDateTime gioKetThucMoi = gioBatDauMoi.plusMinutes(90); // 1h30p

		String sql = """
				    SELECT ngayDat
				    FROM PhieuDatBan pdb
				    JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
				    WHERE pdbb.maBan = ?
				      AND pdb.trangThai = N'Đặt trước'
				      AND pdb.isDeleted = 0
				""";

		try (Connection conn = ConnectSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maBan);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LocalDateTime gioBatDauCu = rs.getTimestamp("ngayDat").toLocalDateTime();
				LocalDateTime gioKetThucCu = gioBatDauCu.plusMinutes(90);

				// Kiểm tra giao nhau
				if (gioBatDauMoi.isBefore(gioKetThucCu) && gioKetThucMoi.isAfter(gioBatDauCu)) {
					return true; // trùng giờ
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false; // không trùng
	}

	public Map<String, PhieuDatBan> layTatCaPhieuDatTruoc() {
		Map<String, PhieuDatBan> map = new HashMap<>();

		List<PhieuDatBan> ds = layPhieuTheoTrangThai("Đặt trước");

		for (PhieuDatBan pdb : ds) {
			for (Ban ban : pdb.getDanhSachBan()) {
				if (ban.isBanChinh()) {
				    map.put(ban.getMaBan(), pdb);
				}
			}
		}
		return map;
	}

	public Map<String, PhieuDatBan> layTatCaPhieuDangPhucVu() {
		Map<String, PhieuDatBan> map = new HashMap<>();

		List<PhieuDatBan> ds = layPhieuTheoTrangThai("Đang phục vụ");

		for (PhieuDatBan pdb : ds) {
			if (pdb.getDanhSachBan() != null) {
				for (Ban ban : pdb.getDanhSachBan()) {
					if (ban.isBanChinh()) {
					    map.put(ban.getMaBan(), pdb);
					}
				}
			}
		}

		return map;
	}
	
	public boolean chuyenCumBanDangPhucVu(
	        String maPDB,
	        List<Ban> banCu,
	        List<Ban> banMoi
	) {
	    String xoaBanCu = "DELETE FROM PhieuDatBan_Ban WHERE maPDB = ? AND isBanChinh = 0";
	    Connection conn = null;

	    try {
	        conn = ConnectSQL.getConnection();
	        conn.setAutoCommit(false);

	        // 1. Gỡ bàn cũ
	        try (PreparedStatement ps = conn.prepareStatement(xoaBanCu)) {
	            ps.setString(1, maPDB);
	            ps.executeUpdate();
	        }
	        
	        boolean coBanChinh = banMoi.stream().anyMatch(Ban::isBanChinh);
	        if (!coBanChinh) {
	            throw new IllegalStateException("Cụm bàn mới phải có bàn chính");
	        }

	        // 2. Thêm bàn mới
	        new PhieuDatBan_BanDAO().themBanVaoPhieu(conn, maPDB, banMoi);

	        // 3. Cập nhật trạng thái bàn
	        banDAO.capNhatTrangThaiDanhSach(banCu, "Trống");
	        banDAO.capNhatTrangThaiDanhSach(banMoi, "Đang phục vụ");

	        conn.commit();
	        return true;

	    } catch (Exception e) {
	        try {
	            if (conn != null) conn.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public Ban layBanChinhCuaPhieu(String maPDB) {
	    String sql = """
	        SELECT b.maBan, b.tenBan, b.trangThai, b.maTang,
	               lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien
	        FROM PhieuDatBan_Ban pdb
	        JOIN Ban b ON pdb.maBan = b.maBan
	        LEFT JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
	        WHERE pdb.maPDB = ? AND pdb.isBanChinh = 1
	    """;

	    try (Connection conn = ConnectSQL.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, maPDB);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                Ban ban = new Ban();
	                ban.setMaBan(rs.getString("maBan"));
	                ban.setTenBan(rs.getString("tenBan"));
	                ban.setTrangThai(rs.getString("trangThai"));

	                Tang tang = new Tang();
	                tang.setMaTang(rs.getString("maTang"));
	                ban.setTang(tang);

	                // LoaiBan
	                String maLoaiBan = rs.getString("maLoaiBan");
	                if (maLoaiBan != null) {
	                    LoaiBan lb = new LoaiBan();
	                    lb.setMaLoaiBan(maLoaiBan);
	                    lb.setTenLoaiBan(rs.getString("tenLoai"));
	                    lb.setSoChoNgoi(rs.getInt("soChoNgoi"));
	                    lb.setGiaTien(rs.getBigDecimal("giaTien"));
	                    ban.setLoaiBan(lb);
	                }

	                return ban;
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}

}
