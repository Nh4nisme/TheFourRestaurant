package com.thefourrestaurant.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiMon;

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
