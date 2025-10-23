package com.thefourrestaurant.model;

import java.math.BigDecimal;

public class ChiTietKhuyenMai {

    private String maCTKM;
    private KhuyenMai khuyenMai; // Foreign Key to KhuyenMai
    private MonAn monAnApDung;   // Foreign Key to MonAn
    private MonAn monAnTang;       // Foreign Key to MonAn (nullable)
    private BigDecimal tyLeGiam;   // Nullable
    private BigDecimal soTienGiam; // Nullable
    private Integer soLuongTang;   // Nullable

    public ChiTietKhuyenMai() {
    }

    // Getters and Setters

    public String getMaCTKM() {
        return maCTKM;
    }

    public void setMaCTKM(String maCTKM) {
        this.maCTKM = maCTKM;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public MonAn getMonAnApDung() {
        return monAnApDung;
    }

    public void setMonAnApDung(MonAn monAnApDung) {
        this.monAnApDung = monAnApDung;
    }

    public MonAn getMonAnTang() {
        return monAnTang;
    }

    public void setMonAnTang(MonAn monAnTang) {
        this.monAnTang = monAnTang;
    }

    public BigDecimal getTyLeGiam() {
        return tyLeGiam;
    }

    public void setTyLeGiam(BigDecimal tyLeGiam) {
        this.tyLeGiam = tyLeGiam;
    }

    public BigDecimal getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(BigDecimal soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    public Integer getSoLuongTang() {
        return soLuongTang;
    }

    public void setSoLuongTang(Integer soLuongTang) {
        this.soLuongTang = soLuongTang;
    }
}
