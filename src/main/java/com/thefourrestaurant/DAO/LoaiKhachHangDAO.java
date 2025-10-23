package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiKhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoaiKhachHangDAO {

    public List<LoaiKhachHang> layDanhSachLoaiKhachHang() {
        List<LoaiKhachHang> ds = new ArrayList<>();
        String sql = "select * from LoaiKhachHang where isDeleted=0";

        try(Connection conn = ConnectSQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                LoaiKhachHang kh = new LoaiKhachHang();
                kh.setMaLoaiKH(rs.getString("maLoaiKH"));
                kh.setTenLoaiKH(rs.getString("tenLoaiKH"));
                ds.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static LoaiKhachHang layLoaiKhachHangTheoMa(String maLoaiKH) {
        String sql = "select * from LoaiKhachHang where maLoaiKH=?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LoaiKhachHang kh = new LoaiKhachHang();
                    kh.setMaLoaiKH(rs.getString("maLoaiKH"));
                    kh.setTenLoaiKH(rs.getString("tenLoaiKH"));
                    return kh;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
