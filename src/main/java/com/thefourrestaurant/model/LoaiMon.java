package com.thefourrestaurant.model;

public class LoaiMon {
    private String maLoaiMon;
    private String tenLoaiMon;
    private String hinhAnh;

    public LoaiMon() {}

    public LoaiMon(String maLoaiMon, String tenLoaiMon, String hinhAnh) {
        this.maLoaiMon = maLoaiMon;
        this.tenLoaiMon = tenLoaiMon;
        this.hinhAnh = hinhAnh;
    }

    public String getMaLoaiMon() {
        return maLoaiMon;
    }

    public void setMaLoaiMon(String maLoaiMon) {
        this.maLoaiMon = maLoaiMon;
    }

    public String getTenLoaiMon() {
        return tenLoaiMon;
    }

    public void setTenLoaiMon(String tenLoaiMon) {
        this.tenLoaiMon = tenLoaiMon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return tenLoaiMon;
    }
}
