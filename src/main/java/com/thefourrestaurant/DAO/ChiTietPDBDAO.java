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
            SELECT *
            FROM ChiTietPDB
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

    // 🔹 Lấy chi tiết theo mã phiếu đặt bàn
    public List<ChiTietPDB> layTheoPhieu(String maPDB) {
        List<ChiTietPDB> dsChiTiet = new ArrayList<>();
        String sql = """
        SELECT * FROM ChiTietPDB WHERE maPDB = ?
    """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPDB);
            ResultSet rs = ps.executeQuery();

            BanDAO banDAO = new BanDAO();
            MonAnDAO monAnDAO = new MonAnDAO();

            while (rs.next()) {
                Ban ban = banDAO.layTheoMa(rs.getString("maBan"));
                MonAn monAn = monAnDAO.layMonAnTheoMa(rs.getString("maMonAn"));
                PhieuDatBan pdb = new PhieuDatBan(rs.getString("maPDB"));

                ChiTietPDB ct = new ChiTietPDB(
                        rs.getString("maCT"),
                        pdb,
                        ban,
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

    // 🔹 Thêm chi tiết phiếu đặt bàn mới
    public boolean them(ChiTietPDB chiTiet) {
    	String sql = """
    		    INSERT INTO ChiTietPDB (maCT, maPDB, maBan, maMonAn, soLuong, donGia, ghiChu)
    		    VALUES (?, ?, ?, ?, ?, ?, ?)
    		""";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

        	 String maMoi = taoMaChiTietMoi();
             chiTiet.setMaCT(maMoi);

             ps.setString(1, maMoi);
             ps.setString(2, chiTiet.getPhieuDatBan().getMaPDB());
             ps.setString(3, chiTiet.getBan().getMaBan());
             ps.setString(4, chiTiet.getMonAn().getMaMonAn());
             ps.setInt(5, chiTiet.getSoLuong());
             ps.setDouble(6, chiTiet.getDonGia());
             ps.setString(7, chiTiet.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật chi tiết phiếu (VD: khi đổi món hoặc số lượng)
    public boolean capNhat(ChiTietPDB chiTiet) {
        String sql = """
            UPDATE ChiTietPDB
		    SET maBan = ?, maMon = ?, soLuong = ?, donGia = ?, ghiChu = ?
		    WHERE maCT = ?
        """;

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, chiTiet.getBan().getMaBan());
            ps.setString(2, chiTiet.getMonAn().getMaMonAn());
            ps.setInt(3, chiTiet.getSoLuong());
            ps.setDouble(4, chiTiet.getDonGia());
            ps.setString(5, chiTiet.getGhiChu());
            ps.setString(6, chiTiet.getMaCT());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xóa chi tiết phiếu
    public boolean xoa(String maCT) {
        String sql = "DELETE FROM ChiTietPDB WHERE maCT = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maCT);
            return ps.executeUpdate() > 0;

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
