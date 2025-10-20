package com.thefourrestaurant.model;

import java.math.BigDecimal;

public class MonAn {
    private String maMonAn;
    private String tenMon;
    private BigDecimal donGia;
    private String trangThai;
    private String maLoaiMon;
    private String hinhAnh;

    public MonAn() {
    }

    public MonAn(String maMonAn, String tenMon, BigDecimal donGia, String trangThai, String maLoaiMon, String hinhAnh) {
        this.maMonAn = maMonAn;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.trangThai = trangThai;
        this.maLoaiMon = maLoaiMon;
        this.hinhAnh = hinhAnh;
    }

    // Getters and Setters
    public String getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaLoaiMon() {
        return maLoaiMon;
    }

    public void setMaLoaiMon(String maLoaiMon) {
        this.maLoaiMon = maLoaiMon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return "MonAn{" +
                "maMonAn='" + maMonAn + '\'' +
                ", tenMon='" + tenMon + '\'' +
                ", donGia=" + donGia +
                ", trangThai='" + trangThai + '\'' +
                ", maLoaiMon='" + maLoaiMon + '\'' +
                ", hinhAnh='" + hinhAnh + '\'' +
                '}';
    }
}
