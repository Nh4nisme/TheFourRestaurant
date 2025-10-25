package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    public static TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
       String sql = "SELECT " +
           "TK.maTK, TK.tenDangNhap, TK.matKhau, TK.isDeleted AS tkIsDeleted, " +
           "VT.maVT AS maVT, VT.tenVaiTro AS tenVT, VT.isDeleted AS vtIsDeleted " +
           "FROM TaiKhoan TK " +
           "JOIN VaiTro VT ON TK.maVT = VT.maVT " +
           "WHERE TK.tenDangNhap = ? AND TK.matKhau = ? AND TK.isDeleted = 0 AND VT.isDeleted = 0";


       try (Connection conn = ConnectSQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

           ps.setString(1, tenDangNhap);
           ps.setString(2, matKhau);

           try (ResultSet rs = ps.executeQuery()) {
               if (rs.next()) {
                   String maTK = rs.getString("maTK");
                   String tenDN = rs.getString("tenDangNhap");
                   String mk = rs.getString("matKhau");
                   boolean isDeleted = rs.getBoolean("tkIsDeleted");

                   // Tạo đối tượng VaiTro (từ khóa ngoại)
                   String maVT = rs.getString("maVT");
                   String tenVT = rs.getString("tenVT");
                   boolean vtIsDeleted = rs.getBoolean("vtIsDeleted");
                   VaiTro vaiTro = new VaiTro(maVT, tenVT, vtIsDeleted);

                   // Trả về đối tượng TaiKhoan đầy đủ
                   return new TaiKhoan(maTK, tenDN, mk, vaiTro, isDeleted);
               }
           }

       } catch (SQLException e) {
           e.printStackTrace();
       }

        return null;
    }

    public static List<TaiKhoan> layDanhSachTaiKhoan() {
        List<TaiKhoan> ds = new ArrayList<>();
        String sql = "SELECT maTK, tenDangNhap, matKhau, maVT, isDeleted FROM TaiKhoan WHERE isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setMaTK(rs.getString("maTK"));
                tk.setTenDN(rs.getString("tenDangNhap"));
                tk.setMatKhau(rs.getString("matKhau"));

                String maVT = rs.getString("maVT");
                if (maVT != null) {
                    tk.setVaiTro(VaiTroDAO.layVaiTroTheoMa(maVT));
                }

                tk.setDeleted(rs.getBoolean("isDeleted"));
                ds.add(tk);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public static TaiKhoan layTaiKhoanTheoMa(String maTK) {
        String sql = "SELECT maTK, tenDangNhap, matKhau, maVT, isDeleted FROM TaiKhoan WHERE maTK = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaTK(rs.getString("maTK"));
                    tk.setTenDN(rs.getString("tenDangNhap"));
                    tk.setMatKhau(rs.getString("matKhau"));

                    String maVT = rs.getString("maVT");
                    if (maVT != null) {
                        tk.setVaiTro(VaiTroDAO.layVaiTroTheoMa(maVT));
                    }

                    tk.setDeleted(rs.getBoolean("isDeleted"));
                    return tk;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean themTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (MaTK, tenDangNhap, MatKhau, MaVT) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getMaTK());
            ps.setString(2, tk.getTenDN());
            ps.setString(3, tk.getMatKhau());
            ps.setString(4, tk.getVaiTro() != null ? tk.getVaiTro().getMaVT() : null);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET tenDangNhap = ?, MatKhau = ?, maVT = ? WHERE MaTK = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getTenDN());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getVaiTro().getMaVT());
            ps.setString(4, tk.getMaTK());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaTaiKhoan(String maTK) {
        String sql = "UPDATE TaiKhoan SET isDeleted = 1 WHERE maTK = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTK);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static TaiKhoan layTaiKhoanTheoTenDangNhap(String tenDN) {
        String sql = "SELECT maTK, tenDangNhap, matKhau, maVT, isDeleted FROM TaiKhoan WHERE tenDangNhap = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDN);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaTK(rs.getString("maTK"));
                    tk.setTenDN(rs.getString("tenDangNhap"));
                    tk.setMatKhau(rs.getString("matKhau"));

                    String maVT = rs.getString("maVT");
                    if (maVT != null) {
                        tk.setVaiTro(VaiTroDAO.layVaiTroTheoMa(maVT));
                    }

                    tk.setDeleted(rs.getBoolean("isDeleted"));
                    return tk;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // không tìm thấy
    }
}













