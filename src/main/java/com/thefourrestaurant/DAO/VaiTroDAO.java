package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.VaiTro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VaiTroDAO {
    public List<VaiTro> layDanhSachVaiTro() {
        List<VaiTro> ds = new ArrayList<>();
        String sql = "SELECT maVT, tenVaiTro, isDeleted FROM VaiTro WHERE isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VaiTro vt = new VaiTro();
                vt.setMaVT(rs.getString("maVT"));
                vt.setTenVaiTro(rs.getString("tenVaiTro"));
                vt.setDeleted(rs.getBoolean("isDeleted"));
                ds.add(vt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public static VaiTro layVaiTroTheoMa(String maVT) {
        String sql = "SELECT maVT, tenVaiTro, isDeleted FROM VaiTro WHERE maVT = ? AND isDeleted = 0";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maVT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    VaiTro vt = new VaiTro();
                    vt.setMaVT(rs.getString("maVT"));
                    vt.setTenVaiTro(rs.getString("tenVaiTro"));
                    vt.setDeleted(rs.getBoolean("isDeleted"));
                    return vt;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
