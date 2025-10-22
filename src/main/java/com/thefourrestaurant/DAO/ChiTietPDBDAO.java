package com.thefourrestaurant.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.*;

public class ChiTietPDBDAO {

    // 🔹 Lấy toàn bộ chi tiết phiếu đặt bàn
    public List<ChiTietPDB> layTatCa() {
        List<ChiTietPDB> dsChiTiet = new ArrayList<>();
        String sql = """
            SELECT maCT, maPDB, maBan, maMon, soLuong, donGia
            FROM ChiTietPhieuDatBan
        """;

        try (Connection conn = ConnectSQL.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ChiTietPDB ct = new ChiTietPDB(
                        rs.getString("maCT"),
                        new PhieuDatBan(rs.getString("maPDB")),
                        new Ban(rs.getString("maBan")),
                        new MonAn(rs.getString("maMon")),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia")
                );
                dsChiTiet.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsChiTiet;
    }

    // 🔹 Lấy chi tiết theo mã phiếu đặt bàn
    public List<ChiTietPDB> layTheoPhieu(String maPDB) {
        List<ChiTietPDB> dsChiTiet = new ArrayList<>();
        String sql = """
            SELECT maCT, maPDB, maBan, maMon, soLuong, donGia
            FROM ChiTietPhieuDatBan
            WHERE maPDB = ?
        """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPDB);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dsChiTiet.add(new ChiTietPDB(
                        rs.getString("maCT"),
                        new PhieuDatBan(rs.getString("maPDB")),
                        new Ban(rs.getString("maBan")),
                        new MonAn(rs.getString("maMon")),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsChiTiet;
    }

    // 🔹 Thêm chi tiết phiếu đặt bàn mới
    public boolean them(ChiTietPDB chiTiet) {
        String sql = """
            INSERT INTO ChiTietPhieuDatBan (maCT, maPDB, maBan, maMon, soLuong, donGia)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, chiTiet.getMaCT());
            ps.setString(2, chiTiet.getPhieuDatBan().getMaPDB());
            ps.setString(3, chiTiet.getBan().getMaBan());
            ps.setString(4, chiTiet.getMonAn().getMaMonAn());
            ps.setInt(5, chiTiet.getSoLuong());
            ps.setDouble(6, chiTiet.getDonGia());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật chi tiết phiếu (VD: khi đổi món hoặc số lượng)
    public boolean capNhat(ChiTietPDB chiTiet) {
        String sql = """
            UPDATE ChiTietPhieuDatBan
            SET maBan = ?, maMon = ?, soLuong = ?, donGia = ?
            WHERE maCT = ?
        """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, chiTiet.getBan().getMaBan());
            ps.setString(2, chiTiet.getMonAn().getMaMonAn());
            ps.setInt(3, chiTiet.getSoLuong());
            ps.setDouble(4, chiTiet.getDonGia());
            ps.setString(5, chiTiet.getMaCT());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xóa chi tiết phiếu
    public boolean xoa(String maCT) {
        String sql = "DELETE FROM ChiTietPhieuDatBan WHERE maCT = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maCT);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
