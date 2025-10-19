package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    public static List<TaiKhoan> layDanhSachTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT TK.maTK, TK.tenDangNhap, TK.matKhau, VT.tenVaiTro\n" +
                "FROM TaiKhoan TK\n" +
                "JOIN VaiTro VT ON TK.maVT = VT.maVT";

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
