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

    // 🔹 Thêm liên kết giữa Phiếu và Bàn
    public boolean themLienKet(String maDatBan, List<Ban> danhSachBan) {
        String sql = "INSERT INTO PhieuDatBan_Ban (maPDB, maBan) VALUES (?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Ban b : danhSachBan) {
                ps.setString(1, maDatBan);
                ps.setString(2, b.getMaBan());
                ps.addBatch();
            }
            int[] ketQua = ps.executeBatch();
            for (int n : ketQua) if (n <= 0) return false;
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
