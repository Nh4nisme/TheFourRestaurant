package com.thefourrestaurant.model;

import java.math.BigDecimal;

public class LoaiBan {
    private String maLoaiBan;
    private String tenLoaiBan;
    private BigDecimal giaTien;
    private int soChoNgoi;
    private String moTa;

    // Constructor mặc định
    public LoaiBan() {}

    // Constructor chỉ với mã loại bàn
    public LoaiBan(String maLoaiBan) {
        this.maLoaiBan = maLoaiBan;
    }

    // Constructor đầy đủ
    public LoaiBan(String maLoaiBan, String tenLoaiBan, BigDecimal giaTien, int soChoNgoi, String moTa) {
        this.maLoaiBan = maLoaiBan;
        this.tenLoaiBan = tenLoaiBan;
        this.giaTien = giaTien;
        this.soChoNgoi = soChoNgoi;
        this.moTa = moTa;
    }

    // Getter và Setter
    public String getMaLoaiBan() {
        return maLoaiBan;
    }

    public void setMaLoaiBan(String maLoaiBan) {
        this.maLoaiBan = maLoaiBan;
    }

    public String getTenLoaiBan() {
        return tenLoaiBan;
    }

    public void setTenLoaiBan(String tenLoaiBan) {
        this.tenLoaiBan = tenLoaiBan;
    }

    public BigDecimal getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(BigDecimal giaTien) {
        this.giaTien = giaTien;
    }

    public int getSoChoNgoi() {
        return soChoNgoi;
    }

    public void setSoChoNgoi(int soChoNgoi) {
        this.soChoNgoi = soChoNgoi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return tenLoaiBan + " (" + soChoNgoi + " chỗ)";
    }
}
