package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.ChiTietHoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {

//    public static List<ChiTietHoaDon> getChiTietByMaHD(String maHD) {
//        List<ChiTietHoaDon> list = new ArrayList<>();
//        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD = ?";
//
//        try (Connection conn = ConnectSQL.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, maHD);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    String maMonAn = rs.getString("maMonAn");
//                    int soLuong = rs.getInt("soLuong");
//                    BigDecimal donGia = rs.getBigDecimal("donGia");
//
//                    list.add(new ChiTietHoaDon(maHD, maMonAn, soLuong, donGia));
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    private ChiTietHoaDon mapResultSetToChiTiet(ResultSet rs) throws SQLException {
//        String maHD = rs.getString("maHD");
//        String maMonAn = rs.getString("maMonAn");
//        int soLuong = rs.getInt("soLuong");
//        BigDecimal donGia = rs.getBigDecimal("donGia");
//
//        return new ChiTietHoaDon(maHD, maMonAn, soLuong, donGia);
//    }
}
