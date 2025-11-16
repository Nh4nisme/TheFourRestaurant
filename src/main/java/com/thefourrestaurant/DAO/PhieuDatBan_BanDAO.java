package com.thefourrestaurant.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.model.LoaiBan;

public class PhieuDatBan_BanDAO {

    // 🔹 Lấy danh sách bàn theo mã phiếu với thông tin đầy đủ
    public List<Ban> layDanhSachBanTheoPhieu(String maPDB) {
        List<Ban> list = new ArrayList<>();
        String sql = """
            SELECT b.maBan, b.tenBan, b.trangThai, b.toaDoX, b.toaDoY,
                   t.maTang, t.tenTang,
                   lb.maLoaiBan, lb.tenLoaiBan, lb.giaTien, lb.soChoNgoi, lb.moTa
            FROM PhieuDatBan_Ban pdbb
            JOIN Ban b ON pdbb.maBan = b.maBan
            JOIN Tang t ON b.maTang = t.maTang
            JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
            WHERE pdbb.maPDB = ?
        """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPDB);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ban b = new Ban();
                b.setMaBan(rs.getString("maBan"));
                b.setTenBan(rs.getString("tenBan"));
                b.setTrangThai(rs.getString("trangThai"));
                b.setToaDoX(rs.getInt("toaDoX"));
                b.setToaDoY(rs.getInt("toaDoY"));

                // Tang
                Tang tang = new Tang();
                tang.setMaTang(rs.getString("maTang"));
                tang.setTenTang(rs.getString("tenTang"));
                b.setTang(tang);

                // LoaiBan
                LoaiBan lb = new LoaiBan();
                lb.setMaLoaiBan(rs.getString("maLoaiBan"));
                lb.setTenLoaiBan(rs.getString("tenLoaiBan"));
                lb.setGiaTien(rs.getBigDecimal("giaTien"));
                lb.setSoChoNgoi(rs.getInt("soChoNgoi"));
                lb.setMoTa(rs.getString("moTa"));
                b.setLoaiBan(lb);

                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themBanVaoPhieu(String maPDB, List<Ban> danhSachBan) {

        String sqlCheckPDB = "SELECT 1 FROM PhieuDatBan WHERE maPDB = ?";
        String sqlCheckBan = "SELECT 1 FROM Ban WHERE maBan = ?";
        String sqlCheckBanDaDat =
                "SELECT 1 FROM PhieuDatBan_Ban WHERE maBan = ? AND maPDB <> ?";
        String sqlInsert =
                "INSERT INTO PhieuDatBan_Ban (maPDB, maBan) VALUES (?, ?)";

        try (Connection conn = ConnectSQL.getConnection()) {

            conn.setAutoCommit(false); // mở transaction

            // 1. Kiểm tra phiếu có tồn tại
            try (PreparedStatement ps = conn.prepareStatement(sqlCheckPDB)) {
                ps.setString(1, maPDB);
                if (!ps.executeQuery().next()) {
                    System.err.println("Phiếu đặt bàn không tồn tại: " + maPDB);
                    conn.rollback();
                    return false;
                }
            }

            // 2. Lặp từng bàn trong List<Ban>
            for (Ban ban : danhSachBan) {
                String maBan = ban.getMaBan();

                // 2.1 Bàn có tồn tại?
                try (PreparedStatement ps = conn.prepareStatement(sqlCheckBan)) {
                    ps.setString(1, maBan);
                    if (!ps.executeQuery().next()) {
                        System.err.println("Bàn không tồn tại: " + maBan);
                        conn.rollback();
                        return false;
                    }
                }

                // 2.2 Bàn đã được đặt bởi phiếu khác chưa?
                try (PreparedStatement ps = conn.prepareStatement(sqlCheckBanDaDat)) {
                    ps.setString(1, maBan);
                    ps.setString(2, maPDB);
                    if (ps.executeQuery().next()) {
                        System.err.println("Bàn " + maBan + " đã được đặt bởi phiếu khác!");
                        conn.rollback();
                        return false;
                    }
                }

                // 2.3 Thêm vào bảng liên kết
                try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                    ps.setString(1, maPDB);
                    ps.setString(2, maBan);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
