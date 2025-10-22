package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    public static TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
//        String sql = "SELECT TK.maTK, TK.tenDangNhap, TK.matKhau, VT.tenVaiTro " +
//                "FROM TaiKhoan TK " +
//                "JOIN VaiTro VT ON TK.maVT = VT.maVT " +
//                "WHERE TK.tenDangNhap = ? AND TK.matKhau = ? AND VT.isDeleted = 0";
//
//
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, tenDangNhap);
//            ps.setString(2, matKhau);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    String maTK = rs.getString("maTK");
//                    String tenDN = rs.getString("tenDangNhap");
//                    String mk = rs.getString("matKhau");
//                    boolean isDeleted = rs.getBoolean("isDeleted");
//
//                    // Tạo đối tượng VaiTro (từ khóa ngoại)
//                    String maVT = rs.getString("maVT");
//                    String tenVT = rs.getString("tenVT");
//                    VaiTro vaiTro = new VaiTro(maVT, tenVT);
//
//                    // Trả về đối tượng TaiKhoan đầy đủ
//                    return new TaiKhoan(maTK, tenDN, mk, vaiTro, isDeleted);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        return null;
    }

    public static List<TaiKhoan> layDanhSachTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = """
                SELECT TK.maTK, TK.tenDangNhap, TK.matKhau, TK.isDeleted,
                       VT.maVT, VT.tenVaiTro
                FROM TaiKhoan TK
                JOIN VaiTro VT ON TK.maVT = VT.maVT
                WHERE TK.isDeleted = 0
                """;

        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maTK = rs.getString("maTK");
                String tenDN = rs.getString("tenDangNhap");
                String matKhau = rs.getString("matKhau");
                boolean isDeleted = rs.getBoolean("isDeleted");

                String maVT = rs.getString("maVT");
                String tenVT = rs.getString("tenVaiTro");
                VaiTro vaiTro = new VaiTro(maVT, tenVT);

                TaiKhoan tk = new TaiKhoan(maTK, tenDN, matKhau, vaiTro, isDeleted);
                list.add(tk);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
