package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    private KhuyenMai anhXaResultSetVaoKhuyenMai(ResultSet rs) throws SQLException {
        KhuyenMai km = new KhuyenMai();
        km.setMaKM(rs.getString("maKM"));
        try { km.setTenKM(rs.getString("tenKM")); } catch (SQLException ignored) {}
        try { km.setKieuKM(rs.getString("kieuKM")); } catch (SQLException ignored) {}
        try { km.setMaCode(rs.getString("maCode")); } catch (SQLException ignored) {}
        try {
            int soLuot = rs.getInt("soLuotSuDung");
            km.setSoLuotSuDung(rs.wasNull() ? null : soLuot);
        } catch (SQLException ignored) {}
        try {
            Timestamp ngayBDTimestamp = rs.getTimestamp("ngayBatDau");
            if (ngayBDTimestamp != null) km.setNgayBatDau(ngayBDTimestamp.toLocalDateTime());
        } catch (SQLException ignored) {}
        try {
            Timestamp ngayKTTimestamp = rs.getTimestamp("ngayKetThuc");
            if (ngayKTTimestamp != null) km.setNgayKetThuc(ngayKTTimestamp.toLocalDateTime());
        } catch (SQLException ignored) {}
        try { km.setMoTa(rs.getString("moTa")); } catch (SQLException ignored) {}
        try { km.setDeleted(rs.getBoolean("isDeleted")); } catch (SQLException ignored) {}

        try {
            String maLoai = rs.getString("maLoaiKM");
            if (maLoai != null) {
                LoaiKhuyenMai lkm = new LoaiKhuyenMai();
                lkm.setMaLoaiKM(maLoai);
                try { lkm.setTenLoaiKM(rs.getString("tenLoaiKM")); } catch (SQLException ignored) {}
                km.setLoaiKhuyenMai(lkm);
            }
        } catch (SQLException ignored) {}

        return km;
    }

    private String layCauTruyVanCoBan() {
        return "SELECT km.*, lkm.tenLoaiKM " +
                "FROM KhuyenMai km " +
                "LEFT JOIN LoaiKhuyenMai lkm ON km.maLoaiKM = lkm.maLoaiKM ";
    }

    public List<KhuyenMai> layDanhSachKhuyenMai() {
        List<KhuyenMai> danhSach = new ArrayList<>();
        String sql = layCauTruyVanCoBan() + " WHERE km.isDeleted = 0 ORDER BY km.maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(anhXaResultSetVaoKhuyenMai(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<KhuyenMai> layDanhSachKhuyenMaiTheoKieu(String kieuKM) {
        List<KhuyenMai> danhSach = new ArrayList<>();
        String sql = layCauTruyVanCoBan() + "WHERE km.kieuKM = ? AND km.isDeleted = 0 ORDER BY km.maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kieuKM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(anhXaResultSetVaoKhuyenMai(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<KhuyenMai> layDanhSachKhuyenMaiConHieuLucTheoKieu(String kieuKM) {
        List<KhuyenMai> danhSach = new ArrayList<>();

        String sql = """
        SELECT *
        FROM KhuyenMai
        WHERE isDeleted = 0
          AND kieuKM = ?
          AND (ngayBatDau IS NULL OR CAST(ngayBatDau AS DATE) <= CAST(GETDATE() AS DATE))
          AND (ngayKetThuc IS NULL OR CAST(ngayKetThuc AS DATE) >= CAST(GETDATE() AS DATE))
        """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kieuKM);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("maKM"));
                km.setTenKM(rs.getString("tenKM"));
                km.setKieuKM(rs.getString("kieuKM"));
                km.setMaCode(rs.getString("maCode"));
                km.setSoLuotSuDung(rs.getObject("soLuotSuDung", Integer.class));

                Timestamp nbd = rs.getTimestamp("ngayBatDau");
                if (nbd != null) km.setNgayBatDau(nbd.toLocalDateTime());

                Timestamp nkt = rs.getTimestamp("ngayKetThuc");
                if (nkt != null) km.setNgayKetThuc(nkt.toLocalDateTime());

                km.setMoTa(rs.getString("moTa"));

                danhSach.add(km);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }


    public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
        String sql = layCauTruyVanCoBan() + " WHERE km.maKM = ? AND km.isDeleted = 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return anhXaResultSetVaoKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String taoMaKhuyenMaiMoi() {
        String newId = "KM000001";
        String sql = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY maKM DESC";
        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("maKM");
                int num = Integer.parseInt(lastId.substring(2));
                num++;
                newId = String.format("KM%06d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (maKM, tenKM, maLoaiKM, kieuKM, maCode, soLuotSuDung, ngayBatDau, ngayKetThuc, moTa, isDeleted) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setString(3, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setString(4, km.getKieuKM());
            ps.setString(5, km.getMaCode());
            if (km.getSoLuotSuDung() != null) {
                ps.setInt(6, km.getSoLuotSuDung());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setObject(7, km.getNgayBatDau());
            ps.setObject(8, km.getNgayKetThuc());
            ps.setString(9, km.getMoTa());
            ps.setBoolean(10, false);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKM = ?, maLoaiKM = ?, kieuKM = ?, maCode = ?, soLuotSuDung = ?, " +
                "ngayBatDau = ?, ngayKetThuc = ?, moTa = ? WHERE maKM = ? AND isDeleted = 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getTenKM());
            ps.setString(2, km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getMaLoaiKM() : null);
            ps.setString(3, km.getKieuKM());
            ps.setString(4, km.getMaCode());
            if (km.getSoLuotSuDung() != null) {
                ps.setInt(5, km.getSoLuotSuDung());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setObject(6, km.getNgayBatDau());
            ps.setObject(7, km.getNgayKetThuc());
            ps.setString(8, km.getMoTa());
            ps.setString(9, km.getMaKM());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhuyenMai(String maKM) {
        String sql = "UPDATE KhuyenMai SET isDeleted = 1 WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhuyenMai timKhuyenMaiTheoMaHoacTen(String input) {
        String sql = layCauTruyVanCoBan() +
                " WHERE (km.maKM = ? OR km.tenKM = ?) " +
                " AND GETDATE() BETWEEN km.ngayBatDau AND km.ngayKetThuc AND km.isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, input);
            ps.setString(2, input);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return anhXaResultSetVaoKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KhuyenMai timKhuyenMaiTheoMaCode(String maCode) {
        String sql = layCauTruyVanCoBan() +
                " WHERE km.maCode = ? AND km.kieuKM = ? " +
                " AND GETDATE() BETWEEN km.ngayBatDau AND km.ngayKetThuc" +
                " AND (km.soLuotSuDung IS NULL OR km.soLuotSuDung > 0) AND km.isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maCode);
            ps.setString(2, KhuyenMai.KIEU_MA_GIAM_GIA);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return anhXaResultSetVaoKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<KhuyenMai> layDanhSachKhuyenMaiSuKienHieuLuc() {
        List<KhuyenMai> danhSach = new ArrayList<>();
        // For now ignore SQL-level filters (kieuKM / dates) — return all promotions so UI can pick
        return layDanhSachKhuyenMai();
    }

    public boolean giamSoLuotSuDung(String maKM) {
        String sql = "UPDATE KhuyenMai SET soLuotSuDung = soLuotSuDung - 1 " +
                "WHERE maKM = ? AND soLuotSuDung IS NOT NULL AND soLuotSuDung > 0";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<KhuyenMai> layKhuyenMaiDaXoa() {
        List<KhuyenMai> ds = new ArrayList<>();
        String sql = layCauTruyVanCoBan() + " WHERE km.isDeleted = 1";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(anhXaResultSetVaoKhuyenMai(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public boolean khoiPhucKhuyenMai(String maKM) {
        String sql = "UPDATE KhuyenMai SET isDeleted = 0 WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
