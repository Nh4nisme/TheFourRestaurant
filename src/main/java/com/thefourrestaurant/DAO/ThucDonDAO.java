package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ThucDonDAO {

    private static final Set<String> ALLOWED = Set.of("Sáng","Trưa","Chiều","Tối");

    private String mapUiLoaiToDbLoai(String ui) {
        if (ui == null) return null;
        String s = ui.trim().toLowerCase();
        if (s.equals("coffee") || s.equals("nước giải khát")) return "Đồ nước";
        if (s.equals("đồ ăn nhanh")) return "Món đặc biệt";
        if (s.equals("cơm")) return "Cơm";
        return ui; 
    }

    public static String taoMaThucDonMoi(Connection cn) throws SQLException {
    try (PreparedStatement ps = cn.prepareStatement("SELECT TOP 1 maTD FROM dbo.ThucDon ORDER BY maTD DESC");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String last = rs.getString(1);
                int num = Integer.parseInt(last.substring(2)) + 1;
                return String.format("TD%06d", num);
            }
            return "TD000001";
        }
    }

    private String layMaTDTheoTen(Connection cn, String tenTD) throws SQLException {
    try (PreparedStatement ps = cn.prepareStatement("SELECT maTD FROM dbo.ThucDon WHERE tenTD = ?")) {
            ps.setNString(1, tenTD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        }
        return null;
    }

    private String taoHoacLayMaTD(Connection cn, String tenTD) throws SQLException {
        String ma = layMaTDTheoTen(cn, tenTD);
        if (ma != null) return ma;

        String newId = taoMaThucDonMoi(cn);
    try (PreparedStatement ps = cn.prepareStatement("INSERT INTO dbo.ThucDon(maTD, tenTD) VALUES(?, ?)")) {
            ps.setString(1, newId);
            ps.setNString(2, tenTD);
            ps.executeUpdate();
        }
        return newId;
    }

    public boolean luuThucDonTheoLoaiMon(String tenThucDon, List<String> loaiMonUi) {
        if (tenThucDon == null) return false;
        String ten = tenThucDon.trim();
        if (!ALLOWED.contains(ten)) {
            throw new IllegalArgumentException("Tên thực đơn phải là: Sáng, Trưa, Chiều, hoặc Tối.");
        }
        List<String> loaiDb = loaiMonUi == null ? List.of() :
                loaiMonUi.stream()
                         .map(this::mapUiLoaiToDbLoai)
                         .filter(Objects::nonNull)
                         .distinct()
                         .collect(Collectors.toList());

        try (Connection cn = ConnectSQL.getConnection()) {
            cn.setAutoCommit(false);
            try {
                String maTD = taoHoacLayMaTD(cn, ten);

                // Xóa chi tiết cũ (nếu muốn cập nhật lại)
                try (PreparedStatement del = cn.prepareStatement("DELETE FROM dbo.ChiTietThucDon WHERE maTD = ?")) {
                    del.setString(1, maTD);
                    del.executeUpdate();
                }

                if (!loaiDb.isEmpty()) {
                    // Lấy tất cả món thuộc các loại đã chọn
                    String inClause = loaiDb.stream().map(x -> "?").collect(Collectors.joining(","));
                    String sqlMon = """
                        SELECT maMonAn FROM dbo.MonAn ma
                        JOIN dbo.LoaiMonAn l ON ma.maLoaiMon = l.maLoaiMon
                        WHERE l.tenLoaiMon IN (""" + inClause + ") AND ma.isDeleted = 0";

                    try (PreparedStatement ps = cn.prepareStatement(sqlMon)) {
                        int i = 1;
                        for (String lm : loaiDb) ps.setNString(i++, lm);
                        try (ResultSet rs = ps.executeQuery()) {
                            // Insert từng món vào ChiTietThucDon
                try (PreparedStatement ins = cn.prepareStatement(
                    "INSERT INTO dbo.ChiTietThucDon(maMonAn, maTD) VALUES(?, ?)")) {
                                while (rs.next()) {
                                    ins.setString(1, rs.getString(1));
                                    ins.setString(2, maTD);
                                    ins.addBatch();
                                }
                                ins.executeBatch();
                            }
                        }
                    }
                }

                cn.commit();
                return true;
            } catch (Exception ex) {
                cn.rollback();
                ex.printStackTrace();
                return false;
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 208) {
                System.err.println("[ThucDonDAO] Không tìm thấy bảng dbo.ThucDon hoặc các bảng liên quan. Vui lòng khởi tạo CSDL bằng file src/main/sql/NhaHang.sql.");
            }
            e.printStackTrace();
            return false;
        }
    }

    public static class ThucDonView {
        public final String maTD;
        public final String tenTD;
        public final String loaiMon; 

        public ThucDonView(String maTD, String tenTD, String loaiMon) {
            this.maTD = maTD;
            this.tenTD = tenTD;
            this.loaiMon = loaiMon;
        }
    }

    public List<ThucDonView> layTatCaThucDonGomLoai() {
        List<ThucDonView> ds = new ArrayList<>();
        String sql = """
          SELECT td.maTD, td.tenTD,
                   STUFF((
                    SELECT DISTINCT N', ' + l2.tenLoaiMon
                    FROM dbo.ChiTietThucDon c2
                    JOIN dbo.MonAn ma2 ON c2.maMonAn = ma2.maMonAn
                    JOIN dbo.LoaiMonAn l2 ON ma2.maLoaiMon = l2.maLoaiMon
                        WHERE c2.maTD = td.maTD
                        FOR XML PATH(''), TYPE
                   ).value('.', 'NVARCHAR(MAX)'), 1, 2, N'') AS loaiMon
          FROM dbo.ThucDon td
            ORDER BY td.maTD
        """;
        try (Connection cn = ConnectSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new ThucDonView(
                        rs.getString("maTD"),
                        rs.getNString("tenTD"),
                        rs.getNString("loaiMon") == null ? "" : rs.getNString("loaiMon")
                ));
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 208) { 
                System.err.println("[ThucDonDAO] Không tìm thấy bảng hoặc view cần thiết (dbo.ThucDon/ChiTietThucDon/MonAn/LoaiMonAn). Hãy chạy script SQL khởi tạo CSDL (src/main/sql/NhaHang.sql) trên database NhaHangDB.");
            }
            e.printStackTrace();
        }
        return ds;
    }

    public List<String> layLoaiMonTheoThucDon(String tenThucDon) {
        List<String> kq = new ArrayList<>();
        String sql = """
            SELECT DISTINCT l.tenLoaiMon
            FROM dbo.ThucDon td
            JOIN dbo.ChiTietThucDon cttd ON td.maTD = cttd.maTD
            JOIN dbo.MonAn ma ON cttd.maMonAn = ma.maMonAn
            JOIN dbo.LoaiMonAn l ON ma.maLoaiMon = l.maLoaiMon
            WHERE td.tenTD = ?
            ORDER BY l.tenLoaiMon
        """;
        try (Connection cn = ConnectSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setNString(1, tenThucDon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) kq.add(rs.getNString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq;
    }
}