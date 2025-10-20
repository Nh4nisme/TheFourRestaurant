package com.thefourrestaurant.model;

public class LoaiKhuyenMai {
    private String maLoaiKM;
    private String tenLoaiKM;

    public LoaiKhuyenMai() {
    }

    public LoaiKhuyenMai(String maLoaiKM, String tenLoaiKM) {
        this.maLoaiKM = maLoaiKM;
        this.tenLoaiKM = tenLoaiKM;
    }

    public String getMaLoaiKM() {
        return maLoaiKM;
    }

    public void setMaLoaiKM(String maLoaiKM) {
        this.maLoaiKM = maLoaiKM;
    }

    public String getTenLoaiKM() {
        return tenLoaiKM;
    }

    public void setTenLoaiKM(String tenLoaiKM) {
        this.tenLoaiKM = tenLoaiKM;
    }

    @Override
    public String toString() {
        return this.tenLoaiKM; // Important for ComboBox display
    }
}
