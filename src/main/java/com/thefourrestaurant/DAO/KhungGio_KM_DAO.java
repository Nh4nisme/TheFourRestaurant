package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhungGio;
import com.thefourrestaurant.model.KhungGio_KM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhungGio_KM_DAO {

    private KhungGioDAO khungGioDAO = new KhungGioDAO();

    // Lấy danh sách KhungGio liên kết với một KhuyenMai cụ thể
    public List<KhungGio> layKhungGioTheoMaKM(String maKM) {
        List<KhungGio> danhSachKhungGio = new ArrayList<>();
        String sql = "SELECT kg.maTG, kg.gioBatDau, kg.gioKetThuc, kg.lapLaiHangNgay " +
                     "FROM KhungGio kg JOIN KhungGio_KM kgkm ON kg.maTG = kgkm.maTG " +
                     "WHERE kgkm.maKM = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachKhungGio.add(new KhungGio(
                            rs.getString("maTG"),
                            rs.getTime("gioBatDau").toLocalTime(),
                            rs.getTime("gioKetThuc").toLocalTime(),
                            rs.getBoolean("lapLaiHangNgay")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhungGio;
    }

    // Thêm một liên kết KhungGio_KM mới
    public boolean themKhungGio_KM(String maTG, String maKM) {
        String sql = "INSERT INTO KhungGio_KM (maTG, maKM) VALUES (?, ?)";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTG);
            ps.setString(2, maKM);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa một liên kết KhungGio_KM
    public boolean xoaKhungGio_KM(String maTG, String maKM) {
        String sql = "DELETE FROM KhungGio_KM WHERE maTG = ? AND maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTG);
            ps.setString(2, maKM);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa tất cả các liên kết KhungGio_KM cho một KhuyenMai cụ thể
    public boolean xoaTatCaKhungGio_KMTheoMaKM(String maKM) {
        String sql = "DELETE FROM KhungGio_KM WHERE maKM = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
