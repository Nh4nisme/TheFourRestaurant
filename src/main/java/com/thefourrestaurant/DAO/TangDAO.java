//package com.thefourrestaurant.DAO;
//
//import com.thefourrestaurant.connect.ConnectSQL;
//import com.thefourrestaurant.model.Tang;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TangDAO {
//
//    // Lấy tất cả tầng
//    public List<Tang> getAllTang() {
//        List<Tang> dsTang = new ArrayList<>();
//        String sql = "SELECT maTang, tenTang, moTa FROM Tang ORDER BY maTang";
//
//        try (Connection conn = ConnectSQL.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                Tang tang = new Tang(
//                        rs.getString("maTang"),
//                        rs.getString("tenTang"),
//                        rs.getString("moTa")
//                );
//                dsTang.add(tang);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return dsTang;
//    }
//}
