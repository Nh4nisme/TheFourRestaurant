package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.LoaiThue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoaiThueDAO {
    public LoaiThue layLoaiThueTheoMa(String maLoaiThue) {
        String sql = "select * from LoaiThue where maLoaiThue=?";
        try(Connection conn = ConnectSQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiThue);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LoaiThue lt = new LoaiThue();
                    lt.setMaLoaiThue(rs.getString("maLoaiThue"));
                    lt.setTenLoaiThue(rs.getString("tenLoaiThue"));
                    return lt;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
