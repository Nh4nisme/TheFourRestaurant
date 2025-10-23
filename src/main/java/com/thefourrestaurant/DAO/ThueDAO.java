package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.Thue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ThueDAO {
    public Thue layThueTheoMa(String maThue) {
        String sql = "select * from Thue where maThue=?";
        try(Connection conn = ConnectSQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maThue);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Thue(
                            rs.getString("maThue"),
                            rs.getInt("tyLe"),
                            rs.getString("ghiChu"),
                            new LoaiThueDAO().layLoaiThueTheoMa("maLoaiThue")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

