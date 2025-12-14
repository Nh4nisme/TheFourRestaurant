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

    // 🔹 Lấy tất cả phiếu chưa xóa
    public List<PhieuDatBan> layTatCaPhieu() {
	    List<PhieuDatBan> danhSach = new ArrayList<>();
	
	    String sql = """
	        SELECT pdb.*, 
			       kh.maKH, kh.hoTen, kh.soDT, 
			       nv.maNV, nv.hoTen AS tenNV,
			       b.maBan, b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
			       lb.maLoaiBan, lb.tenLoaiBan, lb.soChoNgoi, lb.giaTien,
			       ctpdb.maCT, m.tenMon, ctpdb.soLuong
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
	
	    try (Connection conn = ConnectSQL.getConnection();
	         Statement st = conn.createStatement();
	         ResultSet rs = st.executeQuery(sql)) {
	
	        Map<String, PhieuDatBan> mapPhieu = new HashMap<>();
	        Map<String, Ban> mapBan = new HashMap<>();
	
	        while (rs.next()) {
	            String maPDB = rs.getString("maPDB");
	            PhieuDatBan pdb = mapPhieu.get(maPDB);
	            if (pdb == null) {
	                pdb = new PhieuDatBan();
	                pdb.setMaPDB(maPDB);
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
	                    kh.setHoTen(rs.getString("hoTen"));
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
	
	            // Danh sách bàn
	            String maBan = rs.getString("maBan");
	            if (maBan != null && !mapBan.containsKey(maBan)) {
	                Ban ban = new Ban();
	                ban.setMaBan(maBan);
	                ban.setTenBan(rs.getString("tenBan"));
	                ban.setToaDoX(rs.getInt("toaDoX"));
	                ban.setToaDoY(rs.getInt("toaDoY"));
	                ban.setTrangThai(rs.getString("trangThaiBan"));
	
	                // Loại bàn
	                String maLoaiBan = rs.getString("maLoaiBan");
	                if (maLoaiBan != null) {
	                    LoaiBan lb = new LoaiBan();
	                    lb.setMaLoaiBan(maLoaiBan);
	                    lb.setTenLoaiBan(rs.getString("tenLoaiBan"));
	                    lb.setSoChoNgoi(rs.getInt("soChoNgoi"));
	                    lb.setGiaTien(rs.getBigDecimal("giaTien"));
	                    ban.setLoaiBan(lb);
	                }
	
	                pdb.getDanhSachBan().add(ban);
	                mapBan.put(maBan, ban);
	            }
	
	            // Chi tiết phiếu
	            String maChiTiet = rs.getString("maCT");
	            if (maChiTiet != null) {
	                ChiTietPDB ct = new ChiTietPDB();
	                ct.setMaCT(maChiTiet);
	                ct.setMonAn(new MonAn(rs.getString("tenMon")));
	                ct.setSoLuong(rs.getInt("soLuong"));
	                pdb.getChiTietPDB().add(ct);
	            }
	        }
	
	        danhSach.addAll(mapPhieu.values());
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	
	    return danhSach;
	}

    // 🔹 Lấy phiếu theo mã
    public PhieuDatBan layPhieuTheoMa(String maPDB) {
        String sql = """
            SELECT pdb.*, 
                   kh.maKH, kh.hoTen AS tenKH, kh.soDT,
                   nv.maNV, nv.hoTen AS tenNV,
                   b.maBan, b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
                   lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien,
                   ctpdb.maCT AS maChiTiet, m.tenMon, m.maMonAn, ctpdb.soLuong
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

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPDB);
            ResultSet rs = ps.executeQuery();

            PhieuDatBan pdb = null;
            Map<String, Ban> mapBan = new HashMap<>();
            Map<String, ChiTietPDB> mapChiTiet = new HashMap<>();

            while (rs.next()) {
                if (pdb == null) {
                    pdb = new PhieuDatBan();
                    pdb.setMaPDB(rs.getString("maPDB"));
                    pdb.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                    Timestamp tsNgayDat = rs.getTimestamp("ngayDat");
                    if (tsNgayDat != null) {
                        pdb.setNgayDat(tsNgayDat.toLocalDateTime());
                    }
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
                String maBan = rs.getString("maBan");
                if (maBan != null && !mapBan.containsKey(maBan)) {
                    Ban ban = new Ban();
                    ban.setMaBan(maBan);
                    ban.setTenBan(rs.getString("tenBan"));
                    ban.setToaDoX(rs.getInt("toaDoX"));
                    ban.setToaDoY(rs.getInt("toaDoY"));
                    ban.setTrangThai(rs.getString("trangThaiBan"));

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
                    mapBan.put(maBan, ban);
                }

                // Chi tiết phiếu
                String maChiTiet = rs.getString("maChiTiet");
                if (maChiTiet != null && !mapChiTiet.containsKey(maChiTiet)) {
                    ChiTietPDB ct = new ChiTietPDB();
                    ct.setMaCT(maChiTiet);
                    MonAn mon = new MonAn();
                    mon.setMaMonAn(rs.getString("maMonAn"));
                    mon.setTenMon(rs.getString("tenMon"));
                    ct.setMonAn(mon);
                    ct.setSoLuong(rs.getInt("soLuong"));
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

    // 🔹 Thêm phiếu mới (tự động lưu tiền cọc nếu là "Đặt trước")
    public boolean themPhieu(PhieuDatBan pdb, String context,List<Ban> danhSachBan) {
        String sql = "INSERT INTO PhieuDatBan (maPDB, ngayDat, soNguoi, maKH, maNV, trangThai, tienCoc) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
            ps.setString(6, trangThaiThucTe);
            ps.setBigDecimal(7, tienCoc);
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
            	if (danhSachBan != null && !danhSachBan.isEmpty()) {
            		
            		PhieuDatBan_BanDAO pdbbDAO = new PhieuDatBan_BanDAO();

            	    pdbbDAO.themBanVaoPhieu(maMoi, danhSachBan);

            	    banDAO.capNhatTrangThaiDanhSach(danhSachBan, trangThaiThucTe);
            	}

                return true;
            }

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
    
 // PhieuDatBanDAO
    public PhieuDatBan layPhieuDangHoatDongTheoBan(String maBan) {
        String sql = """
            SELECT pdb.*, 
                   kh.maKH, kh.hoTen AS tenKH, kh.soDT,
                   nv.maNV, nv.hoTen AS tenNV,
                   b.maBan, b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
                   lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien,
                   ctpdb.maCT, m.maMonAn, m.tenMon, ctpdb.soLuong
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

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
                    ct.setMonAn(new MonAn(rs.getString("tenMon")));
                    ct.setSoLuong(rs.getInt("soLuong"));
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
        List<PhieuDatBan> danhSach = new ArrayList<>();
        String sql = """
            SELECT pdb.*, 
                   kh.maKH, kh.hoTen AS tenKH, kh.soDT,
                   nv.maNV, nv.hoTen AS tenNV,
                   b.maBan, b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
                   lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien,
                   ctpdb.maCT, m.tenMon, m.maMonAn, ctpdb.soLuong
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

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();

            Map<String, PhieuDatBan> mapPhieu = new HashMap<>();
            Map<String, Ban> mapBan = new HashMap<>();
            Map<String, ChiTietPDB> mapChiTiet = new HashMap<>();

            while (rs.next()) {
                String maPDB = rs.getString("maPDB");
                PhieuDatBan pdb = mapPhieu.get(maPDB);
                if (pdb == null) {
                    pdb = new PhieuDatBan();
                    pdb.setMaPDB(maPDB);
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

                    mapPhieu.put(maPDB, pdb);
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
                    ct.setMonAn(new MonAn(rs.getString("tenMon")));
                    ct.setSoLuong(rs.getInt("soLuong"));
                    pdb.getChiTietPDB().add(ct);
                    mapChiTiet.put(maChiTiet, ct);
                }
            }

            danhSach.addAll(mapPhieu.values());

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
							JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
							JOIN Ban b ON pdbb.maBan = b.maBan
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
	        SELECT pdb.*, 
	               kh.maKH, kh.hoTen AS tenKH, kh.soDT,
	               nv.maNV, nv.hoTen AS tenNV,
	               b.maBan, b.tenBan, b.toaDoX, b.toaDoY, b.trangThai AS trangThaiBan,
	               lb.maLoaiBan, lb.tenLoaiBan AS tenLoai, lb.soChoNgoi, lb.giaTien,
	               ctpdb.maCT, m.maMonAn, ctpdb.soLuong
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
	
	    try (Connection conn = ConnectSQL.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	
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
	                ct.setMonAn(new MonAn(rs.getString("tenMon")));
	                ct.setSoLuong(rs.getInt("soLuong"));
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

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
    
    public Map<String, PhieuDatBan> layTatCaPhieuDangPhucVuTheoTang() {
        Map<String, PhieuDatBan> map = new HashMap<>();

        String sql = """
            SELECT pdb.*, pdbb.maBan
            FROM PhieuDatBan pdb
            JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
            WHERE pdb.trangThai = N'Đang phục vụ' AND pdb.isDeleted = 0
        """;

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String maBan = rs.getString("maBan");

                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
                pdb.setTrangThai("Đang phục vụ");

                map.put(maBan, pdb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
    
    public Map<String, PhieuDatBan> layTatCaPhieuDatTruocTheoTang() {
        Map<String, PhieuDatBan> map = new HashMap<>();

        String sql = """
            SELECT pdb.*, pdbb.maBan
            FROM PhieuDatBan pdb
            JOIN PhieuDatBan_Ban pdbb ON pdb.maPDB = pdbb.maPDB
            WHERE pdb.trangThai = N'Đặt trước' AND pdb.isDeleted = 0
        """;

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String maBan = rs.getString("maBan");

                PhieuDatBan pdb = new PhieuDatBan();
                pdb.setMaPDB(rs.getString("maPDB"));
                pdb.setNgayDat(rs.getTimestamp("ngayDat").toLocalDateTime());
                pdb.setTrangThai("Đặt trước");

                map.put(maBan, pdb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }


}
