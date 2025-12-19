package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class TaiKhoanDAO {

    public static String taoMaTaiKhoanMoi() {
        String sql = "SELECT TOP 1 maTK FROM TaiKhoan ORDER BY maTK DESC";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String last = rs.getString(1);
                int num = Integer.parseInt(last.substring(2)) + 1;
                return String.format("TK%06d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "TK000001";
    }

    public static TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
       String sql = "SELECT " +
           "TK.maTK, TK.tenDangNhap, TK.matKhau, TK.isDeleted AS tkIsDeleted, " +
           "VT.maVT AS maVT, VT.tenVaiTro AS tenVT, VT.isDeleted AS vtIsDeleted " +
           "FROM TaiKhoan TK " +
           "JOIN VaiTro VT ON TK.maVT = VT.maVT " +
           "WHERE TK.tenDangNhap = ? AND TK.isDeleted = 0 AND VT.isDeleted = 0";


       try (Connection conn = ConnectSQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

           ps.setString(1, tenDangNhap);

           try (ResultSet rs = ps.executeQuery()) {
               if (rs.next()) {
                   String storedHash = rs.getString("matKhau");
                   if (!verifyPassword(matKhau, storedHash)) {
                       return null;
                   }

                   String maTK = rs.getString("maTK");
                   String tenDN = rs.getString("tenDangNhap");
                   boolean isDeleted = rs.getBoolean("tkIsDeleted");

                   // Tạo đối tượng VaiTro (từ khóa ngoại)
                   String maVT = rs.getString("maVT");
                   String tenVT = rs.getString("tenVT");
                   boolean vtIsDeleted = rs.getBoolean("vtIsDeleted");
                   VaiTro vaiTro = new VaiTro(maVT, tenVT, vtIsDeleted);

                   // Trả về đối tượng TaiKhoan (matKhau là hash)
                   return new TaiKhoan(maTK, tenDN, storedHash, vaiTro, isDeleted);
               }
           }

       } catch (SQLException e) {
           e.printStackTrace();
       }

        return null;
    }

    private static final int SALT_BYTES = 16;
    private static final int HASH_BYTES = 32;
    private static final int PBKDF2_ITERATIONS = 65536;

    private static String hashPassword(String password) {
        try {
            byte[] salt = new byte[SALT_BYTES];
            SecureRandom sr = SecureRandom.getInstanceStrong();
            sr.nextBytes(salt);
            byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTES);
            String saltB64 = Base64.getEncoder().encodeToString(salt);
            String hashB64 = Base64.getEncoder().encodeToString(hash);
            return saltB64 + "$" + hashB64;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean verifyPassword(String password, String stored) {
        if (stored == null || !stored.contains("$")) return false;
        String[] parts = stored.split("\\$");
        if (parts.length != 2) return false;
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hashStored = Base64.getDecoder().decode(parts[1]);
        try {
            byte[] hashInput = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, hashStored.length);
            return MessageDigestIsEqual(hashStored, hashInput);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

    private static boolean MessageDigestIsEqual(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) result |= a[i] ^ b[i];
        return result == 0;
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
            ps.setString(3, hashPassword(tk.getMatKhau()));
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
            ps.setString(2, hashPassword(tk.getMatKhau()));
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













