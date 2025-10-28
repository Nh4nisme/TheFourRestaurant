package com.thefourrestaurant.DAO;

import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.model.PhuongThucThanhToan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhuongThucThanhToanDAO {

    public List<PhuongThucThanhToan> layDanhSachPhuongThucThanhToan(){
        List<PhuongThucThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhuongThucThanhToan";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String maPTTT = rs.getString("maPTTT");
                String loaiStr = rs.getString("tenPTTT");
                String moTa = rs.getString("moTa");

                PhuongThucThanhToan.LoaiPTTT loaiPTTT;

                if (loaiStr.equalsIgnoreCase("Tiền mặt")) {
                    loaiPTTT = PhuongThucThanhToan.LoaiPTTT.TIEN_MAT;
                } else {
                    loaiPTTT = PhuongThucThanhToan.LoaiPTTT.CHUYEN_KHOAN;
                }

                list.add(new PhuongThucThanhToan(maPTTT, loaiPTTT, moTa));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public PhuongThucThanhToan layPTTTTheoMa(String maPTTT) {
        String sql = "SELECT * FROM PhuongThucThanhToan WHERE maPTTT=?";

        try(Connection conn = ConnectSQL.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPTTT);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    String tenPT = rs.getString("tenPTTT");
                    String moTa = rs.getString("moTa");
                    PhuongThucThanhToan.LoaiPTTT loaiPTTT;
                    if (tenPT.equalsIgnoreCase("Tiền mặt")) {
                        loaiPTTT = PhuongThucThanhToan.LoaiPTTT.TIEN_MAT;
                    } else {
                        loaiPTTT = PhuongThucThanhToan.LoaiPTTT.CHUYEN_KHOAN;
                    }

                    return new PhuongThucThanhToan(maPTTT,loaiPTTT,moTa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
