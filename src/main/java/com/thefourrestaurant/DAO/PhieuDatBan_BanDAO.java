package com.thefourrestaurant.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.Ban;

public class PhieuDatBan_BanDAO {

    // 🔹 Thêm liên kết giữa phiếu và danh sách bàn
    public boolean themLienKet(String maPDB, List<Ban> danhSachBan) {
        String sql = "INSERT INTO PhieuDatBan_Ban (maPDB, maBan) VALUES (?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Ban b : danhSachBan) {
                ps.setString(1, maPDB);
                ps.setString(2, b.getMaBan());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xóa tất cả bàn của một phiếu (khi hủy phiếu hoặc cập nhật lại)
    public boolean xoaLienKetTheoPhieu(String maPDB) {
        String sql = "DELETE FROM PhieuDatBan_Ban WHERE maPDB = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPDB);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Lấy danh sách bàn theo mã phiếu
    public List<Ban> layDanhSachBanTheoPhieu(String maPDB) {
        List<Ban> danhSach = new ArrayList<>();
        String sql = """
            SELECT b.* FROM PhieuDatBan_Ban pb
            JOIN Ban b ON pb.maBan = b.maBan
            WHERE pb.maPDB = ?
        """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPDB);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ban b = new BanDAO().layTheoMa(rs.getString("maBan"));
                danhSach.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // 🔹 Lấy danh sách phiếu theo mã bàn
    public List<String> layDanhSachPhieuTheoBan(String maBan) {
        List<String> dsPhieu = new ArrayList<>();
        String sql = "SELECT maPDB FROM PhieuDatBan_Ban WHERE maBan = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dsPhieu.add(rs.getString("maPDB"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsPhieu;
    }
}
