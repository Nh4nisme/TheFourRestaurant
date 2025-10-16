package com.thefourrestaurant.model;

public class LoaiMon {
    private String maLoai;    // maLoaiMon
    private String tenLoai;   // tenLoaiMon

    public LoaiMon() {}

    public LoaiMon(String maLoai, String tenLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    @Override
    public String toString() {
        return tenLoai;
    }
}
