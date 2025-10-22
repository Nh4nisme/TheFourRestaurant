package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiKhuyenMai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoaiKhuyenMaiDAO {

    public List<LoaiKhuyenMai> layTatCaLoaiKhuyenMai() {
        List<LoaiKhuyenMai> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM LoaiKhuyenMai";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LoaiKhuyenMai loaiKM = new LoaiKhuyenMai();
                loaiKM.setMaLoaiKM(rs.getString("maLoaiKM"));
                loaiKM.setTenLoaiKM(rs.getString("tenLoaiKM"));
                danhSach.add(loaiKM);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
}
