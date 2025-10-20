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

    public List<PhuongThucThanhToan> layDanhSachPhuongThucThanhToan(String maPTTT) throws SQLException {
        List<PhuongThucThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhuongThucThanhToan WHERE maPTTT = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPTTT);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String tenPT = rs.getString("tenPTTT");
                    String moTa = rs.getString("moTa");

                    PhuongThucThanhToan.LoaiPTTT loaiPTTT;
                    if (tenPT.equalsIgnoreCase("Tiền mặt")) {
                        loaiPTTT = PhuongThucThanhToan.LoaiPTTT.TIEN_MAT;
                    } else {
                        loaiPTTT = PhuongThucThanhToan.LoaiPTTT.CHUYEN_KHOAN;
                    }

                    list.add(new PhuongThucThanhToan(maPTTT,loaiPTTT,moTa));
                }
            }
        }
        return list;
    }
}
