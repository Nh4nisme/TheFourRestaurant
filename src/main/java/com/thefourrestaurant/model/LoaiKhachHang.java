package com.thefourrestaurant.model;

public class LoaiKhachHang {
    private String maLoaiKH;
    private String tenLoaiKH;

    public LoaiKhachHang() {
    }

    public LoaiKhachHang(String maLoaiKH,  String tenLoaiKH) {
        setMaLoaiKH(maLoaiKH);
        setTenLoaiKH(tenLoaiKH);
    }

    public String getMaLoaiKH() {
        return maLoaiKH;
    }

    public void setMaLoaiKH(String maLoaiKH) {
        this.maLoaiKH = maLoaiKH;
    }

    public String getTenLoaiKH() {
        return tenLoaiKH;
    }

    public void setTenLoaiKH(String tenLoaiKH) {
        this.tenLoaiKH = tenLoaiKH;
    }

    @Override
    public String toString() {
        return "LoaiKhachHang{" +
                "maLoaiKH='" + maLoaiKH + '\'' +
                ", tenLoaiKH='" + tenLoaiKH + '\'' +
                '}';
    }
}
