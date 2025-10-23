package com.thefourrestaurant.model;

import java.math.BigDecimal;

public class ChiTietKhuyenMai {
    private String maCTKM;
    private KhuyenMai khuyenMai;
    private MonAn monApDung;
    private MonAn monTang;
    private BigDecimal tyLeGiam;
    private BigDecimal soTienGiam;
    private Integer soLuongTang; // Use Integer for nullability

    public ChiTietKhuyenMai() {
    }

    public ChiTietKhuyenMai(String maCTKM, KhuyenMai khuyenMai, MonAn monApDung, MonAn monTang, BigDecimal tyLeGiam, BigDecimal soTienGiam, Integer soLuongTang) {
        this.maCTKM = maCTKM;
        this.khuyenMai = khuyenMai;
        this.monApDung = monApDung;
        this.monTang = monTang;
        this.tyLeGiam = tyLeGiam;
        this.soTienGiam = soTienGiam;
        this.soLuongTang = soLuongTang;
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

    public MonAn getMonApDung() {
        return monApDung;
    }

    public void setMonApDung(MonAn monApDung) {
        this.monApDung = monApDung;
    }

    public MonAn getMonTang() {
        return monTang;
    }

    public void setMonTang(MonAn monTang) {
        this.monTang = monTang;
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
