package com.thefourrestaurant.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.model.LoaiBan;

public class PhieuDatBan_BanDAO {

	public List<Ban> layDanhSachBanTheoPhieu(String maPDB) {
		List<Ban> list = new ArrayList<>();
		String sql = """
				    SELECT pdbb.isBanChinh,
				    	   b.maBan, b.tenBan, b.trangThai, b.toaDoX, b.toaDoY,
				           t.maTang, t.tenTang,
				           lb.maLoaiBan, lb.tenLoaiBan, lb.giaTien, lb.soChoNgoi, lb.moTa
				    FROM PhieuDatBan_Ban pdbb
				    JOIN Ban b ON pdbb.maBan = b.maBan
				    JOIN Tang t ON b.maTang = t.maTang
				    JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				    WHERE pdbb.maPDB = ?
				""";

		try (Connection conn = ConnectSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

	public boolean themBanVaoPhieu(Connection conn, String maPDB, List<Ban> danhSachBan) throws SQLException {

		String sqlInsert = "INSERT INTO PhieuDatBan_Ban (maPDB, maBan) VALUES (?, ?)";

		for (Ban ban : danhSachBan) {
			try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
				ps.setString(1, maPDB);
				ps.setString(2, ban.getMaBan());
				ps.executeUpdate();
			}
		}
		return true;
	}
	
	public boolean xoaTatCaBanKhoiPhieu(Connection conn, String maPDB) throws SQLException {
	    String sql = "DELETE FROM PhieuDatBan_Ban WHERE maPDB = ?";
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, maPDB);
	        ps.executeUpdate();
	    }
	    return true;
	}


}
