package com.thefourrestaurant.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.*;

public class ChiTietPDBDAO {

    // 🔹 Lấy danh sách chi tiết theo phiếu
    public List<ChiTietPDB> layTheoPhieu(String maPDB) {
        List<ChiTietPDB> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPDB WHERE maPDB = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPDB);
            ResultSet rs = ps.executeQuery();

            MonAnDAO monAnDAO = new MonAnDAO();

            while (rs.next()) {
                MonAn monAn = monAnDAO.layMonAnTheoMa(rs.getString("maMonAn"));
                PhieuDatBan pdb = new PhieuDatBan(rs.getString("maPDB"));

                ChiTietPDB ct = new ChiTietPDB(
                        rs.getString("maCT"),
                        pdb,
                        monAn,
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("ghiChu")
                );
                dsChiTiet.add(ct);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsChiTiet;
    }

    // 🔹 Thêm chi tiết phiếu
    public boolean them(ChiTietPDB chiTiet) {
        String sql = "INSERT INTO ChiTietPDB (maCT, maPDB, maMonAn, soLuong, donGia, ghiChu) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String maMoi = taoMaChiTietMoi();
            chiTiet.setMaCT(maMoi);

            ps.setString(1, maMoi);
            ps.setString(2, chiTiet.getPhieuDatBan().getMaPDB());
            ps.setString(3, chiTiet.getMonAn().getMaMonAn());
            ps.setInt(4, chiTiet.getSoLuong());
            ps.setDouble(5, chiTiet.getDonGia());
            ps.setString(6, chiTiet.getGhiChu());

            int affected = ps.executeUpdate();
            boolean success = affected > 0;
            if (success) {
                MonAnDAO monAnDAO = new MonAnDAO();
                monAnDAO.giamSoLuong(chiTiet.getMonAn().getMaMonAn(), chiTiet.getSoLuong());
            }
            return success;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String taoMaChiTietMoi() {
        String sql = "SELECT TOP 1 maCT FROM ChiTietPDB ORDER BY maCT DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("maCT");
                int number = Integer.parseInt(lastId.substring(3)) + 1;
                return String.format("CTP%05d", number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "CTP00001";
    }
}
