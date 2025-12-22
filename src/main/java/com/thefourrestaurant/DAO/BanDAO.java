package com.thefourrestaurant.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.model.LoaiBan;

public class BanDAO {

	// 🔹 Lấy tất cả bàn
	public List<Ban> layTatCaBan() {
		List<Ban> dsBan = new ArrayList<>();
		String sql = """
				    SELECT b.maBan, b.tenBan, b.trangThai, b.toaDoX, b.toaDoY, b.anhBan,
				           t.maTang, t.tenTang,
				           lb.maLoaiBan, lb.tenLoaiBan, lb.giaTien, lb.soChoNgoi, lb.moTa
				    FROM Ban b
				    JOIN Tang t ON b.maTang = t.maTang
				    JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				""";

		try (Connection conn = ConnectSQL.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				Tang tang = new Tang(rs.getString("maTang"), rs.getString("tenTang"));

				LoaiBan loaiBan = new LoaiBan(rs.getString("maLoaiBan"), rs.getString("tenLoaiBan"),
						rs.getBigDecimal("giaTien"), rs.getInt("soChoNgoi"), rs.getString("moTa"));

				Ban ban = new Ban(rs.getString("maBan"), rs.getString("tenBan"), rs.getString("trangThai"),
						rs.getInt("toaDoX"), rs.getInt("toaDoY"), tang, loaiBan, rs.getString("anhBan"));

				dsBan.add(ban);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsBan;
	}

	// 🔹 Lấy bàn theo mã
	public Ban layTheoMa(String maBan) {
		String sql = """
				    SELECT b.maBan, b.tenBan, b.trangThai, b.toaDoX, b.toaDoY, b.anhBan,
				           t.maTang, t.tenTang,
				           lb.maLoaiBan, lb.tenLoaiBan, lb.giaTien, lb.soChoNgoi, lb.moTa
				    FROM Ban b
				    JOIN Tang t ON b.maTang = t.maTang
				    JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				    WHERE b.maBan = ?
				""";

		try (Connection conn = ConnectSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, maBan);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Tang tang = new Tang(rs.getString("maTang"), rs.getString("tenTang"));
				LoaiBan loaiBan = new LoaiBan(rs.getString("maLoaiBan"), rs.getString("tenLoaiBan"),
						rs.getBigDecimal("giaTien"), rs.getInt("soChoNgoi"), rs.getString("moTa"));

				return new Ban(rs.getString("maBan"), rs.getString("tenBan"), rs.getString("trangThai"),
						rs.getInt("toaDoX"), rs.getInt("toaDoY"), tang, loaiBan, rs.getString("anhBan"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int capNhatTrangThaiDanhSach(List<Ban> dsMaBan, String trangThai) {
		if (dsMaBan == null || dsMaBan.isEmpty())
			return 0;

		String sql = "UPDATE Ban SET trangThai = ? WHERE maBan = ?";
		int[] result;

		try (Connection conn = ConnectSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			for (Ban ban : dsMaBan) {
				ps.setString(1, trangThai);
				ps.setString(2, ban.getMaBan());
				ps.addBatch();
			}

			result = ps.executeBatch();
			return Arrays.stream(result).sum();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 🔹 Cập nhật tọa độ bàn
	public boolean capNhatToaDo(String maBan, int toaDoX, int toaDoY) {
		String sql = "UPDATE Ban SET toaDoX = ?, toaDoY = ? WHERE maBan = ?";
		try (Connection conn = ConnectSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, toaDoX);
			ps.setInt(2, toaDoY);
			ps.setString(3, maBan);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 🔹 Lấy danh sách bàn theo tầng
	public List<Ban> layTheoTang(String maTang) {
		List<Ban> dsBan = new ArrayList<>();
		String sql = """
				SELECT b.maBan, b.tenBan, b.trangThai, b.toaDoX, b.toaDoY, b.anhBan,
				       t.maTang, t.tenTang,
				       lb.maLoaiBan, lb.tenLoaiBan, lb.giaTien, lb.soChoNgoi, lb.moTa
				FROM Ban b
				JOIN Tang t ON b.maTang = t.maTang
				JOIN LoaiBan lb ON b.maLoaiBan = lb.maLoaiBan
				WHERE b.maTang = ?
				""";

		try (Connection conn = ConnectSQL.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, maTang);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Tang tang = new Tang(rs.getString("maTang"), rs.getString("tenTang"));

				LoaiBan loaiBan = new LoaiBan(rs.getString("maLoaiBan"), rs.getString("tenLoaiBan"),
						rs.getBigDecimal("giaTien"), rs.getInt("soChoNgoi"), rs.getString("moTa"));

				Ban ban = new Ban(rs.getString("maBan"), rs.getString("tenBan"), rs.getString("trangThai"),
						rs.getInt("toaDoX"), rs.getInt("toaDoY"), tang, loaiBan, rs.getString("anhBan"));

				dsBan.add(ban);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsBan;
	}

	public List<String> layDanhSachTrangThaiTuCSDL() {
		List<String> dsTrangThai = new ArrayList<>();
		String sql = "SELECT DISTINCT trangThai FROM Ban";

		try (Connection conn = ConnectSQL.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				dsTrangThai.add(rs.getString("trangThai"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dsTrangThai;
	}
	
	public boolean themBan(Ban ban) {
	    String sql = """
	        INSERT INTO Ban (maBan, tenBan, trangThai, maTang, maLoaiBan, toaDoX, toaDoY, anhBan)
	        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
	    """;
	    try (Connection conn = ConnectSQL.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	    	
	    	String maBanMoi = new BanDAO().sinhMaBanMoi();
	    	
	    	if (ban.getToaDoX() == 0 && ban.getToaDoY() == 0) {
	            // VD tạm: 100,100
	            ban.setToaDoX(100);
	            ban.setToaDoY(100);
	            // hoặc tính toán dựa vào parentPane nếu bạn pass được chiều rộng/chiều cao
	        }

	        ps.setString(1, maBanMoi);
	        ps.setString(2, ban.getTenBan());
	        ps.setString(3, ban.getTrangThai());
	        ps.setString(4, ban.getTang().getMaTang());
	        ps.setString(5, ban.getLoaiBan().getMaLoaiBan());
	        ps.setInt(6, ban.getToaDoX());
	        ps.setInt(7, ban.getToaDoY());
	        ps.setString(8, ban.getAnhBan());

	        return ps.executeUpdate() > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	// 🔹 Cập nhật thông tin bàn
	public boolean capNhatBan(Ban ban) {
	    String sql = """
	        UPDATE Ban 
	        SET tenBan = ?, trangThai = ?, maLoaiBan = ?, anhBan = ? 
	        WHERE maBan = ?
	    """;
	    try (Connection conn = ConnectSQL.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, ban.getTenBan());
	        ps.setString(2, ban.getTrangThai());
	        ps.setString(3, ban.getLoaiBan().getMaLoaiBan());
	        ps.setString(4, ban.getAnhBan());
	        ps.setString(5, ban.getMaBan());
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public String sinhMaBanMoi() {
	    String sql = "SELECT TOP 1 maBan FROM Ban ORDER BY maBan DESC";
	    try (Connection conn = ConnectSQL.getConnection();
	         Statement st = conn.createStatement();
	         ResultSet rs = st.executeQuery(sql)) {

	        if (rs.next()) {
	            String lastMa = rs.getString("maBan"); // VD: BA000003
	            int number = Integer.parseInt(lastMa.substring(2)); // lấy 000003 -> 3
	            return String.format("BA%06d", number + 1); // BA000004
	        } else {
	            return "BA000001"; // trường hợp chưa có bàn nào
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "BA000001";
	    }
	}
	
	public boolean xoaBan(String maBan) {
	    String sql = "DELETE FROM Ban WHERE maBan = ?";
	    try (Connection conn = ConnectSQL.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, maBan);
	        return ps.executeUpdate() > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}


}
