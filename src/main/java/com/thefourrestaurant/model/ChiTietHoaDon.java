package com.thefourrestaurant.model;

import java.math.BigDecimal;

public class ChiTietHoaDon {
    private String maHD;      // Mã hóa đơn
    private String maMonAn;   // Mã món ăn
    private int soLuong;       // Số lượng
    private BigDecimal donGia; // Đơn giá

    public ChiTietHoaDon(String maHD, String maMonAn, int soLuong, BigDecimal donGia) {
        setMaHD(maHD);
        setMaMonAn(maMonAn);
        setSoLuong(soLuong);
        setDonGia(donGia);
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    // Phương thức tính Thành tiền
    public BigDecimal getThanhTien() {
        if (donGia == null) return BigDecimal.ZERO;
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }
}
