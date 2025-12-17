package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.ChiTietHoaDonDAO;

import java.math.BigDecimal;

public class ChiTietHoaDonController {
    private final ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

    public boolean themChiTietHoaDon(
            String maHD,
            String maMonAn,
            int soLuong,
            BigDecimal donGia
    ) {
        if (maHD == null || maMonAn == null || soLuong <= 0) {
            return false;
        }

        return chiTietHoaDonDAO.themChiTietHD(maHD, maMonAn, soLuong, donGia);
    }
}
