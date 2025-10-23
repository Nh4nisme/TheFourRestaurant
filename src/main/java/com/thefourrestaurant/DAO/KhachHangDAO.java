package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.LoaiKhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

//    public List<KhachHang> getAll() {
//        List<KhachHang> ds = new ArrayList<>();
//        String sql = "SELECT * FROM KhachHang WHERE isDeleted = false";
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                KhachHang kh = new KhachHang(
//                        rs.getString("maKH"),
//                        rs.getString("hoTen"),
//                        rs.getDate("ngaySinh"),
//                        rs.getString("gioiTinh"),
//                        rs.getString("soDT"),
//                        new LoaiKhachHang()
//                        rs.getBoolean("isDeleted")
//                );
//                ds.add(kh);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ds;
//    }
//
////    public KhachHang getById(String maKH) {
////        String sql = "SELECT * FROM KhachHang WHERE maKH = ? AND isDeleted = false";
////        try (Connection conn = Database.getConnection();
////             PreparedStatement ps = conn.prepareStatement(sql)) {
////
////            ps.setString(1, maKH);
////            try (ResultSet rs = ps.executeQuery()) {
////                if (rs.next()) {
////                    return new KhachHang(
////                            rs.getString("maKH"),
////                            rs.getString("tenKH"),
////                            rs.getString("soDienThoai"),
////                            rs.getBoolean("isDeleted")
////                    );
////                }
////            }
////
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
}
