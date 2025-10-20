package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DangNhapDAO {

    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
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
}