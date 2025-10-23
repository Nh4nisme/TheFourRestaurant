package com.thefourrestaurant.model;

import java.math.BigDecimal;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private MonAn monAn;
    private int soLuong;
    private BigDecimal donGia;

    public ChiTietHoaDon(HoaDon hoaDon, MonAn monAn, int soLuong,  BigDecimal donGia) {
        setHoaDon(hoaDon);
        setMonAn(monAn);
        setSoLuong(soLuong);
        setDonGia(donGia);
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
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

    public BigDecimal getThanhTien() {
        if (donGia == null) return BigDecimal.ZERO;
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }
}
