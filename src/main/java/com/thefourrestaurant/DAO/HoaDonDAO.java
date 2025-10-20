package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.ChiTietHoaDon;
import com.thefourrestaurant.model.HoaDon;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    public static List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sqlHD = "SELECT * FROM HoaDon";

        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlHD)) {

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                LocalDateTime ngayLap = rs.getTimestamp("ngayLap").toLocalDateTime();
                String maNV = rs.getString("maNV");
                String maKH = rs.getString("maKH");
                String maPDB = rs.getString("maPDB");
                String maKM = rs.getString("maKM");
                String maThue = rs.getString("maThue");
                BigDecimal tienKhachDua = rs.getBigDecimal("tienKhachDua");
                BigDecimal tienThua = rs.getBigDecimal("tienThua");
                String maPTTT = rs.getString("maPTTT");

                List<ChiTietHoaDon> chiTietHDs = ChiTietHoaDonDAO.getChiTietByMaHD(maHD);

                HoaDon hd = new HoaDon(maHD, ngayLap, maNV, maKH, maPDB, maKM, maThue,
                        tienKhachDua, tienThua, maPTTT, chiTietHDs);
                list.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
