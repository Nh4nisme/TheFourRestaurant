package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.ChiTietHoaDon;
import com.thefourrestaurant.model.HoaDon;
import com.thefourrestaurant.model.MonAn;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    private MonAnDAO monAnDAO;

    public ChiTietHoaDonDAO() {
        monAnDAO = new MonAnDAO();
    }

    public List<ChiTietHoaDon> layCTHDTheoMa(String maHD) {
        List<ChiTietHoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHD WHERE maHD = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hoaDon = new HoaDon();
                    MonAn monAn = monAnDAO.layMonAnTheoMa(rs.getString("maMonAn"));
                    ChiTietHoaDon ct = new ChiTietHoaDon(
                            hoaDon,
                            monAn,
                            rs.getInt("soLuong"),
                            rs.getBigDecimal("donGia")
                    );
                    ds.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

//    private ChiTietHoaDon mapResultSetToChiTiet(ResultSet rs) throws SQLException {
//        String maHD = rs.getString("maHD");
//        String maMonAn = rs.getString("maMonAn");
//        int soLuong = rs.getInt("soLuong");
//        BigDecimal donGia = rs.getBigDecimal("donGia");
//
//        return new ChiTietHoaDon(maHD, maMonAn, soLuong, donGia);
//    }
}
