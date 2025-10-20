package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    public static TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT TK.maTK, TK.tenDangNhap, TK.matKhau, VT.tenVaiTro " +
                "FROM TaiKhoan TK " +
                "JOIN VaiTro VT ON TK.maVT = VT.maVT " +
                "WHERE TK.tenDangNhap = ? AND TK.matKhau = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String maTK = rs.getString("maTK");
                    String tenDN = rs.getString("tenDangNhap");
                    String mk = rs.getString("matKhau");
                    String vaiTro = rs.getString("tenVaiTro");
                    return new TaiKhoan(maTK, tenDN, mk, vaiTro);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<TaiKhoan> layDanhSachTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT TK.maTK, TK.tenDangNhap, TK.matKhau, VT.tenVaiTro\n" +
                "FROM TaiKhoan TK\n" +
                "JOIN VaiTro VT ON TK.maVT = VT.maVT    ";

        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maTK = rs.getString("MaTK");
                String tenDN = rs.getString("TenDangNhap");
                String matKhau = rs.getString("MatKhau");
                String vaiTro = rs.getString("TenVaiTro");

                list.add(new TaiKhoan(maTK, tenDN, matKhau, vaiTro));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
