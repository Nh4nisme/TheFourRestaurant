package com.thefourrestaurant.model;

import java.time.LocalTime;
import java.util.List;

public class KhungGio {

    private String maTG;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private boolean lapLaiHangNgay;

    // For Many-to-Many relationships
    private List<KhuyenMai> khuyenMais;
//    private List<Combo> combos;

    public KhungGio() {
    }

    // Getters and Setters

    public String getMaTG() {
        return maTG;
    }

    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public boolean isLapLaiHangNgay() {
        return lapLaiHangNgay;
    }

    public void setLapLaiHangNgay(boolean lapLaiHangNgay) {
        this.lapLaiHangNgay = lapLaiHangNgay;
    }

    public List<KhuyenMai> getKhuyenMais() {
        return khuyenMais;
    }

    public void setKhuyenMais(List<KhuyenMai> khuyenMais) {
        this.khuyenMais = khuyenMais;
    }

//    public List<Combo> getCombos() {
//        return combos;
//    }
//
//    public void setCombos(List<Combo> combos) {
//        this.combos = combos;
//    }
}
