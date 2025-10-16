package com.thefourrestaurant.DAO;

import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.connect.ConnectSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiMonDAO {

    public List<LoaiMon> getAllLoaiMon() {
        List<LoaiMon> dsLoaiMon = new ArrayList<>();
        String sql = "SELECT maLoaiMon, tenLoaiMon FROM LoaiMonAn";

        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maLoai = rs.getString("maLoaiMon");
                String tenLoai = rs.getString("tenLoaiMon");
                dsLoaiMon.add(new LoaiMon(maLoai, tenLoai));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsLoaiMon;
    }
}
